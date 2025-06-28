package com.danb.dca.product_serivce.helpers;

import com.danb.dca.product_serivce.models.dto.CompanyDto;
import com.danb.dca.product_serivce.models.dto.InvoiceDto;
import com.danb.dca.product_serivce.models.dto.InvoiceItemDto;
import com.danb.dca.product_serivce.models.dto.PaymentInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PDFHelper {
    private static final String PATTERN_EMAIL = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private static final String PATTERN_PHONE = "\\b(\\+39)?3\\d{8,9}\\b";
    private static final String PATTERN_INVOICE_NUMBER = "FATTURA nr\\. (.+?) del (.+)";
    private static final String PATTERN_ISSUER = "(?i).*P\\.iva.*C\\.F\\..*";
    private static final String PATTERN_ADDRESS_REPLACE = ",\\s*$";
    private static final String PATTERN_PRODUCT = "(\\d+) pz.*€ ([\\d,\\.]+).*€ ([\\d,\\.]+)";
    private static final String PATTERN_PRODUCT_CODE = "^\\d{3} .*";
    private static final String PATTERN_DUE_DATE_AND_AMOUNT = "\\d{2}/\\d{2}/\\d{4}:\\s*€?\\s*[\\d.,]+";
    private static final String PATTERN_IBAN = "([A-Z]{2}\\d{2}[A-Z0-9]{1,30})";
    private static final String PREFIX_FILE_NAME = "uploaded-";
    private static final String SUFFIX_FILE_NAME = ".tmp";
    private static final String INVOICE_NUMBER = "FATTURA nr.";
    private static final String VAT_NUMBER_CODE = "P.iva ";
    private static final String RECIPIENT_VAT_NUMBER_CODE = "P.IVA ";
    private static final String FISCAL_CODE = "C.F. ";
    private static final String RECIPIENT_FISCAL_CODE = "CF ";
    private static final String RECIPIENT = "DESTINATARIO";
    private static final String TELEPHONE = "TEL. ";
    private static final String MAIL = "EMAIL ";
    private static final String TOTAL_AMOUNT = "Imponibile €";
    private static final String IBAN = "IBAN";
    private static final String PAYMENT_METHOD = "MODALITÀ DI PAGAMENTO";
    private static final String UNKNOW = "UNKNOW";


    public InvoiceDto invoiceStripper(MultipartFile file) throws IOException {
        InvoiceDto invoiceDto = new InvoiceDto();
        File tempFile = File.createTempFile(PREFIX_FILE_NAME, SUFFIX_FILE_NAME);
        file.transferTo(tempFile);
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

                // ==============================
                // NUMERO E DATA FATTURA
                // ==============================
                if (line.startsWith(INVOICE_NUMBER)) {
                    Matcher m = Pattern.compile(PATTERN_INVOICE_NUMBER).matcher(line);
                    if (m.find()) {
                        invoiceDto.setInvoiceNumber(m.group(1).trim());
                        invoiceDto.setInvoiceDate(m.group(2).trim());
                    }
                }

                // ==============================
                // MITTENTE
                // ==============================
                if (line.matches(PATTERN_ISSUER)) {
                    issuerDto.setVatNumber(extractAfter(line, VAT_NUMBER_CODE));
                    issuerDto.setFiscalCode(extractAfter(line, FISCAL_CODE));
                    if (i > 0) issuerDto.setAddress(lines[i - 1].trim());
                    if (i > 1) issuerDto.setName(lines[i - 2].trim());
                }

                // ==============================
                // DESTINATARIO
                // ==============================
                if (line.equalsIgnoreCase(RECIPIENT) && i + 1 < lines.length) {
                    recipientDto.setName(lines[i + 1].trim());
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int j = i + 2; j < Math.min(i + 5, lines.length); j++) {
                        String l = lines[j].trim();
                        if (l.isEmpty()) break;
                        addressBuilder.append(l).append(", ");
                    }
                    recipientDto.setAddress(addressBuilder.toString().replaceAll(PATTERN_ADDRESS_REPLACE, ""));
                }

                if (line.toUpperCase().startsWith(RECIPIENT_VAT_NUMBER_CODE)) {
                    recipientDto.setVatNumber(extractAfter(line, RECIPIENT_VAT_NUMBER_CODE));
                }
                if (line.toUpperCase().startsWith(RECIPIENT_FISCAL_CODE)) {
                    recipientDto.setFiscalCode(extractAfter(line, RECIPIENT_FISCAL_CODE));
                }
                if (line.toUpperCase().startsWith(TELEPHONE)) {
                    recipientDto.setPhone(extractAfter(line, TELEPHONE));
                }
                if (line.toUpperCase().startsWith(MAIL)) {
                    recipientDto.setEmail(extractAfter(line, MAIL));
                }

                // ==============================
                // PRODOTTI
                // ==============================
                if (line.matches(PATTERN_PRODUCT_CODE) && i + 1 < lines.length) {
                    String code = line.split(" ")[0];
                    String desc = line.substring(code.length()).trim();
                    String nextLine = lines[i + 1].trim();
                    Matcher m = Pattern.compile(PATTERN_PRODUCT).matcher(nextLine);
                    if (m.find()) {
                        int qty = Integer.parseInt(m.group(1));
                        double unitPrice = parseDouble(m.group(2));
                        double total = parseDouble(m.group(3));
                        itemsDtoList.add(new InvoiceItemDto(code, desc, qty, unitPrice, 0, total));
                    }
                }

                // ==============================
                // PAGAMENTO
                // ==============================
                if (line.toUpperCase().contains(IBAN) && i + 1 < lines.length) {
                    if (i >= 2 && lines[i - 2].toUpperCase().contains(PAYMENT_METHOD)) {
                        paymentDto.setMethod(lines[i - 1].trim());
                    } else {
                        paymentDto.setMethod(UNKNOW);
                    }

                    Matcher ibanMatcher = Pattern.compile(PATTERN_IBAN).matcher(line);
                    if (ibanMatcher.find()) {
                        paymentDto.setIban(ibanMatcher.group(1));
                    }

                    if (i + 1 < lines.length) {
                        paymentDto.setAccountHolder(lines[i + 1].trim());
                    }
                }

                if (line.matches(PATTERN_DUE_DATE_AND_AMOUNT)) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        paymentDto.setDueDate(parts[0].trim());
                        paymentDto.setDueAmount(parseDouble(parts[1]));
                    }
                }

                // ==============================
                // TOTALE IMPONIBILE
                // ==============================
                if (line.startsWith(TOTAL_AMOUNT)) {
                    invoiceDto.setTotalAmount(parseDouble(line.replace(TOTAL_AMOUNT, "").trim()));
                }

                // ==============================
                // CONTATTI MITTENTE (EMAIL/TELEFONO)
                // ==============================
                Matcher emailMatcher = Pattern.compile(PATTERN_EMAIL).matcher(line);
                if (issuerDto.getEmail() == null && emailMatcher.find()) {
                    issuerDto.setEmail(emailMatcher.group());
                }

                Matcher phoneMatcher = Pattern.compile(PATTERN_PHONE).matcher(line);
                if (issuerDto.getPhone() == null && phoneMatcher.find()) {
                    issuerDto.setPhone(phoneMatcher.group());
                }
            }

            // ==============================
            // ASSEMBLA L'OGGETTO FINALE
            // ==============================
            invoiceDto.setIssuer(issuerDto);
            invoiceDto.setRecipient(recipientDto);
            invoiceDto.setItems(itemsDtoList);
            invoiceDto.setPayment(paymentDto);
        }

        return invoiceDto;
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
