package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.DeviceHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "设备待办接口", description = "提供设备待办记录查询")
public interface UpcomingControllerApi {
    @ApiOperation(value = "待办信息分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true, dataType = "int"),
    })
    ResponseResult findPage(int pageIndex, int pageSize);

    @ApiOperation(value = "待办信息查询", httpMethod = "GET")
    @ApiImplicitParam(name = "deviceHistoryId", value = "待办信息ID", paramType = "query", required = true, dataType = "String")
    DeviceHistory findOne(String deviceHistoryId);

    @ApiOperation(value = "待办信息修改", httpMethod = "POST")
    @ApiImplicitParam(name = "deviceHistory", value = "待办信息", paramType = "body", required = true, dataType = "DeviceHistory")
    ResponseResult edit(DeviceHistory deviceHistory);

    @ApiOperation(value = "待办信息删除", httpMethod = "GET")
    @ApiImplicitParam(name = "deviceHistoryId", value = "待办信息", paramType = "query", required = true, dataType = "String")
    ResponseResult delete(String deviceHistoryId);
}
