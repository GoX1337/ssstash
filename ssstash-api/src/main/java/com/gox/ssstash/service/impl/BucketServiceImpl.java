package com.gox.ssstash.service.impl;

import com.gox.ssstash.component.MimeType;
import com.gox.ssstash.entity.Bucket;
import com.gox.ssstash.entity.S3Object;
import com.gox.ssstash.repository.BucketRepository;
import com.gox.ssstash.repository.S3ObjectRepository;
import com.gox.ssstash.service.BucketService;
import com.gox.ssstash.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final S3ObjectRepository s3ObjectRepository;
    private final FileService fileService;
    private final MimeType mimeType;

    public BucketServiceImpl(BucketRepository bucketRepository, S3ObjectRepository s3ObjectRepository, FileService fileService, MimeType mimeType) {
        this.bucketRepository = bucketRepository;
        this.s3ObjectRepository = s3ObjectRepository;
        this.fileService = fileService;
        this.mimeType = mimeType;
    }

    @Override
    public Optional<Bucket> findBucketByName(String bucketName) {
        return Optional.ofNullable(bucketRepository.findBucketByName(bucketName));
    }

    @Override
    public Bucket createNewBucket(String bucketName) {
        return bucketRepository.save(new Bucket(bucketName));
    }

    @Override
    public Optional<S3Object> findS3ObjectByBucketNameAndKey(String bucketName, String objectKey) {
        return Optional.ofNullable(s3ObjectRepository.findS3ObjectByBucketNameAndKey(bucketName, objectKey));
    }

    @Override
    public S3Object createNewS3Object(Bucket bucket, String objectKey, String filename, InputStream objectInputStream) {
        String path = fileService.saveFile(bucket, objectKey, objectInputStream);
        return s3ObjectRepository.save(new S3Object(objectKey, path, mimeType.getMimeType(filename), bucket));
    }
}
