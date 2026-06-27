package com.aegis.backend.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class BusinessExceptionTest {
    @Test
    void businessExceptionShouldCarryErrorCode(){
        BusinessException exception = new BusinessException(ErrorCode.UNAUTHORIZED);
        assertEquals(ErrorCode.UNAUTHORIZED,exception.getErrorCode());
        assertEquals("UNAUTHORIZED",exception.getErrorCode().getCode());
        assertEquals("未登录",exception.getMessage());
        System.out.println("success");
    }
}
