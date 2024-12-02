package com.tilon.ojt_back.domain.Management;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class PostRequestDTO {
    private int post_id;
    private String title;
    private String content;
    private PostCategory category;
    private int admin_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private PostStatus status;
    private String link;
    private String image_url;
}
