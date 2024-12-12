package com.tilon.ojt_back.controller.homepage;

import java.net.MalformedURLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tilon.ojt_back.domain.manage.PostCategory;
import com.tilon.ojt_back.domain.user.LoginDTO;
import com.tilon.ojt_back.exception.CustomException;
import com.tilon.ojt_back.exception.ErrorCode;
import com.tilon.ojt_back.service.homepage.UserService;
import com.tilon.ojt_back.service.user.AdminService;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    // user의 post 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(
            @RequestParam(name = "category") PostCategory category,
            @RequestParam(name = "page") int page) {

        int size = 10;
        int offset = (page - 1) * size;
        try {
            return ResponseEntity.ok(userService.getPosts(category, offset, size));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // user의 post 상세 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable int postId) {
        try {
            return ResponseEntity.ok(userService.getPost(postId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // user의 homepage 조회
    @GetMapping("/homepage")
    public ResponseEntity<?> getHomepage(@RequestParam(name = "category") PostCategory category) {
        try {
            return ResponseEntity.ok(userService.getHomepage(category));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // 어드민 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDTO loginDTO) {
        // 로그인 메서드 호출
        return adminService.login(loginDTO); // JSON 형식으로 응답 반환
    }

    // 이미지 조회
    @GetMapping("get/{fileName}")
    public ResponseEntity<?> serveImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("http://172.16.5.51:8080/image/").resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                // 파일 이름에서 확장자 추출
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                // 확장자에 따른 MediaType 설정
                MediaType mediaType = switch (fileExtension) {
                    case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
                    case "png" -> MediaType.IMAGE_PNG;
                    case "gif" -> MediaType.IMAGE_GIF;
                    case "webp" -> MediaType.valueOf("image/webp");
                    case "svg" -> MediaType.valueOf("image/svg+xml");
                    default -> MediaType.APPLICATION_OCTET_STREAM;
                };
                
                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
            } else {
                throw new CustomException(ErrorCode.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
