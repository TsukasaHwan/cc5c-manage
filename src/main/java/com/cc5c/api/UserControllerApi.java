package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户管理接口", description = "提供用户的增、删、改、查")
public interface UserControllerApi {
    @ApiOperation(value = "获取登录用户信息", httpMethod = "GET")
    User findLoginUser();

    @ApiOperation(value = "用户添加", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户信息", paramType = "body", required = true, dataType = "User")
    ResponseResult add(User user);

    @ApiOperation(value = "单用户查询", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", required = true, dataType = "String")
    User findOne(String userId);

    @ApiOperation(value = "用户删除", httpMethod = "POST")
    @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "body", required = true, dataType = "String")
    ResponseResult delete(String userId);

    @ApiOperation(value = "用户禁用", httpMethod = "POST")
    @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "body", required = true, dataType = "String")
    ResponseResult lock(String userId);

    @ApiOperation(value = "子用户查询", httpMethod = "GET")
    @ApiImplicitParam(name = "parentId", value = "父级ID", paramType = "query", required = true, dataType = "String")
    ResponseResult list(String parentId);

    @ApiOperation(value = "判断原密码是否输入正确", httpMethod = "GET")
    @ApiImplicitParam(name = "password", value = "原密码", paramType = "query", required = true, dataType = "String")
    ResponseResult checkPassword(String password);

    @ApiOperation(value = "更新密码", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户信息（注意只传入password即可）", paramType = "body", required = true, dataType = "User")
    ResponseResult updatePassword(User user);

    @ApiOperation(value = "修改某一用户密码", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户信息（注意需要传入id）", paramType = "body", required = true, dataType = "User")
    ResponseResult updateOnePassword(User user);

    @ApiOperation(value = "用户更新", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "用户信息", paramType = "body", required = true, dataType = "User")
    ResponseResult update(User user);

    @ApiOperation(value = "查询全部用户", httpMethod = "GET")
    ResponseResult findAll();
}
