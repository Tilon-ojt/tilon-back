package com.tilon.ojt_back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tilon.ojt_back.domain.CustomUserDetails;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청 당 한번만 실행
    private final JwtTokenProvider jwtTokenProvider; // JwtTokenProvider 인스턴스 주입
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // SecurityConfig에서 허용된 경로는 필터를 통과하도록 설정
        if (requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/swagger-resources/") ||
                requestURI.startsWith("/user/") ||
                requestURI.startsWith("/static/")) { // 정적 리소스 경로 필터 통과
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
            return; // 필터 체인 진행 중단
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Authorization 헤더가 없거나 Bearer로 시작하지 않는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            errorResponse(response); // 오류 응답 호출
            return; // 필터 체인 진행 중단
        }

        String token = authorizationHeader.split(" ")[1]; // Bearer 이후의 토큰 추출
        System.out.println("token = " + token);

        // 토큰이 Access Token인지 확인
        try {
            if (!jwtTokenProvider.isAccessToken(token)) { // JwtTokenProvider 인스턴스를 사용
                errorResponse(response); // Access Token이 아닌 경우 오류 응답 호출
                return; // 필터 체인 진행 중단
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 토큰 검증 실패 시 오류 응답
            return;
        }

        try {
            if (jwtTokenProvider.isExpired(token)) { // JwtTokenProvider 인스턴스를 사용
                throw new JwtTokenProvider.TokenValidationException("Token expired");
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 만료된 토큰 시 오류 응답
            return;
        }

        // Access Token인 경우 처리
        CustomUserDetails userDetails = jwtTokenProvider.getUserDetailsFromToken(token); // CustomUserDetails 추출

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities() // 인증 토큰 생성
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 세부 정보 설정

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 인증 토큰 설정
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달

        logger.info("Processing request for URI: {}", requestURI);
    }

    // 토큰 자체에 문제가 있을 때 리턴
    private void errorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + "인증 실패" + "\"}");
    }
}