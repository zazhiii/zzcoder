package com.zazhi.controller.oj;

import com.zazhi.result.Result;
import com.zazhi.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/common")
@Tag(name = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    CommonService commonService;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        return Result.success(commonService.uploadFile(file));
    }
}