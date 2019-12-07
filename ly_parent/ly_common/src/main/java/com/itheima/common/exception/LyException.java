package com.itheima.common.exception;

import com.itheima.common.exception.enms.ResponseCode;
import lombok.Data;

/**
 * 自定义异常：封装返回状态码
 *
 * @author 高策
 * @version V1.0
 * @Package com.itheima.common.exception
 * @date 2019/12/6 23:02
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */

@Data
public class LyException extends RuntimeException {
    private Integer status;

    public LyException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.status = responseCode.getStatus();
    }

    public LyException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMessage(), cause);
        this.status = responseCode.getStatus();
    }
}
