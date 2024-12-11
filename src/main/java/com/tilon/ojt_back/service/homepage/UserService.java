package com.tilon.ojt_back.service.homepage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.tilon.ojt_back.dao.homepage.UserMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // user의 posts 조회
    public Page<PostResponseDTO> getPosts(PostCategory category, int offset, int size) {
        // 1. category 검증
        if (category == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        // 2. offset, size 검증
        if (offset < 0 || size <= 0) {
            throw new CustomException(ErrorCode.INVALID_OFFSET_OR_SIZE);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("offset", offset);
        params.put("size", size);
        List<PostResponseDTO> posts = userMapper.getPostsRow(params);

        int pageNumber = offset / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        return new PageImpl<>(posts, pageable, userMapper.getPostsCountRow(category));
    }

    // user의 post 상세 조회
    public PostResponseDTO getPost(int postId) {
        // 1. postId 검증
        if (postId <= 0) {
            throw new CustomException(ErrorCode.INVALID_POST_ID);
        }
        return userMapper.getPostRow(postId);
    }

    // user의 homepage 조회
    public List<PostResponseDTO> getHomepage(PostCategory category) {
        if (category == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }
        return userMapper.getHomepage(category);
    }
}
