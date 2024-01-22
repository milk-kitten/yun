package com.mpy.server.config.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    //荷载 用户名的key
    private static final String CLAIM_KEY_USERNAME = "sub";
    //jwt的创建时间
    private static final String CLAIM_KEY_CREATED = "created";

    //jwt 的密钥以及失效时间，通过刚刚的配置目录去拿。通过value注释
    @Value("${jwt.secret}")
    private String secret;
    //失效时间
    @Value("${jwt.expiration}")
    private Long expiration;



    /*
    1.根据用户名生成token
    2.根据token拿到用户名
    3。判断token是否失效
    4.判断token是否能被刷新
    5.刷新token
     */

    /**
     * 1.根据用户名信息生成token
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        //将荷载存入
        return generateToken(claims);
    }

    /**
     * 2.从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username = null;
        try {
            //先获取荷载，因为登录用户名是放在荷载中的
            Claims claims = getClaimsFromToken(token);
            //拿到荷载，通过荷载拿到登录用户名
            username = claims.getSubject();
        } catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 2.1 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //密钥放进去
                    .setSigningKey(secret)
                    //转荷载
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    /**
     * 3.判断token是否有效
     *  1.是否过期
     *  2.token荷载的用户名和userDetails 的用户名是否一致
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        //获取用户名
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpiration(token);
    }

    /**
     * 3.1 判断token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpiration(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        //判断：失效时间：是否在当前时间的前面
        return expireDate.before(new Date());
    }

    /**
     * 3.2 从token中获取失效时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        //从token中获取荷载，因为过期时间在荷载里面
        Claims claims = getClaimsFromToken(token);
        //过期时间
        return claims.getExpiration();
    }

    /**
     * 4.判断token是否能被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        //如果过期了就可以被刷新
        return !isTokenExpiration(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String RefreshToken(String token){
        //刷新就是把过期时间更改一下
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }


    /**
     * 1. 根据荷载生成Jwt token
     * 参数是荷载
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                //传入荷载
                .setClaims(claims)
                //设定过期时间
                .setExpiration(generateExpirationDate())
                //签名算法，密钥
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
