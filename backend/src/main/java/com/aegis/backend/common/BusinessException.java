package com.aegis.backend.common;

import org.junit.jupiter.api.Test;

public class BusinessException extends RuntimeException{
//业务失败时，不能到处throw new RuntimeException("xxx")
//业务异常属于RuntimeException,原因是业务异常不应该要求每一层都写 throws。
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());//继承RuntimeException->exception.getMessage()
        this.errorCode=errorCode;
    }
    public ErrorCode getErrorCode(){
        return errorCode;
        //后面GlobalExceptionHandler需要拿到错误码，exception.getMessage
    }
    /*
 BusinessException 继承 RuntimeException，是为了接入 Java/Spring 的异常处理机制，让它可以被 throw，并且可以被 @ExceptionHandler 捕获。
 BusinessException 保存 ErrorCode，是为了让 GlobalExceptionHandler 能知道具体业务错误类型。
 super(errorCode.getMessage()) 是把中文错误信息传给 RuntimeException，之后可以通过 exception.getMessage() 取到。
    */

}
