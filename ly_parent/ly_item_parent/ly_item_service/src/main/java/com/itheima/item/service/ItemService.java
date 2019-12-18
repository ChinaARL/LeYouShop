package com.itheima.item.service;

import com.itheima.item.pojo.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {

    public Item saveGoods(Item item){
        item.setId(123L);
        return item;
    }
}
