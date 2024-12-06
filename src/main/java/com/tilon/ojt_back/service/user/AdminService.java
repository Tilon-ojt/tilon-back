package com.tilon.ojt_back.service.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tilon.ojt_back.controller.user.AdminController;
import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.CustomUserDetails;
import com.tilon.ojt_back.domain.user.AdminRequestDTO;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.AdminUpdateDTO;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${default-password}")
    private String DEFAULT_PASSWORD;

    // 1. 어드민 목록 조회
    public List<AdminResponseDTO> getAdminList() {
        return adminMapper.getAdminList();

    }

    // 2. 어드민 로그인
    public ResponseEntity<Map<String, Object>> login(LoginDTO loginDTO) {
        try {
            // 로그 추가: 로그인 시도
            System.out.println("로그인 시도: " + loginDTO.getEmpName());

            AdminResponseDTO user = adminMapper.getUserByEmpName(loginDTO.getEmpName());
            if (user == null) {
                // 로그 추가: 사용자 정보 없음
                System.out.println("사용자 정보 없음: " + loginDTO.getEmpName());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "사원번호 혹은 비밀번호가 틀렸습니다."));
            }

            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                // 로그 추가: 비밀번호 불일치
                System.out.println("비밀번호 불일치: " + loginDTO.getEmpName());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "사원번호 혹은 비밀번호가 틀렸습니다."));
            }

            CustomUserDetails userDetails = new CustomUserDetails(
                    user.getAdminId(),
                    user.getEmpName(),
                    user.getPassword(),
                    user.getNickname(),
                    new ArrayList<>(),
                    user.getRole());

            String token = jwtTokenProvider.createAccessToken(userDetails);

            // 로그 추가: 토큰 생성 성공
            System.out.println("토큰 생성 성공: " + user.getEmpName());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("adminId", user.getAdminId());
            response.put("role", user.getRole());
            response.put("empName", user.getEmpName());
            response.put("nickname", user.getNickname());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리: 로그 추가
            System.err.println("로그인 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "로그인 처리 중 오류가 발생했습니다."));
        }
    }

    // 3. 어드민 등록
    public ResponseEntity<Map<String, Object>> registerAdmin(AdminRequestDTO adminRequestDTO) throws Exception {
        try {
            // 어드민 존재 여부 확인
            AdminResponseDTO existingAdmin = adminMapper.getUserByEmpName(adminRequestDTO.getEmpName());
            if (existingAdmin != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message", "이미 존재하는 어드민입니다."));
            }

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
            adminRequestDTO.setPassword(encodedPassword);

            // 매퍼를 통해 어드민 등록
            adminMapper.insertAdmin(adminRequestDTO);
            System.out.println("어드민 등록 성공: " + adminRequestDTO.getEmpName());

            // 등록된 어드민 정보 조회
            AdminResponseDTO newAdmin = adminMapper.getUserByEmpName(adminRequestDTO.getEmpName());

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "어드민이 성공적으로 등록되었습니다.");
            response.put("admin", newAdmin);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            throw e; // 예외 다시 던지기
        }
    }

    // 4. 비밀번호 초기화

    public ResponseEntity<Map<String, Object>> resetPassword(int adminId) {
        try {
            // 기본 비밀번호를 인코딩
            String encodedPassword = passwordEncoder.encode(DEFAULT_PASSWORD);

            // 매퍼를 통해 비밀번호 초기화
            adminMapper.resetPassword(adminId, encodedPassword);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "비밀번호가 성공적으로 초기화되었습니다.");
            response.put("adminId", adminId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "비밀번호 초기화 중 오류가 발생했습니다."));
        }
    }

    // 5. 비밀번호 동일성 체크

    public Map<String, Object> checkPassword(int adminId, String newPassword) {
        Map<String, Object> response = new HashMap<>();
        try {
            String currentPassword = adminMapper.getCurrentPassword(adminId);
            logger.info("현재 비밀번호: {}", currentPassword);
            logger.info("새로운 비밀번호: {}", newPassword);

            // 암호화된 비밀번호와 평문 비밀번호 비교
            if (passwordEncoder.matches(newPassword, currentPassword)) {
                response.put("status", HttpStatus.OK);
                response.put("message", "비밀번호가 일치합니다.");
            } else {
                response.put("status", HttpStatus.BAD_REQUEST);
                response.put("message", "비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            logger.error("비밀번호 확인 중 오류 발생: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", "비밀번호 확인 중 오류가 발생했습니다.");
        }
        return response;
    }

    // 6. 계정 정보 수정
    public Map<String, Object> updateAdminInfo(int adminId, AdminUpdateDTO adminUpdateDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminUpdateDTO.setAdminId(adminId);

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(adminUpdateDTO.getPassword());
            adminUpdateDTO.setPassword(encodedPassword);

            logger.info("adminUpdateDTO: adminId-{}, password-{}, nickname-{}", adminUpdateDTO.getAdminId(),
                    adminUpdateDTO.getPassword(), adminUpdateDTO.getNickname());
            adminMapper.updateAdminInfo(adminUpdateDTO);

            response.put("status", HttpStatus.OK);
            response.put("message", "어드민 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            logger.error("어드민 정보 수정 중 오류 발생: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", "어드민 정보 수정 중 오류가 발생했습니다.");
        }
        return response;
    }

    // 7. 어드민 삭제

    @Transactional
    public Map<String, Object> deleteAdmins(List<Integer> adminIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminMapper.deleteByAdminIds(adminIds);
            response.put("message", "어드민 삭제가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            logger.error("어드민 삭제 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("어드민 삭제 중 오류가 발생했습니다.");
        }
        return response;
    }

}
