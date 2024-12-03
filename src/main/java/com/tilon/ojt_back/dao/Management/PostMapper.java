package com.tilon.ojt_back.dao.management;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.management.PostCategory;
import com.tilon.ojt_back.domain.management.PostRequestDTO;
import com.tilon.ojt_back.domain.management.PostResponseDTO;

@Mapper
public interface PostMapper {
    // post 조회
    public List<PostResponseDTO> getPostRow(PostCategory category);

    // post 작성
    public void createPostRow(PostRequestDTO param);

    // post 수정
    public void updatePostRow(PostRequestDTO param);

    // post 삭제
    public void deletePostRow(int post_id);
}
