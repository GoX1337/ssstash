package com.gox.ssstash.controller;

import com.gox.ssstash.entity.Bucket;
import com.gox.ssstash.entity.S3Object;
import com.gox.ssstash.repository.BucketRepository;
import com.gox.ssstash.repository.S3ObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@Slf4j
public class BucketController {

    public BucketRepository bucketRepository;
    public S3ObjectRepository s3ObjectRepository;

    public BucketController(BucketRepository bucketRepository, S3ObjectRepository s3ObjectRepository) {
        this.bucketRepository = bucketRepository;
        this.s3ObjectRepository = s3ObjectRepository;
    }

    @GetMapping(value = "/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bucket> buckets(@PathVariable String bucketName){
        log.info("Get bucket {}", bucketName);
        Bucket b = bucketRepository.findBucketByName(bucketName);
        if(b == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PostMapping(value = "/{bucketName}/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBucket(HttpServletRequest request, @PathVariable String bucketName, @RequestParam("object") MultipartFile objectFile){
        String objectKey = request.getRequestURI().replace("/" + bucketName, "");
        if(objectKey != null && objectKey.length() > 0){
            objectKey = objectKey.substring(1);
        }
        log.info("Create object {} in bucket {}", bucketName, objectKey);

        Bucket b = bucketRepository.findBucketByName(bucketName);
        if(b == null){
            return new ResponseEntity<>("Bucket " + bucketName + " not found", HttpStatus.NOT_FOUND);
        } else {
            S3Object s3Object = s3ObjectRepository.findS3ObjectByBucketNameAndKey(bucketName, objectKey);
            if(s3Object == null){
                s3Object = new S3Object();
                s3Object.setKey(objectKey);
                s3Object.setBucket(b);
                return new ResponseEntity<>(s3ObjectRepository.save(s3Object), HttpStatus.OK);
            } else {
                log.info(s3Object.toString());
                return new ResponseEntity<>("S3Object " + objectKey + " already exists", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping(value = "/{bucketName}/**")
    public byte[] getObject(HttpServletRequest request, @PathVariable String bucketName) throws IOException {
        String objectKey = request.getRequestURI().replace("/" + bucketName, "");
        if(objectKey != null && objectKey.length() > 0){
            objectKey = objectKey.substring(1);
        }
        log.info("Get object {} in bucket {}", bucketName, objectKey);
        File file = new File("C:\\Users\\m430504\\IdeaProjects\\ssstash\\ssstash-api\\src\\main\\resources\\image.png");
        return Files.readAllBytes(file.toPath());
    }

    @PostMapping(value = "/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createS3Object(HttpServletRequest request, @PathVariable String bucketName){
        log.info("Create bucket {}", bucketName);
        Bucket b = bucketRepository.findBucketByName(bucketName);
        if(b != null){
            return new ResponseEntity<>("Bucket " + bucketName + " already exists", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(bucketRepository.save(new Bucket(bucketName)), HttpStatus.OK);
        }
    }
}
