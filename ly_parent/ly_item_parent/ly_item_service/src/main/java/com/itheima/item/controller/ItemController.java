package com.itheima.item.controller;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.item.pojo.Item;
import com.itheima.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.controller
 * @date 2019/12/6 20:31
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello item!");
    }

    /**
     * 异常处理：1、设置返回http状态码动态设置，返回提示信息。  目标:{status:401,message:"价格不能为空","timestamp":"2019-10-11"}
     * 增加商品
     *
     * @param item 商品信息
     * @return 返回：保存成功数据结果
     */
    @PostMapping("/goods")
    public Item saveGoods(Item item) {
        //判断价格 为空 给前端返回提示信息
        if (item.getPrice() == null) {
//            throw new RuntimeException("价格不能为空");
            throw new LyException(ResponseCode.PIRCE_NOT_NULL);
        }
        itemService.saveGoods(item);
        return item;
    }


}
