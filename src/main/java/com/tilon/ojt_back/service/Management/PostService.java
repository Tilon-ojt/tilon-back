package com.tilon.ojt_back.service.management;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.management.PostMapper;
import com.tilon.ojt_back.domain.management.PostCategory;
import com.tilon.ojt_back.domain.management.PostRequestDTO;
import com.tilon.ojt_back.domain.management.PostResponseDTO;

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

    // post 삭제
    public void deletePost(int post_id) {
        postMapper.deletePostRow(post_id);
    }
}
