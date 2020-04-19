package com.macro.mall.demo.service;


import cn.hutool.core.io.FileUtil;

import java.io.File;

public interface MailService {
    void sendSimpleMail(String from, String to, String cc, String subject, String content);

    void sendHtmlMail(String from, String to, String cc, String subject, String context, File file);

    public void sendAttachFileMail(String from, String to, String subject, String content, File file);
}
