package com.project.admin.controller.download;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/download")
public class FileDownloadController {

    // 使用项目根目录 + file 文件夹
    private final String fileDir = System.getProperty("user.dir") + File.separator + "file" + File.separator;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        System.out.println(">>>> 进入 downloadFile 方法，fileName=" + fileName);
        try {
            File file = new File(fileDir + fileName);

            if (!file.exists() || !file.isFile()) {
                System.out.println("文件不存在: " + file.getAbsolutePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            System.out.println("文件路径: " + file.getAbsolutePath());
            System.out.println("文件大小: " + file.length() + " bytes");

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(encodedFileName)
                    .build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(file.length());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
