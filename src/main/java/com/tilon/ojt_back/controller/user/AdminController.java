package com.tilon.ojt_back.controller.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.user.AdminRequestDTO;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.service.user.AdminServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/user/admin")
    public List<AdminResponseDTO> getMethodName() {
        return adminServiceImpl.getAdminList();
    }

    @PostMapping("/super/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        // 어드민 등록 처리
        //adminServiceImpl.registerAdmin(adminRequestDTO);
        
        return ResponseEntity.ok("어드민이 성공적으로 등록되었습니다.");
    }

    @PostMapping("/user/admin/login") 
     public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDTO loginDTO) {
        // 로그인 메서드 호출
        ResponseEntity<Map<String, Object>> responseEntity = adminServiceImpl.login(loginDTO);

        // 로그인 서비스에서 반환된 응답을 그대로 반환
        return responseEntity; // JSON 형식으로 응답 반환
    }
    

}