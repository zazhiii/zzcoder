package com.zazhi.service.impl;

import com.zazhi.service.CommonService;
import com.zazhi.common.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 通用业务
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件
     *
     * @param file
     */
    public String uploadFile(MultipartFile file) {
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
            return aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        }
    }
}
