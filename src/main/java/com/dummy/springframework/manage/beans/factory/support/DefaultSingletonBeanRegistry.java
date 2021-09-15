package com.dummy.springframework.manage.beans.factory.support;

import com.dummy.springframework.manage.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String,Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingletonBean(String beanName,Object singletonObject){
        singletonObjects.put(beanName,singletonObject);
    }
}
