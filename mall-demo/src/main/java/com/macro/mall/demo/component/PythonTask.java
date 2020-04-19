package com.macro.mall.demo.component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.macro.mall.demo.service.ExecPythonService;
import com.macro.mall.demo.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Louis
 * @description 定时调用Python定时器
 * @create 2020-04-18 10:52
 */
@Component
@Slf4j
public class PythonTask {

    @Autowired
    private ExecPythonService execPythonService;

    @Resource
    private MailService mailService;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${python.path}")
    private String pyPath;

    @Scheduled(cron = "0 20 0,12 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ? ")
    public void execPyTask() throws Exception {
        if (StringUtils.isEmpty(pyPath)) {
            log.error("python脚本不存在");
            return;
        }
        log.info("pyPath:" + pyPath);
        String result = execPythonService.execPython(pyPath);
        log.info("执行py结果:" + result);

        InputStream inputStream = null;
        try {
            Context context = new Context();
            context.setVariable("userName", "小龙哥");
            context.setVariable("execDetail", result);
            String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            context.setVariable("execTime", now);
            String mail = templateEngine.process("mailTemplate.html", context);

            // File file = ResourceUtils.getFile("classpath:pic/goodPic.gif");
            ClassPathResource classPathResource = new ClassPathResource("pic" + File.separator + "goodPic.gif");
            // File file = classPathResource.getFile();
            // File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "pic/goodPic.gif");

            // springboot 打包后resouce下的文件无法直接使用
            inputStream = classPathResource.getInputStream();
            File newFile = File.createTempFile("test", ".gif");
            FileUtils.copyInputStreamToFile(inputStream, newFile);

            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(() -> {
                try {
                    mailService.sendHtmlMail("958053629@qq.com", "958053629@qq.com", "crazyalon110@gmail.com", "签到提醒", mail, newFile);
                } catch (Exception e) {
                    log.error("发送邮件失败：{}", e.getMessage());
                }
                log.info("发送邮件成功");
            });
        } catch (Exception e) {
            log.error("执行Py失败:{}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
