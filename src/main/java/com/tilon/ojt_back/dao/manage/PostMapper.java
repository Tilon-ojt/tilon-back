package com.tilon.ojt_back.dao.manage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostFix;
import com.tilon.ojt_back.domain.manage.PostRequestDTO;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;
import com.tilon.ojt_back.domain.manage.PostStatus;

@Mapper
public interface PostMapper {
    // posts 조회
    public List<PostResponseDTO> getPostsRow(Map<String, Object> param);

    // posts 개수 조회
    public int getPostsCountRow(PostCategory category);

    // post 상세 조회
    public PostResponseDTO getPostRow(int postId);

    // post 작성
    public void createPostRow(PostRequestDTO param);

    // 최신 postId 조회
    public int getLatestPostIdRow();

    // post 수정
    public void updatePostRow(PostRequestDTO param);

    // post status 수정
    public void updatePostStatusRow(Map<String, Object> param);

    // post fix 조회
    public PostFix getPostFixRow(int postId);

    // post fix 수정
    public void updatePostFixRow(Map<String, Object> param);

    // post status 조회
    public PostStatus getPostStatusRow(int postId);

    // post 삭제
    public void deletePostRow(int postId);
}
