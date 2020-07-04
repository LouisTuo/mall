package com.crazy.demo.gencoode.config;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hanfb
 * @Date: 2020/1/12 15:50
 * @describe:
 * @Version: 1.0
 */
@Configuration
public class UnitTestGlobaConfig {

    //全局变量
    public static Map<String, String> globaParamMap = new HashMap<>();

    //接口结果
    public static Map<String, Map<String,Object>> saveResponseNameMap = new HashMap<>();
}
