package com.tilon.ojt_back.service.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.CustomUserDetails;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;

@Service
public class AdminServiceImpl  {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //1. 어드민 목록 조회
    public List<AdminResponseDTO> getAdminList() {
        return adminMapper.getAdminList();

    }

    //2. 어드민 로그인 
    public ResponseEntity<Map<String, Object>> login(LoginDTO loginDTO) {
        AdminResponseDTO user = adminMapper.getUserByEmpno(loginDTO.getEmpno());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("message", "이메일 혹은 비밀번호가 틀렸습니다."));
        }

        CustomUserDetails userDetails = new CustomUserDetails(
            user.getAdminId(),
            user.getEmpno(),
            user.getAdminName(),
            user.getPassword(),
            new ArrayList<>(),
            user.getRole()
        );

        String token = jwtTokenProvider.createAccessToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("adminId", user.getAdminId());

        return ResponseEntity.ok(response);
    }

}
