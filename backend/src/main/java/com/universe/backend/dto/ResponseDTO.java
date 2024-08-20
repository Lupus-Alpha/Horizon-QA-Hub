package com.universe.backend.dto;

import com.universe.backend.common.constant.ResponseCode;
import lombok.Data;

@Data
public class ResponseDTO<T> {

    private int status;

    private String message;

    private T data;

    public ResponseDTO(T data) {
        this(ResponseCode.SUCCESS, data);
    }

    public ResponseDTO(ResponseCode resultCode, T data) {
        this.status = resultCode.getStatus();
        this.message = resultCode.getMessage();
        this.data = data;
    }
}
