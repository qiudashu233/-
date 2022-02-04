package com.example.myxuanke.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;

import lombok.Data;

@Entity
@Data
@Table(name = "classdata")
public class ClassdataEntity {

    @Id
    @Column
    private Integer pno;

    @Column
    private String cname;
    @Column
    private Integer credit;

    @Column
    private String cid;

    @Column
    private String cno;

    @Column
    private String teaname;

    @Column
    private String tno;

    @Column
    private String time;

    @Column
    private String room;

    @Column
    private Integer capacity;

    @Column
    private Integer num;

    @Override
    public String toString() {
        return "ClassdataEntity{" +
                "pno='" + pno + '\'' +
                ", cname='" + cname + '\'' +
                ", credit=" + credit +
                ", cid='" + cid + '\'' +
                ", cno='" + cno + '\'' +
                ", teaname='" + teaname + '\'' +
                ", tno='" + tno + '\'' +
                ", time='" + time + '\'' +
                ", room='" + room + '\'' +
                ", capacity='" + capacity + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
