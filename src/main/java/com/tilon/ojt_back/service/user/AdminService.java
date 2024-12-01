package com.tilon.ojt_back.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.user.AdminMapper;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public List<AdminResponseDTO> getAdminList() {
        return adminMapper.getAdminList();
    }
}
