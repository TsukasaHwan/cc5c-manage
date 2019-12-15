package com.cc5c.api;

import com.cc5c.common.request.QueryLogPageRequest;
import com.cc5c.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "日志接口", description = "提供日志的查询")
public interface LogControllerApi {
    @ApiOperation(value = "分页查询日志信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true, dataType = "int"),
    })
    ResponseResult list(int pageIndex, int pageSize, QueryLogPageRequest queryLogPageRequest);
}
