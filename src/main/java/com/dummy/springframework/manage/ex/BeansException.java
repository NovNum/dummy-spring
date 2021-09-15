package com.dummy.springframework.manage.ex;

public class BeansException extends RuntimeException{

    public BeansException(String msg){
     super(msg);
    }

    public BeansException(String msg,Throwable a){
        super(msg,a);
    }
}
