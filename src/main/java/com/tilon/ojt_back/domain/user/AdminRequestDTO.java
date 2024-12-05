package com.tilon.ojt_back.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDTO {
    private int adminId; // 어드민 ID
    private String empName; // 사번
    private String password; // 암호화된 비밀번호
    private String role; // 역할 (SUPER_ADMIN, ADMIN)
    private String nickname;
    private String createdAt;
    private String updatedAt;

}
