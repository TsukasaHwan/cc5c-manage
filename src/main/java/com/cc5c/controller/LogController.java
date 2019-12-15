package com.cc5c.controller;

import com.cc5c.api.LogControllerApi;
import com.cc5c.common.request.QueryLogPageRequest;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/log")
public class LogController implements LogControllerApi {
    @Autowired
    private LogService logService;

    @Override
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("pageIndex") int pageIndex,
                               @RequestParam("pageSize") int pageSize,
                               QueryLogPageRequest queryLogPageRequest) {
        return logService.list(pageIndex, pageSize, queryLogPageRequest);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
