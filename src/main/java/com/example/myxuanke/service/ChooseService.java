package com.example.myxuanke.service;

import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.ClassdataEntity;

import java.util.List;

public interface ChooseService {

//    List<PlanEntity> getPlanEntityList();

//    ListDTO<PlanEntity> getPlanEntityListDTO(Integer pageNum, Integer size);

    ListDTO<ClassdataEntity> getClassListPage(Integer pageNum, Integer size);

    ListDTO<ClassdataEntity> getNeedClassListPageByCname(Integer pageNum, Integer size,String cname);

    ListDTO<ClassdataEntity> getNeedClassListPageByCid(Integer pageNum, Integer size,String cid);

    ListDTO<ClassdataEntity> getNeedClassListPageByTeaname(Integer pageNum, Integer size,String teaname);

    ResultDTO<String> doChoose(Integer pno);

    void executeChoose(String sno, Integer pno);

}
