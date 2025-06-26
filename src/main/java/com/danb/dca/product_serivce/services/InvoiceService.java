package com.danb.dca.product_serivce.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Service
public class InvoiceService {

    public void invoceElaborator(MultipartFile file) throws IOException {
        log.info("-- Service START");
        String folderName = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));

//        if(s3Service.checkFolderExists(folderName)){
//            log.info("-- Folder Created");
//            s3Service.createFolder(folderName);
//        }else{
//            log.info("-- Folder already exist");
//        }
//
//        s3Service.uploadFile(file.getOriginalFilename(),folderName);

        /*
        Step 2
        - Lettura file
        - Recupero Info
        - Creazione file repilogo
        - Invio file riepilogativo fine giornata?
         */
        File tempFile = File.createTempFile("uploaded-", ".tmp");
        file.transferTo(tempFile);
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(tempFile))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] split = text.split("\r\n");
            log.error(Arrays.toString(split));
        }
    }
}
