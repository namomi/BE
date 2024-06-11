package com.minizin.travel.user.jwt;

import com.minizin.travel.user.domain.dto.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 특정 경로 무시 (무한 루프 방지)
        String requestUri = request.getRequestURI();
        if (requestUri.matches("^\\/login(?:\\/.*)?$") ||
                requestUri.matches("^\\/oauth2(?:\\/.*)?$") ||
                requestUri.matches("^\\/auth\\/join(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        // cookie들을 불러온 뒤 Authorization key에 담긴 쿠키를 찾음
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("Authorization")) {
                token = cookie.getValue();
            }
        }

        // 토큰이 유효하다면
        if (StringUtils.hasText(token) && !tokenProvider.isExpired(token)) {
            //토큰에서 username 획득
            String username = tokenProvider.getUsername(token);

            //UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
                    .username(username)
                    .build();

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customOAuth2User, null, null);

            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
