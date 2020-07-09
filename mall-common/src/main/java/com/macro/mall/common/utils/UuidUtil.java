package com.macro.mall.common.utils;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Louis
 * @description
 * @create 2020-07-04 18:56
 */
public class UuidUtil {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ENGLISH);
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        boolean empty = CollectionUtil.isEmpty(list);
        System.out.println(empty);
        //String s = list.get(0);
    }
}
