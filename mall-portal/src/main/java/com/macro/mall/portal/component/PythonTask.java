package com.macro.mall.portal.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Louis
 * @description 定时调用Python定时器
 * @create 2020-04-18 10:52
 */
@Component
@Slf4j
public class PythonTask {

    @Autowired
    private PythonTask pythonTask;

    @Scheduled(cron = "0 0/2 * * * ? ")
    public void execPyTask() {
        pythonTask.execPyTask();
        log.info("调用py脚本完成");
    }

}
