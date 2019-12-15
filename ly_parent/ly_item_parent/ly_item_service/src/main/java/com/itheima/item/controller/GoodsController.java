package com.itheima.item.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.SkuDTO;
import com.itheima.item.dto.SpuDTO;
import com.itheima.item.dto.SpuDetailDTO;
import com.itheima.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 商品SPU列表查询
     * @param key
     * @param saleable
     * @param page
     * @param size
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuDTO>> queryByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", defaultValue = "true", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(goodsService.queryByPage(key, saleable, page, size));
    }

    /**
     * TODO 注意商品数据页面中提交json格式，在服务器端需要使用@ReqeustBody接受json参数
     * 保存商品信息
     * @param spuDTO
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuDTO spuDTO){
        goodsService.saveGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 商品上下架
     * @param spuId 商品ID
     * @param saleable 状态
     * @return
     */
    @PutMapping("/spu/saleable")
    public ResponseEntity<Void> updateSaleable(@RequestParam("id") Long spuId, @RequestParam("saleable") Boolean saleable){
        goodsService.updateSaleable(spuId, saleable);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 根据商品ID查询商品详情对象
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    public ResponseEntity<SpuDetailDTO> querySpuDetailBySpuId(@RequestParam("id") Long spuId){
        return ResponseEntity.ok(goodsService.querySpuDetailBySpuId(spuId));
    }

    /**
     * 根据商品ID查询商品sku列表
     * @param spuId
     * @return
     */
    @GetMapping("/sku/of/spu")
    public ResponseEntity<List<SkuDTO>> querySkusBySpuId(@RequestParam("id") Long spuId){
        return ResponseEntity.ok(goodsService.querySkusBySpuId(spuId));
    }

    /**
     * 修改商品数据
     * @param spuDTO
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuDTO spuDTO){
        goodsService.updateGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
