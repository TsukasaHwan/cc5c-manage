package com.cc5c.controller;

import com.cc5c.api.DeviceHistoryControllerApi;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.service.DeviceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class DeviceHistoryController implements DeviceHistoryControllerApi {
    @Autowired
    private DeviceHistoryService deviceHistoryService;

    @Override
    @GetMapping("/findPage")
    public ResponseResult findPage(int pageIndex, int pageSize, String deviceId) {
        return deviceHistoryService.findPage(pageIndex, pageSize, deviceId);
    }
}
