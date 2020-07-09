package com.macro.mall.service;

import com.macro.mall.model.SysDict;

/**
 * @author Louis
 * @description
 * @create 2020-07-09 10:49
 */
public interface SysDictService {
    boolean insert(SysDict record);

    SysDict queryDict(String groupCode, String dictKey);

}
