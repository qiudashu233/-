package com.example.myxuanke.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.base.Preconditions;
import com.example.myxuanke.common.CodeMsg;
import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.ClassdataEntity;
import com.example.myxuanke.entiy.XuankedataEntity;
import com.example.myxuanke.redis.RedisService;
import com.example.myxuanke.utils.StudentIDUtils;
import com.example.myxuanke.exception.GlobalException;
import com.example.myxuanke.repository.ClassdataRepository;
import com.example.myxuanke.repository.XuankedataRepository;
import com.example.myxuanke.service.ChooseService;
import com.example.myxuanke.service.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ChooseServiceImpl implements ChooseService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseServiceImpl.class);

    @Autowired
    private ClassdataRepository planRepository;

    @Autowired
    private XuankedataRepository resultRepository;

    //本地缓存
    @Autowired
    private Cache<Integer, Boolean> caffeineCache;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ResultService resultService;


    //实现分页逻辑
    @Override
    public ListDTO<ClassdataEntity> getClassListPage(Integer pageNum, Integer size) {

        ListDTO<ClassdataEntity> listDTO = (ListDTO<ClassdataEntity>)redisService.getFromHash("forClass", String.valueOf(pageNum));
        if(listDTO != null) return listDTO;

        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("pno"));

        //复杂条件查询,只查询余量不为0的课程
        Page<ClassdataEntity> page = planRepository.findAll((Specification<ClassdataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //余量需要大于0
            predicates.add(builder.greaterThan(root.get("num"), 0));

            return builder.and(predicates.toArray(new Predicate[0]));

        }, pageable);

        ListDTO<ClassdataEntity> planDto = new ListDTO<>(page.stream().collect(Collectors.toList()), page.getNumber(), size, page.getTotalPages());

        //结果变动快，缓存一分钟，减少数据库压力，缺陷是课程余量显示会存在延迟
        redisService.setToHash("forClass", String.valueOf(pageNum), planDto, 1, TimeUnit.MINUTES);
        return planDto;
    }

    //模糊查询
    @Override
    public ListDTO<ClassdataEntity> getNeedClassListPageByCname(Integer pageNum, Integer size,String cname) {

        ListDTO<ClassdataEntity> listDTO = (ListDTO<ClassdataEntity>)redisService.getFromHash("forClass_Cname", String.valueOf(pageNum));
        if(listDTO != null) return listDTO;
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("pno"));

        //复杂条件查询,只查询余量不为0的课程
        Page<ClassdataEntity> page = planRepository.findAll((Specification<ClassdataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //余量需要大于0
            predicates.add(builder.greaterThan(root.get("num"), 0));

            predicates.add(builder.like(root.get("cname"), "%"+cname+"%"));

            return builder.and(predicates.toArray(new Predicate[0]));

        }, pageable);

        ListDTO<ClassdataEntity> planDto = new ListDTO<>(page.stream().collect(Collectors.toList()), page.getNumber(), size, page.getTotalPages());
        //结果变动快，缓存一分钟，减少数据库压力，缺陷是课程余量显示会存在延迟
        redisService.setToHash("forClass_Cname", String.valueOf(pageNum), planDto, 1, TimeUnit.MINUTES);
        return planDto;
    }

    @Override
    public ListDTO<ClassdataEntity> getNeedClassListPageByCid(Integer pageNum, Integer size,String cid) {

        ListDTO<ClassdataEntity> listDTO = (ListDTO<ClassdataEntity>)redisService.getFromHash("forClass_Cid", String.valueOf(pageNum));
        if(listDTO != null) return listDTO;

        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("pno"));

        //复杂条件查询,只查询余量不为0的课程
        Page<ClassdataEntity> page = planRepository.findAll((Specification<ClassdataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //余量需要大于0
            predicates.add(builder.greaterThan(root.get("num"), 0));

            predicates.add(builder.like(root.get("cid"), "%"+cid+"%"));

            return builder.and(predicates.toArray(new Predicate[0]));

        }, pageable);

        ListDTO<ClassdataEntity> planDto = new ListDTO<>(page.stream().collect(Collectors.toList()), page.getNumber(), size, page.getTotalPages());

        //结果变动快，缓存一分钟，减少数据库压力，缺陷是课程余量显示会存在延迟
        redisService.setToHash("forClass_Cid", String.valueOf(pageNum), planDto, 1, TimeUnit.MINUTES);
        return planDto;
    }

    @Override
    public ListDTO<ClassdataEntity> getNeedClassListPageByTeaname(Integer pageNum, Integer size,String tname) {

        ListDTO<ClassdataEntity> listDTO = (ListDTO<ClassdataEntity>)redisService.getFromHash("forClass_Tname", String.valueOf(pageNum));
        if(listDTO != null) return listDTO;

        Pageable pageable = PageRequest.of(pageNum, size, Sort.by("pno"));

        //复杂条件查询,只查询余量不为0的课程
        Page<ClassdataEntity> page = planRepository.findAll((Specification<ClassdataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //余量需要大于0
            predicates.add(builder.greaterThan(root.get("num"), 0));

            predicates.add(builder.like(root.get("teaname"), "%"+tname+"%"));

            return builder.and(predicates.toArray(new Predicate[0]));

        }, pageable);

        ListDTO<ClassdataEntity> planDto = new ListDTO<>(page.stream().collect(Collectors.toList()), page.getNumber(), size, page.getTotalPages());

        //结果变动快，缓存一分钟，减少数据库压力，缺陷是课程余量显示会存在延迟
        redisService.setToHash("forClass_Tname", String.valueOf(pageNum), planDto, 1, TimeUnit.MINUTES);
        return planDto;
    }

    @Override
    public ResultDTO<String> doChoose(Integer pno) {

        // 1.本地标记，判断是否有余量
        Boolean over = caffeineCache.get(pno, (k) -> {
            Boolean flag = caffeineCache.getIfPresent(k);
            if (flag == null) return false;
            return flag;
        });

        //无余量直接返回
        if (over) {
//            LOGGER.info("通过本地标记判断已无余量");
            throw new GlobalException(CodeMsg.PlAN_OVER);
        }

        //2.判断是否重选
        String sno = StudentIDUtils.getStudentIDFromMap();

        // 先读取此节课的选课结果
        // 优先从redis中加载
        XuankedataEntity entity = resultService.findChooseClassByPnoAndSno(pno, sno);

        if (entity != null) {
            throw new GlobalException(CodeMsg.CHOOSE_REPEAT);
        }

        //3.库存预减
        //如果key不存在，会写入并返回-1,需先判断
        if(!redisService.hasKey("forClassCount")){
            try {
                //重新加载，防止键过期读错数据
                afterPropertiesSet();
                LOGGER.info("redis缓存课程余量数据过期，重新加载");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Long num = redisService.hdecr("forClassCount", String.valueOf(pno), 1);
//        LOGGER.info("redis中读取pno={} 的授课计划余量为{}", pno, num);
        if(num < 0){
            //没余量，写入本地缓存中
            caffeineCache.put(pno, true);
            throw new GlobalException(CodeMsg.PlAN_OVER);
        }

        //4.返回结果

        executeChoose(sno,pno);
        return new ResultDTO<>(CodeMsg.CHOOSE_END.getMsg());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeChoose(String sno, Integer pno) {

        XuankedataEntity entity = resultService.findChooseClassByPnoAndSno(pno, sno);
        Preconditions.checkArgument(entity==null, "重复选课！");

        //保存选课结果
        ClassdataEntity classentity = planRepository.findClassdataEntityByPno(pno);
        entity = new XuankedataEntity();
        entity.setPno(pno);
        entity.setSno(sno);
        entity.setCid(classentity.getCid());
        entity.setCname(classentity.getCname());
        entity.setCno(classentity.getCno());
        resultRepository.saveAndFlush(entity);

        //余量减一
        Integer a = planRepository.reduceNumByPno(pno);
        if(a ==0) caffeineCache.put(pno, true);
        Preconditions.checkArgument(a != 0, "此节课已经没有剩余数量可选！");

        //该条选课结果写入redis中
        redisService.setToHash("forResult" + "-" + sno, String.valueOf(pno), entity, 30,
                TimeUnit.MINUTES);
        //选课结果发生变化，删掉redis中旧数据
        redisService.del("forResultList::" + sno);
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        //系统启动时就将授课计划对应的余量加载进redis中
        List<ClassdataEntity> list = planRepository.findAll();

        for(ClassdataEntity entity: list){
            //授课计划余量
            redisService.setToHash("forClassCount", String.valueOf(entity.getPno()),
                    entity.getNum(), 1, TimeUnit.DAYS);

        }

        //系统启动，将预选结果加载
        List<XuankedataEntity> resultEntities = resultRepository.findAll();
        for(XuankedataEntity result: resultEntities){
            redisService.setToHash("forResult" + "-" +result.getSno(),
                    "" + result.getPno(), result, 1, TimeUnit.DAYS);
        }
    }


}
