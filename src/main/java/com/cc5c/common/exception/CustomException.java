package com.cc5c.common.exception;

import com.cc5c.common.response.ResultCode;

/**
 * 自定义异常
 *
 * @author 4you
 * @date 2019/7/10
 */
public class CustomException extends RuntimeException {
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }
}
