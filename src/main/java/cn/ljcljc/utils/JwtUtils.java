package cn.ljcljc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

import static cn.ljcljc.utils.StringToMapUtils.mapStringToMap;

/**
 * Jwt令牌工具类
 * @author 李锦成
 * @version 1.0(2023.06)
 */

public class JwtUtils {

    private static String signKey = "ljcsys2023.%T$!HMQglpEIgBxu";
    private static Long expire = 43200000L;

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    /**
     * 校验JWT令牌
     * @param token JWT令牌
     * @return 从JWT令牌中解析出的用户id
     */
    public static String verifyUser(String token) {
        String claim = parseJWT(token).toString();

        Map<String, String> newMap = mapStringToMap(claim);

        String uid = newMap.get("uid");

        return uid;
    }
}
