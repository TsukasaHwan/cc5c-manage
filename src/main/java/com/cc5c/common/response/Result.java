package com.cc5c.common.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Result extends ResponseResult {
    private Object data;

    public Result(ResultCode resultCode, Object data) {
        super(resultCode);
        this.data = data;
    }
}
