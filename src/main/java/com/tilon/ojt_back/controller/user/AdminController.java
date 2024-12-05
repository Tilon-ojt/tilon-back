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
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.service.user.AdminService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //1. super_amdin 권한 필요

    // 어드민 목록 조회
    @GetMapping("/accounts")
    public List<AdminResponseDTO> getAdminList() {
        logger.info("Admin list request received");
        List<AdminResponseDTO> adminList = adminService.getAdminList();
        logger.info("Admin list response: {}", adminList);
        return adminList;
    }

    // 어드민 등록
    @PostMapping("/accounts")
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

    // 계정 비밀번호 초기화 
    @PutMapping("/accounts/{adminId}/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@PathVariable String adminId) {
        logger.info("비밀번호 리셋할 어드민 아이디: {}", adminId);
        try {
            // 비밀번호 초기화 요청을 서비스에 전달
            ResponseEntity<Map<String, Object>> response = adminService.resetPassword(adminId);
            return response;
        } catch (Exception e) {
            logger.error("비밀번호 초기화 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "비밀번호 초기화 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //2. super_admin + admin 권한 필요

    // 어드민 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutAdmin(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "유효한 토큰이 필요합니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출
        logger.info("Admin logout request received for token: {}", token);
        try {
            jwtTokenProvider.invalidateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "성공적으로 로그아웃되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during admin logout: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그아웃 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}