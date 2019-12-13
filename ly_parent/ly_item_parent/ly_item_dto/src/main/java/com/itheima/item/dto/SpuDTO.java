package com.itheima.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author HuYi
 */
@Data
public class SpuDTO {
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String name;// 名称
    private String subTitle;// 子标题  {subTitle:"123"}----getSubtime(){return "123"}
    private Boolean saleable;// 是否上架
    private Date createTime;// 创建时间

    private String categoryName; // 商品分类名称拼接
    private String brandName;// 品牌名称

    private SpuDetailDTO spuDetail;  //商品详情信息
    private List<SkuDTO> skus;  //商品sku列表


    /**
     * 方便同时获取3级分类--直接根据集合ID查询分类集合
     * @return
     */
    @JsonIgnore  //对属性转json 忽略该属性
    public List<Long> getCategoryIds(){
        return Arrays.asList(cid1, cid2, cid3);
    }
}
