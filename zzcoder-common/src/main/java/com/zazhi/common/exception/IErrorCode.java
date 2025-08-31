package com.zazhi.common.exception;

import lombok.Data;

/**
 *
 * @author lixh
 * @since 2025/8/31 13:52
 */
public interface IErrorCode {
    int getCode();
    String getMessage();
}
