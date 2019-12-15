package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "历史记录接口", description = "提供设备信息历史记录查询")
public interface DeviceHistoryControllerApi {
    @ApiOperation(value = "历史记录信息查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true, dataType = "int"),
    })
    ResponseResult findPage(int pageIndex, int pageSize, String deviceId);
}
