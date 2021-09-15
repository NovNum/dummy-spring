package com.dummy.springframework.manage.beans.factory.config;

/**
 * 单例 注册
 */
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);
}
