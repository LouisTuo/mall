package com.macro.mall.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.utils.AutoSetObjPropertyUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Louis
 * @description
 * @create 2020-07-09 13:44
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class MybatisResultInterceptor implements Interceptor {

    // 拦截目标对象
    // 多个插件的时候，按照从后到前的顺序执行
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnVal = invocation.proceed();
        if (returnVal instanceof ArrayList<?>) {
            List<?> list = (List<?>) returnVal;
            if (CollectionUtil.isNotEmpty(list) && list.get(0) != null) {
                return AutoSetObjPropertyUtil.autoSetProperty((List)list);
            }
        }
        return returnVal;
    }

    // 包装目标对象
    // 为目标对象创建动态代理，并按照从前到后的顺序执行
    @Override
    public Object plugin(Object target) {
        // 读取 @Signature中的配置，判断是否需要生成代理类
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    // 获取插件初始化参数
    @Override
    public void setProperties(Properties properties) {

    }
}
