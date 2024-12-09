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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        logger.info("들어오는 요청 URI: {}", requestURI);

        // /admin/ 경로 제외하고 모든 요청을 허용해 필터 체인 진행
        if (!requestURI.startsWith("/admin/")) {
            logger.info("{} 요청은 인증 없이 허용됌!", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("Authorization header: {}", authorizationHeader);

        // AuthorizationHeader가 없거나 Bearer로 시작하지 않는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization 헤더가 없거나 Bearer로 시작하지 않음! : {}", requestURI);
            errorResponse(response);
            return;
        }

        // Bearer 이후의 토큰 추출
        String token = authorizationHeader.split(" ")[1];

        // Access 토큰이 아니거나 만료된 경우
        try {
            if (!jwtTokenProvider.isAccessToken(token) || jwtTokenProvider.isExpired(token)) {
                logger.warn("유효하지 않거나 만료된 토큰: {}", token);
                errorResponse(response);
                return;
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            logger.error("토큰 검증 중 오류 발생: {}", e.getMessage());
            errorResponse(response);
            return;
        }

        // jwtTokenProvider를 사용해 토큰에서 사용자 정보 추출
        CustomUserDetails userDetails = jwtTokenProvider.getUserDetailsFromToken(token);
        logger.info("사용자 정보 추출 성공: {}", userDetails.getUsername());

        // 요청된 엔드포인트에 필요한 역할을 사용자가 가지고 있는지 확인
        if (requestURI.startsWith("/admin/accounts") &&
                userDetails.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            logger.warn("SUPER_ADMIN이 아닌 사용자의 접근 거부: {}", requestURI);
            errorResponse(response, "SUPER_ADMIN 권한이 필요합니다.");
            return;
        }

        if (requestURI.startsWith("/admin/posts") &&
                userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            logger.warn("ADMIN이 아닌 사용자의 접근 거부: {}", requestURI);
            errorResponse(response, "ADMIN 권한이 필요합니다.");
            return;
        }

        // /admin/update 및 /admin/check-password 경로에 대한 접근 권한 확인
        if ((requestURI.startsWith("/admin/update")) &&
                userDetails.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            logger.warn("ADMIN이 아닌 사용자의 접근 거부: {}", requestURI);
            errorResponse(response, "ADMIN 권한이 필요합니다.");
            return;
        }

        // 로그아웃 요청 처리
        if (requestURI.equals("/admin/logout")) {
            logger.info("로그아웃 요청 처리 중...");

            // 사용자 권한 확인
            if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                    || auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
                logger.warn("로그아웃 권한이 없는 사용자: {}", userDetails.getUsername());
                errorResponse(response, "로그아웃 권한이 필요합니다.");
                return;
            }

            // 로그아웃 요청 시에도 토큰 유효성 검사 추가
            if (jwtTokenProvider.isExpired(token)) {
                logger.warn("만료된 토큰으로 로그아웃 요청: {}", requestURI);
                errorResponse(response);
                return;
            }
        }

        // 리프레시 토큰 검증 및 재발급
        if (requestURI.equals("/admin/refresh-token")) {
            String refreshToken = request.getHeader("Refresh-Token");
            if (refreshToken != null && jwtTokenProvider.validateRefreshToken(refreshToken)) {
                CustomUserDetails refreshUserDetails = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
                String newAccessToken = jwtTokenProvider.createAccessToken(refreshUserDetails);
                response.setHeader("Access-Token", newAccessToken);
                logger.info("새로운 액세스 토큰 발급: {}", newAccessToken);
            } else {
                logger.warn("유효하지 않은 리프레시 토큰: {}", refreshToken);
                errorResponse(response, "유효하지 않은 리프레시 토큰입니다.");
                return;
            }
        }

        // 인증 토큰 생성 및 SecurityContext 설정
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        logger.info("Authentication 인증 정보 로깅 : {}", SecurityContextHolder.getContext().getAuthentication());

        // 다음 필터 요청 전달
        filterChain.doFilter(request, response);

        logger.info("처리중인 URI: {}", requestURI);
    }

    private void errorResponse(HttpServletResponse response) throws IOException {
        errorResponse(response, "인증 정보가 필요합니다.");
    }

    private void errorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}