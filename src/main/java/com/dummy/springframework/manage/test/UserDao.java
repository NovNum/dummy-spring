package com.dummy.springframework.manage.test;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String,String> stringMap = new HashMap<>();

    static {
        stringMap.put("10001","张三");
        stringMap.put("10002","李四");
        stringMap.put("10003","小王");
    }
    public String queryUserName(String uId){
        return stringMap.get(uId);
    }
}
