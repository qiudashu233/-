package com.example.myxuanke.shiro;

import com.example.myxuanke.entiy.StudataEntity;
import com.example.myxuanke.entiy.TeadataEntity;
import com.example.myxuanke.service.StudentService;
import com.example.myxuanke.service.TeacherService;
import com.example.myxuanke.utils.JudgeSorT;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class UserRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        LOGGER.info("===>>>>>>进行了授权逻辑PrincipalCollection");
        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getSession().getId();
        //判断身份
        Integer identity = JudgeSorT.getRoleFromMap(id);
        List<String> roles =new ArrayList<>();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(identity == 1){
            LOGGER.info("学生授权");
            roles.add("student");
            //simpleAuthorizationInfo.addRole("student");
        }
        if(identity == 2){
            LOGGER.info("老师授权");
            roles.add("teacher");
            //simpleAuthorizationInfo.addRole("teacher");
        }
        simpleAuthorizationInfo.addRoles(roles);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        LOGGER.info("===>>>>>>进行了认证逻辑AuthenticationToken");

        //获取token
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        Subject subject = SecurityUtils.getSubject();
        String id = (String) subject.getSession().getId();
        //判断身份
        Integer identity = JudgeSorT.getRoleFromMap(id);
        LOGGER.info("身份：");
        LOGGER.info(String.valueOf(identity));

        //检索用户
        if(identity == 1) {
            StudataEntity student = studentService.findStudentById(token.getUsername());
            if (student == null) {
                LOGGER.info("用户不存在");
                return null;
            }

            LOGGER.info("从数据库取得用户密码{}", student.getPassword());

            return new SimpleAuthenticationInfo(student, student.getPassword(), "");
        }
        if(identity == 2){
            TeadataEntity teacher = teacherService.findTeacherById(token.getUsername());
            if (teacher == null) {
                LOGGER.info("用户不存在");
                return null;
            }

            LOGGER.info("从数据库取得用户密码{}", teacher.getPassword());

            return new SimpleAuthenticationInfo(teacher, teacher.getPassword(), "");
        }
        return null;
    }
}

