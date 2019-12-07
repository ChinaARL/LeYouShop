package com.itheima.item.dto;

import lombok.Data;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.dto
 * @date 2019/12/7 16:49
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
