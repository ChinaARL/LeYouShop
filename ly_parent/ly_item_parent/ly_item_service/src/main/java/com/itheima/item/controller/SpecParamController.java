package com.itheima.item.controller;

import com.itheima.item.dto.SpecParamDTO;
import com.itheima.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecParamController {
    @Autowired
    private SpecParamService specParamService;


    /**
     * 根据规格参数组ID查询当前规格参数组中包含的规格参数列表
     * @param groupId
     * @return
     */
    @GetMapping("/spec/params")
    public ResponseEntity<List<SpecParamDTO>> querySpecParamByGroupId(@RequestParam(value = "gid", required = false) Long groupId, @RequestParam(value = "cid", required = false) Long categoryId){
        return ResponseEntity.ok(specParamService.querySpecParamByGroupId(groupId, categoryId));
    }
}
