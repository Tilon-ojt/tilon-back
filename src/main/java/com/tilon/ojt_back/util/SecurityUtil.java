package com.tilon.ojt_back.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tilon.ojt_back.exception.NoUserAuthorizationException;

@Component
public class SecurityUtil {
    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 현재 인증 정보 가져오기
        System.out.println("authentication.getName() = " + authentication.getName()); // 인증 정보 출력
        if (authentication.getName().equals("anonymousUser") || authentication.getName() == null) { // 인증 정보가 없거나 익명
                                                                                                    // 사용자일 경우 로그인 토큰이
                                                                                                    // 없을 시
            throw new NoUserAuthorizationException("유저 인증 정보가 없습니다. 다시 로그인 해주세요.");
        }
        return Long.parseLong(authentication.getName()); // 사용자 ID 반환
    }
}
