package com.aegis.backend.common;

public enum ErrorCode {
/*
  code：返回给前端看的机器码，比如 USERNAME_ALREADY_EXISTS
  message：中文提示，比如 用户名已存在
  httpStatus：HTTP 状态码，比如 409
*/
    //只是定义类型，后续给BusinessException/GlobalExceptionHander使用。
    //枚举项
    //真正创建对象的地方
    OK("OK","成功"),
    VALIDATION_ERROR("VALIDATION_ERROR","请求参数不合法"),
    UNAUTHORIZED("UNAUTHORIZED","未登录"),
    INTERNAL_ERROR("INTERNAL_ERROR","系统异常");

    //定义-不允许修改。(错误码不应该在运行时被更改。)
    private final String code;
    private final String message;
    //把系统里所有“固定的错误类型”集中定义起来
    //enum的构造方法不能是public
    ErrorCode(String code,String message){
        this.code=code;
        this.message=message;
    }
    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}
