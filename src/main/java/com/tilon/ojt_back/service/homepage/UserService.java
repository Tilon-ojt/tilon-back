package com.tilon.ojt_back.service.homepage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.dao.homepage.UserMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    // user의 posts 조회
    public Page<PostResponseDTO> getPosts(PostCategory category, int offset, int size) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("category", category);
            params.put("offset", offset);
            params.put("size", size);

            List<PostResponseDTO> posts = userMapper.getPostsRow(params);
            int pageNumber = offset / size;
            Pageable pageable = PageRequest.of(pageNumber, size);
            return new PageImpl<>(posts, pageable, userMapper.getPostsCountRow(category));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get posts", e);
        }
    }

    // user의 post 상세 조회
    public PostResponseDTO getPost(int postId) {
        try {
            return userMapper.getPostRow(postId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get post", e);
        }
    }

    // user의 homepage 조회
    public List<PostResponseDTO> getHomepage(PostCategory category) {
        try {
            return userMapper.getHomepage(category);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get homepage", e);
        }
    }
}
