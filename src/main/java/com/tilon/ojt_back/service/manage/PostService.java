package com.tilon.ojt_back.service.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PostResponseDTO> getPosts(PostCategory category) {
        return postMapper.getPostsRow(category);
    }

    // post 상세 조회
    public PostResponseDTO getPost(int postId) {
        return postMapper.getPostRow(postId);
    }

    // post 작성
    public void createPost(PostRequestDTO param) {
        postMapper.createPostRow(param);
    }

    // post 수정
    public void updatePost(int postId, PostRequestDTO param) {
        param.setPostId(postId);
        postMapper.updatePostRow(param);
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
            postMapper.updatePostStatusRow(param);
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
            postMapper.updatePostFixRow(param);
        } else if(presentStatus == PostStatus.DRAFT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글이 게시되어 있을 때만 고정 가능합니다.");
        }
    }

    // post 삭제
    public void deletePost(int postId) {
        postMapper.deletePostRow(postId);
    }
}
