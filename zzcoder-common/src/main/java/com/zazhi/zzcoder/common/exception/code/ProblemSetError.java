package com.zazhi.zzcoder.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lixh
 * @since 2025/8/31 13:55
 */
@Getter
@AllArgsConstructor
public enum ProblemSetError implements IErrorCode {
    PROBLEM_SET_NOT_FOUND(40001, "题单不存在"),
    PROBLEM_ALREADY_IN_PROBLEM_SET(40002, "题目已存在于题单中"),
    NO_PERMISSION(40003, "没有权限操作该题单"),
    DELETE_FAILED(40004, "删除题单失败");

    private final int code;
    private final String message;
}
