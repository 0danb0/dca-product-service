package com.danb.dca.product_serivce.helper;

import com.danb.dca.product_serivce.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Helper {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public boolean checkFolderExists(String folderName) {
        log.info("-- CheckFolderExists START");
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(s3Properties.getBucket())
                .prefix(folderName + "/")
                .maxKeys(1)
                .build();
        return !s3Client.listObjectsV2(request).contents().isEmpty();
    }

    public void createFolder(String folderName) {
        log.info("-- Create S3 Folder START");
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(folderName)
                    .contentLength(0L)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.empty());

            log.info("-- Create S3 Folder END");
        } catch (S3Exception e) {
            log.error("--- Create S3 Folder ERROR: {}", e.awsErrorDetails().errorMessage());
        }
    }

    public void uploadFile(String objectKey, String filePath) {
        log.info("-- Upload File S3 START");
        try {
            Path path = Paths.get(filePath);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(objectKey)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

            log.info("-- File caricato con successo. ETag: " + response.eTag());
        } catch (S3Exception e) {
            log.error("-- Errore durante il caricamento del file: {}", e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            log.error("-- Errore generale: {}", e.getMessage());
        }
    }
}
