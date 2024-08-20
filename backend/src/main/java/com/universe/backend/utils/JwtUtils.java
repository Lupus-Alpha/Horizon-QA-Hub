package com.universe.backend.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.universe.backend.database.domain.Engine;
import com.universe.backend.database.domain.User;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // 验签工具

    private static final String SECRET = "Test_Secret"; // 加密秘钥 动态生成secret

    private static final long PLATFORM_EXPIRATION = 2*60*60L; // 过期时间2小时

    private static final long ENGINE_EXPIRATION = 7*24*60*60L; // 过期时间2小时

    public static String createPlatformToken(User user) {
        // 生成token
        Date expireDate = new Date(System.currentTimeMillis() + PLATFORM_EXPIRATION*1000); //过期时间
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("userId", user.getId())//用户id
                .withClaim("account", user.getAccount())//用户账号
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    public static String createEngineToken(Engine engine) {
        // 生成token
        Date expireDate = new Date(System.currentTimeMillis() + ENGINE_EXPIRATION*1000); //过期时间
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                //可以将基本信息放到claims中
                .withClaim("engineCode", engine.getId())//用户id
                .withClaim("engineSecret", engine.getSecret())//用户账号
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    /**
     * 校验token并解析token
     */
    public static DecodedJWT verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (TokenExpiredException e) {
            // 时间校验出错抛出过期
            throw new TokenExpiredException("token已过期");
        }catch (Exception e) {
            // 解码异常抛出校验出错
            throw new JWTDecodeException("token校验出错");
        }
        // 返回基本信息
        return jwt;
    }
}
