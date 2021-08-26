package com.gox.ssstash.repository;

import com.gox.ssstash.entity.S3Object;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3ObjectRepository extends JpaRepository<S3Object, Long> {

    S3Object findS3ObjectByBucketNameAndKey(String bucketName, String objectKey);
}
