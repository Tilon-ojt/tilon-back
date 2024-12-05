package com.tilon.ojt_back.domain.manage;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostRequestDTO {
    private int postId;
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
