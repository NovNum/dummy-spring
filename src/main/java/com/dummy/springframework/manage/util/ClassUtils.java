package com.dummy.springframework.manage.util;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader(){
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {

        }
        if (classLoader == null){
            classLoader = ClassUtils.class.getClassLoader();
        }
        return  classLoader;
    }
}
