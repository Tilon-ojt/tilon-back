package com.tilon.ojt_back.dao.homepage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.homepage.PostResponseDTO;

@Mapper
public interface PostMapper {
    // 인사이트 조회
    public List<PostResponseDTO> getInsightRow();
    // PR 조회
    public List<PostResponseDTO> getPrRow();
}
