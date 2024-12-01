package com.tilon.ojt_back.domain.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {
    private int  adminId;          // 어드민 ID
    private String empno;          // 사번
    private String password;        // 암호화된 비밀번호
    private String adminName;       // 사용자 이름
    private String role;           // 역할 (SUPER_ADMIN, ADMIN)
    private String createdAt; // 생성일
    private String updatedAt; // 수정일
}
