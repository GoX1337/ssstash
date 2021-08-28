package com.gox.ssstash.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Bucket {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<S3Object> objects = new ArrayList<>();

    public Bucket() {
    }

    public Bucket(String name) {
        this.name = name;
    }
}
