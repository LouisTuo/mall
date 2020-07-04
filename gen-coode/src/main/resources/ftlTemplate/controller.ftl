package ${basePackageController};

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.UUIDGenerator;
import ${basePackageEntity}.${entityName};
import ${basePackageService}.I${entityName}Service;
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
* @Description: ${entityName}Controller类
* @author ${author}
* @date ${date}
*/
@Api(tags="${reMarks}")
@RestController
@RequestMapping("/${baseRequestMapping}")
public class ${entityName}Controller extends JeecgController<${entityName}, I${entityName}Service>{

    @Autowired
    private I${entityName}Service ${entityVarName}Service;
    
    	
	/**
	 * 分页列表查询
	 *
	 * @param ${entityName}
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="${reMarks}-分页列表查询", notes="${reMarks}-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(${entityName} ${entityVarName},
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<${entityName}> queryWrapper = QueryGenerator.initQueryWrapper(${entityVarName}, req.getParameterMap());
		Page<${entityName}> page = new Page<${entityName}>(pageNo, pageSize);
		IPage<${entityName}> pageList = ${entityVarName}Service.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param ${entityName}
	 * @return
	 */
    @PostMapping("/add")
    @ApiOperation(value="${reMarks}-添加", notes="${reMarks}-添加")
    public Result<?> add(${entityName} ${entityVarName}) throws Exception{
		${entityVarName}.setId(UUIDGenerator.generate());
    	${entityVarName}Service.save(${entityVarName});
        return Result.ok("新增成功!");
    }
    
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
    @PostMapping("/deleteById")
    @ApiOperation(value="${reMarks}-通过id删除", notes="${reMarks}-通过id删除")
    public Result<?> deleteById(@RequestParam String id) throws Exception {
        boolean result = ${entityVarName}Service.removeById(id);
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
	@ApiOperation(value="${reMarks}-批量删除", notes="${reMarks}-批量删除")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		boolean result = this.${entityVarName}Service.removeByIds(Arrays.asList(ids.split(",")));
		String msg = "批量删除成功!";
        if(!result){
        	msg = "批量删除失败!";
        }
		return Result.ok(msg);
	}
	/**
	 * 编辑
	 *
	 * @param ${entityName}
	 * @return
	 */
    @PostMapping("/edit")
	@ApiOperation(value="${reMarks}-编辑", notes="${reMarks}-编辑")
    public Result<?> edit(${entityName} ${entityVarName}) throws Exception {
        boolean result = ${entityVarName}Service.updateById(${entityVarName});
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
	@ApiOperation(value="${reMarks}-通过id查询", notes="${reMarks}-通过id查询")
    public Result<?> selectById(@RequestParam String id) throws Exception {
        ${entityName} ${entityVarName} = ${entityVarName}Service.getById(id);
        return Result.ok(${entityVarName});
    }
   /**
    * 导出excel
    *
    * @param request
    * @param ${entityName}
    */
   @RequestMapping(value = "/exportXls")
   @ApiOperation(value="${reMarks}-导出Excel", notes="${reMarks}-导出Excel")
   public ModelAndView exportXls(HttpServletRequest request, ${entityName} ${entityVarName}) {
       return super.exportXls(request, ${entityVarName}, ${entityName}.class, "${reMarks}");
   }
	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   	@ApiOperation(value="${reMarks}-导入Excel", notes="${reMarks}-导入Excel")
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
	    return super.importExcel(request, response, ${entityName}.class);
	}
}