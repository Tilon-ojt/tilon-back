package com.tilon.ojt_back.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.service.user.AdminServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @GetMapping("/user/admin")
    public List<AdminResponseDTO> getMethodName() {
        return adminServiceImpl.getAdminList();
    }

}