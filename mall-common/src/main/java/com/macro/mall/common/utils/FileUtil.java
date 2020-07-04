package com.macro.mall.common.utils;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Louis
 * @description
 * @create 2020-07-04 11:02
 */
@Slf4j
public class FileUtil {

    /**
     * @param response 响应对象
     * @return
     * @File file 下载文件
     */
    public static void downloadFile(HttpServletResponse response, File file) {
        if (!file.exists()) {
            log.error("文件不存在，下载失败");
            return;
        }
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException:{}", e.toString());
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (FileNotFoundException e) {
            log.error("系统找不到指定的文件:{}", e.toString());
        } catch (IOException e) {
            log.error("IO异常，：{}", e.toString());
        } finally {
            IoUtil.close(bis);
            IoUtil.close(os);
        }
    }

    //需要注意的是当删除某一目录时，必须保证该目录下没有其他文件才能正确删除，否则将删除失败。
    public static void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    //删除
                    file.delete();
                }
            }
        }
        //删除
        folder.delete();

    }
}
