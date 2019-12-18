package com.itheima.page.service;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.item.client.ItemClient;
import com.itheima.item.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageService {

    @Autowired
    private ItemClient itemClient;

    /**
     * 通过商品ID 查询商品详情页需要的参数
     * - categories：商品分类对象集合  根据spu后获取三级分类ID ，根据id集合查询
     * - brand：品牌对象     根据spu后根据品牌ID查询
     * - spuName：应该 是spu表中的name属性 根据spu
     * - subTitle：spu中 的副标题    根据spu
     * - detail：商品详情SpuDetail   根据spuID查询详情
     * - skus：商品spu下的sku集合    根据spuID查询skus列表
     * - specs：规格参数这个比较 特殊：  根据分类ID查询规格组（包括规格参数）
     * @param spuId
     * @return
     */
    public Map<String, Object> loadItemData(Long spuId) {
        HashMap<String, Object> map = new HashMap<>();
        //查询spu
        if(spuId==null){
            throw new LyException(ResponseCode.INVALID_PARAM_ERROR);
        }
        SpuDTO spuDTO = itemClient.queryBySpuId(spuId);

        map.put("spuName", spuDTO.getName());
        map.put("subTitle", spuDTO.getSubTitle());

        //根据三级分类ID集合查询分类集合
        final List<CategoryDTO> categoryDTOList = itemClient.queryCategoryListByIds(spuDTO.getCategoryIds());
        map.put("categories", categoryDTOList);
        //查询品牌
        final BrandDTO brand = itemClient.queryBrandById(spuDTO.getBrandId());
        map.put("brand", brand);

        final SpuDetailDTO spuDetailDTO = itemClient.querySpuDetailBySpuId(spuId);
        map.put("detail", spuDetailDTO);

        final List<SkuDTO> skus = itemClient.querySkusBySpuId(spuId);
        map.put("skus", skus);

        //根据分类ID 查询规格参数组列表，包含规格参数
        List<SpecGroupDTO> specGroupDTOList = itemClient.queryByCategoryId(spuDTO.getCid3());

        //根据分类 查询规格参数列表
        List<SpecParamDTO> specParamDTOList = itemClient.querySpecParamByGroupId(null, spuDTO.getCid3(), null);


        //借助stream流
        final Map<Long, List<SpecParamDTO>> collect = specParamDTOList.stream().collect(Collectors.groupingBy(SpecParamDTO::getGroupId));

        for (SpecGroupDTO specGroupDTO : specGroupDTOList) {
            specGroupDTO.setParams(collect.get(specGroupDTO.getId()));
        }

        //遍历规格参数List 根据组ID作为map的key  value 为改组规格参数
        /*Map<Long, List<SpecParamDTO>> listMap = new HashMap<>();
        for (SpecParamDTO specParamDTO : specParamDTOList) {
            if (!listMap.containsKey(specParamDTO.getGroupId())) {
                listMap.put(specParamDTO.getGroupId(), new ArrayList<>());
            }
            listMap.get(specParamDTO.getGroupId()).add(specParamDTO);
        }
        for (SpecGroupDTO specGroupDTO : specGroupDTOList) {
            specGroupDTO.setParams(listMap.get(specGroupDTO.getId()));
        }*/


        /*for (SpecGroupDTO specGroupDTO : specGroupDTOList) {
            for (SpecParamDTO specParamDTO : specParamDTOList) {
                if(specParamDTO.getGroupId().equals(specGroupDTO.getId())){
                    //当前规格参数属于改组
                    if(specGroupDTO.getParams()==null){
                        specGroupDTO.setParams(new ArrayList<>());
                    }
                    specGroupDTO.getParams().add(specParamDTO);
                }
            }
        }*/

        map.put("specs", specGroupDTOList);

        return map;
    }
}
