package cn.com.cnpc.cpoa.core;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用授权令牌
 *
 * @author mail@chenyong.name
 * @create 2018-06-19
 */
@Component
public class AppToken {

    public static final String USER_KEY = "user";
    public static final String CONT_KEY = "cont";
    public static final String ACCESS_KEY = "access";

    public static final String  TIMEOUT_KEY = "timeout";
 //   private static final String TOKEN_KEY = "____________________cpoa_api";
    private static final Log log = LogFactory.getLog(AppToken.class);


    private static  String TOKEN_KEY;

    private static final int AUTH_TIMEOUT = 1 * 60 * 60 * 1000;
    private static final int AUTH_DELAY = 10 * 60 * 1000;

    public static String getByUserId(String userId) {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(AppToken.USER_KEY, userId);
        long tokenTimeout = System.currentTimeMillis() + AUTH_TIMEOUT;
        keyMap.put(AppToken.TIMEOUT_KEY, tokenTimeout);
        //先随意放置两个值
        keyMap.put(AppToken.CONT_KEY, AppToken.CONT_KEY);
        keyMap.put(AppToken.ACCESS_KEY, AppToken.CONT_KEY);
        return AppToken.create(keyMap, tokenTimeout + AUTH_DELAY);
    }

    public static String getByContId(String contId,String accessId) {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(AppToken.CONT_KEY, contId);
        keyMap.put(AppToken.ACCESS_KEY, accessId);
        long tokenTimeout = System.currentTimeMillis() + AUTH_TIMEOUT;
        keyMap.put(AppToken.TIMEOUT_KEY, tokenTimeout);
        return AppToken.create(keyMap, tokenTimeout + AUTH_DELAY);
    }

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(TOKEN_KEY);
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
            log.error(String.format("json web token verify failed! token: %s", jwt));
            return null;
        }
    }

    public static boolean isSigned(String jwt) {
        try {
            return Jwts.parser().setSigningKey(getKeyInstance()).isSigned(jwt);
        } catch (Exception e) {
            log.error(String.format("json web token verify failed! token: %s", jwt));
            return false;
        }
    }

    @Value("${jwt.tokenKey}")
    public void setTokenKey(String tokenKey) {
        TOKEN_KEY = tokenKey;
    }
}
