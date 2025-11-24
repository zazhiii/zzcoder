package com.zazhi.zzcoder.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件
     * @param file
     */
    String uploadFile(MultipartFile file);

    /**
     * 通过url删除文件
     * @param url 文件url
     */
    void deleteFileByUrl(String url);
}
