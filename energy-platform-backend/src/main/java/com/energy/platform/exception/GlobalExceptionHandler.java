package com.energy.platform.exception;


import com.energy.platform.common.BusinessException;
import com.energy.platform.common.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> business(BusinessException e) {
        return ResponseEntity.status(e.getCode()).body(Result.fail(e.getCode(), e.getMessage()));
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<Result<Void>> validation(Exception e) {
        return ResponseEntity.badRequest().body(Result.fail(400, "请求参数不符合要求"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Void>> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.fail(403, "无访问权限"));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<Void>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.fail(401, "未登录或登录已过期"));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> system(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.fail(500, "系统处理失败，请稍后重试"));
    }
}
