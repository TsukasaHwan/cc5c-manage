package com.cc5c.controller;

import com.cc5c.api.DeviceControllerApi;
import com.cc5c.common.request.QueryPageRequest;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.Device;
import com.cc5c.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/device")
public class DeviceController implements DeviceControllerApi {

    @Autowired
    private DeviceService deviceService;

    @Override
    @GetMapping("/findPage")
    public ResponseResult findPage(@RequestParam("pageIndex") int pageIndex,
                                   @RequestParam("pageSize") int pageSize,
                                   QueryPageRequest queryPageRequest) {
        return deviceService.findPage(pageIndex, pageSize, queryPageRequest);
    }

    @Override
    @GetMapping("/findOne")
    public Device findOne(@RequestParam("deviceId") String deviceId) {
        return deviceService.findOne(deviceId);
    }

    @Override
    @PostMapping("/add")
    public ResponseResult add(@RequestBody Device device) {
        return deviceService.add(device);
    }

    @Override
    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody Device device) {
        return deviceService.edit(device);
    }

    @Override
    @GetMapping("/delete")
    public ResponseResult delete(String id) {
        return deviceService.delete(id);
    }

    @Override
    @PostMapping("/import")
    public ResponseResult importExcel(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "categoryId") String categoryId) {
        return deviceService.importExcel(file, categoryId);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
