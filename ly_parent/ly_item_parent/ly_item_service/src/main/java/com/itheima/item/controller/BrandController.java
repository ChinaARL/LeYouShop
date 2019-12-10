package com.itheima.item.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.BrandDTO;
import com.itheima.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @action 功能:
 * @Package com.itheima.item.controller
 * @date 2019/12/7 20:44
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;


//    GET /brand/page?key=&page=1&rows=5&sortBy=id&desc=false

    /**
     * 根据关键字 查询
     *
     * @param key    查询关键字 模糊匹配品牌名称
     * @param page   当前页
     * @param size   页大小
     * @param sortBy 排序字段
     * @param desc   是否降序
     * @return
     */
    @GetMapping("brand/page")
    public ResponseEntity<PageResult<BrandDTO>> queryByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "desc") Boolean desc
    ) {
        PageResult<BrandDTO> pageResult = brandService.queryByPage(key, page, size, sortBy, desc);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 接受参数
     * 页码中提交表单数据form-data  服务器：直接通过实体对象接受参数  前提：属性名称一致  如果不一致，采用@RequestParam获取
     */

    /**
     * 品牌-分类：多对多
     * 保存品牌- 1、保存品牌数据  2、保存分类品牌数据
     */
    @PostMapping("/brand")
    public ResponseEntity<Void> saveBrand(BrandDTO brandDTO, @RequestParam("cids") List<Long> categoryIds){
        brandService.saveBrand(brandDTO, categoryIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
