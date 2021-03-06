package com.itheima.item.client;

        import com.itheima.common.vo.PageResult;
        import com.itheima.item.dto.*;
        import org.springframework.cloud.openfeign.FeignClient;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestParam;

        import java.util.List;

@FeignClient("item-service")
public interface ItemClient {

    /**
     * 商品SPU列表查询
     * @param key
     * @param saleable
     * @param page
     * @param size
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuDTO> queryByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", defaultValue = "true", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer size
    );

    /**
     * 根据商品ID查询商品sku列表
     * @param spuId
     * @return
     */
    @GetMapping("/sku/of/spu")
    public List<SkuDTO> querySkusBySpuId(@RequestParam("id") Long spuId);


    @GetMapping("/spec/params")
    public List<SpecParamDTO> querySpecParamByGroupId(
            @RequestParam(value = "gid", required = false) Long groupId,
            @RequestParam(value = "cid", required = false) Long categoryId,
            @RequestParam(value = "searching", required = false) Boolean searching);


    /**
     * 根据商品ID查询商品详情对象
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    public SpuDetailDTO querySpuDetailBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据分类id集合获取分类集合
     * @param categoryIds
     * @return
     */
    @GetMapping("/category/list")
    public List<CategoryDTO> queryCategoryListByIds(@RequestParam("ids") List<Long> categoryIds);


    /**
     * 根据品牌ID 集合 查询品牌集合
     * @param brandIdList
     * @return
     */
    @GetMapping("/brand/list")
    public List<BrandDTO> queryBrandListByIds(@RequestParam("ids") List<Long> brandIdList);

    /**
     * 根据商品ID查询商品SPU数据
     * @param spuId
     * @return
     */
    @GetMapping("/spu/{id}")
    public SpuDTO queryBySpuId(@PathVariable("id") Long spuId);


    /**
     * 根据品牌ID 查询品牌对象
     * @param brandId
     * @return
     */
    @GetMapping("/brand/{id}")
    public BrandDTO queryBrandById(@PathVariable("id") Long brandId);


    /***
     * 根据分类Id查询 规格组列表，不包含规格参数
     * @param categoryId
     * @return
     */
    @GetMapping("/spec/groups/of/category")
    public List<SpecGroupDTO> queryByCategoryId(@RequestParam("id") Long categoryId);
}
