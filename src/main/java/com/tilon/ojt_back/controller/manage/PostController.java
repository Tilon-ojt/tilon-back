package com.tilon.ojt_back.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostFix;
import com.tilon.ojt_back.domain.manage.PostRequestDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;
import com.tilon.ojt_back.service.manage.PostService;

import java.util.UUID;

@RestController
@RequestMapping("/admin/posts")
public class PostController {
    @Autowired
    private PostService postService;

    // post 조회
    @GetMapping("")
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "category") PostCategory category,
            @RequestParam(name = "page") int page) {

        int size = 10;
        int offset = (page - 1) * size;
        try {
            return ResponseEntity.ok(postService.getPosts(category, offset, size));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable int postId) {
        try {
            return ResponseEntity.ok(postService.getPost(postId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post 작성 시 임시 postId 생성
    @PostMapping("/start")
    public ResponseEntity<String> startPostCreation() {
        // 서버에서 tempPostId 생성
        String tempPostId = postService.startPostCreation();
        return ResponseEntity.ok().body(tempPostId);
    }

    // post 작성
    @PostMapping("")
    public ResponseEntity<?> createPost(
            @RequestBody PostRequestDTO param,
            @RequestParam(name = "tempPostId") String tempPostId) {
        try {
            postService.createPost(param, tempPostId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable(name = "postId") int postId,
            @RequestBody PostRequestDTO param) {

        try {
            postService.updatePost(postId, param);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post status 수정
    @PatchMapping("/{postId}/status")
    public ResponseEntity<String> updatePostStatus(
            @PathVariable(name = "postId") int postId,
            @RequestParam(name = "status") PostStatus status) {

        try {
            postService.updatePostStatus(postId, status);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post fix 수정
    @PatchMapping("/{postId}/fix")
    public ResponseEntity<String> updatePostFix(
            @PathVariable(name = "postId") int postId,
            @RequestParam(name = "fix") PostFix fix) {

        try {
            postService.updatePostFix(postId, fix);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "postId") int postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
