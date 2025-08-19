package com.zazhi.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件
     * @param file
     */
    String uploadFile(MultipartFile file);
}
