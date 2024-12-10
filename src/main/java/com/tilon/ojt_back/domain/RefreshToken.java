package com.tilon.ojt_back.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 43200)
public class RefreshToken {
    // value는 reids key 값, timeToLive는 만료시간 - 12시간
    // redis 저장소의 key로는 {value}:{@id어노테이션에 붙여준 값}이 저장

    @Id
    private String adminId;
    private String token;

    // 기본 생성자, getter, setter
    public RefreshToken() {
    }

    public RefreshToken(String adminId, String token) {
        this.adminId = adminId;
        this.token = token;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
