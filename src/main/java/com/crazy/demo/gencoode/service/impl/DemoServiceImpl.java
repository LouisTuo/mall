package com.crazy.demo.gencoode.service.impl;

import com.crazy.demo.gencoode.entity.Demo;
import com.crazy.demo.gencoode.service.IDemoService;
import com.crazy.demo.gencoode.mapper.DemoMapper;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: Demo接口实现类
 * @author sys
 * @date 2020-07-04 21:37
 * @Version: 1.0.0
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo>  implements IDemoService {

	@Autowired
    private DemoMapper demoMapper;

}
