package com.gox.ssstash.service.impl;

import com.gox.ssstash.entity.Bucket;
import com.gox.ssstash.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private static final String directoryPath = "C:\\tmp\\ssstash";

    @Override
    public void createFileDirectory() {
        File directory = new File(directoryPath);
        if (!directory.exists()){
            boolean ok = directory.mkdirs();
            log.info("Created directory " + directory.getAbsolutePath() + " " + ok);
        }
    }

    @Override
    public String saveFile(Bucket bucket, String objectKey, InputStream objectInputStream) {
        File file = new File(directoryPath + "\\" + bucket.getId() + "." + bucket.getName() + '.' + objectKey + ".bin");
        try (OutputStream output = new FileOutputStream(file, false)) {
            objectInputStream.transferTo(output);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return file.getAbsolutePath();
    }
}
