package com.crazy.demo.gencoode.controller;

import com.crazy.demo.gencoode.config.CodeGenerator;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.common.utils.FileUtil;
import com.macro.mall.common.utils.UuidUtil;
import com.macro.mall.common.utils.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自动化生成代码
 *
 * @author Linsk
 */
@Api(tags = "自动化生成代码")
@Slf4j
@RestController
@RequestMapping("/codeGen")
public class CodeGeneratorController {
    @Autowired
    CodeGenerator codeGenerator;

    @ApiOperation(value = "代码自动生成-单表生成", notes = "代码自动生成-单表生成")
    @GetMapping(value = "/singleTableGen")
    public CommonResult<String> generCode(@RequestParam(name = "tableName", required = true) String tableName) {
        CommonResult<String> result = new CommonResult<>();
        try {
            codeGenerator.generCode(tableName);
            result.setCode(ResultCode.SUCCESS.getCode());
            result.setData("代码生成成功");
        } catch (Exception e) {
            result.setCode(ResultCode.FAILED.getCode());
            log.error("代码生成失败，错误信息{}", e.toString());
            e.printStackTrace();
            result.setMessage("代码生成失败，请查看日志信息");
        }
        return result;
    }

    @ApiOperation("下载")
    @GetMapping("/downloadSDK")
    public void download(HttpServletResponse response) throws Exception {

        File demoFile = codeGenerator.generCode("demo");

        List<File> fileList = Arrays.asList(
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\doc\\cn-hangzhou"), //
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\lib"), //
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\Readme.en.md") //
        );
        List<File> files = new ArrayList<>(fileList);
        files.add(demoFile);

        String rootPath = ResourceUtils.getURL("classpath:").getPath();
        String uuid = UuidUtil.getUuid();
        String fileName = "demo.zip";

        String zipFilePath = rootPath + uuid + File.separator + fileName;

        File file = ZipUtil.genZipFile(files, zipFilePath);


        FileUtil.downloadFile(response, file);

        cn.hutool.core.io.FileUtil.del(file.getParentFile());
    }
}
