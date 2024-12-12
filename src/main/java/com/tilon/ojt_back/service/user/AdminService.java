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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.CustomUserDetails;
import com.tilon.ojt_back.domain.user.AdminRequestDTO;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.AdminUpdateDTO;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.security.JwtTokenProvider;
import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;

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
        List<AdminResponseDTO> adminList = adminMapper.getAdminList();
        if (adminList == null || adminList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return adminList;
    }

    // 2. 어드민 로그인
    public ResponseEntity<Map<String, Object>> login(LoginDTO loginDTO) {
        // 로그 추가: 로그인 시도
        System.out.println("로그인 시도: " + loginDTO.getEmpName());

        AdminResponseDTO user = adminMapper.getUserByEmpName(loginDTO.getEmpName());
        if (user == null) {
            // 로그 추가: 사용자 정보 없음
            System.out.println("사용자 정보 없음: " + loginDTO.getEmpName());
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            // 로그 추가: 비밀번호 불일치
            System.out.println("비밀번호 불일치: " + loginDTO.getEmpName());
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getAdminId(),
                user.getEmpName(),
                user.getPassword(),
                user.getNickname(),
                new ArrayList<>(),
                user.getRole());

        String accessToken = jwtTokenProvider.createAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDetails);

        // 로그 추가: 토큰 생성 성공
        System.out.println("토큰 생성 성공: " + user.getEmpName());

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("adminId", user.getAdminId());
        response.put("role", user.getRole());
        response.put("empName", user.getEmpName());
        response.put("nickname", user.getNickname());

        // 헤더에 리프레시 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Refresh", refreshToken);

        return ResponseEntity.ok().headers(headers).body(response);
    }

    // 3. 어드민 등록
    public ResponseEntity<Map<String, Object>> registerAdmin(AdminRequestDTO adminRequestDTO) {
        // 어드민 존재 여부 확인
        AdminResponseDTO existingAdmin = adminMapper.getUserByEmpName(adminRequestDTO.getEmpName());
        if (existingAdmin != null) {
            throw new CustomException(ErrorCode.CONFLICT); // 이미 존재하는 어드민
        }

        // empName 유효성 검사: 영어만 가능
        if (!isValidEmpName(adminRequestDTO.getEmpName())) {
            throw new CustomException(ErrorCode.INVALID_EMP_NAME); // 유효하지 않은 empName
        }

        // 비밀번호가 null인 경우 디폴트 비밀번호 사용
        String passwordToUse = adminRequestDTO.getPassword() != null ? adminRequestDTO.getPassword() : DEFAULT_PASSWORD;

        // 비밀번호 유효성 검사: 영어, 숫자 조합으로 6자 이상
        if (!isValidPassword(passwordToUse)) {
            throw new CustomException(ErrorCode.BAD_REQUEST); // 유효하지 않은 비밀번호
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(passwordToUse);
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
        response.put("status", HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // empName 유효성 검사 메서드 수정
    private boolean isValidEmpName(String empName) {
        return empName.matches("^[a-zA-Z0-9]*$"); // 영어와 숫자만 포함하는지 확인
    }

    // 비밀번호 유효성 검사 메서드 추가
    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).+$");
    }

    // 4. 비밀번호 초기화

    public ResponseEntity<Map<String, Object>> resetPassword(String token, Map<String, Object> payload) {
        // 권한 검증 : 슈퍼 어드민만 가능
        String userRole = jwtTokenProvider.getUserRoleFromToken(token);
        if (!"ROLE_SUPER_ADMIN".equals(userRole)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 파라미터 검증
        List<Integer> adminIds = (List<Integer>) payload.get("adminIds");
        String password = (String) payload.get("password");

        if (adminIds == null || adminIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_ADMIN_IDS);
        }

        if (password == null || password.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 비밀번호 검증 : 슈퍼 어드민의 비밀번호가 일치하는지
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        if (!validatePassword(userId, password)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 비밀번호 초기화 실행 : 어드민 리스트를 넘겨서 초기화 실행
        String encodedDefaultPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
        int updatedCount = adminMapper.resetPasswords(adminIds, encodedDefaultPassword);

        if (updatedCount != adminIds.size()) {
            throw new CustomException(ErrorCode.PASSWORD_RESET_FAILED);
        }

        // 성공 응답
        Map<String, Object> response = new HashMap<>();
        response.put("message", "비밀번호가 성공적으로 초기화되었습니다.");
        response.put("status", HttpStatus.OK.value());
        response.put("updatedCount", updatedCount);
        return ResponseEntity.ok(response);
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

    // 6. 비밀번호 변경
    public ResponseEntity<Map<String, Object>> updateAdminInfo(AdminUpdateDTO adminUpdateDTO, String token) {
        int adminId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 ID 추출
        String userRole = jwtTokenProvider.getUserRoleFromToken(token); // 역할 추출
        // AdminId 설정 전후 로그 추가
        logger.info("AdminId 설정 전: {}", adminUpdateDTO.getAdminId());
        adminUpdateDTO.setAdminId(adminId);
        logger.info("AdminId 설정 후: {}", adminUpdateDTO.getAdminId());

        if ("ROLE_SUPER_ADMIN".equals(userRole)) {
            throw new CustomException(ErrorCode.FORBIDDEN_SUPER_ADMIN_PASSWORD_CHANGE); // SUPER_ADMIN은 비밀번호를 변경할 수
                                                                                        // 없습니다.
        }

        // 현재 비밀번호 가져오기
        String currentPassword = adminMapper.getCurrentPassword(adminId);
        if (currentPassword == null) {
            throw new CustomException(ErrorCode.NOT_FOUND); // 어드민 정보를 찾을 수 없습니다.
        }

        // 비밀번호 검증
        if (!isValidPassword(adminUpdateDTO.getNewPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST); // 비밀번호는 영문자와 숫자의 조합으로 6자 이상이어야 합니다.
        }

        // 현재 비밀번호와 입력된 비밀번호 비교
        if (!passwordEncoder.matches(adminUpdateDTO.getCurrentPassword(), currentPassword)) {
            logger.debug("현재 비밀번호: {}", currentPassword);
            logger.debug("입력된 비밀번호: {}", adminUpdateDTO.getCurrentPassword());
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 현재 비밀번호가 일치하지 않습니다.
        }

        // 현재 비밀번호와 새 비밀번호가 동일한지 확인
        if (adminUpdateDTO.getCurrentPassword().equals(adminUpdateDTO.getNewPassword())) {
            throw new CustomException(ErrorCode.SAME_PASSWORD); // 두 입력값이 동일합니다.
        }

        // 새 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(adminUpdateDTO.getNewPassword());
        adminUpdateDTO.setNewPassword(encodedPassword);

        logger.info("adminUpdateDTO: adminId-{}, newPassword-{}", adminUpdateDTO.getAdminId(),
                adminUpdateDTO.getNewPassword());
        adminMapper.updatePassword(adminUpdateDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "어드민 정보가 성공적으로 수정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 7. 어드민 삭제

    @Transactional
    public Map<String, Object> deleteAdminsWithValidation(String token, String refreshToken,
            Map<String, Object> payload) {
        List<Integer> adminIds = (List<Integer>) payload.get("adminIds");
        String password = (String) payload.get("password");

        if (adminIds == null || adminIds.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST); // 삭제할 어드민 ID가 필요합니다.
        }

        if (password == null || password.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST); // 비밀번호가 필요합니다.
        }

        int userId = jwtTokenProvider.getUserIdFromToken(token);
        String userRole = jwtTokenProvider.getUserRoleFromToken(token);

        logger.info("삭제 요청을 받은 어드민 id 리스트와 요청한 사용자 ID : {}, {}", adminIds, userId);
        logger.info("사용자 역할 : {}", userRole);

        // 비밀번호 확인 로직
        boolean isPasswordValid = validatePassword(userId, password);
        if (!isPasswordValid) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 비밀번호가 일치하지 않습니다.
        }

        if ("ROLE_ADMIN".equals(userRole)) {
            // ADMIN은 본인 계정만 삭제 가능
            if (!adminIds.contains(userId) || adminIds.size() != 1) {
                throw new CustomException(ErrorCode.FORBIDDEN_SELF_DELETION);
            }
            // ADMIN이 본인 계정을 삭제할 때 액세스 토큰과 리프레시 토큰 모두 무효화
            jwtTokenProvider.invalidateToken(token);
            jwtTokenProvider.invalidateToken(refreshToken);
        }

        // SUPER_ADMIN은 모든 ID 삭제 가능
        adminMapper.deleteByAdminIds(adminIds);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "어드민 삭제가 성공적으로 완료되었습니다.");
        response.put("status", HttpStatus.OK);

        return response;
    }

    // 비밀번호 확인 로직
    private boolean validatePassword(int userId, String inputPassword) {
        // 데이터베이스에서 암호화된 비밀번호 가져오기
        String encryptedPassword = adminMapper.getCurrentPassword(userId);

        // 입력된 비밀번호를 암호화하여 비교
        return passwordEncoder.matches(inputPassword, encryptedPassword);
    }

}
