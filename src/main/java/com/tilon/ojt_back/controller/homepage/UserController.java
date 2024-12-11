package com.tilon.ojt_back.controller.homepage;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.service.homepage.UserService;
import com.tilon.ojt_back.service.user.AdminService;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

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

    // 어드민 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            // 로그인 메서드 호출
            ResponseEntity<Map<String, Object>> responseEntity = adminService.login(loginDTO);
            // 로그인 서비스에서 반환된 응답을 그대로 반환
            return responseEntity; // JSON 형식으로 응답 반환
        } catch (Exception e) {
            // 예외 처리: 로그 추가
            System.err.println("로그인 중 오류 발생: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
