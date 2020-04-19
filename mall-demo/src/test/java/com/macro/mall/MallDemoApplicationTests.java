package com.macro.mall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.demo.MallDemoApplication;
import com.macro.mall.demo.component.PythonTask;
import com.macro.mall.demo.service.MailService;
import com.macro.mall.model.PmsProduct;
import org.apache.coyote.http11.filters.VoidOutputFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallDemoApplication.class)
public class MallDemoApplicationTests {
	private Logger logger = LoggerFactory.getLogger(MallDemoApplicationTests.class);

	@Resource
	private MailService mailService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testLogStash() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		PmsProduct product = new PmsProduct();
		product.setId(1L);
		product.setName("小米手机");
		product.setBrandName("小米");
		logger.info(mapper.writeValueAsString(product));
		logger.error(mapper.writeValueAsString(product));
	}

	@Autowired
	TemplateEngine templateEngine;

	@Autowired
	PythonTask pythonTask;

	@Test
	public void testPython() throws Exception{
		pythonTask.execPyTask();
	}

	@Test
	public void test(){
		String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
		System.out.println(now);
	}

	@Test
	public void sendMail() {
			//mailService.sendSimpleMail("958053629@qq.com","958053629@qq.com","","标题","Hello World");
		try {

			Context context = new Context();
			context.setVariable("userName","小龙哥");
			context.setVariable("execDetail","小龙哥");
			String mail = templateEngine.process("mailTemplate.html",context);

			File file = ResourceUtils.getFile("classpath:pic/goodPic.gif");

			mailService.sendHtmlMail("958053629@qq.com","958053629@qq.com","","标题", mail,file);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
