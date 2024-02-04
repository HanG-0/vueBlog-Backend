package com.vueblog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
//jwt token由头部header 荷载payload 签名部signature组成
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "vueblog.jwt")
public class JwtUtils {

    private String secret;//密钥（如何设置）
    private  long expire;
    private String header;

    /*
    生成jwt token
     */
    public String generateToken(long userId){
        //获取当前时间
        Date nowDate = new Date();
        //计算过期时间
        Date expireDate = new Date(nowDate.getTime()+ expire * 1000);
        //为jwt设置payload信息
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userId+"")//签发人 JWT所有者 通常是username
                .setIssuedAt(nowDate)//签发时间
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,secret)//设置生成签名的算法和秘钥
                .compact();
    }

    /**用过jwt获取用户数据
     *
     * @param token （jwt加密后的）
     * @return claims 返回部分键值对 Map类型
     */
    public Claims getClaimByToken(String token){
        try {
            return Jwts.parser()//得到DefautParser
                    .setSigningKey(secret)//设置签名密钥
                    .parseClaimsJws(token)//设置需要解析的jwt
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error",e);
            return null;
        }
    }

    /*
    token是否过期
    @return true；过期
     */
    public boolean isTokenExpired(Date expiration){
        return expiration.before(new Date());
    }
}
