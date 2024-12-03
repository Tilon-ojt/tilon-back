package com.tilon.ojt_back.controller.manage;

import java.util.List;

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
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;
import com.tilon.ojt_back.service.manage.PostService;


@RestController
@RequestMapping("/admin/post")
public class PostController {
    @Autowired
    private PostService postService;

    // post 조회
    @GetMapping("")
    public ResponseEntity<List<PostResponseDTO>> getPost(
        @RequestParam(name = "category") PostCategory category){
        return ResponseEntity.ok(postService.getPost(category));
    }

    // post 작성
    @PostMapping("")
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDTO param) {
        postService.createPost(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // post 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable(name = "postId") int post_id,
        @RequestBody PostRequestDTO param) {

        postService.updatePost(post_id, param);
        return ResponseEntity.noContent().build();
    }

    // post status 수정
    @PatchMapping("/{postId}/status")
    public ResponseEntity<String> updatePostStatus(
        @PathVariable(name = "postId") int post_id,
        @RequestParam(name = "status") PostStatus status,
        @RequestParam(name = "fix") PostFix fix) {

        try {
            postService.updatePostStatus(post_id, status, fix);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post fix 수정
    @PatchMapping("/{postId}/fix")
    public ResponseEntity<String> updatePostFix(
        @PathVariable(name = "postId") int post_id,
        @RequestParam(name = "status") PostStatus status,
        @RequestParam(name = "fix") PostFix fix) {

        try {
            postService.updatePostFix(post_id, status, fix);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // post 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") int post_id) {
        postService.deletePost(post_id);
        return ResponseEntity.noContent().build();
    }
}
