package com.itheima.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.common.utils.JsonUtils;
import com.itheima.common.vo.PageResult;
import com.itheima.item.client.ItemClient;
import com.itheima.item.dto.*;
import com.itheima.search.dto.GoodsDTO;
import com.itheima.search.dto.SearchRequest;
import com.itheima.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsIndexService {

    @Autowired
    private ItemClient itemClient;

    /**
     * @param spuDTO spu信息
     * @return
     */
    public Goods buildGoods(SpuDTO spuDTO) {
        Goods goods = new Goods();
        goods.setId(spuDTO.getId());
        goods.setSubTitle(spuDTO.getSubTitle());
        goods.setBrandId(spuDTO.getBrandId());
        goods.setCategoryId(spuDTO.getCid3());
        goods.setCreateTime(spuDTO.getCreateTime().getTime());

        String all = spuDTO.getName() + spuDTO.getBrandName() + spuDTO.getCategoryName();
        goods.setAll(all);

        //根据商品ID查询 sku列表
        List<SkuDTO> skuDTOList = itemClient.querySkusBySpuId(spuDTO.getId());

        List<Map<String, Object>> skuList = new ArrayList<>();
        for (SkuDTO skuDTO : skuDTOList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", skuDTO.getId());
            map.put("title", skuDTO.getTitle());
            map.put("price", skuDTO.getPrice());  //用户选择sku小图片 显示价格
            map.put("image", StringUtils.substringBefore(skuDTO.getImages(), ","));  //只获取第一张图片 作为检索结果中图片
            skuList.add(map);
        }
        goods.setSkus(JsonUtils.toString(skuList));  //字符串 "[{id,title,price,image}，{id,title,price,image}]"
        goods.setPrice(skuDTOList.stream().map(SkuDTO::getPrice).collect(Collectors.toSet())); //为了将来排序 [1559,15220] set去重
        //规格参数

        //先查询规格参数表：需要当前分类，进行搜索的规格参数  获取规格参数key
        List<SpecParamDTO> specParamDTOList = itemClient.querySpecParamByGroupId(null, spuDTO.getCid3(), true);
        //根据spuID查询商品详情对象  获取规格参数value
        SpuDetailDTO spuDetailDTO = itemClient.querySpuDetailBySpuId(spuDTO.getId());

        //将普通的规格参数json 转为Map<规格参数ID，参数值>
        Map<Long, String> specParamMap = JsonUtils.toMap(spuDetailDTO.getGenericSpec(), Long.class, String.class);

        //特殊规格参数
        String specialSpecJson = spuDetailDTO.getSpecialSpec();
        Map<Long, List<String>> specialSpecMap = JsonUtils.nativeRead(specialSpecJson, new TypeReference<Map<Long, List<String>>>() {
        });

        //遍历规格参数-包含普通，特殊
        Map<String, Object> specs = new HashMap<>();
        for (SpecParamDTO specParamDTO : specParamDTOList) {
            String key = specParamDTO.getName();
            Object value = "";
            if (specParamDTO.getGeneric()) {
                //普通规格参数
                value = specParamMap.get(specParamDTO.getId());
            } else {
                //特殊规格参数
                value = specialSpecMap.get(specParamDTO.getId());
            }
            //判断数值类型
            if (specParamDTO.getNumeric()) {
                //如果是数值类型-需要设置单位
                value = chooseSegment(value, specParamDTO);
                //如果是区间范围规格参数值 ，需要确定哪个区间
            }
            specs.put(key, value);
        }

        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(Object value, SpecParamDTO p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 采用 SpringDataElasticSearch （ElasticSearchTemplate）执行 ElasticSearch原生代码
     * 查询索引库从
     *
     * @param searchRequest
     * @return
     */
    public PageResult<GoodsDTO> searchByKey(SearchRequest searchRequest) {
        //创建原生查询构建器对象
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //设置查询条件（词条，分词查询，查询所有，模糊查询，容错查询，范围查询，布尔查询）
        String key = searchRequest.getKey();
        if (StringUtils.isBlank(key)) {
            throw new LyException(ResponseCode.INVALID_PARAM_ERROR);
        }
        builder.withQuery(basicQuery(searchRequest));
        //设置分页
        Integer page = searchRequest.getPage();
        Integer size = searchRequest.getSize();
        builder.withPageable(PageRequest.of(page, size));

        //设置返回字段-过滤字段
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //执行查询
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(builder.build(), Goods.class);
        //处理结果
        long totalElements = result.getTotalElements();
        int totalPages = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        return new PageResult<GoodsDTO>(totalElements, totalPages, BeanHelper.copyWithCollection(goodsList, GoodsDTO.class));
    }

    /**
     * 抽取基本查询条件
     *
     * @param searchRequest
     * @return
     */
    private MatchQueryBuilder basicQuery(SearchRequest searchRequest) {
        return QueryBuilders.matchQuery("all", searchRequest.getKey());
    }

    /**
     * 查询，对查询到数据进行聚合
     *
     * @param searchRequest
     * @return {"品牌",[{"name:小米"}...],"分类"：【{name:手机}】}
     */
    public Map<String, List<?>> searchFilter(SearchRequest searchRequest) {

        Map<String, List<?>> mapResult = new HashMap<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //设置查询条件
        MatchQueryBuilder matchQueryBuilder = basicQuery(searchRequest);
        queryBuilder.withQuery(matchQueryBuilder);

        //设置分页  不需要查询到商品数据
        queryBuilder.withPageable(PageRequest.of(0, 1));

        //添加分类聚合
        String categoryAgg = "categoryAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAgg).field("categoryId"));
        //添加品牌聚合
        String brandAgg = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));

        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //处理聚合结果
        Aggregations aggregations = result.getAggregations();

        //处理分类聚合
        Terms categoryTerms = aggregations.get(categoryAgg);
        handlerCategory(categoryTerms, mapResult);

        //处理品牌聚合
        Terms brandTerms = aggregations.get(brandAgg);
        handlerBrandAgg(brandTerms, mapResult);
        return mapResult;
    }


    /**
     * 处理分类聚合
     * @param categoryTerms
     * @param mapResult
     */
    private void handlerCategory(Terms categoryTerms, Map<String, List<?>> mapResult) {
        List<Long> categoryIdList = categoryTerms.getBuckets().stream().map(Terms.Bucket::getKeyAsNumber).map(Number::longValue).collect(Collectors.toList());
        List<CategoryDTO> categoryDTOList = itemClient.queryCategoryListByIds(categoryIdList);
        mapResult.put("分类", categoryDTOList);
    }

    /**
     * 处理品牌聚合
     * @param brandTerms
     * @param mapResult
     */
    private void handlerBrandAgg(Terms brandTerms, Map<String, List<?>> mapResult) {
        List<Long> brandIdList = brandTerms.getBuckets().stream().map(Terms.Bucket::getKeyAsNumber).map(Number::longValue).collect(Collectors.toList());
        List<BrandDTO> brandDTOList = itemClient.queryBrandListByIds(brandIdList);
        mapResult.put("品牌", brandDTOList);
    }
}
