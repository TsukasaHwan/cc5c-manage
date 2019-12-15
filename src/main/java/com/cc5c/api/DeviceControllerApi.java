package com.cc5c.api;

import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.request.QueryPageRequest;
import com.cc5c.entity.pojo.Device;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "设备信息管理接口", description = "提供设备信息增删改查数据")
public interface DeviceControllerApi {
    @ApiOperation(value = "分页查询设备信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", required = true, dataType = "int"),
    })
    ResponseResult findPage(int pageIndex, int pageSize, QueryPageRequest queryPageRequest);

    @ApiOperation(value = "查询设备信息", httpMethod = "GET")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", paramType = "query", required = true, dataType = "String")
    Device findOne(String deviceId);

    @ApiOperation(value = "设备信息新增", httpMethod = "POST")
    @ApiImplicitParam(name = "device", value = "设备信息", paramType = "body", required = true, dataType = "Device")
    ResponseResult add(Device device);

    @ApiOperation(value = "设备信息修改", httpMethod = "POST")
    @ApiImplicitParam(name = "device", value = "设备信息", paramType = "body", required = true, dataType = "Device")
    ResponseResult edit(Device device);

    @ApiOperation(value = "设备信息删除", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "设备信息ID", paramType = "query", required = true, dataType = "String")
    ResponseResult delete(String id);

    @ApiOperation(value = "设备信息EXCEL导入", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "对应file", paramType = "query", required = false),
            @ApiImplicitParam(name = "categoryId", value = "类目ID", paramType = "query", required = true, dataType = "String"),
    })
    ResponseResult importExcel(MultipartFile file, String categoryId);

}
