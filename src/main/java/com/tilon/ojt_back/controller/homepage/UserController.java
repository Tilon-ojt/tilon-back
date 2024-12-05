package com.tilon.ojt_back.controller.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.service.homepage.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // user의 post 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getPosts(@RequestParam(name = "category") PostCategory category) {
        return ResponseEntity.ok(userService.getPosts(category));
    }

    // user의 post 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable int postId) {
        return ResponseEntity.ok(userService.getPost(postId));
    }

    // user의 homepage 조회
    @GetMapping("/homepage")
    public ResponseEntity<List<PostResponseDTO>> getHomepage(@RequestParam(name = "category") PostCategory category) {
        return ResponseEntity.ok(userService.getHomepage(category));
    }
}
