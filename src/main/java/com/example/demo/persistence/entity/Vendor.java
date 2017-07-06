package com.example.demo.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Vendor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "HOGE")
    @SequenceGenerator(name = "HOGE",
            sequenceName = "seq_vendor_id",
            initialValue = 100,
            allocationSize = 1)
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
