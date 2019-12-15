package com.cc5c.controller;

import com.cc5c.api.UpcomingControllerApi;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.DeviceHistory;
import com.cc5c.service.UpcomingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upcoming")
public class UpcomingController implements UpcomingControllerApi {
    @Autowired
    private UpcomingService upcomingService;

    @Override
    @GetMapping("/findPage")
    public ResponseResult findPage(int pageIndex, int pageSize) {
        return upcomingService.findPage(pageIndex, pageSize);
    }

    @Override
    @GetMapping("/findOne")
    public DeviceHistory findOne(@RequestParam("deviceHistoryId") String deviceHistoryId) {
        return upcomingService.findOne(deviceHistoryId);
    }

    @Override
    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody DeviceHistory deviceHistory) {
        return upcomingService.edit(deviceHistory);
    }

    @Override
    @GetMapping("/delete")
    public ResponseResult delete(String deviceHistoryId) {
        return upcomingService.delete(deviceHistoryId);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
