package com.tilon.ojt_back.controller.manage;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tilon.ojt_back.service.manage.ImageService;

@RestController
@RequestMapping("/admin/posts/image")
public class ImageController {
    @Autowired private ImageService imageService;

    // 이미지 업로드
    @PostMapping("upload")
    public ResponseEntity<?> uploadImage(
        @RequestParam("ImgFile") MultipartFile file,
        @RequestParam(value = "postId", required = false) Integer postId,
        @RequestParam(value = "tempPostId", required = false) String tempPostId) {
        // 이미지 업로드
        String imageUrl = imageService.uploadImage(file, tempPostId, postId);

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        // CKEditor에 이미지 URL 반환
        return ResponseEntity.ok(response);
    }

    // 이미지 삭제
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteImage(@RequestParam("fileName") String fileName) {
        imageService.deleteImage(fileName);
        return ResponseEntity.noContent().build();
    }
}
