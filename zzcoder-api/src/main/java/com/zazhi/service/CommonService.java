package com.zazhi.service;

import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    /**
     * 上传文件
     * @param file
     */
    String uploadFile(MultipartFile file);
}
