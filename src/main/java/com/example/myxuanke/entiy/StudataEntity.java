package com.example.myxuanke.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "studata")
public class StudataEntity {

    @Id
    @Column
    private String sno;

    @Column
    private String stuname;
    @Column
    private String password;



    @Override
    public String toString() {
        return "ClassdataEntity{" +
                "sno='" + sno + '\'' +
                ", stuname='" + stuname + '\'' +
                ", password=" + password +
                '}';
    }
}