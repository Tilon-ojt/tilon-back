package com.tilon.ojt_back.dao.homepage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.homepage.PostResponseDTO;
import com.tilon.ojt_back.domain.homepage.PostCategory;
import com.tilon.ojt_back.domain.homepage.PostRequestDTO;

@Mapper
public interface PostMapper {
    // post 조회
    public List<PostResponseDTO> getPostRow(PostCategory category);

    // news 작성
    public void createNewsRow(PostRequestDTO param);
}
