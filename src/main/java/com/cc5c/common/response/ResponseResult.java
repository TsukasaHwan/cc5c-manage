package com.cc5c.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 4you
 * @date 2019/7/10
 */
@Data
@ToString
@NoArgsConstructor
public class ResponseResult implements Response {
    private int code = SUCCESS_CODE;
    private String msg = SUCCESS;

    public ResponseResult(ResultCode resultCode) {
        this.code = resultCode.code();
        this.msg = resultCode.msg();
    }

    public static ResponseResult SUCCESS() {
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public static ResponseResult FAIL() {
        return new ResponseResult(CommonCode.FAIL);
    }
}
