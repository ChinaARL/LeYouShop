package com.itheima.item.mapper;

import com.itheima.item.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand, Long> {


    @Transactional
    int insertCategoryBrands(@Param("bid") Long id, @Param("cids") List<Long> categoryIds);

    @Select("SELECT b.* FROM `tb_brand` b,tb_category_brand cb\n" +
            "where cb.brand_id = b.id and cb.category_id = #{categoryId}")
    List<Brand> queryByCategoryId(Long categoryId);
}
