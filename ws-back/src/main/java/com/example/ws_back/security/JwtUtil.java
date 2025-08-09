package com.example.ws_back.security;

import com.example.ws_back.usr.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.crypto.SecretKey;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtUtil {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1 * 60 * 1000L; // 15분 (임시 1분)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    private final  RedisTemplate<String, Object> redisTemplate;
    private final SecretKey key;
   

    public JwtUtil(@Value("${jwt.secret}") String secretKey, RedisTemplate<String, Object> redisTemplate) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    /**
     * Access Token, RefreshToken 생성
     * @param userDto
     * @return Token String
     */
    public String createAccessToken(UserDto userDto) {
        return createAccessToken(userDto, ACCESS_TOKEN_EXPIRE_TIME);
    }
    public String createRefreshToken(UserDto userDto) {
        return createRefreshToken(userDto, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * JWT 생성
     * @param userDto
     * @param expireTime
     * @return JWT String
     */
    private String createAccessToken(UserDto userDto, long expireTime) {
        ZonedDateTime now = ZonedDateTime.now(); 
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime / 1000); 
        return Jwts.builder()
                .claim("uuid", userDto.getUserUuid())       
                .claim("isAdmin", userDto.getIsAdmin())     
                .claim("type", "access")
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }
    
    private String createRefreshToken(UserDto userDto, long expireTime) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime / 1000);
        return Jwts.builder()
                .claim("uuid", userDto.getUserUuid())
                .claim("issuedAt", now.toInstant().toEpochMilli())
                .claim("expiration", tokenValidity.toInstant().toEpochMilli())
                .issuedAt(Date.from(now.toInstant()))   
                .expiration(Date.from(tokenValidity.toInstant()))
                .claim("type", "refresh")
                .signWith(key)
                .compact();
    }
    
    

    /**
     * Token에서 UserUUID 추출
     * @param token
     * @return getUserUuid
     */
    public String getUserUuid(String token) {
        return parseClaims(token).get("uuid", String.class);
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
        	
        	Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        	
            String blacListKey = "blacktoken:" + token;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacListKey))) {
                log.warn("블랙리스트 등록된 토큰 입니다.");
                return false;
            }
        	
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    /**
     * JWT AccessToken 유효시간 추출
     * @param accessToken
     * @return Long Time
     */
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        long tokenExpSeconds = (expiration.getTime()-now) / 1000;
    	long tokenExpMinute = tokenExpSeconds / 60;
    	long tokenExpHour = tokenExpMinute / 60;
    	long tokenExpDay = tokenExpHour / 24;
    	System.out.println("토큰 유효 시간 : " + 
    			tokenExpDay +"일 " + 
    			tokenExpHour % 24 +"시간 " + 
    			tokenExpMinute % 60+ "분 " + 
    			tokenExpSeconds % 60 + "초"
    			);
        
        return (expiration.getTime() - now);
    }
}