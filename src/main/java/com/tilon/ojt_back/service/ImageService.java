package com.tilon.ojt_back.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Value("${image.upload.path}")
    private String uploadPath;

    @Value("${server.domain}")
    private String serverDomain;

    public String uploadImage(MultipartFile file) throws IOException{
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 파일 경로 생성
        Path filePath = Paths.get(uploadPath, fileName);
        // 디렉토리가 존재하지 않으면 생성
        if (!filePath.getParent().toFile().exists()) {
            filePath.getParent().toFile().mkdirs();
        }

        // 서버에 파일 저장
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일 경로 반환
        return serverDomain + "/static/image/" + fileName;
    }

    // 이미지 삭제
    public void deleteImage(String fileName) {
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
