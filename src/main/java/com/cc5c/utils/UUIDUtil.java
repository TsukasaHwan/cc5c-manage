package com.cc5c.utils;

import java.util.UUID;

public class UUIDUtil {
    private UUIDUtil() {}

    public static String getUUID() {
        return UUID.randomUUID().toString().trim();
    }
}
