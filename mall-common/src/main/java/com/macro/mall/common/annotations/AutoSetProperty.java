package com.macro.mall.common.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoSetProperty {
    // 字典 group_code
    String group();
    // 字典 目标字段值
    String sourceField();
}
