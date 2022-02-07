package com.example.myxuanke.service;

import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.XuankedataEntity;


public interface ResultService {

//    List<ResultEntity> getResultListBySno(Integer sno);

//    ListDTO<ResultEntity> getResultListBySno(Integer pageNum, Integer size, Integer sno);

    ListDTO<XuankedataEntity> getChooseClassListPageBySno(Integer pageNum, Integer size, String sno);

    ListDTO<XuankedataEntity> getChooseClassListPageByTno(Integer pageNum, Integer size, String tno);

    ListDTO<XuankedataEntity> getChooseClassListPageByCidAndCno(Integer pageNum, Integer size, String cid, String cno);

    ResultDTO<String> noChoose(Integer pno);

    XuankedataEntity findChooseClassByPnoAndSno(Integer pno, String sno);

}
