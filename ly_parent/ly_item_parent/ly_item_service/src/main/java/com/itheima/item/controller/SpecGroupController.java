package com.itheima.item.controller;

import com.itheima.item.dto.SpecGroupDTO;
import com.itheima.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecGroupController {

    @Autowired
    private SpecGroupService specGroupService;

    /**
     * 根据分类ID 查询 当前分类包含的规格参数组列表
     * @param categoryId
     * @return
     */
    @GetMapping("/spec/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> queryByCategoryId(@RequestParam("id") Long categoryId){
        return ResponseEntity.ok(specGroupService.queryByCategoryId(categoryId));
    }
}
