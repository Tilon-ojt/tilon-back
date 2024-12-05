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
        List<PostResponseDTO> posts = userMapper.getPostsRow(category);
        for(PostResponseDTO post : posts) {
            post.setImageUrl(imageService.getImages(post.getPostId()));
        }
        return posts;
    }

    // user의 post 상세 조회
    public PostResponseDTO getPost(int postId) {
        PostResponseDTO post = userMapper.getPostRow(postId);
        post.setImageUrl(imageService.getImages(postId));
        return post;
    }

    // user의 homepage 조회
    public List<PostResponseDTO> getHomepage(PostCategory category) {
        List<PostResponseDTO> posts = userMapper.getHomepage(category);
        for(PostResponseDTO post : posts) {
            post.setImageUrl(imageService.getImages(post.getPostId()));
        }
        return posts;
    }
}
