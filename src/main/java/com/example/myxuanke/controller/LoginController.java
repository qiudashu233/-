package com.example.myxuanke.controller;

import com.example.myxuanke.common.CodeMsg;
import com.example.myxuanke.entiy.StudataEntity;
import com.example.myxuanke.entiy.TeadataEntity;
import com.example.myxuanke.exception.GlobalException;
import com.example.myxuanke.service.StudentService;
import com.example.myxuanke.service.TeacherService;
import com.example.myxuanke.utils.StudentIDUtils;
import com.example.myxuanke.utils.TeacherIDUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/do/loginbystu")
    @ResponseBody
    public String doLoginbystu(StudataEntity studentEntity, HttpServletResponse response) {

        String sno = studentEntity.getSno();
        String password = studentEntity.getPassword();
        LOGGER.info("从前端取得的输入密码{}", password);

        //查询学号对应学生的实体类
        StudataEntity student = studentService.findStudentById(sno);

        // 获取当前的subject
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(sno), password);

        // 执行登录方法，如果没有异常就说明正常
        try {
            // shiro 登录 授权 认证
            subject.login(token);

            //将学生id保存起来，以备后续使用
            StudentIDUtils.addStudentIDToMap(sno);
            String msg = "success";

            //得到session过期时间
            long timeout = subject.getSession().getTimeout();
            //将学生姓名添加到cookie中
            addCookie("stuname", student.getStuname(), timeout, response);
            LOGGER.info("do login result ==> " + msg);

            return msg;
        } catch (UnknownAccountException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.STUDENT_NUM_NOT_EXISTS.getMsg());
            throw new GlobalException(CodeMsg.STUDENT_NUM_NOT_EXISTS);
        } catch (IncorrectCredentialsException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.PASSWORD_WRONG.getMsg());
            throw new GlobalException(CodeMsg.PASSWORD_WRONG);
        } catch (LockedAccountException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.ACCOUNT_LOCKED.getMsg());
            throw new GlobalException(CodeMsg.ACCOUNT_LOCKED);
        }
    }

    @PostMapping("/do/loginbytea")
    @ResponseBody
    public String doLoginbytea(TeadataEntity teadentEntity, HttpServletResponse response) {

        String tno = teadentEntity.getTno();
        String password = teadentEntity.getPassword();
        LOGGER.info("从前端取得的输入密码{}", password);

        //查询学号对应学生的实体类
        TeadataEntity teacher = teacherService.findTeacherById(tno);

        // 获取当前的subject
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(String.valueOf(tno), password);

        // 执行登录方法，如果没有异常就说明正常
        try {
            // shiro 登录 授权 认证
            subject.login(token);

            //将学生id保存起来，以备后续使用
            TeacherIDUtils.addStudentIDToMap(tno);
            String msg = "success";

            //得到session过期时间
            long timeout = subject.getSession().getTimeout();
            //将学生姓名添加到cookie中
            addCookie("teaname", teacher.getTeaname(), timeout, response);
            LOGGER.info("do login result ==> " + msg);

            return msg;
        } catch (UnknownAccountException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.STUDENT_NUM_NOT_EXISTS.getMsg());
            throw new GlobalException(CodeMsg.STUDENT_NUM_NOT_EXISTS);
        } catch (IncorrectCredentialsException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.PASSWORD_WRONG.getMsg());
            throw new GlobalException(CodeMsg.PASSWORD_WRONG);
        } catch (LockedAccountException e) {
            LOGGER.warn("do login result ==> " + CodeMsg.ACCOUNT_LOCKED.getMsg());
            throw new GlobalException(CodeMsg.ACCOUNT_LOCKED);
        }
    }

    @GetMapping("/logout")
    public String logout() {

        Subject subject = SecurityUtils.getSubject();
        // 登出
        StudentIDUtils.removeUserIdFromMap();
        LOGGER.info("User " + subject.getPrincipal() + " logout.");
        subject.logout();

        return "redirect:/home";
    }

    private void addCookie(String key, String value, long timeout, HttpServletResponse response) {

        Cookie cookie = new Cookie(key, value);
        //这个必须要设置
        cookie.setPath("/");
        cookie.setMaxAge((int) timeout);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

    }
}