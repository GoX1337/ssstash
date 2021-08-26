package com.gox.ssstash.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Bucket {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Bucket() {
    }

    public Bucket(String name) {
        this.name = name;
    }
}
