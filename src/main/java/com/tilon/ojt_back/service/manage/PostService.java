package com.tilon.ojt_back.service.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.manage.PostMapper;
import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostRequestDTO;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;
import com.tilon.ojt_back.domain.manage.PostFix;
import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;

@Service
public class PostService {
    @Autowired private PostMapper postMapper;
    @Autowired private ImageService imageService;

    // post 조회
    public Page<PostResponseDTO> getPosts(PostCategory category, int offset, int size) {
        // 1. category 검증
        if (category == null) {
            throw new CustomException(ErrorCode.INVALID_CATEGORY);
        }

        // 2. offset, size 검증
        if (offset < 0 || size <= 0) {
            throw new CustomException(ErrorCode.INVALID_OFFSET_OR_SIZE);
        }

        Map<String, Object> param = new HashMap<>();
        param.put("category", category);
        param.put("offset", offset);
        param.put("size", size);
        List<PostResponseDTO> posts = postMapper.getPostsRow(param);

        int pageNumber = offset / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        return new PageImpl<>(posts, pageable, postMapper.getPostsCountRow(category));
    }

    // post 상세 조회
    public PostResponseDTO getPost(int postId) {
        // 1. postId 검증
        if (postId <= 0) {
            throw new CustomException(ErrorCode.INVALID_POST_ID);
        }

        return postMapper.getPostRow(postId);
    }

    // post 작성 시 임시 postId 생성
    public String startPostCreation() {
        return UUID.randomUUID().toString();
    }

    // post 작성
    public void createPost(PostRequestDTO param, String tempPostId, int adminId) {
        // 1. 작성자 검증
        if (adminId <= 0) {
            throw new CustomException(ErrorCode.INVALID_ADMIN_ID);
        }
        // 2. 임시 postId 검증
        if (tempPostId == null || tempPostId.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TEMP_POST_ID);
        }
        // 작성자 설정
        param.setAdminId(adminId);
        // post 작성
        postMapper.createPostRow(param);
        // 최신 postId 조회
        int postId = postMapper.getLatestPostIdRow();
        // 임시 postId를 실제 postId로 업데이트
        imageService.updatePostIdForImage(tempPostId, postId);

    }

    // post 수정
    public void updatePost(int postId, PostRequestDTO param) {
        // 1. postId 검증
        if (postId <= 0) {
            throw new CustomException(ErrorCode.INVALID_POST_ID);
        }
        // postId 설정
        param.setPostId(postId);
        // post 수정
        postMapper.updatePostRow(param);
    }

    // post status 수정
    public void updatePostStatus(int postId, PostStatus status) {

        PostStatus presentStatus = postMapper.getPostStatusRow(postId);
        PostFix presentFix = postMapper.getPostFixRow(postId);

        if (PostStatus.DRAFT == presentStatus && PostFix.FIX == presentFix) {
            throw new CustomException(ErrorCode.INVALID_DRAFT_FIX);
        }
        
        Map<String, Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("status", status);

        if(presentFix == PostFix.NOT_FIX){
            postMapper.updatePostStatusRow(param);
        } else if(presentFix == PostFix.FIX){
            throw new CustomException(ErrorCode.INVALID_POST_STATUS);
        }
    }

    // post fix 수정
    public void updatePostFix(int postId, PostFix fix) {

        PostStatus presentStatus = postMapper.getPostStatusRow(postId);
        PostFix presentFix = postMapper.getPostFixRow(postId);

        if (PostStatus.DRAFT == presentStatus && PostFix.FIX == presentFix) {
            throw new CustomException(ErrorCode.INVALID_DRAFT_FIX);
        }

        Map<String, Object> param = new HashMap<>();
        param.put("postId", postId);
        param.put("fix", fix);

        if(presentStatus == PostStatus.PUBLISHED){
            postMapper.updatePostFixRow(param);
        } else if(presentStatus == PostStatus.DRAFT){
            throw new CustomException(ErrorCode.INVALID_POST_FIX);
        }
    }

    // post 삭제
    public void deletePost(int postId) {
        // 서버에서 이미지 삭제
        imageService.deleteImageByPostId(postId);
        // post 삭제
        postMapper.deletePostRow(postId);
    }
}
