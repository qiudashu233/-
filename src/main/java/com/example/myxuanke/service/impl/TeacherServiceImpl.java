package com.example.myxuanke.service.impl;

import com.google.common.base.Preconditions;
import com.example.myxuanke.entiy.TeadataEntity;
import com.example.myxuanke.redis.RedisService;
import com.example.myxuanke.repository.TeadataRepository;
import com.example.myxuanke.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private TeadataRepository teacherRepository;

    @Autowired
    private RedisService redisService;

    @Override
    public TeadataEntity findTeacherById(String tno) {

        TeadataEntity studentEntity = (TeadataEntity) redisService.get("forTeacher::", String.valueOf(tno));
        if(studentEntity != null) {
            LOGGER.info("从redis加载teacher信息");
            return studentEntity;
        }

        Optional<TeadataEntity> optional = Optional.ofNullable(teacherRepository.findTeadataEntityByTno(String.valueOf(tno)));
        Preconditions.checkArgument(optional.isPresent(), "用户不存在！");
        studentEntity = optional.get();

        redisService.set("forTeacher::", String.valueOf(tno), studentEntity, 1, TimeUnit.DAYS);
        LOGGER.info("从数据库加载到teacher信息，并存入redis中");

        return studentEntity;
    }
}
