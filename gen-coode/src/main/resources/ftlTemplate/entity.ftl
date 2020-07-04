package ${basePackageEntity};

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
* @Description: ${entityName}实体类
* @author ${author}
* @date ${date}
*/
@Data
@TableName("${tableName}")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="${tableName}对象", description="${reMarks}")
public class ${entityName} {

<#--循环生成变量-->
<#list columns as col>
    /**${col.remarks}*/
    <#if col.fieldName == 'id'>
        @TableId(type = IdType.ID_WORKER_STR)
    <#else>

        <#if col.javaType =='Date'>
            <#if col.jdbcType == 'DATE'>
                @Excel(name = "${col.remarks}", width = 20, format = "yyyy-MM-dd")
                @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
                @DateTimeFormat(pattern="yyyy-MM-dd")
            <#else>

                @Excel(name = "${col.remarks}", width = 20, format = "yyyy-MM-dd HH:mm:ss")
                @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
            </#if>
        <#else>
            @Excel(name = "${col.remarks}", width = 15)
        </#if>
    </#if>

    @ApiModelProperty(value = "${col.remarks}")
    private <#if col.javaType=='java.sql.Blob'>String<#else>${col.javaType}</#if> ${col.fieldName};
</#list>
}
