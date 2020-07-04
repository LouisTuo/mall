package com.crazy.demo.gencoode.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.UUIDGenerator;
import com.crazy.demo.gencoode.entity.Demo;
import com.crazy.demo.gencoode.service.IDemoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
* @Description: DemoController类
* @author sys
* @date 2020-07-04 21:37
*/
@Api(tags="DEMO")
@RestController
@RequestMapping("/demo")
public class DemoController extends JeecgController<Demo, IDemoService>{

    @Autowired
    private IDemoService demoService;
    
    	
	/**
	 * 分页列表查询
	 *
	 * @param Demo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="DEMO-分页列表查询", notes="DEMO-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(Demo demo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Demo> queryWrapper = QueryGenerator.initQueryWrapper(demo, req.getParameterMap());
		Page<Demo> page = new Page<Demo>(pageNo, pageSize);
		IPage<Demo> pageList = demoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param Demo
	 * @return
	 */
    @PostMapping("/add")
    @ApiOperation(value="DEMO-添加", notes="DEMO-添加")
    public Result<?> add(Demo demo) throws Exception{
		demo.setId(UUIDGenerator.generate());
    	demoService.save(demo);
        return Result.ok("新增成功!");
    }
    
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
    @PostMapping("/deleteById")
    @ApiOperation(value="DEMO-通过id删除", notes="DEMO-通过id删除")
    public Result<?> deleteById(@RequestParam String id) throws Exception {
        boolean result = demoService.removeById(id);
        String msg = "删除成功!";
        if(!result){
        	msg = "删除失败!";
        }
        return Result.ok(msg);
    }
    /**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/deleteBatch")
	@ApiOperation(value="DEMO-批量删除", notes="DEMO-批量删除")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		boolean result = this.demoService.removeByIds(Arrays.asList(ids.split(",")));
		String msg = "批量删除成功!";
        if(!result){
        	msg = "批量删除失败!";
        }
		return Result.ok(msg);
	}
	/**
	 * 编辑
	 *
	 * @param Demo
	 * @return
	 */
    @PostMapping("/edit")
	@ApiOperation(value="DEMO-编辑", notes="DEMO-编辑")
    public Result<?> edit(Demo demo) throws Exception {
        boolean result = demoService.updateById(demo);
       	String msg = "更新成功!";
        if(!result){
        	msg = "更新失败!";
        }
		return Result.ok(msg);
    }
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
    @PostMapping("/selectById")
	@ApiOperation(value="DEMO-通过id查询", notes="DEMO-通过id查询")
    public Result<?> selectById(@RequestParam String id) throws Exception {
        Demo demo = demoService.getById(id);
        return Result.ok(demo);
    }
   /**
    * 导出excel
    *
    * @param request
    * @param Demo
    */
   @RequestMapping(value = "/exportXls")
   @ApiOperation(value="DEMO-导出Excel", notes="DEMO-导出Excel")
   public ModelAndView exportXls(HttpServletRequest request, Demo demo) {
       return super.exportXls(request, demo, Demo.class, "DEMO");
   }
	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   	@ApiOperation(value="DEMO-导入Excel", notes="DEMO-导入Excel")
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
	    return super.importExcel(request, response, Demo.class);
	}
}