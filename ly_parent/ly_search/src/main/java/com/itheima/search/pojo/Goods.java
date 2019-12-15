package com.itheima.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.Set;

/**
 * 一个SPU对应一个Goods
 */
@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 1)
public class Goods {
    //要不要进行索引(倒排)：要不要进行检索
    //要不要进行分词： 看数据是否是一个整体。
    //要不要存储：注意ES将所有的数据都存储_source
    @Id
    @Field(type = FieldType.Keyword)  // Keyword 不分词
    private Long id; // spuId
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 卖点 将来不会进行检索
    @Field(type = FieldType.Keyword, index = false)
    private String skus;// sku信息的json结构--将来在检索结果中展示sku信息  "[{skuID:1，title:"",price:100,image:""},]"

    @Field(type = FieldType.Text, analyzer = "ik_max_word")  //Text 分词
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
    private Long brandId;// 品牌id
    private Long categoryId;// 商品3级分类id
    private Long createTime;// spu创建时间
    private Set<Long> price;// 价格
    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}
