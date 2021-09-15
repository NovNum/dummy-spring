package com.dummy.springframework.manage.core;

import com.dummy.springframework.manage.ex.BeansException;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;
}
