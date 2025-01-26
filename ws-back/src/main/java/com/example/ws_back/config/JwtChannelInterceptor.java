package com.example.ws_back.config;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.ws_back.security.CustomUserDetailsService;
import com.example.ws_back.security.JwtUtil;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("웹소켓을 연결하기 위해 토큰 검증합니다.");
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            String token = authorizationHeader.substring(7);
            log.info("WEBSOCKET CONNECT TOKEN :" + token);
            // 토큰 검증
            if (token == null || !jwtUtil.validateToken(token)) {
                throw new AccessDeniedException("토큰이 없거나 검증할 수 없습니다.");
            }

            // 사용자 정보 가져오기
            String userId = jwtUtil.getUserId(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

            // 인증 객체 생성 및 SecurityContext 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            accessor.setUser(authentication);
        }

        return message;
    }
}
