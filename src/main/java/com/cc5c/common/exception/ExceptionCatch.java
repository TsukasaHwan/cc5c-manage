package com.cc5c.common.exception;

import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.response.ResultCode;
import com.cc5c.common.response.code.UserCode;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 *
 * @author 4you
 * @date 2019/7/10
 */
@Slf4j
@ControllerAdvice
public class ExceptionCatch {
    /**
     * GoogleMap线程安全并且不能改变
     */
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;

    private static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        builder.put(UnauthorizedException.class, CommonCode.UN_AUTHORISE);
        builder.put(UnknownAccountException.class, UserCode.ACCOUNT_NOT_EXIST);
        builder.put(IncorrectCredentialsException.class, UserCode.USERNAME_OR_PASSWORD_ERROR);
        builder.put(AuthenticationException.class, UserCode.USERNAME_OR_PASSWORD_ERROR);
        builder.put(LockedAccountException.class, UserCode.ACCOUNT_LOCKED);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult catchException(Exception exception) {
        log.error("catch exception:", exception);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应系统异常
        if (EXCEPTIONS.get(exception.getClass()) == null) {
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return new ResponseResult(EXCEPTIONS.get(exception.getClass()));
    }

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseResult catchCustomException(CustomException customException) {
        log.error("catch exception:", customException);
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }
}
