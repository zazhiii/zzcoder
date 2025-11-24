package com.zazhi.zzcoder.service.impl;

import com.zazhi.zzcoder.common.exception.code.FileError;
import com.zazhi.zzcoder.common.exception.model.BizException;
import com.zazhi.zzcoder.common.utils.MinioUtil;
import com.zazhi.zzcoder.config.MinioConfig;
import com.zazhi.zzcoder.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 通用业务
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final MinioUtil minioUtil;

    private final MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param file 文件
     */
    public String uploadFile(MultipartFile file) {
        try {
            String objectName = minioUtil.upload(file);
            return "http://" + minioConfig.getDomain() + "/" + minioConfig.getBucketName() + "/" + objectName;
        } catch (Exception e) {
            throw new BizException(FileError.FILE_UPLOAD_FAIL);
        }
    }

    /**
     * 通过url删除文件
     *
     * @param url 文件url
     */
    @Override
    public void deleteFileByUrl(String url) {
        try {
            String prefix = "http://" + minioConfig.getDomain() + "/" + minioConfig.getBucketName() + "/";
            if (url.startsWith(prefix)) {
                String objectName = url.substring(prefix.length());
                minioUtil.remove(objectName);
            } else {
                throw new BizException(FileError.FILE_URL_INVALID);
            }
        } catch (Exception e) {
            throw new BizException(FileError.FILE_DELETE_FAIL);
        }
    }
}
