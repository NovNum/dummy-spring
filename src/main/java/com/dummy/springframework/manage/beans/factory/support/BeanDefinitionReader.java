package com.dummy.springframework.manage.beans.factory.support;

import com.dummy.springframework.manage.core.io.Resource;
import com.dummy.springframework.manage.core.io.ResourceLoader;
import com.dummy.springframework.manage.ex.BeansException;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;
}
