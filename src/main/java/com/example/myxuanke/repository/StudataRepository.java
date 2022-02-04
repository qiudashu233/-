package com.example.myxuanke.repository;

import com.example.myxuanke.entiy.StudataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudataRepository extends JpaRepository<StudataEntity, Integer> {

    //sno cha xun
    StudataEntity findStudataEntityBySno(String sno);
}