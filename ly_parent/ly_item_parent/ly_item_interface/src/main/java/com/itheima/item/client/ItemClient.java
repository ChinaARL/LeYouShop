package com.itheima.item.client;

import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.SkuDTO;
import com.itheima.item.dto.SpecParamDTO;
import com.itheima.item.dto.SpuDTO;
import com.itheima.item.dto.SpuDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface ItemClient {

    /**
     * 商品SPU列表查询
     *
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
     *
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
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    public SpuDetailDTO querySpuDetailBySpuId(@RequestParam("id") Long spuId);
}
