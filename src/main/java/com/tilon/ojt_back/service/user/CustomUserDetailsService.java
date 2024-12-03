package com.tilon.ojt_back.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final AdminMapper adminMapper;

    public CustomUserDetailsService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String empno) throws UsernameNotFoundException {
        logger.info("Attempting to load user by empno: {}", empno);
        
        AdminResponseDTO user = adminMapper.getUserByEmpno(empno);
        if (user == null) {
            logger.warn("User not found: {}", empno);
            throw new UsernameNotFoundException("User not found: " + empno);
        }

        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            logger.warn("Role is null or empty for user: {}", empno);
            throw new UsernameNotFoundException("Role not found for user: " + empno);
        }

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        logger.info("User found: {}, Role: {}", user.getAdminName(), role);
        logger.info("Granted Authorities: {}", authorities);

        return new CustomUserDetails(
                user.getAdminId(),
                user.getEmpno(),
                user.getAdminName(),
                user.getPassword(),
                authorities,
                role);
    }
}