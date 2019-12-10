package com.itheima.upload.controller;

import com.itheima.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;


    /**
     * 接受处理上传文件
     * @param file  源文件
     * @return
     */
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(uploadService.uploadImage(file));
    }


    /**
     * 服务端签名 给前端返回上传文件需要参数
     * @return
     */
    @GetMapping("/signature")
    public ResponseEntity<Map<String, String>> getSignature(){
        return ResponseEntity.ok(uploadService.getSignature());
    }
}
