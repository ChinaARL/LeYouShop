package com.itheima.item.service;

import com.itheima.item.pojo.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.service
 * @date 2019/12/6 20:30
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Service
@Transactional
public class ItemService {
    public Item saveGoods(Item item) {
        item.setId(123L);
        return item;
    }
}
