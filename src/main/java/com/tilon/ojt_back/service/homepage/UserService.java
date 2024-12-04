package com.tilon.ojt_back.service.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.homepage.UserMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // user의 post 조회
    public List<PostResponseDTO> getPost(PostCategory category) {
        return userMapper.getPostRow(category);
    }

    // user의 homepage 조회
    public List<PostResponseDTO> getHomepage(PostCategory category) {
        return userMapper.getHomepage(category);
    }
}
