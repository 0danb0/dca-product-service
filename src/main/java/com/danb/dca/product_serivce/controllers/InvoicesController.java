package com.danb.dca.product_serivce.controllers;

import com.danb.dca.product_serivce.builders.GenericBuilder;
import com.danb.dca.product_serivce.exceptions.InvoiceException;
import com.danb.dca.product_serivce.models.response.GenericHealthCheckResponse;
import com.danb.dca.product_serivce.properties.ApplicationProperties;
import com.danb.dca.product_serivce.services.InvoiceService;
import com.danb.dca.product_serivce.enums.DomainMsg;
import com.danb.dca.product_serivce.enums.ErrorMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.danb.dca.product_serivce.utils.ConstantStrings.GENERIC_FILE_PROCESSING_ERROR_STRING;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvoicesController {

    private final GenericBuilder genericBuilder;
    private final ApplicationProperties applicationProperties;
    private final InvoiceService invoiceService;

    @GetMapping(value = "/invoice/health")
    public ResponseEntity<Object> productHealthCheck() {
        GenericHealthCheckResponse genericHealthCheckResponse = genericBuilder.genericHealthCheckResponse(DomainMsg.INVOICE_SERVICE_TECHNICAL.getName(),"Invoice");
        return new ResponseEntity<>(genericHealthCheckResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/invoice/elaborator")
    public ResponseEntity<Object> invoiceElaborator(@RequestParam("file") MultipartFile file,@RequestParam("application-license") String applicationLicense,@RequestParam("email-from") String emailFrom) throws InvoiceException {
        log.info("- InvoceElaborator - START");

        checkApplicationLicense(applicationLicense);

        try {
            invoiceService.invoceElaborator(file,emailFrom);
            log.info("- File uploaded and processed successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("-- Error processing the file: {}", e.getMessage());
            throw new InvoiceException(
                    ErrorMsg.DCA_PRD_SRV_99.getCode(),
                    ErrorMsg.DCA_PRD_SRV_99.getMessage(),
                    DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                    GENERIC_FILE_PROCESSING_ERROR_STRING
            );
        } finally {
            log.info("- InvoceElaborator - END");
        }
    }

    private void checkApplicationLicense(String applicationLicense) throws InvoiceException {
        if (!applicationProperties.getLicensed().containsValue(applicationLicense)) {
            log.error("- Invalid application license: {}", applicationLicense);
            throw new InvoiceException(
                    ErrorMsg.DCA_PRD_SRV_02.getCode(),
                    ErrorMsg.DCA_PRD_SRV_02.getMessage(),
                    DomainMsg.MICROSERVICE_FUNCTIONAL.getName(),
                    ErrorMsg.DCA_PRD_SRV_02.getDetail()
            );
        }
    }
}
