package com.macro.mall.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.common.annotations.AutoSetProperty;
import com.macro.mall.model.SysDict;
import com.macro.mall.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Louis
 * @description 自动设置属性值
 * @create 2020-07-09 14:30
 */
@Slf4j
@Component
public class AutoSetObjPropertyUtil implements InitializingBean {
    @Autowired
    private SysDictService sysDictService;

    private static SysDictService staticSysDictService;

    @Override
    public void afterPropertiesSet() throws Exception {
        staticSysDictService = sysDictService;
    }

    public static final String TARGET_FIELD_NAME = "targetFieldName";
    public static final String GROUP = "group";
    public static final String SOURCE_FIELD_NAME = "sourceFieldName";

    public static List<Object> autoSetProperty(List<Object> list) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        // Field[] fields = list.get(0).getClass().getDeclaredFields();
        Field[] fields = FieldUtils.getAllFields(list.get(0).getClass());
        List<Map<String, String>> autoExpandNameList = new ArrayList<>();

        for (Field field : fields) {
            AutoSetProperty annotation = field.getAnnotation(AutoSetProperty.class);
            if (annotation != null) {
                Map<String, String> map = new HashMap<>();
                map.put(TARGET_FIELD_NAME, field.getName());
                map.put(GROUP, annotation.group());
                map.put(SOURCE_FIELD_NAME, annotation.sourceField());
                autoExpandNameList.add(map);
            }
        }

        for (Object obj : list) {
            for (Map<String, String> map : autoExpandNameList) {
                try {
                    String dictKey = (String) getFieldValueByName(map.get(SOURCE_FIELD_NAME), obj);
                    SysDict sysDict = staticSysDictService.queryDict(map.get(GROUP), dictKey);
                    if (sysDict != null) {
                        setFieldValue(map.get(TARGET_FIELD_NAME), obj, sysDict.getDictValue());
                    }
                } catch (Exception e) {
                    log.error("字典反射失败:{}", e);
                }
            }
        }
        return list;
    }

    public static Object getFieldValueByName(String name, Object obj) throws IllegalAccessException {
        try {
            String firstletter = name.substring(0, 1).toUpperCase();
            String getter = "get" + firstletter + name.substring(1);
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            return method.invoke(obj, new Object[]{});
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(String fieldName, Object obj, String data) throws IllegalAccessException {
        if (data == null) {
            return;
        }
        try {
            String firstletter = fieldName.substring(0, 1).toUpperCase();
            String getter = "set" + firstletter + fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter, String.class);
            method.invoke(obj, data);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
