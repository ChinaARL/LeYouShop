package com.itheima.item.mapper;

import com.itheima.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.mapper
 * @date 2019/12/7 20:35
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
public interface BrandMapper extends Mapper<Brand> {


    @Transactional
    int insertCategoryBrands(@Param("bid") Long id, @Param("cids") List<Long> categoryIds);

    @Select("SELECT b.* FROM `tb_brand` b,tb_category_brand cb\n" +
            "where cb.brand_id = b.id and cb.category_id = #{categoryId}")
    List<Brand> queryByCategoryId(Long categoryId);
}