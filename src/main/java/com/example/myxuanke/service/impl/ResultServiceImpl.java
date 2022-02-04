package com.example.myxuanke.service.impl;

import com.example.myxuanke.common.CodeMsg;
import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.XuankedataEntity;
import com.example.myxuanke.redis.RedisService;
import com.example.myxuanke.utils.StudentIDUtils;
import com.example.myxuanke.exception.GlobalException;
import com.example.myxuanke.repository.ClassdataRepository;
import com.example.myxuanke.repository.XuankedataRepository;
import com.example.myxuanke.service.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ResultServiceImpl implements ResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    private XuankedataRepository resultRepository;

    @Autowired
    private ClassdataRepository planRepository;

    @Autowired
    private RedisService redisService;

    @Override
    public ListDTO<XuankedataEntity> getChooseClassListPageBySno(Integer pageNum, Integer size, String sno) {

        ListDTO<XuankedataEntity> listDTO = (ListDTO<XuankedataEntity>) redisService.getFromHash("forResultList::" + sno, sno + "-" + pageNum);

        if (listDTO != null) {
//            LOGGER.info("从redis中加载选课结果");
            return listDTO;
        }

        //分页查询
        Pageable pageable = PageRequest.of(pageNum, size);

        //jpa复杂查询
        Page<XuankedataEntity> page = resultRepository.findAll((Specification<XuankedataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //根据学号查询
            if (sno != null) {
                predicates.add(builder.equal(root.get("sno"), sno));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        listDTO = new ListDTO<XuankedataEntity>(page.stream().collect(Collectors.toList()), pageNum, size, page.getTotalPages());

        //加载到redis中
        redisService.setToHash("forResultList::" + sno, sno + "-" + pageNum, listDTO, 30, TimeUnit.MINUTES);
        LOGGER.info("从数据中加载选课结果，并将选课结果写入redis中");
        return listDTO;
    }

    @Override
    public ListDTO<XuankedataEntity> getChooseClassListPageByCidAndCno(Integer pageNum, Integer size, String cid,String cno) {

        ListDTO<XuankedataEntity> listDTO = (ListDTO<XuankedataEntity>) redisService.getFromHash("forResultList::" + cid + cno, cid + cno + "-" + pageNum);

        if (listDTO != null) {
//            LOGGER.info("从redis中加载选课结果");
            return listDTO;
        }

        //分页查询
        Pageable pageable = PageRequest.of(pageNum, size);

        //jpa复杂查询
        Page<XuankedataEntity> page = resultRepository.findAll((Specification<XuankedataEntity>) (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //根据cid cname查询
            predicates.add(builder.equal(root.get("cid"), cid));
            predicates.add(builder.equal(root.get("cno"), cno));

            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        listDTO = new ListDTO<XuankedataEntity>(page.stream().collect(Collectors.toList()), pageNum, size, page.getTotalPages());

        //加载到redis中
        redisService.setToHash("forResultList::" + cid + cno, cid + cno + "-" + pageNum, listDTO, 30, TimeUnit.MINUTES);
        LOGGER.info("从数据中加载选课结果，并将选课结果写入redis中");
        return listDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<String> noChoose(Integer pno) {

        String sno = StudentIDUtils.getStudentIDFromMap();

        //判断是否存在此选课结果
        XuankedataEntity resultEntity = findChooseClassByPnoAndSno(pno, sno);
        if(resultEntity == null){
            throw new GlobalException(CodeMsg.RESULT_NOT_EXIST);
        }

        //选课结果表删除掉该条选课记录
        resultRepository.delete(resultEntity);

        //授课计划余量加一
        Integer a = planRepository.increaseNumByPno(Integer.valueOf(resultEntity.getPno()));
        if(a == 0){
            throw new GlobalException(CodeMsg.PLAN_NUM_ERROR);
        }

        //redis中对应的课程余量加一
        redisService.hdecr("forPlanCount", String.valueOf(pno), -1);

        //删除选课记录中此条选课结果
        redisService.delFromHash("forResult" + "-" + sno, String.valueOf(pno));

        //选课内容发生变化，删除redis中旧有数据
        redisService.del("forResultList::" + sno);

        return new ResultDTO<>(CodeMsg.RESULT_NO_CHOOSE_SUCCESS.getMsg());
    }

    @Override
    public XuankedataEntity findChooseClassByPnoAndSno(Integer pno, String sno) {

        XuankedataEntity entity ;

        //从redis中得到是否有对应选课记录
        entity = (XuankedataEntity) redisService.getFromHash("forResult" + "-" + sno, String.valueOf(pno));
        if( entity!=null ) return entity;

        entity = resultRepository.findXuankedataEntityByPnoAndSno(String.valueOf(pno), String.valueOf(sno));
        if(entity != null){
            redisService.setToHash("forResult" + "-" + sno, String.valueOf(pno), entity, 1, TimeUnit.DAYS);
        }
        return entity;
    }
}
