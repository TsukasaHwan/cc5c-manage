package com.cc5c.common.response.code;

import com.cc5c.common.response.ResultCode;

public enum FileCode implements ResultCode {
    FILE_TYPE_ERROR(997, "文件格式错误");

    int code;
    String msg;

    FileCode(int code, String msg) {
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
