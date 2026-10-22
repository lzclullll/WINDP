package com.energy.platform.common;


public class BusinessException extends RuntimeException {
    private final int code;
    public BusinessException(String message) { super(message); this.code = 400; }
    public BusinessException(ErrorCode errorCode) { super(errorCode.message); this.code = errorCode.code; }
    public int getCode() { return code; }
}
