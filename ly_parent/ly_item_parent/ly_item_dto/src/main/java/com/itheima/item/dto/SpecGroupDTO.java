package com.itheima.item.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 黑马程序员
 */
@Data
public class SpecGroupDTO {
    private Long id;

    private Long cid;

    private String name;

    private List<SpecParamDTO> params;  //包含当前组下规格参数
}
