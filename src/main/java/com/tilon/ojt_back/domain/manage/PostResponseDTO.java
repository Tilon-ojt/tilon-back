package com.tilon.ojt_back.domain.manage;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Integer postId;
    private String title;
    private String content;
    private PostCategory category;
    private int adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus status;
    private PostFix fix;
    private String link;
}