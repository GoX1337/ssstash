package com.gox.ssstash.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
