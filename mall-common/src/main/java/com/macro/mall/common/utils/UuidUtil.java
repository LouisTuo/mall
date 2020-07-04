package com.macro.mall.common.utils;

import java.util.UUID;

/**
 * @author Louis
 * @description
 * @create 2020-07-04 18:56
 */
public class UuidUtil {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
