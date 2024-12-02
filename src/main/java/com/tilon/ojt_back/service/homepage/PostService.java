package com.tilon.ojt_back.service.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.homepage.PostMapper;
import com.tilon.ojt_back.domain.homepage.PostResponseDTO;
import com.tilon.ojt_back.domain.homepage.PostCategory;
import com.tilon.ojt_back.domain.homepage.PostRequestDTO;

@Service
public class PostService {
    @Autowired private PostMapper postMapper;

    // post 조회
    public List<PostResponseDTO> getPost(PostCategory category) {
        return postMapper.getPostRow(category);
    }

    // post 작성
    public void createPost(PostRequestDTO param) {
        postMapper.createPostRow(param);
    }

    // post 수정
    public void updatePost(PostRequestDTO param) {
        postMapper.updatePostRow(param);
    }
}
