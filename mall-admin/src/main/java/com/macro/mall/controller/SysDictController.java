package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.SysDict;
import com.macro.mall.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Louis
 * @description 字典
 * @create 2020-07-09 10:45
 */
@RestController
@RequestMapping("/dict")
@Api(value = "字典表管理", tags = "字典表管理")
public class SysDictController {

    @Autowired
    private SysDictService dictService;

    @PostMapping("/addDict")
    @ApiOperation(value = "添加字典")
    public CommonResult<String> addDict(@RequestBody SysDict dict) {
        boolean result = dictService.insert(dict);
        if(result) {
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }
}
