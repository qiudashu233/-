package com.example.myxuanke.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xuankedata")
public class XuankedataEntity {

    @Id
    @Column
    private Integer pno;

    @Column
    private String sno;
    @Column
    private String cname;
    @Column
    private String cid;
    @Column
    private String cno;



    @Override
    public String toString() {
        return "ClassdataEntity{" +
                "pno='" + pno + '\'' +
                ", sno='" + sno + '\'' +
                ", cname='" + cname + '\'' +
                ", cid='" + cid + '\'' +
                ", cno=" + cno +
                '}';
    }
}