package com.tilon.ojt_back.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.CustomUserDetails;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminMapper adminMapper;

    public CustomUserDetailsService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String empno) throws UsernameNotFoundException {
        AdminResponseDTO user = adminMapper.getUserByEmpno(empno);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + empno);
        }

        return new CustomUserDetails(
            user.getAdminId(),
            user.getEmpno(),
            user.getAdminName(),
            user.getPassword(),
            new ArrayList<>(),
            user.getRole()
        );
    }
}