package com.tilon.ojt_back.controller.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tilon.ojt_back.domain.homepage.PostResponseDTO;
import com.tilon.ojt_back.service.homepage.PostService;


@RestController
@RequestMapping("/homepage")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/insight")
    public List<PostResponseDTO> getInsight() {
        return postService.getInsight();
    }
}
