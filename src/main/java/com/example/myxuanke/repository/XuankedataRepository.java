package com.example.myxuanke.repository;

import com.example.myxuanke.entiy.XuankedataEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XuankedataRepository extends JpaRepository<XuankedataEntity, Integer>,JpaSpecificationExecutor<XuankedataEntity> {

    //sno cha xun
    List<XuankedataEntity> findXuankedataEntitiesBySno(String sno);

    //pno cha xun
    XuankedataEntity findXuankedataEntityByPno(String pno);

    //cid cha xun
    List<XuankedataEntity> findXuankedataEntitiesByCid(String cid);

    //pno sno chaxun
    XuankedataEntity findXuankedataEntityByPnoAndSno(Integer pno,String sno);
}