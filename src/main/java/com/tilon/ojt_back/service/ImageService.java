package com.tilon.ojt_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tilon.ojt_back.dao.ImageMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    // 이미지 조회
    public List<String> getImages(int postId) {
        // 데이터베이스에서 이미지 이름 가져오기
        List<String> imageNames = getImageNamesFromDB(postId);
        // 이미지 URL 생성
        List<String> imageUrls = new ArrayList<>();
        for (String imageName : imageNames) {
            String imageUrl = "/static/images/" + imageName;
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
    
    // 이미지 업로드
    @Transactional
    public void uploadImages(MultipartFile[] files, int postId){
        // 데이터베이스에서 기존 이미지 목록 가져오기
        List<String> existingFileNames = getImageNamesFromDB(postId);
        if(!existingFileNames.isEmpty()){
            // 기존 이미지 삭제
            for (String imageUrl : existingFileNames) {
                deleteImage(imageUrl);
            }
        }

        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            // 이미지 이름 생성
            String fileName = generateImageName(file.getOriginalFilename());
            // 이미지 경로 생성
            String filePath = "resource/static/images/" + fileName;

            try {
                // 서버에 이미지 저장
                file.transferTo(new File(filePath));
                // 이미지 이름 리스트에 추가
                fileNames.add(fileName);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
            }
        }
        
        // 모든 이미지 이름을 하나의 String으로 만들고 데이터베이스에 저장
        saveImageNameToDB(fileNames, postId);
    }
        
    // 이미지 이름 생성
    private String generateImageName(String originalFileName) {
        // 확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 랜덤 UUID 생성
        String uuid = UUID.randomUUID().toString();
        // UUID와 확장자 결합
        return uuid.concat(extension);
    }

    // 이미지 이름 저장
    private void saveImageNameToDB(List<String> fileNames, int postId){
        // 이미지 이름을 쉼표로 결합
        String combinedFileNames = String.join(",", fileNames);
        // 데이터베이스에 저장
        Map<String, Object> map = new HashMap<>();
        map.put("image_url", combinedFileNames);
        map.put("post_id", postId);
        try{
            imageMapper.saveImageNameToDBRow(map);
        } catch (Exception e){
            throw new RuntimeException("이미지 이름 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 데이터베이스에서 이미지 이름 가져오기
    private List<String> getImageNamesFromDB(int postId) {
        String combinedFileNames = imageMapper.getImageNames(postId);
        if (combinedFileNames == null || combinedFileNames.isEmpty()) {
            return new ArrayList<>();
        }
        // 쉼표로 구분된 문자열을 분리하여 리스트로 변환
        String[] fileNameArray = combinedFileNames.split(",");
        return new ArrayList<>(Arrays.asList(fileNameArray));
    }

    // 서버에서 기존 이미지 삭제
    private void deleteImage(String imageUrl){
        String filePath = "resource/static/images/" + imageUrl;
        File file = new File(filePath);
    
        try{
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e){
            throw new RuntimeException("이미지 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
