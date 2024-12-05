package com.tilon.ojt_back.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

@Service
public class ImageService {

    @Value("${image.upload.path}")
    private String uploadPath;

    @Value("${server.domain}")
    private String serverDomain;

    public String uploadImage(MultipartFile file) throws IOException{
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        System.out.println("imageService uploadImage fileName: " + fileName);

        // 파일 경로 생성
        File filePath = new File(uploadPath, fileName);
        System.out.println("imageService uploadImage filePath: " + filePath);

        // 서버에 파일 저장
        file.transferTo(filePath);
        
        // 파일 경로 반환
        return serverDomain + "/image/" + fileName;
    }

    // 이미지 삭제
    public void deleteImage(String fileName) {
        File filePath = new File(uploadPath, fileName);
        if (filePath.exists()) {
            filePath.delete();
        }
    }
}
