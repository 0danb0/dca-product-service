package com.danb.dca.product_serivce.services;

import com.danb.dca.product_serivce.models.dto.CompanyDto;
import com.danb.dca.product_serivce.models.dto.InvoiceDto;
import com.danb.dca.product_serivce.models.dto.InvoiceItemDto;
import com.danb.dca.product_serivce.models.dto.PaymentInfoDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        InvoiceDto invoiceDto = new InvoiceDto();
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(tempFile))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            String[] lines = text.split("\r\n");

            CompanyDto issuerDto = new CompanyDto();
            CompanyDto recipientDto = new CompanyDto();
            List<InvoiceItemDto> itemsDtoList = new ArrayList<>();
            PaymentInfoDto paymentDto = new PaymentInfoDto();

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();

                // Numero e data fattura
                if (line.startsWith("FATTURA nr.")) {
                    Pattern p = Pattern.compile("FATTURA nr\\. (.+?) del (.+)");
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        invoiceDto.setInvoiceNumber(m.group(1).trim());
                        invoiceDto.setInvoiceDate(m.group(2).trim());
                    }
                }

                // Mittente
                if (line.matches("(?i).*P\\.iva.*C\\.F\\..*")) {
                    issuerDto.setVatNumber(extractAfter(line, "P.iva"));
                    issuerDto.setFiscalCode(extractAfter(line, "C.F."));

                    // Linea precedente = indirizzo
                    if (i > 0) issuerDto.setAddress(lines[i - 1].trim());

                    // Due linee prima = nome mittente
                    if (i > 1) issuerDto.setName(lines[i - 2].trim());
                }

                // Destinatario
                if (line.equalsIgnoreCase("DESTINATARIO") && i + 1 < lines.length) {
                    // Linee successive = nome, indirizzo, CAP, ecc.
                    recipientDto.setName(lines[i + 1].trim());
                    StringBuilder addressBuilder = new StringBuilder();

                    for (int j = i + 2; j < Math.min(i + 5, lines.length); j++) {
                        String l = lines[j].trim();
                        if (l.isEmpty()) break;
                        addressBuilder.append(l).append(", ");
                    }

                    recipientDto.setAddress(addressBuilder.toString().replaceAll(",\\s*$", ""));
                }

                // Prodotti
                if (line.matches("^\\d{3} .*")) {
                    // Inizia una riga di prodotto
                    String code = line.split(" ")[0];
                    String desc = line.substring(code.length()).trim();
                    String nextLine = lines[i + 1].trim();
                    Pattern p2 = Pattern.compile("(\\d+) pz.*€ ([\\d,\\.]+).*€ ([\\d,\\.]+)");
                    Matcher m2 = p2.matcher(nextLine);
                    if (m2.find()) {
                        int qty = Integer.parseInt(m2.group(1));
                        double unitPrice = parseDouble(m2.group(2));
                        double total = parseDouble(m2.group(3));
                        itemsDtoList.add(new InvoiceItemDto(code, desc, qty, unitPrice, 0, total));
                    }
                }

                // IBAN e metodo di pagamento
                if (line.contains("IBAN:")) {
                    paymentDto.setMethod("Bonifico Bancario");
                    paymentDto.setIban(extractAfter(line, "IBAN:"));
                    paymentDto.setAccountHolder(lines[i + 1].trim());
                }

                // Scadenza
                if (line.matches("\\d{2}/\\d{2}/\\d{4}:\\s*€?\\s*[\\d.,]+")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        paymentDto.setDueDate(parts[0].trim());
                        paymentDto.setDueAmount(parseDouble(parts[1]));
                    }
                }

                // Totale
                if (line.startsWith("Imponibile €")) {
                    invoiceDto.setTotalAmount(parseDouble(line.replace("Imponibile €", "").trim()));
                }

//                    // Note
//                    if (line.startsWith("Documento privo")) {
//                        invoice.notes = line;
//                    }

            }
            invoiceDto.setIssuer(issuerDto);
            invoiceDto.setRecipient(recipientDto);
            invoiceDto.setItems(itemsDtoList);
            invoiceDto.setPayment(paymentDto);
        }
    }

    // Helpers
    private static String extractAfter(String text, String key) {
        int idx = text.indexOf(key);
        if (idx == -1) return "";
        return text.substring(idx + key.length()).split(" ")[0].trim();
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(value.replace("€", "").replace(",", ".").trim());
    }
}
