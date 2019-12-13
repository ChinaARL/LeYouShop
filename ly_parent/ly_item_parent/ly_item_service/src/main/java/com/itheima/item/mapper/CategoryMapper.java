package com.itheima.item.mapper;

import com.itheima.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.mapper
 * @date 2019/12/7 16:44
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {

}
