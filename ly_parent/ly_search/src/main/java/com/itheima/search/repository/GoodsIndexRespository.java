package com.itheima.search.repository;

import com.itheima.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsIndexRespository extends ElasticsearchRepository<Goods, Long> {
}
