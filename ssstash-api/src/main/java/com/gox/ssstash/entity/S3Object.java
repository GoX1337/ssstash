package com.gox.ssstash.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class S3Object {

    @Id
    @GeneratedValue
    private Long id;
    private String key;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bucket bucket;

    public S3Object(String key, String filePath, Bucket bucket) {
        this.key = key;
        this.filePath = filePath;
        this.bucket = bucket;
    }
}
