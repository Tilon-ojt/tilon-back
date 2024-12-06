package com.tilon.ojt_back.service.manage;

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

import com.tilon.ojt_back.dao.manage.PostMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostRequestDTO;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;
import com.tilon.ojt_back.domain.manage.PostFix;

@Service
public class PostService {
    @Autowired private PostMapper postMapper;

    // post 조회
    public Page<PostResponseDTO> getPosts(PostCategory category, int offset, int size) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("category", category);
            param.put("offset", offset);
            param.put("size", size);

            List<PostResponseDTO> posts = postMapper.getPostsRow(param);
            int pageNumber = offset / size;
            Pageable pageable = PageRequest.of(pageNumber, size);
            return new PageImpl<>(posts, pageable, postMapper.getPostsCountRow(category));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get posts", e);
        }
    }

    // post 상세 조회
    public PostResponseDTO getPost(int postId) {
        try {
            return postMapper.getPostRow(postId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get post", e);
        }
    }

    // post 작성
    public void createPost(PostRequestDTO param) {
        try {
            postMapper.createPostRow(param);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create post", e);
        }
    }

    // post 수정
    public void updatePost(int postId, PostRequestDTO param) {
        try {
            param.setPostId(postId);
            postMapper.updatePostRow(param);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update post", e);
        }
    }

    // post status 수정
    public void updatePostStatus(int postId, PostStatus status) {

        PostStatus presentStatus = postMapper.getPostStatusRow(postId);
        PostFix presentFix = postMapper.getPostFixRow(postId);

        if (PostStatus.DRAFT == presentStatus && PostFix.FIX == presentFix) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시되어 있지 않지만 고정되어 있는 게시물입니다.");
        }
        
        Map<String, Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("status", status);

        if(presentFix == PostFix.NOT_FIX){
            try {
                postMapper.updatePostStatusRow(param);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update post status", e);
            }
        } else if(presentFix == PostFix.FIX){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "고정 게시글은 상태 변경이 불가능합니다.");
        }
    }

    // post fix 수정
    public void updatePostFix(int postId, PostFix fix) {

        PostStatus presentStatus = postMapper.getPostStatusRow(postId);
        PostFix presentFix = postMapper.getPostFixRow(postId);

        if (PostStatus.DRAFT == presentStatus && PostFix.FIX == presentFix) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시되어 있지 않지만 고정되어 있는 게시물입니다.");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("fix", fix);

        if(presentStatus == PostStatus.PUBLISHED){
            try {
                postMapper.updatePostFixRow(param);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update post fix", e);
            }
        } else if(presentStatus == PostStatus.DRAFT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글이 게시되어 있을 때만 고정 가능합니다.");
        }
    }

    // post 삭제
    public void deletePost(int postId) {
        try {
            postMapper.deletePostRow(postId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete post", e);
        }
    }
}
