package com.itheima.item.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.SpuDTO;
import com.itheima.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
