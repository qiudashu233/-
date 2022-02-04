package com.example.myxuanke.repository;

import com.example.myxuanke.entiy.ClassdataEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassdataRepository extends JpaRepository<ClassdataEntity, Integer>, JpaSpecificationExecutor<ClassdataEntity>{
    //选课
    @Modifying
    @Query("update ClassdataEntity p set p.num = p.num-1 where p.pno =?1 and p.num > 0")
    Integer reduceNumByPno(Integer pno);

    //退课
    @Modifying
    @Query("update ClassdataEntity p set p.num = p.num+1 where p.pno =?1 and p.num < p.capacity")
    Integer increaseNumByPno(Integer pno);

    //cname mo hu chaxun
    @Query("select p from ClassdataEntity p where p.cname like CONCAT('%',?1,'%')")
    List<ClassdataEntity> getClassByCname(String cname);

    //cid mo hu chaxun
    @Query("select p from ClassdataEntity p where p.cid like CONCAT('%',?1,'%')")
    List<ClassdataEntity> getClassByCid(String cid);

    //teaname mo hu chaxun
    @Query("select p from ClassdataEntity p where p.teaname like CONCAT('%',?1,'%')")
    List<ClassdataEntity> getClassByTeaname(String teaname);

    //pno cha xun
    ClassdataEntity findClassdataEntityByPno(Integer pno);

    List<ClassdataEntity> findClassdataEntitiesByTno(String tno);


}
