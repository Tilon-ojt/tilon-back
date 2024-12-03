package com.tilon.ojt_back.domain.user;

public class AdminResponseDTO {
    private int adminId; // 어드민 ID
    private String empno; // 사번
    private String password; // 암호화된 비밀번호
    private String adminName; // 사용자 이름
    private String role; // 역할 (SUPER_ADMIN, ADMIN)
    private String createdAt; // 생성일
    private String updatedAt; // 수정일
    private String email;

    // Constructor
    public AdminResponseDTO() {}

    public AdminResponseDTO(int adminId, String empno, String password, String adminName, String role, String createdAt, String updatedAt, String email) {
        this.adminId = adminId;
        this.empno = empno;
        this.password = password;
        this.adminName = adminName;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.email = email;
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
