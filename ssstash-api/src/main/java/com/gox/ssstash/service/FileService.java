package com.gox.ssstash.service;

import com.gox.ssstash.entity.Bucket;

import java.io.InputStream;

public interface FileService {

    void createFileDirectory();

    String saveFile(Bucket bucket, String uri, InputStream objectInputStream);
}
