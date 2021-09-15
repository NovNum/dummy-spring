package com.dummy.springframework.manage.beans.factory.support;

import com.dummy.springframework.manage.beans.factory.config.BeanDefinition;
import com.dummy.springframework.manage.ex.BeansException;

import java.lang.reflect.Constructor;

/**
 * 实例化策略处理 注入类参数
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;
}
