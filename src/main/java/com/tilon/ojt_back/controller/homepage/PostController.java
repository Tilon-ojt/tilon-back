package com.tilon.ojt_back.controller.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.homepage.PostCategory;
import com.tilon.ojt_back.domain.homepage.PostRequestDTO;
import com.tilon.ojt_back.domain.homepage.PostResponseDTO;
import com.tilon.ojt_back.service.homepage.PostService;


@RestController
@RequestMapping("/admin")
public class PostController {
    @Autowired
    private PostService postService;

    // post 조회
    @GetMapping("/post")
    public ResponseEntity<List<PostResponseDTO>> getPost(
        @RequestParam(name = "category") PostCategory category){
        return ResponseEntity.ok(postService.getPost(category));
    }

    // post 작성
    @PostMapping("/post")
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDTO param) {
        postService.createPost(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // post 수정
    @PutMapping("/post/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable(name = "postId") int post_id,
        @RequestBody PostRequestDTO param) {
        param.setPost_id(post_id);
        postService.updatePost(param);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
