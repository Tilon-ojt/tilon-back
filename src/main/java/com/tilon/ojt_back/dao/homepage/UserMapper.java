package com.tilon.ojt_back.dao.homepage;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.manage.PostResponseDTO;

@Mapper
public interface UserMapper {
    // user의 post 조회
    List<PostResponseDTO> getPostsRow(Map<String, Object> params);

    // user의 post 총 개수 조회
    int getPostsCountRow(PostCategory category);

    // user의 post 상세 조회
    PostResponseDTO getPostRow(int postId);

    // user의 homepage 조회
    List<PostResponseDTO> getHomepage(PostCategory category);
}
