package com.tilon.ojt_back.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tilon.ojt_back.service.ImageService;

@RestController
@RequestMapping("/admin/posts/image")
public class ImageController {
    @Autowired private ImageService imageService;

    // 이미지 업로드
    @PostMapping("upload")
    public ResponseEntity<String> uploadImage(@RequestParam("ImgFile") MultipartFile file) {
        try {
            System.out.println("imageController uploadImage file: " + file);
            // 이미지 업로드
            String imageUrl = imageService.uploadImage(file);
            System.out.println("imageController uploadImage imageUrl: " + imageUrl);

            // CKEditor에 이미지 URL 반환
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    // 이미지 삭제
    @DeleteMapping("delete")
    public ResponseEntity<Void> deleteImage(@RequestParam("fileName") String fileName) {
        imageService.deleteImage(fileName);
        return ResponseEntity.noContent().build();
    }
}
