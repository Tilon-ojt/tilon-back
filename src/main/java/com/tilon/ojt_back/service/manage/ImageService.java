package com.tilon.ojt_back.service.manage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.dao.manage.ImageMapper;
import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;

@Service
public class ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Value("${image.upload.path}")
    private String uploadPath;

    public String uploadImage(MultipartFile file, String tempPostId, Integer postId){
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 파일 경로 생성
        Path filePath = Paths.get(uploadPath, fileName);
        // 디렉토리가 존재하지 않으면 생성
        if (!filePath.getParent().toFile().exists()) {
            filePath.getParent().toFile().mkdirs();
        }

        // postId와 tempPostId 둘 다 없으면 예외 발생
        if (postId == null && tempPostId == null) {
            throw new CustomException(ErrorCode.INVALID_POST_ID_OR_TEMP_POST_ID);
        }

        // postId로 이미지 저장 (post 수정시)
        if (postId != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("postId", postId);
            param.put("fileName", fileName);
            param.put("filePath", filePath.toString());
            // DB에 fileName, filePath 저장
            imageMapper.insertImageRow(param);
        } 
        // tempPostId로 이미지 저장 (post 작성시)
        else if (tempPostId != null) {
            Map<String, Object> param = new HashMap<>();
            param.put("tempPostId", tempPostId);
            param.put("fileName", fileName);
            param.put("filePath", filePath.toString());
            // DB에 fileName, filePath 저장
            imageMapper.insertImageRow(param);
        }

        // 서버에 이미지 저장
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image", e);
        }

        // 파일 경로 반환
        return "image/" + fileName;
    }

    // 임시 postId를 실제 postId로 업데이트
    public void updatePostIdForImage(String tempPostId, int postId){
        Map<String, Object> param = new HashMap<>();
        param.put("tempPostId", tempPostId);
        param.put("postId", postId);
        // DB에서 임시 postId를 실제 postId로 업데이트
        imageMapper.updatePostIdForImageRow(param);
    }

    // fileName으로 이미지 삭제
    public void deleteImage(String fileName) {
        Path filePath = Paths.get(uploadPath, fileName);
        // 서버에서 이미지 삭제
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image", e);
        }
        // DB에서 이미지 삭제
        imageMapper.deleteImageRow(fileName);
    }

    // postId로 서버에서 이미지 삭제
    public void deleteImageByPostId(int postId) {
        List<String> fileNames = imageMapper.selectFileNameByPostIdRow(postId);
        for (String fileName : fileNames) {
            deleteImage(fileName);
        }
    }
}
