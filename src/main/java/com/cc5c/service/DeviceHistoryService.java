package com.cc5c.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc5c.common.exception.ExceptionCast;
import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.QueryPageResult;
import com.cc5c.common.response.QueryResult;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.DeviceHistory;
import com.cc5c.mapper.DeviceHistoryMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceHistoryService {
    @Autowired
    private DeviceHistoryMapper deviceHistoryMapper;

    public ResponseResult findPage(int pageIndex, int pageSize, String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        QueryWrapper<DeviceHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DeviceHistory::getDeviceId, deviceId).eq(DeviceHistory::getIsUpcoming, 1).orderByDesc(DeviceHistory::getInsertTime);
        PageHelper.startPage(pageIndex, pageSize);
        List<DeviceHistory> deviceHistories = deviceHistoryMapper.selectList(wrapper);
        QueryResult<DeviceHistory> queryResult = new QueryResult<>(deviceHistories);
        return new QueryPageResult(CommonCode.SUCCESS, queryResult);
    }
}
