package com.tilon.ojt_back.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tilon.ojt_back.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String>{
    
}
