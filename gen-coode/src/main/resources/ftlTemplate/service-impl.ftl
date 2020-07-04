package ${basePackageServiceImpl};

import ${basePackageEntity}.${entityName};
import ${basePackageService}.I${entityName}Service;
import ${basePackageDao}.${entityName}Mapper;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: ${entityName}接口实现类
 * @author ${author}
 * @date ${date}
 * @Version: ${version}
 */
@Service
public class ${entityName}ServiceImpl extends ServiceImpl<${entityName}Mapper, ${entityName}>  implements I${entityName}Service {

	@Autowired
    private ${entityName}Mapper ${entityVarName}Mapper;

}
