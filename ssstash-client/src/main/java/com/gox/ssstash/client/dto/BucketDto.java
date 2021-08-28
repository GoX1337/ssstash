package com.gox.ssstash.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BucketDto {

    private Long id;
    private String name;
    private List<S3ObjectDto> objects = new ArrayList<>();
}
