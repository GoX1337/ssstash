package com.gox.ssstash.repository;

import com.gox.ssstash.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {

    Bucket findBucketByName(String name);
}
