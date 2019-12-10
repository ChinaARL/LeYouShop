package com.itheima.item.controller;


import com.itheima.item.dto.CategoryDTO;
import com.itheima.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.controller
 * @date 2019/12/7 17:40
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@RestController
//@CrossOrigin(value = "http://mange.lyou.com",allowCredentials = "", maxAge = 123, methods = "", allowedHeaders = )
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * get请求地址中参数  使用@requestParama注解获取参数值
     *
     * @param pId 上级分类ID
     * @return
     */
    @GetMapping(value = "/category/of/parent") //,produces = "application/json;charset=UTF-8"
    public ResponseEntity<List<CategoryDTO>> queryByParentId(@RequestParam("pid") Long pId) {
        List<CategoryDTO> list = categoryService.queryByParentId(pId);
        System.out.println(list);
        return ResponseEntity.ok(list);
    }

}
