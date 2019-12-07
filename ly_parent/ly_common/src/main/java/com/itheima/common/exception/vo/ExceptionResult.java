package com.itheima.common.exception.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.common.exception.vo
 * @date 2019/12/6 23:05
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Data
public class ExceptionResult {
    //返回对象 三属性 状态/消息/时间戳
    private Integer status;
    private String message;
    private Date timestamp;

    //通过 状态/消息 进行构造
    public ExceptionResult(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
