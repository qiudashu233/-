package com.example.myxuanke.repository;

import com.example.myxuanke.entiy.TeadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeadataRepository extends JpaRepository<TeadataEntity, Integer> {

    //tno cha xun
    TeadataEntity findTeadataEntityByTno(String tno);
}