package com.gox.ssstash.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gox.ssstash.client.dto.BucketDto;
import com.gox.ssstash.client.dto.S3ObjectDto;
import com.gox.ssstash.client.exception.BucketAlreadyExistsException;
import com.gox.ssstash.client.exception.BucketCreationException;
import com.gox.ssstash.client.exception.BucketListObjectsException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SSStashClient {

    private final String s3StashServerEndpoint = "http://localhost:60606";
    private final static String boundary = new BigInteger(256, new Random()).toString();
    private final static String multipartDataKey = "object";

    public class Bucket {

        private final String name;
        private final ObjectMapper objectMapper = new ObjectMapper();

        private Bucket(String name) {
            this.name = name;
        }

        public List<String> listObjects() throws BucketListObjectsException {
            List<String> objectKeys = new ArrayList<>();
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(s3StashServerEndpoint + "/" + name))
                        .GET()
                        .build();
                HttpResponse<String> response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());

                BucketDto bucketDto = objectMapper.readValue(response.body(), BucketDto.class);
                for(S3ObjectDto o : bucketDto.getObjects()){
                    objectKeys.add(o.getKey());
                }
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new BucketListObjectsException();
            }
            return objectKeys;
        }

        public void createObject(String objectKey, Path filepath){
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                        .uri(new URI(s3StashServerEndpoint + "/" + name + "/" + objectKey))
                        .POST(ofMimeMultipartData(Map.of(multipartDataKey, filepath), boundary))
                        .build();
                HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public void getObject(String objectKey, String destinationFolder){
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(s3StashServerEndpoint + "/" + name + "/" + objectKey))
                        .GET()
                        .build();
                HttpResponse<byte[]> response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofByteArray());

                try (FileOutputStream fos = new FileOutputStream(destinationFolder + "/" + objectKey)) {
                    fos.write(response.body());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Bucket createBucket(String bucketName) throws BucketCreationException, BucketAlreadyExistsException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(s3StashServerEndpoint + "/" + bucketName))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 400){
                throw new BucketAlreadyExistsException();
            }
            return new Bucket(bucketName);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new BucketCreationException();
        }
    }

    public static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n")
                        .getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    public static void main(String[] args) {
        SSStashClient s3client = new SSStashClient();
        try {
            Bucket bucket = s3client.createBucket("test" + Math.random());
            bucket.listObjects().forEach(System.out::println);
            Path filepath = Paths.get(ClassLoader.getSystemResource("emperor.jpg").toURI());
            bucket.createObject("emperor", filepath);
            bucket.listObjects().forEach(System.out::println);
            bucket.getObject("emperor", "C:\\tmp");
        } catch (BucketCreationException | BucketAlreadyExistsException | BucketListObjectsException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
