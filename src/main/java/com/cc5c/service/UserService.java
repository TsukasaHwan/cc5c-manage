package com.cc5c.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.response.Result;
import com.cc5c.common.response.code.UserCode;
import com.cc5c.entity.pojo.User;
import com.cc5c.mapper.UserMapper;
import com.cc5c.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String DEFAULT_PASSWORD = "123456";
    @Autowired
    private UserMapper userMapper;

    public User findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        wrapper.lambda().eq(User::getStatus, 1);
        return userMapper.selectOne(wrapper);
    }

    public ResponseResult login(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        User loginUser = findByUsername(user.getUsername());
        loginUser.setLoginDate(new Date());
        loginUser.setLoginIp(IPUtil.getIpAddr(HttpContextUtil.getHttpServletRequest()));
        userMapper.updateById(loginUser);
        return new Result(UserCode.LOGIN_SUCCESS, subject.getSession().getId());
    }

    public User findLoginUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return null;
        }
        user.setPassword("");
        return user;
    }

    public ResponseResult add(User user) {
        User loginUser = findLoginUser();
        String username = user.getUsername();
        String realName = user.getRealName();
        String email = user.getEmail();
        if (loginUser == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        if (StringUtils.isBlank(username)) {
            return new ResponseResult(UserCode.USERNAME_EMPTY);
        }
        if (StringUtils.isBlank(realName)) {
            return new ResponseResult(UserCode.REALNAME_EMPTY);
        }
        if (StringUtils.isBlank(email)) {
            return new ResponseResult(UserCode.EMAIL_EMPTY);
        }
        if (!ValidatorUtil.isEmail(email)) {
            return new ResponseResult(UserCode.EMAIL_FORMAT_ERROR);
        }
        String parentId = loginUser.getParentId();
        if ("-1".equals(parentId)) {
            user.setAccountType(2);
        } else {
            user.setAccountType(3);
        }
        user.setPassword(MD5Util.encrypt(DEFAULT_PASSWORD, username + "cc5c"));
        user.setParentId(loginUser.getUserId());
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setUserId(UUIDUtil.getUUID());
        userMapper.insert(user);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult delete(String userId) {
        JSONObject jsonObject = JSON.parseObject(userId);
        userId = String.valueOf(jsonObject.get("userId"));
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.FAIL();
        }
        User one = findOne(userId);
        if (one == null) {
            return ResponseResult.FAIL();
        }
        userMapper.deleteById(userId);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult lock(String userId) {
        JSONObject jsonObject = JSON.parseObject(userId);
        userId = String.valueOf(jsonObject.get("userId"));
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.FAIL();
        }
        User one = findOne(userId);
        if (one == null) {
            return ResponseResult.FAIL();
        }
        one.setStatus(-1);
        userMapper.updateById(one);
        return ResponseResult.SUCCESS();
    }

    public User findOne(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getStatus, 1);
        wrapper.lambda().eq(User::getUserId, userId);
        return userMapper.selectOne(wrapper);
    }

    public ResponseResult list(String parentId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getParentId, parentId);
        wrapper.lambda().eq(User::getStatus, 1);
        wrapper.lambda().orderByDesc(User::getCreateTime);
        List<User> users = userMapper.selectList(wrapper);
        users = users.stream().peek(user -> user.setPassword("")).collect(Collectors.toList());
        return new Result(CommonCode.SUCCESS, users);
    }

    public ResponseResult checkPassword(String password) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        String encryptPassword = loginUser.getPassword();
        if (!MD5Util.eqPassword(password, loginUser.getUsername() + "cc5c", encryptPassword)) {
            return new ResponseResult(UserCode.PASSWORD_ERROR);
        }
        return ResponseResult.SUCCESS();
    }

    public ResponseResult updatePassword(User user) {
        User login = (User) SecurityUtils.getSubject().getPrincipal();
        if (login == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return new ResponseResult(UserCode.PASSWORD_EMPTY);
        }
        User one = userMapper.selectById(login.getUserId());
        one.setPassword(MD5Util.encrypt(user.getPassword(), login.getUsername() + "cc5c"));
        userMapper.updateById(one);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult updateOnePassword(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            return new ResponseResult(UserCode.PASSWORD_EMPTY);
        }
        User one = findOne(user.getUserId());
        if (one == null) {
            return new ResponseResult(UserCode.ACCOUNT_NOT_EXIST);
        }
        one.setPassword(MD5Util.encrypt(user.getPassword(), user.getUsername() + "cc5c"));
        userMapper.updateById(one);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult update(User user) {
        String userId = user.getUserId();
        String username = user.getUsername();
        String realName = user.getRealName();
        String email = user.getEmail();
        if (StringUtils.isBlank(userId)) {
            return ResponseResult.FAIL();
        }
        User one = findOne(userId);
        if (one == null) {
            return ResponseResult.FAIL();
        }
        if (StringUtils.isBlank(username)) {
            return new ResponseResult(UserCode.USERNAME_EMPTY);
        }
        if (StringUtils.isBlank(realName)) {
            return new ResponseResult(UserCode.REALNAME_EMPTY);
        }
        if (StringUtils.isBlank(email)) {
            return new ResponseResult(UserCode.EMAIL_EMPTY);
        }
        if (!ValidatorUtil.isEmail(email)) {
            return new ResponseResult(UserCode.EMAIL_FORMAT_ERROR);
        }
        userMapper.updateById(user);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult findAll() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getStatus, 1);
        return new Result(CommonCode.SUCCESS, userMapper.selectList(wrapper));
    }
}
