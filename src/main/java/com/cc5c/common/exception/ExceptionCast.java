package com.cc5c.common.exception;

import com.cc5c.common.response.ResultCode;

/**
 * @author 4you
 * @date 2019/7/10
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
