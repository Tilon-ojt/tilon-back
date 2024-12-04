package com.tilon.ojt_back.service.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.homepage.UserMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.service.ImageService;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ImageService imageService;

    // user의 posts 조회
    public List<PostResponseDTO> getPosts(PostCategory category) {
        return userMapper.getPostsRow(category);
    }

    // user의 post 상세 조회
    public PostResponseDTO getPost(int postId) {
        return userMapper.getPostRow(postId);
    }

    // user의 homepage 조회
    public List<PostResponseDTO> getHomepage(PostCategory category) {
        return userMapper.getHomepage(category);
    }
}
