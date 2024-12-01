package com.tilon.ojt_back.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tilon.ojt_back.domain.user.AdminResponseDTO;

@Mapper
public interface AdminMapper {
    List<AdminResponseDTO> getAdminList();
}
