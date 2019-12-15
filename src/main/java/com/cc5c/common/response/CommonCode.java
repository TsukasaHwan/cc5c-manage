package com.cc5c.common.response;

import lombok.ToString;

/**
 * @author 4you
 * @date 2019/7/10
 */
@ToString
public enum CommonCode implements ResultCode {
    //操作成功
    SUCCESS(200, "操作成功"),
    //权限不足，无权操作
    UN_AUTHORISE(403, "权限不足，无权操作"),
    //操作失败
    FAIL(500, "操作失败"),
    //非法参数
    INVALID_PARAM(998, "非法参数"),
    //系统错误
    SERVER_ERROR(999, "系统错误");

    int code;
    String msg;

    CommonCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
