package com.tilon.ojt_back.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tilon.ojt_back.domain.user.AdminResponseDTO;

@Mapper
public interface AdminMapper {
    List<AdminResponseDTO> getAdminList();

    // empno로 사용자 정보를 조회하는 메서드 추가
    AdminResponseDTO getUserByEmpno(@Param("empno") String empno);
}
