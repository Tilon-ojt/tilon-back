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
                errorResponse(response);
                return;
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response);
            return;
        }

        // jwtTokenProvider를 사용해 토큰에서 사용자 정보 추출 : 정보 인증을 위해 사용
        CustomUserDetails userDetails = jwtTokenProvider.getUserDetailsFromToken(token);

        // // 추가된 코드: ADMIN 역할이지만 SUPER_ADMIN이 아닌 경우 예외 처리
        // if (userDetails.getAuthorities().stream().anyMatch(auth ->
        // auth.getAuthority().equals("ROLE_ADMIN")) &&
        // userDetails.getAuthorities().stream()
        // .noneMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
        // logger.warn("ADMIN 역할이지만 SUPER_ADMIN이 아님: {}", requestURI);
        // errorResponse(response, "SUPER_ADMIN 권한이 필요합니다.");
        // return;
        // }

        // 요청된 엔드포인트에 필요한 역할을 사용자가 가지고 있는지 확인
        if (requestURI.startsWith("/admin/account/") &&
                userDetails.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            logger.warn("SUPER_ADMIN이 아닌 사용자의 접근 거부: {}", requestURI);
            errorResponse(response, "SUPER_ADMIN 권한이 필요합니다.");
            return;
        }

        if (requestURI.startsWith("/admin/post/") &&
                userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                        || auth.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            logger.warn("ADMIN이 아닌 사용자의 접근 거부: {}", requestURI);
            errorResponse(response, "ADMIN 권한이 필요합니다.");
            return;
        }

        // 로그아웃 요청은 특별한 역할 검증 없이 인증된 사용자라면 허용
        if (requestURI.equals("/admin/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 토큰 생성 : 사용자 정보와 권한을 포함하고 있는 객체 생성(이 객체는 시큐리티 인증 매커니즘에 사용된다더라)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 인증토큰에 요청에 대한 세부정보
                                                                                                    // 설정(추가 정보 제공위함)

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 인증 토큰을 시큐리티 컨테스트에 저장 -> 이후 요청 처리에서
                                                                                   // 인증된 사용자 정보 사용

        logger.info("Authentication 인증 정보 로깅 : {}", SecurityContextHolder.getContext().getAuthentication());

        // 다음 필터 요청 전달 (필터체인 진행 )
        filterChain.doFilter(request, response);

        logger.info("처리중인  for URI: {}", requestURI);
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