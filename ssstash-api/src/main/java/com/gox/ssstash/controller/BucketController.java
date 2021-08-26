package com.gox.ssstash.controller;

import com.gox.ssstash.entity.Bucket;
import com.gox.ssstash.entity.S3Object;
import com.gox.ssstash.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
@Slf4j
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping(value = "/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bucket> buckets(@PathVariable String bucketName){
        log.info("Get bucket {}", bucketName);
        Optional<Bucket> bucket = bucketService.findBucketByName(bucketName);
        if(bucket.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bucket.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/{bucketName}/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createS3Object(HttpServletRequest request, @PathVariable String bucketName, @RequestParam("object") MultipartFile objectFile) throws IOException {
        String objectKey = request.getRequestURI().replace("/" + bucketName, "");
        if(objectKey.length() > 0){
            objectKey = objectKey.substring(1);
        }
        log.info("Create object {} in bucket {}", bucketName, objectKey);

        Optional<Bucket> b = bucketService.findBucketByName(bucketName);
        if(b.isEmpty()){
            return new ResponseEntity<>("Bucket " + bucketName + " not found", HttpStatus.NOT_FOUND);
        } else {
            Optional<S3Object> s3Object = bucketService.findS3ObjectByBucketNameAndKey(bucketName, objectKey);
            if(s3Object.isEmpty()){
                S3Object s3Obj = bucketService.createNewS3Object(b.get(), objectKey, objectFile.getInputStream());
                return new ResponseEntity<>(s3Obj, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("S3Object " + objectKey + " already exists", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping(value = "/{bucketName}/**")
    public ResponseEntity getObject(HttpServletRequest request, @PathVariable String bucketName) throws IOException {
        String objectKey = request.getRequestURI().replace("/" + bucketName, "");
        if(objectKey.length() > 0){
            objectKey = objectKey.substring(1);
        }
        log.info("Get object {} in bucket {}", bucketName, objectKey);
        Optional<S3Object> s3Object = bucketService.findS3ObjectByBucketNameAndKey(bucketName, objectKey);
        if(s3Object.isEmpty()){
            return new ResponseEntity<>(s3Object, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(Files.readAllBytes(Path.of(s3Object.get().getFilePath())), HttpStatus.OK);
    }

    @PostMapping(value = "/{bucketName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBucket(@PathVariable String bucketName){
        log.info("Create bucket {}", bucketName);
        Optional<Bucket> bucket = bucketService.findBucketByName(bucketName);
        if(bucket.isPresent()){
            return new ResponseEntity<>("Bucket " + bucketName + " already exists", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(bucketService.createNewBucket(bucketName), HttpStatus.OK);
        }
    }
}
