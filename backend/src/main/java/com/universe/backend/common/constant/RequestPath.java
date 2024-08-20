package com.universe.backend.common.constant;

public enum RequestPath {
    LOGIN_PATH("^/auto_test/login$"),

    ENGINE_TOKEN_PATH("^/openapi/engine/token/apply$"),

    ENGINE_PATH("^/openapi/engine/.+$");

    public String value;

    RequestPath(String value) {
        this.value = value;
    }

}
