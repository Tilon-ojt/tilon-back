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
import com.tilon.ojt_back.domain.user.AdminUpdateDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.service.user.AdminService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 1. super_amdin 권한 필요

    // 어드민 목록 조회
    @GetMapping("/accounts")
    public List<AdminResponseDTO> getAdminList() {
        return adminService.getAdminList();
    }

    // 어드민 등록
    @PostMapping("/accounts")
    public ResponseEntity<Map<String, Object>> registerAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        logger.info("Admin register request received: {}", adminRequestDTO);
        ResponseEntity<Map<String, Object>> response = adminService.registerAdmin(adminRequestDTO);
        logger.info("Admin register response: {}", response);
        return response;
    }

    // 계정 비밀번호 초기화
    @PutMapping("/accounts/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, Object> payload) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);
        return adminService.resetPassword(token, payload);
    }

    // 2. super_admin + admin 권한 필요

    // 어드민 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutAdmin(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader("Refresh-Token") String refreshTokenHeader) {

        // 토큰 유효성 검사
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        if (refreshTokenHeader == null || !jwtTokenProvider.validateRefreshToken(refreshTokenHeader)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String accessToken = authorizationHeader.substring(7);
        logger.info("로그아웃을 요청한 액세스 토큰: {}", accessToken);
        logger.info("로그아웃을 요청한 리프레시 토큰: {}", refreshTokenHeader);

        // 토큰 무효화 처리
        jwtTokenProvider.invalidateToken(accessToken);
        jwtTokenProvider.invalidateToken(refreshTokenHeader);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 로그아웃되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 어드민 삭제 (비밀번호 확인 추가)
    @DeleteMapping("/account")
    public ResponseEntity<Map<String, Object>> deleteAdmins(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader("Refresh-Token") String refreshTokenHeader,
            @RequestBody Map<String, Object> payload) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);

        Map<String, Object> response = adminService.deleteAdminsWithValidation(token, refreshTokenHeader, payload);
        HttpStatus status = (HttpStatus) response.get("status");
        return ResponseEntity.status(status).body(response);
    }

    // 3. admin 권한 필요

    // 어드민 비밀번호 변경
    @PatchMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAdminInfo(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AdminUpdateDTO adminUpdateDTO) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 유효한 토큰이 필요합니다.
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출

        // 서비스 메서드 호출
        return adminService.updateAdminInfo(adminUpdateDTO, token);
    }

}