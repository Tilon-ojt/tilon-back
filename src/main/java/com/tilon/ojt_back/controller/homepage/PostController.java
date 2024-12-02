package com.tilon.ojt_back.controller.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.homepage.PostCategory;
import com.tilon.ojt_back.domain.homepage.PostResponseDTO;
import com.tilon.ojt_back.service.homepage.PostService;


@RestController
@RequestMapping("/homepage")
public class PostController {
    @Autowired
    private PostService postService;

    // post 조회
    @GetMapping("/post")
    public List<PostResponseDTO> getPost(
        @RequestParam(name = "category") PostCategory category){
        return postService.getPost(category);
    }
}
