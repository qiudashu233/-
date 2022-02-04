package com.example.myxuanke.entiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "teadata")
public class TeadataEntity {

    @Id
    @Column
    private String tno;

    @Column
    private String teaname;
    @Column
    private String password;



    @Override
    public String toString() {
        return "ClassdataEntity{" +
                "tno='" + tno + '\'' +
                ", teaname='" + teaname + '\'' +
                ", password=" + password +
                '}';
    }
}