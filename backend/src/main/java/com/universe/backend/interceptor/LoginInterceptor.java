package com.universe.backend.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.universe.backend.common.constant.RequestPath;
import com.universe.backend.common.exception.TokenNonException;
import com.universe.backend.database.domain.User;
import com.universe.backend.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {
    //拦截器，登录拦截请求，判断是否有token，如果没有token则抛出异常
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path=request.getServletPath();
        if(path.matches(RequestPath.LOGIN_PATH.value) || path.matches(RequestPath.ENGINE_TOKEN_PATH.value)) {
            return true;
        }else if (path.matches(RequestPath.ENGINE_PATH.value)){
            String token=request.getHeader("token");
            if(token==null){
                new TokenNonException("token不能为空");
            }
            JwtUtils.verifyToken(token);
            return true;
        }else {
            String token = request.getHeader("token");
            if (token == null){
                throw new TokenNonException("token不能为空");
            }
            DecodedJWT jwt = JwtUtils.verifyToken(token);
            request.getSession().setAttribute("userId", jwt.getClaim("userId").asString());
            request.getSession().setAttribute("account", jwt.getClaim("account").asString());
            Date createDate = jwt.getIssuedAt();
            if((new Date(System.currentTimeMillis() - 10*60*1000)).after(createDate)){
                User user = new User();
                user.setId(jwt.getClaim("userId").asString());
                user.setAccount(jwt.getClaim("account").asString());
                String newToken = JwtUtils.createPlatformToken(user);
                response.addHeader("token", newToken);
            }
            return true;
        }

    }
    }
