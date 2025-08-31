package com.zazhi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lixh
 * @since 2025/8/31 13:58
 */
@Getter
@AllArgsConstructor
public enum ProblemError implements IErrorCode {
    PROBLEM_NOT_FOUND(30001, "题目不存在");

    private final int code;
    private final String message;
}
