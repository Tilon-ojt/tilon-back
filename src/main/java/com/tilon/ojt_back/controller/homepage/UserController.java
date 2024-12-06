package com.tilon.ojt_back.controller.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.service.homepage.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // user의 post 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(
        @RequestParam(name = "category") PostCategory category,
        @RequestParam(name = "page") int page) {

        int size = 10;
        int offset = (page - 1) * size;
        try {
            return ResponseEntity.ok(userService.getPosts(category, offset, size));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // user의 post 상세 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable int postId) {
        try {
            return ResponseEntity.ok(userService.getPost(postId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // user의 homepage 조회
    @GetMapping("/homepage")
    public ResponseEntity<?> getHomepage(@RequestParam(name = "category") PostCategory category) {
        try {
            return ResponseEntity.ok(userService.getHomepage(category));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
