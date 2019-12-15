package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "类目信息查询接口", description = "提供类目信息查询")
public interface CategoryControllerApi {
    @ApiOperation(value = "查询类目信息", httpMethod = "GET")
    ResponseResult findAll();
}
