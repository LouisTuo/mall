package com.macro.mall.demo.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.macro.mall.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @author Louis
 * @description 日志参数切面
 * @create 2020-06-30 11:10
 */
@Aspect
@Component
@Slf4j
public class LogRecordAspectUtils {

    // 定义切点 Pointcut
    @Pointcut("execution(* *..*.*.controller..*.*(..))")
    public void executeService() {

    }

    // 执行切点之前
    @Before("executeService()")
    public void executeBefore(JoinPoint joinPoint) {

//        HttpServletRequest request = getRequest();
//
//        String method = request.getMethod();
//        String uri = request.getRequestURI();
//
//        // 获取目标方法的参数信息
//        Object[] args = joinPoint.getArgs();
//
//        String param = getParams(args);
//
//        log.info("\n -------------参数信息------------ \n");
//        log.info("\n 入参信息：【requestMethod】:{}, 【url】:{},【params】:{}", method, uri, param);


    }

    private String getParams(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            // 如果参数类型是请求和响应的HTTP，则不需要拼接
            // 【这2个参数，使用JSON.toJSONString()转换会抛异常】
            if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
                continue;
            }
            String param = args[i] == null ? "" : JSONUtil.toJsonStr(args[i]);
            sb.append(param);
        }
        return sb.toString();
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    // 通知(环绕)
    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();
        String params = "";
        // result 的值 就是被拦截方法的返回值
        Object result = joinPoint.proceed();

        if (args.length > 0) {
            if ("POST".equals(method)) {
                params = getParams(args);
            } else if ("GET".equals(method)) {
                params = queryString;
            }
            if (!StringUtils.isEmpty(params)) {
                params = URLDecoder.decode(params, "UTF-8");
            }

            log.info("\n 监听参数信息:【params】:{}", params);
            log.info("\n 出参信息:【responBody】:{}", JSONUtil.toJsonStr(result));

        }
        return result;
    }

    // 执行点以后
    @After("executeService()")
    public void executeAfter() {
        log.info("\n ------------------------ \n");
    }

}
