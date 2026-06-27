package com.aegis.backend.common;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //这是一个全局 Controller 异常处理器(前后端分离要用到)
public class GlobalExceptionHandler {
    //异常抛出后，JSON返回给前端。

}
