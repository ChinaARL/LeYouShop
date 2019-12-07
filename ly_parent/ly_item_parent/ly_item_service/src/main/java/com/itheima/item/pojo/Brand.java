package com.itheima.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.pojo
 * @date 2019/12/7 20:33
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Data
@Table(name = "tb_brand")
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private String image;
    private Character letter;
    private Date createTime;
    private Date updateTime;
}
