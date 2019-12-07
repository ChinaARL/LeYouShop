package com.itheima.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @action 功能:分页查询得到对象
 * @Package com.itheima.common.common
 * @date 2019/12/7 20:39
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private Long total;  //总记录数
    private Integer totalPage;  //总页数
    private List<T> items;    // 当前页数据
}
