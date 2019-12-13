package com.itheima.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.CategoryDTO;
import com.itheima.item.dto.SkuDTO;
import com.itheima.item.dto.SpuDTO;
import com.itheima.item.dto.SpuDetailDTO;
import com.itheima.item.mapper.SkuMapper;
import com.itheima.item.mapper.SpuDetailMapper;
import com.itheima.item.mapper.SpuMapper;
import com.itheima.item.pojo.Sku;
import com.itheima.item.pojo.Spu;
import com.itheima.item.pojo.SpuDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;


    /**
     * 查询商品SPU列表，每个商品SPU中包括brandID cateegoryId 根据id查询分类名称 品牌名称
     *
     * @param key
     * @param saleable
     * @param page
     * @param size
     * @return
     */
    public PageResult<SpuDTO> queryByPage(String key, Boolean saleable, Integer page, Integer size) {

        PageHelper.startPage(page, size);

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //默认排序
        example.setOrderByClause("create_time desc");

        List<Spu> spuList = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spuList)) {
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }

        PageInfo<Spu> pageInfo = new PageInfo<>(spuList);

        List<SpuDTO> spuDTOList = BeanHelper.copyWithCollection(pageInfo.getList(), SpuDTO.class);

        //对spuDTO集合处理，遍历spuDTOList 遍历过程中 跟spu的brandid,categoryIds 查询对应名称
        handlerCategoryNameAndBrandName(spuDTOList);

        return new PageResult<SpuDTO>(pageInfo.getTotal(), pageInfo.getPages(), spuDTOList);
    }

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;


    private void handlerCategoryNameAndBrandName(List<SpuDTO> spuDTOList) {
        //先遍历spuDto集合，给SpuDTO对象中categoryName,brandName赋值
        //forEach 遍历对象不会改变
        spuDTOList.stream().forEach(spuDTO -> {
            spuDTO.setBrandName(brandService.queryByBrandId(spuDTO.getBrandId()).getName());  //
            List<CategoryDTO> categoryDTOList = categoryService.queryByCategoryIds(spuDTO.getCategoryIds());
//            map:接受参数并且返回新对象数据
//            foreach:接受参数对象类型不改变
            //遍历集合对象中CategoryDTO对象，获取对象中名称 ，返回数据类型变了 map 映射每个对象
            String categoryNames = categoryDTOList.stream().map(CategoryDTO::getName).collect(Collectors.joining("/"));
            spuDTO.setCategoryName(categoryNames);
        });
    }

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    /**
     * 保存商品逻辑
     * 1、保存spu  2、保存spuDetail 跟spu共用主键值  3、保存sku列表 跟spu关联：设置逻辑外键spu_id
     *
     * @param spuDTO
     */
    @Transactional
    public void saveGoods(SpuDTO spuDTO) {
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(false);  //新增商品默认 下架状态
        //保存spu信息
        int count = spuMapper.insertSelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        //获取spuDtail保存spuDetail
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spu.getId());  //商品详情spu_Detail关联spu
        count = spuDetailMapper.insertSelective(spuDetail);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        //保存sku列表
        List<Sku> skuList = new ArrayList<>();
        List<SkuDTO> skus = spuDTO.getSkus();

//        List<Sku> skuList1 = skus.stream().map(skuDTO -> {
//            skuDTO.setEnable(false);
//            skuDTO.setSpuId(spu.getId());
//            return BeanHelper.copyProperties(skuDTO, Sku.class);
//        }).collect(Collectors.toList());

        skus.stream().forEach(skuDTO -> {
            //给sku设置逻辑外键
            skuDTO.setSpuId(spu.getId());
            skuDTO.setEnable(false);
            Sku sku = BeanHelper.copyProperties(skuDTO, Sku.class);
            skuList.add(sku);
        });
        count = skuMapper.insertList(skuList);
        if (count != skuList.size()) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * 商品上下架：1、修改tb_spu表中上下架转态   2、修改sku表中状态
     *
     * @param spuId
     * @param saleable
     */
    @Transactional
    public void updateSaleable(Long spuId, Boolean saleable) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(saleable);
//        1、修改tb_spu表中上下架转态
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
        //2、修改sku数据转态
        //2.1、根据spuID 作为条件更新tb_sku表中记录
        Sku sku = new Sku();
        sku.setEnable(saleable);
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", spuId); //where s.spu_id = ?
        count = skuMapper.updateByExampleSelective(sku, example);//参数一：实体对象  参数二：更新调节

        int size = skuMapper.selectCountByExample(example);//根据spuID 查询 sku记录条数
        if (size != count) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 根据主键查询spu详情
     *
     * @param spuId
     * @return
     */
    public SpuDetailDTO querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }
        return BeanHelper.copyProperties(spuDetail, SpuDetailDTO.class);
    }


    public List<SkuDTO> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ResponseCode.GOODS_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(skuList, SkuDTO.class);
    }

    /**
     * 商品修改
     * 1、更新Spu   2、更新SpuDetail  3、删除旧Sku 新增 sku
     *
     * @param spuDTO
     */
    @Transactional
    public void updateGoods(SpuDTO spuDTO) {
        //1、更新Spu
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(false);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
        //2、更新SpuDetail
        SpuDetailDTO spuDetailDTO = spuDTO.getSpuDetail();
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDetailDTO, SpuDetail.class);
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        if (count != 1) {
            throw new LyException(ResponseCode.UPDATE_OPERATION_FAIL);
        }
        //3、删除旧Sku 新增 sku

        //3.1先查询旧的sku数量
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        int size = skuMapper.selectCount(sku);

        count = skuMapper.delete(sku);
        if (size != count) {
            throw new LyException(ResponseCode.DELETE_OPERATION_FAIL);
        }
        //3.2再次获取最新sku列表 批量新增
        List<SkuDTO> spuDTOSkus = spuDTO.getSkus();

        List<Sku> skuList = spuDTOSkus.stream().map(skuDTO -> {
            skuDTO.setEnable(false);
            skuDTO.setSpuId(spu.getId());
            return BeanHelper.copyProperties(skuDTO, Sku.class);
        }).collect(Collectors.toList());

        count = skuMapper.insertList(skuList);
        if(count!=skuList.size()){
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
    }
}
