package com.tilon.ojt_back.domain.manage;

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
    private PostFix fix;
    private String link;
    private String imageUrl;
}
