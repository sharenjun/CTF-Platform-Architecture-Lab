package com.aegis.backend.health;

import com.aegis.backend.common.BusinessException;
import com.aegis.backend.common.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
    //适合GlobalExceptionHander完成后
    @GetMapping("/api/test-error")
    public String testError(){
        throw  new BusinessException(ErrorCode.UNAUTHORIZED);
    }
}
