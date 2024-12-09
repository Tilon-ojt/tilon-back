package com.tilon.ojt_back.service.manage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.dao.manage.ImageMapper;

@Service
public class ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Value("${image.upload.path}")
    private String uploadPath;

    @Value("${server.domain}")
    private String serverDomain;

    public String uploadImage(MultipartFile file, String tempPostId){
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 파일 경로 생성
        Path filePath = Paths.get(uploadPath, fileName);
        // 디렉토리가 존재하지 않으면 생성
        if (!filePath.getParent().toFile().exists()) {
            filePath.getParent().toFile().mkdirs();
        }

        Map<String, Object> param = new HashMap<>();
        param.put("tempPostId", tempPostId);
        param.put("fileName", fileName);
        param.put("filePath", filePath.toString());

        try {
            //DB에 fileName, filePath 저장
            imageMapper.insertImageRow(param);
            // 서버에 이미지 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image", e);
        }

        // 파일 경로 반환
        return serverDomain + "/static/image/" + fileName;
    }

    // 임시 postId를 실제 postId로 업데이트
    public void updatePostIdForImage(String tempPostId, int postId){
        System.out.println("ImageService.updatePostIdForImage() - tempPostId: " + tempPostId);
        System.out.println("ImageService.updatePostIdForImage() - postId: " + postId);
        Map<String, Object> param = new HashMap<>();
        param.put("tempPostId", tempPostId);
        param.put("postId", postId);
        try {
            System.out.println("ImageService.updatePostIdForImage() - param: " + param);
            imageMapper.updatePostIdForImageRow(param);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update postId for image", e);
        }
    }

    // 이미지 삭제
    public void deleteImage(String fileName) {
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            Files.deleteIfExists(filePath);
            imageMapper.deleteImageRow(fileName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image", e);
        }
    }
}
