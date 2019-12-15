package com.cc5c.common.response;

/**
 * @author 4you
 * @date 2019/7/10
 */
public interface ResultCode {
    /**
     * 操作码
     *
     * @return
     */
    int code();

    /**
     * 提示信息
     *
     * @return
     */
    String msg();
}
