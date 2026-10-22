package com.energy.platform.common;


public enum ErrorCode {
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无访问权限"),
    NOT_FOUND(404, "资源不存在"),
    BUSINESS_ERROR(500, "业务处理失败");
    public final int code;
    public final String message;
    ErrorCode(int code, String message) { this.code = code; this.message = message; }
}
