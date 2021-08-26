package com.gox.ssstash.service;

import com.gox.ssstash.entity.Bucket;
import com.gox.ssstash.entity.S3Object;

import java.io.InputStream;
import java.util.Optional;

public interface BucketService {
    Optional<Bucket> findBucketByName(String bucketName);

    Bucket createNewBucket(String bucketName);

    Optional<S3Object> findS3ObjectByBucketNameAndKey(String bucketName, String objectKey);

    S3Object createNewS3Object(Bucket bucket, String objectKey, InputStream objectInputStream);
}
