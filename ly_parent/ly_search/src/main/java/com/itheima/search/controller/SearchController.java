package com.itheima.search.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.search.dto.GoodsDTO;
import com.itheima.search.dto.SearchRequest;
import com.itheima.search.service.GoodsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private GoodsIndexService goodsIndexService;


    /**
     * 根据关键字检索 分页查询
     * @param searchRequest 查询条件对象
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<GoodsDTO>> searchByKey(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(goodsIndexService.searchByKey(searchRequest));
    }

    /**
     * 根据查询关键字对数据进行聚合（品牌，分类，其他的规格参数）
     * @param searchRequest
     * @return
     */
    @PostMapping("/filter")
    public ResponseEntity<Map<String, List<?>>> searchFilter(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(goodsIndexService.searchFilter(searchRequest));
    }
}
