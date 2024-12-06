package com.tilon.ojt_back.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.service.manage.ImageService;

@RestController
@RequestMapping("/admin/posts/image")
public class ImageController {
    @Autowired private ImageService imageService;

    // 이미지 업로드
    @PostMapping("upload/{postId}")
    public ResponseEntity<String> uploadImage(
        @RequestParam("ImgFile") MultipartFile file,
        @PathVariable("postId") int postId) {
        try {
            // 이미지 업로드
            String imageUrl = imageService.uploadImage(file, postId);

            // CKEditor에 이미지 URL 반환
            return ResponseEntity.ok(imageUrl);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 이미지 삭제
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteImage(@RequestParam("fileName") String fileName) {
        try {
            imageService.deleteImage(fileName);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
