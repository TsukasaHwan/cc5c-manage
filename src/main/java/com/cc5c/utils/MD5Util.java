package com.cc5c.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author 4you
 * @date 2019/7/29
 */
public class MD5Util {
    /**
     * 密码加密
     *
     * @param password 明文密码
     * @param salt     盐值
     * @return
     */
    public static String encrypt(String password, String salt) {
        //盐：为了即使相同的密码不同的盐加密后的结果也不同
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        return new SimpleHash(Md5Hash.ALGORITHM_NAME, password, byteSalt, 2).toHex();
    }

    /**
     * 密码对比
     *
     * @param password
     * @param salt
     * @param encryptPassword
     * @return
     */
    public static boolean eqPassword(String password, String salt, String encryptPassword) {
        password = encrypt(password, salt);
        return password.equals(encryptPassword);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456", "admincc5c"));
    }
}
