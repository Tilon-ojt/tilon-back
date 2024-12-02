package com.tilon.ojt_back.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.service.user.AdminService;

@Service
public class AdminServiceImpl {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public List<AdminResponseDTO> getAdminList() {
        // test 완료 디비 세팅 끝!
        return adminMapper.getAdminList();

    }

}
