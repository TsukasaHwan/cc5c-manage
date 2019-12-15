package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户登录接口", description = "用户登录接口")
public interface HomeControllerApi {
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户信息", paramType = "body", required = true, dataType = "User")
    ResponseResult login(User user);

    @ApiOperation(value = "用户未登录返回接口", httpMethod = "GET")
    ResponseResult unLogin();
}
