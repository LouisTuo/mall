package com.macro.mall.demo.service.impl;

import com.macro.mall.demo.service.ExecPythonService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForLocalTime;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Louis
 * @description
 * @create 2020-04-18 10:59
 */
@Service
@Slf4j
public class ExecPythonServiceImpl implements ExecPythonService {

    // Windows环境，要配置成GBK
    // Linux,配置成UTF-8
    public static final String CHARSETNAME = "utf-8";

    @Override
    public String execPython(String path, String... args) {
        String[] arguements = dealArguments(path, args);
        String result;
        try {
                Process process = Runtime.getRuntime().exec(arguements);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), CHARSETNAME));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
                log.info(line);
            }
            result = sb.toString();
            in.close();
            //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
            //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
            int re = process.waitFor();
            if (re == 0) {
                log.info("执行Py成功");
            } else {
                log.info("执行Py失败");
                result = "执行Py失败";
            }

        } catch (Exception e) {
            log.error("调用Py脚本失败:{}", e);
            result = "调用Py脚本失败";
        }
        return result;
    }

    private String[] dealArguments(String path, String[] args) {
        int length = args.length;
        String[] arguements = new String[length + 2];
        for (int i = 0; i < arguements.length; i++) {
            if (i == 0) {
                arguements[0] = "python3";
            } else if (i == 1) {
                arguements[1] = path;
            } else {
                arguements[i] = args[i - 2];
            }
        }
        return arguements;
    }

}
