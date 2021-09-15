package com.dummy.springframework.manage;

import cn.hutool.core.io.IoUtil;
import com.dummy.springframework.manage.beans.PropertyValue;
import com.dummy.springframework.manage.beans.PropertyValues;
import com.dummy.springframework.manage.beans.factory.config.BeanDefinition;
import com.dummy.springframework.manage.beans.factory.config.BeanReference;
import com.dummy.springframework.manage.beans.factory.support.DefaultListableBeanFactory;
import com.dummy.springframework.manage.beans.factory.xml.XmlBeanDefinitionReader;
import com.dummy.springframework.manage.core.io.DefaultResourceLoader;
import com.dummy.springframework.manage.core.io.Resource;
import com.dummy.springframework.manage.test.UserDao;
import com.dummy.springframework.manage.test.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class ApiTest {

	@Test
	void contextLoads() {
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

		//注册bean
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
		defaultListableBeanFactory.registerBeanDefinition("userService",beanDefinition);

		//从单例工厂中获取bean
		UserService userService = (UserService) defaultListableBeanFactory.getBean("userService","张三");
		userService.queryUserInfo();
	}

	@Test
	void applyPropertyValues(){
		// 初始化 beanFactory
		DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
		// userDao 注册
		defaultListableBeanFactory.registerBeanDefinition("userDao",new BeanDefinition(UserDao.class));
		//userService 属性设置
		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
		propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));
		//将userService 注册到BeanDefinition
		BeanDefinition beanDefinition = new BeanDefinition(UserService.class,propertyValues);
		defaultListableBeanFactory.registerBeanDefinition("userService",beanDefinition);
		// userService 获取bean
		UserService userService = (UserService) defaultListableBeanFactory.getBean("userService");
		userService.queryUserInfo();
	}

	private DefaultResourceLoader defaultResourceLoader;
	@BeforeEach
	public void init(){
		defaultResourceLoader = new DefaultResourceLoader();
	}

	@Test
	public void test_classpath() throws IOException {
		Resource resource = defaultResourceLoader.getResource("classpath:important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_file() throws IOException{
		Resource resource = defaultResourceLoader.getResource("src/test/resources/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_url() throws IOException{
		Resource resource = defaultResourceLoader.getResource("https://github.com/fuzhengwei/small-spring/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_xml() throws IOException{
		//初始化beanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		//读取配置文件 注册bean
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
		//获取bean 实例对象 todo  带完成
	}
}
