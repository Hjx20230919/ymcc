package cn.com.cnpc.cpoa.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * 应用授权令牌
 * @author scchenyong@189.cn
 * @create 2019-03-04
 */
public class AppToken {


    public static final String USER_KEY = "user";
    public static final String TIMEOUT_KEY = "timeout";
    public static final String CAPTCHA_KEY = "captcha";
    public static final String API_TOKEN_KEY = "___CPOA_A1B2C3___";

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(API_TOKEN_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    public static String create(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
    }

    public static String create(Map<String, Object> claims, long timeOut) {
        long nowMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(nowMillis + timeOut))
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
    }

    public static Map<String, Object> parser(String jwt) {
        try {
            Map<String, Object> jwtClaims = Jwts.parser()
                    .setSigningKey(getKeyInstance())
                    .parseClaimsJws(jwt).getBody();
            return jwtClaims;
        } catch (Exception e) {
            return null;
        }
    }

}