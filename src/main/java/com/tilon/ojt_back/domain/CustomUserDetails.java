package com.tilon.ojt_back.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private int adminId;
    private String empName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String role;

    public CustomUserDetails(int adminId, String empName, String password,
            Collection<? extends GrantedAuthority> authorities, String role) {
        this.adminId = adminId;
        this.empName = empName;

        this.password = password;
        this.authorities = authorities; // 사용자 권한 목록 : 세부적인 권한
        this.role = role; // 사용자의 일반적인 역할
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return empName;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

}
