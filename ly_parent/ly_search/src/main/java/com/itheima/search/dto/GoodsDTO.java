package com.itheima.search.dto;

import lombok.Data;

/**
 * 在检索结果列表页中 展示商品数据
 */
@Data
public class GoodsDTO {
    public Long id;  //商品ID
    public String skus;  //商品SKU列表 注意：是字符串
    public String subTitle;  //促销信息
}
