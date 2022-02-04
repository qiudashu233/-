package com.example.myxuanke.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TeacherIDUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherIDUtils.class);

    private static final Map<String, String> map = new HashMap<>(1000);

    //subject有多个,对应多个用户
    public static void addStudentIDToMap(String tno) {

        //日志证明每次拿到的subject都不同，此法可用
        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getSession().getId();
//        LOGGER.info("根据sessionID {} 将学号存入map", id);
        map.put(id, tno);
    }

    public static String getStudentIDFromMap() {
        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getSession().getId();
//        LOGGER.info("根据sessionID {} 从map中获得学号", id);
        return map.get(id);
    }

    //退出的时候将数据从map中删除，节省内存
    public static void removeUserIdFromMap() {
        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getSession().getId();
        map.remove(id);
    }

}