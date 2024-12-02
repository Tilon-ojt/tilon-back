package com.tilon.ojt_back.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AdminService {
    UserDetails loadUserByUsername(String empno) throws UsernameNotFoundException;
}
