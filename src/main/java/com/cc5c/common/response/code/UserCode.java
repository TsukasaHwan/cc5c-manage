package com.cc5c.common.response.code;

import com.cc5c.common.response.ResultCode;

public enum UserCode implements ResultCode {
    //登录成功
    LOGIN_SUCCESS(1000, "登录成功"),
    //未登录
    UN_LOGIN(1001, "未登录"),
    //账户被禁用
    ACCOUNT_LOCKED(1002, "账户被禁用"),
    //用户不存在
    ACCOUNT_NOT_EXIST(1003, "用户不存在"),
    //用户名或密码错误
    USERNAME_OR_PASSWORD_ERROR(1004, "用户名或密码错误"),
    //用户名不能为空
    USERNAME_EMPTY(1005, "用户名不能为空"),
    //真实姓名不能为空
    REALNAME_EMPTY(1006, "真实姓名不能为空"),
    //EMAIL不能为空
    EMAIL_EMPTY(1007, "邮箱不能为空"),
    //EMAIL格式错误
    EMAIL_FORMAT_ERROR(1008, "邮箱格式错误"),
    //密码输入错误
    PASSWORD_ERROR(1009, "密码错误"),
    //密码不能为空
    PASSWORD_EMPTY(1010, "密码不能为空");

    int code;
    String msg;

    UserCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
