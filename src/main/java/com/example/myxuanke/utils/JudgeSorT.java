package com.example.myxuanke.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

public class JudgeSorT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JudgeSorT.class);

    private static final Map<String, Integer> map = new HashMap<>(1000);

    public static void addRoleToMap(String number,Integer p) {

        //1代表学生 2代表老师
        map.put(number, p);
    }

    public static Integer getRoleFromMap(String number) {
        return map.get(number);
    }

    public static Integer removeRoleFromMap(String number) {
        return map.remove(number);
    }
}
