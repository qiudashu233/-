package com.example.myxuanke.service.impl;

import com.google.common.base.Preconditions;
import com.example.myxuanke.entiy.StudataEntity;
import com.example.myxuanke.redis.RedisService;
import com.example.myxuanke.repository.StudataRepository;
import com.example.myxuanke.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudataRepository studentRepository;

    @Autowired
    private RedisService redisService;

    @Override
    public StudataEntity findStudentById(String sno) {

        StudataEntity studentEntity = (StudataEntity) redisService.get("forStudent::", String.valueOf(sno));
        if(studentEntity != null) {
            LOGGER.info("从redis加载学生信息");
            return studentEntity;
        }

        Optional<StudataEntity> optional = Optional.ofNullable(studentRepository.findStudataEntityBySno(String.valueOf(sno)));
        Preconditions.checkArgument(optional.isPresent(), "用户不存在！");
        studentEntity = optional.get();

        //学生数据不变动，经常查询，缓存一天
        redisService.set("forStudent::", String.valueOf(sno), studentEntity, 1, TimeUnit.DAYS);
        LOGGER.info("从数据库加载到学生信息，并存入redis中");

        return studentEntity;
    }
}
