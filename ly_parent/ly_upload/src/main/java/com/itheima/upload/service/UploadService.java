package com.itheima.upload.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UploadService {


    private static final List<String> allowedFileType = Arrays.asList("image/png", "image/jpeg", "image/gif");

    private static final String Nginx_dir = "C:\\Nginx\\nginx-1.12.2\\html";


    private static final String Nginx_Http = "http://image.leyou.com";
    /**
     * 实现图片上传
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
        if(file==null){
            throw new LyException(ResponseCode.INVALID_FILE_TYPE);
        }
        //判断图片类型（文件扩展名, 文件内容）
        String type = file.getContentType();
        if(!allowedFileType.contains(type)){
            throw new LyException(ResponseCode.INVALID_FILE_TYPE);
        }
        //校验图片内容
        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LyException(ResponseCode.INVALID_FILE_TYPE);
        }
        //将源文件拷贝到 nginx目录下
        try {
            file.transferTo(new File(Nginx_dir, file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LyException(ResponseCode.FILE_WRITER_ERROR);
        }

        //将nginx服务器图片在线地址返回
        return Nginx_Http+"/"+file.getOriginalFilename();
    }

    public Map<String, String> getSignature() {
        String accessId = "LTAI4FojEhh6akU6xzDv5P5A"; // 请填写您的AccessKeyId。
        String accessKey = "NmPlHITIL3JIZ851R0EObolho83D7m"; // 请填写您的AccessKeySecret。
        String endpoint = "oss-cn-beijing.aliyuncs.com"; // 请填写您的 endpoint。
        String bucket = "chinaarl"; // 请填写您的 bucketname 。
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String dir = ""; // 用户上传文件时指定的前缀。

        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime));
            // respMap.put("expire", formatISO8601Date(expiration));
            return respMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
