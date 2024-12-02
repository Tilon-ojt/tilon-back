package com.tilon.ojt_back.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private int adminId;
    private String empno;
    private String adminName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String role;

    public CustomUserDetails(int adminId, String empno, String adminName, String password,
            Collection<? extends GrantedAuthority> authorities, String role) {
        this.adminId = adminId;
        this.empno = empno;
        this.adminName = adminName;
        this.password = password;
        this.authorities = authorities;
        this.role = role;
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
        return empno;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getEmpno() {
        return empno;
    }

    public String getAdminName() {
        return adminName;
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
