package com.tilon.ojt_back.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.service.user.AdminService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/user")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public List<AdminResponseDTO> getMethodName() {
        return adminService.getAdminList();
    }

}