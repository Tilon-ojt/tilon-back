package com.tilon.ojt_back.dao.manage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    // 이미지 저장
    public void insertImageRow(Map<String, Object> param);

    // 임시 postId를 실제 postId로 업데이트
    public void updatePostIdForImageRow(Map<String, Object> param);

    // fileName으로 이미지 삭제
    public void deleteImageRow(String fileName);

    // postId로 file_name 조회
    public List<String> selectFileNameByPostIdRow(int postId);
}
