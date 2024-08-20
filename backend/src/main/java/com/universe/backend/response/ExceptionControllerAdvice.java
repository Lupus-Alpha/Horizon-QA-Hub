package com.universe.backend.response;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.universe.backend.common.constant.ResponseCode;
import com.universe.backend.common.exception.*;
import com.universe.backend.dto.ResponseDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // 用来接收没有捕获的异常 从而定义异常处理方法
public class ExceptionControllerAdvice {

    @ExceptionHandler(DemoException.class)  // 定义异常发生时处理办法
    public ResponseDTO<String> DemoExceptionHandler(DemoException e) {
        // 未知的失败
        return new ResponseDTO<>(ResponseCode.FAILED, e.getMessage());
    }

    @ExceptionHandler(LoginException.class)  // 定义异常发生时处理办法
    public ResponseDTO<String> LoginExceptionHandler(LoginException e) {
        // 未知的失败
        return new ResponseDTO<>(ResponseCode.LOGIN_ERROR, e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseDTO<String> TokenExpireExceptionHandler(TokenExpiredException e) {
        return new ResponseDTO<>(ResponseCode.EXPIRED_ERROR, e.getMessage());
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseDTO<String> PasswordVerifyExceptionHandler(JWTDecodeException e) {
        return new ResponseDTO<>(ResponseCode.DECODE_ERROR, e.getMessage());
    }

    @ExceptionHandler(TokenNonException.class)
    public ResponseDTO<String> TokenNonExceptionHandler(TokenNonException e) {
        return new ResponseDTO<>(ResponseCode.TOKEN_NON_ERROR, e.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public ResponseDTO<String> FileExceptionHandler(FileException e) {
        return new ResponseDTO<>(ResponseCode.FILE_ERROR, e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseDTO<String> DuplicateExceptionHandler(DuplicateException e) {
        return new ResponseDTO<>(ResponseCode.DUPLICATE_ERROR, e.getMessage());
    }


}
