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
    public UserDetails loadUserByUsername(String empName) throws UsernameNotFoundException {
        logger.info("Attempting to load user by empName: {}", empName);

        AdminResponseDTO user = adminMapper.getUserByEmpName(empName);
        if (user == null) {
            logger.warn("User not found: {}", empName);
            throw new UsernameNotFoundException("User not found: " + empName);
        }

        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            logger.warn("Role is null or empty for user: {}", empName);
            throw new UsernameNotFoundException("Role not found for user: " + empName);
        }

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        logger.info("User found: {}, Role: {}", user.getEmpName(), role);
        logger.info("Granted Authorities: {}", authorities);

        return new CustomUserDetails(
                user.getAdminId(),
                user.getEmpName(),
                user.getNickname(),
                user.getPassword(),
                authorities,
                role);
    }
}