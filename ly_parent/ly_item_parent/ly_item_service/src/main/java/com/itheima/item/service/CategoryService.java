package com.itheima.item.service;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.item.dto.CategoryDTO;
import com.itheima.item.mapper.CategoryMapper;
import com.itheima.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.service
 * @date 2019/12/7 16:47
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryDTO> queryByParentId(Long pId) {
        //sql select * from tb_categeory where pid= ?
        Category category = new Category();
        category.setParentId(pId);
        //select方法使用精确查询
        List<Category> categoryList = categoryMapper.select(category);
        if(CollectionUtils.isEmpty(categoryList)){
            throw new LyException(ResponseCode.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);
    }
}
