package com.universe.backend.response;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.backend.common.exception.DemoException;
import com.universe.backend.dto.ResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {"com.universe.backend.controller"})  // 用来拦截响应
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型是二进制，返回false
        return !aClass.equals(ByteArrayHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<?
            extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            //ObjectMapper对象，它是Jackson库中的核心类，用于在Java对象和JSON之间进行转换
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResponseTemp里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new ResponseDTO<>(data));
            } catch (JsonProcessingException e) {
                throw new DemoException("String类型返回错误");
            }
        }
        // 将原本的数据包装在ResponseTemp里
        return new ResponseDTO<>(data);
    }
}
