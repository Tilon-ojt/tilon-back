package com.tilon.ojt_back.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    // 이미지 이름 저장
    public void saveImageNameToDBRow(Map<String, Object> map);

    // 이미지 이름 가져오기
    public String getImageNames(int postId);
}
