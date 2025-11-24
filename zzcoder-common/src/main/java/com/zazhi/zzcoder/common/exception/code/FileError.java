package com.zazhi.zzcoder.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lixh
 * @since 2025/9/6 21:56
 */
@Getter
@AllArgsConstructor
public enum FileError implements IErrorCode {
    FILE_UPLOAD_FAIL(50001, "文件上传失败"),
    FILE_DELETE_FAIL(50002, "文件删除失败"),
    FILE_URL_INVALID(50003, "文件URL无效");

    private final int code;
    private final String message;
}
