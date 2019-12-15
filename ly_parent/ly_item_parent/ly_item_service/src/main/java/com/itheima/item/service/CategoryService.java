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
        List<CategoryDTO> categoryDTOList = BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);
        return categoryDTOList;
    }

    public List<CategoryDTO> queryByCategoryIds(List<Long> categoryIds){
        List<Category> categoryList = categoryMapper.selectByIdList(categoryIds);
        if (CollectionUtils.isEmpty(categoryIds)) {
            throw new LyException(ResponseCode.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);
    }
}
