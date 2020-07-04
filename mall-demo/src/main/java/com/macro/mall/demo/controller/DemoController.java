package com.macro.mall.demo.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.utils.FileUtil;
import com.macro.mall.common.utils.UuidUtil;
import com.macro.mall.common.utils.ZipUtil;
import com.macro.mall.demo.dto.PmsBrandDto;
import com.macro.mall.demo.service.DemoService;
import com.macro.mall.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * 品牌管理示例controller
 */
@Api(tags = "DemoController", description = "品牌管理示例接口")
@Controller
public class DemoController {
    @Autowired
    private DemoService demoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @ApiOperation(value = "获取全部品牌列表")
    @RequestMapping(value = "/brand/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsBrand>> getBrandList() {
        return CommonResult.success(demoService.listAllBrand());
    }

    @ApiOperation(value = "添加品牌")
    @RequestMapping(value = "/brand/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createBrand(@Validated @RequestBody PmsBrandDto pmsBrand, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.validateFailed(result.getFieldError().getDefaultMessage());
        }
        CommonResult commonResult;
        int count = demoService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = CommonResult.success(pmsBrand);
            LOGGER.debug("createBrand success:{}", pmsBrand);
        } else {
            commonResult = CommonResult.failed("操作失败");
            LOGGER.debug("createBrand failed:{}", pmsBrand);
        }
        return commonResult;
    }

    @ApiOperation(value = "更新品牌")
    @RequestMapping(value = "/brand/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateBrand(@PathVariable("id") Long id, @Validated @RequestBody PmsBrandDto pmsBrandDto, BindingResult result) {
        if (result.hasErrors()) {
            return CommonResult.validateFailed(result.getFieldError().getDefaultMessage());
        }
        CommonResult commonResult;
        int count = demoService.updateBrand(id, pmsBrandDto);
        if (count == 1) {
            commonResult = CommonResult.success(pmsBrandDto);
            LOGGER.debug("updateBrand success:{}", pmsBrandDto);
        } else {
            commonResult = CommonResult.failed("操作失败");
            LOGGER.debug("updateBrand failed:{}", pmsBrandDto);
        }
        return commonResult;
    }

    @ApiOperation(value = "删除品牌")
    @RequestMapping(value = "/brand/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable("id") Long id) {
        int count = demoService.deleteBrand(id);
        if (count == 1) {
            LOGGER.debug("deleteBrand success :id={}", id);
            return CommonResult.success(null);
        } else {
            LOGGER.debug("deleteBrand failed :id={}", id);
            return CommonResult.failed("操作失败");
        }
    }

    @ApiOperation(value = "分页获取品牌列表")
    @RequestMapping(value = "/brand/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsBrand>> listBrand(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        List<PmsBrand> brandList = demoService.listBrand(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsBrand> brand(@PathVariable("id") Long id) {
        return CommonResult.success(demoService.getBrand(id));
    }

    @ApiOperation("下载")
    @GetMapping("/downloadSDK")
    public void download(HttpServletResponse response) throws FileNotFoundException {


        List<File> fileList = Arrays.asList(
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\doc\\cn-hangzhou"), //
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\lib"), //
                new File("D:\\idea\\mall\\mall-demo\\src\\main\\resources\\codeTemplate\\Readme.en.md") //
        );

        String rootPath = ResourceUtils.getURL("classpath:").getPath();
        String uuid = UuidUtil.getUuid();
        String fileName = "demo.zip";

        String zipFilePath = rootPath + uuid + File.separator + fileName;

        File file = ZipUtil.genZipFile(fileList, zipFilePath);


        FileUtil.downloadFile(response, file);

        cn.hutool.core.io.FileUtil.del(file.getParentFile());
    }
}
