package com.zazhi.zzcoder.controller.common;

import com.zazhi.zzcoder.common.pojo.result.Result;
import com.zazhi.zzcoder.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zazhi
 * @date 2024/11/6
 * @description: 通用接口
 */
@RestController
@RequestMapping("/api/file")
@Tag(name = "文件相关")
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}", file.getOriginalFilename());
        return Result.success(fileService.uploadFile(file));
    }
}