package com.tilon.ojt_back.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AdminService {  // username(UserDetailsService 인터페이스의 일부로서, Spring Security에서 제공)은 user id
    UserDetails loadUserByUsername(String empno) throws UsernameNotFoundException;

    //Spring Security의 UserDetailsService 인터페이스에 정의된 메서드
    //사용자 이름을 기반으로 사용자 정보를 로드하는 역할 (시큐리티에서 이 메서드활용해 사용자 정보를 가져옴)
}


