package com.gox.ssstash.component;

import com.gox.ssstash.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateFileDirectoryRunner implements CommandLineRunner {

    private final FileService fileService;

    public CreateFileDirectoryRunner(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) {
        fileService.createFileDirectory();
    }
}
