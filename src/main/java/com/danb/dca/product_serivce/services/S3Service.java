package com.danb.dca.product_serivce.services;

import com.danb.dca.product_serivce.enums.S3ServiceStatus;
import com.danb.dca.product_serivce.exceptions.S3CustomException;
import com.danb.dca.product_serivce.helpers.S3Helper;
import com.danb.dca.product_serivce.utils.Tools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Helper s3Helper;
    private final Tools tools;

    public String folderCheckAndCreate(String folderName){
        log.info("-- Folder check: START with folder name -> {}", folderName);

        String result = S3ServiceStatus.NO_CREATION_NEEDED.getMessage();
        if(s3Helper.checkFolderExists(folderName)){
            try {
                s3Helper.createFolder(folderName);
                log.info("--- Folder created");
                result = S3ServiceStatus.CREATED.getMessage();
            }catch (S3CustomException e){
                log.info("-- Creation folder: Error -> {}", e.toString());
                return S3ServiceStatus.ERROR.getMessage();
            }
        }

        log.info("-- Folder check: DONE with result -> {}", result);
        return result;
    }

    public String uploadFile(String fileUrl, String folderName) {
        log.info("-- Upload file: START with folder name -> {}", folderName);
        String result;

        try {
            // Scarica il file da URL
            byte[] fileBytes = tools.downloadFileFromUrl(fileUrl);

            // Genera nome file dinamico se necessario
            String filename = tools.extractFilenameFromUrl(fileUrl);
            String objectKey = folderName + "/" + filename;

            // Carica su S3
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            s3Helper.uploadFile(objectKey, inputStream, fileBytes.length);

            result = S3ServiceStatus.UPLOADED.getMessage();

        } catch (S3CustomException | IOException e) {
            log.error("-- Upload file: Error -> {}", e.toString());
            return S3ServiceStatus.ERROR.getMessage();
        }

        log.info("-- Upload file: DONE with result -> {}", result);
        return result;
    }


}
