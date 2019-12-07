package com.itheima.common.exception.advice;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.common.exception.advice
 * @date 2019/12/6 23:04
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@ResponseBody
@ControllerAdvice
public class BasicExceptionHandler {
    /**
     * ResponseEntity 封装返回状态码 ，数据结果对象
     *
     * @param e exception
     * @return json
     */
    //当捕获到异常处理
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> errorRuntime(LyException e) {
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResult> errorRuntime(RuntimeException e) {
        return ResponseEntity.status(500).body(new ExceptionResult(500, e.getMessage()));
    }
}
