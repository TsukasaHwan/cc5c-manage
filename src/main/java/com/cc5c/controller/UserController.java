package com.cc5c.controller;

import com.cc5c.api.UserControllerApi;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.User;
import com.cc5c.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController implements UserControllerApi {
    @Autowired
    private UserService userService;

    @Override
    @GetMapping("/findLoinUser")
    public User findLoginUser() {
        return userService.findLoginUser();
    }

    @Override
    @PostMapping("/add")
    public ResponseResult add(@RequestBody User user) {
        return userService.add(user);
    }

    @Override
    @GetMapping("/findOne")
    public User findOne(@RequestParam("userId") String userId) {
        return userService.findOne(userId);
    }

    @Override
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody String userId) {
        return userService.delete(userId);
    }

    @Override
    @PostMapping("/lock")
    public ResponseResult lock(@RequestBody String userId) {
        return userService.lock(userId);
    }

    @Override
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("parentId") String parentId) {
        return userService.list(parentId);
    }

    @Override
    @GetMapping("/checkPassword")
    public ResponseResult checkPassword(@RequestParam("password") String password) {
        return userService.checkPassword(password);
    }

    @Override
    @PostMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody User user) {
        return userService.updatePassword(user);
    }

    @Override
    @PostMapping("/updateOnePassword")
    public ResponseResult updateOnePassword(@RequestBody User user) {
        return userService.updateOnePassword(user);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult update(@RequestBody User user) {
        return userService.update(user);
    }

    @Override
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return userService.findAll();
    }
}
