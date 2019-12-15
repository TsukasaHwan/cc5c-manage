package com.cc5c.controller;

import com.cc5c.api.HomeControllerApi;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.response.code.UserCode;
import com.cc5c.entity.pojo.User;
import com.cc5c.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController implements HomeControllerApi {
    @Autowired
    private UserService userService;

    @Override
    @PostMapping("/ajaxLogin")
    public ResponseResult login(@RequestBody User user) {
        return userService.login(user);
    }

    @Override
    @GetMapping("/unLogin")
    public ResponseResult unLogin() {
        return new ResponseResult(UserCode.UN_LOGIN);
    }
}
