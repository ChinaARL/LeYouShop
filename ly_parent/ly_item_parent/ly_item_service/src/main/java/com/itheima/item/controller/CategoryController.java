package com.itheima.item.controller;

import com.itheima.item.dto.CategoryDTO;
import com.itheima.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(value = "http://mange.lyou.com",allowCredentials = "", maxAge = 123, methods = "", allowedHeaders = )
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * get请求地址中参数  使用@requestParama注解获取参数值
     * @param  pid 上级分类ID
     * @return
     */
    @GetMapping("/category/of/parent")
    public ResponseEntity<List<CategoryDTO>> queryByParentId(@RequestParam("pid") Long pId){
        List<CategoryDTO> list = categoryService.queryByParentId(pId);
        return ResponseEntity.ok(list);
    }

}
