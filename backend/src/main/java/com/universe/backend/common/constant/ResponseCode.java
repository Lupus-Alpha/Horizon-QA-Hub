package com.universe.backend.common.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(0, "成功"),

    FAILED(1000, "失败"),

    LOGIN_ERROR(1001, "登录失败"),

    EXPIRED_ERROR(1002, "token过期错误"),

    DECODE_ERROR(1003, "token解析错误"),

    TOKEN_NON_ERROR(1004, "token不能为空"),

    FILE_ERROR(1020, "文件相关错误"),

    DUPLICATE_ERROR(1030, "重复校验失败");

    private final int status;
    private final String message;

    ResponseCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
