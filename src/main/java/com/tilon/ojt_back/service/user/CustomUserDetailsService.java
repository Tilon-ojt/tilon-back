package com.tilon.ojt_back.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.CustomUserDetails;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;

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

        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new CustomUserDetails(
                user.getAdminId(),
                user.getEmpno(),
                user.getAdminName(),
                user.getPassword(),
                authorities,
                role);
    }
}