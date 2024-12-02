package com.tilon.ojt_back.domain.homepage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private int post_id;
    private String title;
    private String content;
    private PostCategory category;
    private int admin_id;
    private Timestamp created_at;
    private Timestamp updated_at;
    private PostStatus status;
    private String link;
    private String image_url;
}