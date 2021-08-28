package com.gox.ssstash.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class S3ObjectDto {

    private Long id;
    private String key;
    private String filePath;
    private String mimeType;
}
