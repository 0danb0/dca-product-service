package com.danb.dca.product_serivce.services;

import com.danb.dca.product_serivce.builders.InvoiceBuilder;
import com.danb.dca.product_serivce.enums.DomainMsg;
import com.danb.dca.product_serivce.enums.ErrorMsg;
import com.danb.dca.product_serivce.exceptions.InvoiceException;
import com.danb.dca.product_serivce.helpers.PDFHelper;
import com.danb.dca.product_serivce.models.dto.InvoiceDto;
import com.danb.dca.product_serivce.models.po.InvoicePO;
import com.danb.dca.product_serivce.properties.ApplicationProperties;
import com.danb.dca.product_serivce.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.danb.dca.product_serivce.utils.ConstantStrings.S3_SERVICE_STATUS_ERROR_MESSAGE_STRING;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final PDFHelper pdfHelper;
    private final S3Service s3Service;
    private final InvoiceBuilder invoiceBuilder;
    private final InvoiceRepository invoiceRepository;
    private final ApplicationProperties applicationProperties;

    public void invoceElaborator(String fileUrl,String emailFrom) throws IOException, InvoiceException {
        log.info("-- InvoceElaborator START");

        String folderName = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));

        if(applicationProperties.isUploadEnabled()) {
//          Step 1 - Verifica presenza cartella su bucket e eventuale creazione
            String s3FolderCheckAndCreate = s3Service.folderCheckAndCreate(folderName);
            s3ServiceStatusCheck(s3FolderCheckAndCreate);


//          Step 2 - Salvataggio della fattura per backup
            String s3UploadBackupInvoice = s3Service.uploadFile(fileUrl, folderName);
            s3ServiceStatusCheck(s3UploadBackupInvoice);
        }

//      Step 3 - Lettura file
        InvoiceDto invoiceDto = pdfHelper.invoiceStripper(fileUrl);
        invoiceDto.setEmailFrom(emailFrom);
//      Step 4 - Persistenza dati estratti
        if(applicationProperties.isDatabasePersistence()){
            log.info("--- InvoceElaborator: Saving invoice - START");
            InvoicePO invoicePO = invoiceBuilder.buildInvoicePo(invoiceDto);
            invoiceRepository.insert(invoicePO);
            log.info("--- InvoceElaborator: Saving invoice - DONE");
        }

        log.info("-- InvoceElaborator DONE");
    }

    // Helpers
    private static void s3ServiceStatusCheck(String serviceStatusMessage) throws InvoiceException {
        if(serviceStatusMessage.equals(S3_SERVICE_STATUS_ERROR_MESSAGE_STRING)){
            throw new InvoiceException(
                ErrorMsg.DCA_PRD_SRV_03.getCode(),
                ErrorMsg.DCA_PRD_SRV_03.getMessage(),
                DomainMsg.S3_SERVICE_TECHNICAL.getName(),
                ErrorMsg.DCA_PRD_SRV_03.getCode()
            );
        }
    }

}
