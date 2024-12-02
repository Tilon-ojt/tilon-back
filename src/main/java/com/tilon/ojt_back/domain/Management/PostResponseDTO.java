package com.tilon.ojt_back.domain.Management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
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