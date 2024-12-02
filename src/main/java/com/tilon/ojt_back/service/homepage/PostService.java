package com.tilon.ojt_back.service.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tilon.ojt_back.dao.homepage.PostMapper;
import com.tilon.ojt_back.domain.homepage.PostResponseDTO;

@Service
public class PostService {
    @Autowired private PostMapper postMapper;

    public List<PostResponseDTO> getInsight() {
        return postMapper.getInsightRow();
    }
}
