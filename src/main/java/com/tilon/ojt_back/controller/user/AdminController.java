package com.tilon.ojt_back.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.user.AdminRequestDTO;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.oginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.service.user.AdminService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/admin/list")
    public List<AdminResponseDTO> getAdminList() {
        logger.info("Admin list request received");
        List<AdminResponseDTO> adminList = adminService.getAdminList();
        logger.info("Admin list response: {}", adminList);
        return adminList;
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, Object>> registerAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        logger.info("Admin register request received: {}", adminRequestDTO);
        try {
            ResponseEntity<Map<String, Object>> response = adminService.registerAdmin(adminRequestDTO);
            logger.info("Admin register response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error during admin registration: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "어드민 등록 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody oginDTO loginDTO) {
        try {
            // 로그인 메서드 호출
            ResponseEntity<Map<String, Object>> responseEntity = adminService.login(loginDTO);
            // 로그인 서비스에서 반환된 응답을 그대로 반환
            return responseEntity; // JSON 형식으로 응답 반환
        } catch (Exception e) {
            // 예외 처리: 로그 추가
            System.err.println("로그인 중 오류 발생: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}