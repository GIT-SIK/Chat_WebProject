package com.example.ws_back.config;

import com.example.ws_back.security.JwtAuthFilter;
import com.example.ws_back.security.JwtUtil;
import com.example.ws_back.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    //** accessDenied / AuthEntryPoint 핸들러 **/
    // private final CustomAccessDeniedHandler accessDeniedHandler;
    // private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 미인증 사용자까지 허용할 주소 리스트
     */
    private static final String[] AUTH_WHITELIST = {
        "/logout", "/login", "/api/**"
    };	

    private static final String[] AUTH_REQUIRED_LIST = {
    	"/api/auth/**"
    		
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF 설정
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정
            .cors(Customizer.withDefaults())
            
            // 세션 설정
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 기본 로그인 비활성화
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // JWT 필터 추가
            .addFilterBefore(
                new JwtAuthFilter(customUserDetailsService, jwtUtil),
                UsernamePasswordAuthenticationFilter.class
            )
            
            // 예외 처리 (주석 처리된 부분)
            // .exceptionHandling(exception -> exception
            //     .authenticationEntryPoint(authenticationEntryPoint)
            //     .accessDeniedHandler(accessDeniedHandler)
            // )
            
            // 권한 설정
            .authorizeHttpRequests(authorize -> authorize
            	.requestMatchers(AUTH_REQUIRED_LIST).authenticated()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().denyAll() 
                // .anyRequest().authenticated()
            )
            
            .build();
    }
}