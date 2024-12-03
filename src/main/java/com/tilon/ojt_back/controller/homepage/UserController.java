package com.tilon.ojt_back.controller.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;
import com.tilon.ojt_back.service.homepage.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // user의 post 조회
    @GetMapping("/post")
    public ResponseEntity<List<PostResponseDTO>> getPostRow(
        @RequestParam(name = "category") PostCategory category,
        @RequestParam(name = "status") PostStatus status) {

        System.out.println(category);
        System.out.println(status);

        return ResponseEntity.ok(userService.getPostRow(category, status));
    }
}
