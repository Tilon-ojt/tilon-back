package com.tilon.ojt_back.domain.user;

public class AdminResponseDTO {
    private int adminId; // 어드민 ID
    private String empName; // 사번
    private String password; // 암호화된 비밀번호
    private String role; // 역할 (SUPER_ADMIN, ADMIN)
    private String createdAt; // 생성일
    private String updatedAt; // 수정일
    private String email;

    // Constructor
    public AdminResponseDTO() {
    }

    public AdminResponseDTO(int adminId, String empName, String password, String role,
            String createdAt, String updatedAt, String email) {
        this.adminId = adminId;
        this.empName = empName;
        this.password = password;
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

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
