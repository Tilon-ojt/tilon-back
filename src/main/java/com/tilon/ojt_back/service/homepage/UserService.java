package com.tilon.ojt_back.service.homepage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.homepage.UserMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // user의 post 조회
    public List<PostResponseDTO> getPostRow(PostCategory category, PostStatus status) {
        Map<String, Object> param = new HashMap<>();
        param.put("category", category);
        param.put("status", status);
        return userMapper.getPostRow(param);
    }
}
