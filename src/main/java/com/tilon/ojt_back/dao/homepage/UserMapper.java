package com.tilon.ojt_back.dao.homepage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.manage.PostResponseDTO;

@Mapper
public interface UserMapper {
    // user의 post 조회
    List<PostResponseDTO> getPostRow(Map<String, Object> param);
}
