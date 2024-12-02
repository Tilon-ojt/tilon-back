package com.tilon.ojt_back.dao.homepage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.homepage.PostResponseDTO;

@Mapper
public interface PostMapper {
    public List<PostResponseDTO> getInsightRow();
}
