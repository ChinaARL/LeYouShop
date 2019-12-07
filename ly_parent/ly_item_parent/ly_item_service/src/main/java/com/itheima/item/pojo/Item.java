package com.itheima.item.pojo;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.pojo
 * @date 2019/12/6 20:29
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */

import lombok.Data;

/**
 * 模拟商品实体类
 */
@Data
public class Item {

    private Long id;
    private String name;
    private Double price;

}
