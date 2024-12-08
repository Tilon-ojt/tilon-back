package com.tilon.ojt_back.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tilon.ojt_back.domain.user.AdminRequestDTO;
import com.tilon.ojt_back.domain.user.AdminResponseDTO;
import com.tilon.ojt_back.domain.user.AdminUpdateDTO;

@Mapper
public interface AdminMapper {

    // 1. 어드민 전체 목록 조회
    List<AdminResponseDTO> getAdminList();

    // 2. 로그인 : empName로 사용자 정보 조회
    AdminResponseDTO getUserByEmpName(@Param("empName") String empName);

    // 3. 사용자 추가
    void insertAdmin(AdminRequestDTO adminRequestDTO);

    // 사용자 추가 후 존재 여부 검증
    boolean existsByEmpName(String empName);

    // 4. 비밀번호 초기화
    void resetPassword(@Param("adminId") int adminId, @Param("password") String password);

    // 5. 비밀번호 동일성 체크
    String getCurrentPassword(@Param("adminId") int adminId);

    // 6. 계정 정보 변경
    void updateAdminInfo(AdminUpdateDTO adminUpdateDTO);

    // 7. 여러 계정 한번에 삭제
    void deleteByAdminIds(@Param("adminIds") List<Integer> adminIds);

    // 8. 비밀번호 변경 메서드
    int updatePassword(AdminUpdateDTO adminUpdateDTO);

}
