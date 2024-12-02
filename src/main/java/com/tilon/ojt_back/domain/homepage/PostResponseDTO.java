package com.tilon.ojt_back.domain.homepage;

import com.tilon.ojt_back.domain.homepage.PostCategory;
import com.tilon.ojt_back.domain.homepage.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private int postId;
    private String title;
    private String content;
    private PostCategory category;
    private int adminId;
    private String createdAt;
    private String updatedAt;
    private PostStatus status;
    private String link;
    private String imageUrl;
}
