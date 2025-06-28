package com.danb.dca.product_serivce.builders;

import com.danb.dca.product_serivce.mappers.InvoiceMapper;
import com.danb.dca.product_serivce.models.dto.InvoiceDto;
import com.danb.dca.product_serivce.models.po.InvoicePO;
import com.danb.dca.product_serivce.properties.ApplicationProperties;
import com.danb.dca.product_serivce.utils.Tools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceBuilder {

    private final Tools tools;
    private final InvoiceMapper invoiceMapper;
    private final ApplicationProperties applicationProperties;

    public InvoicePO buildInvoicePo(InvoiceDto invoiceDto){

        String pk = tools.createPk(invoiceDto.getInvoiceNumber());
        String sk = tools.getInstant();
        String appId = applicationProperties.getName();

        return invoiceMapper.toPO(invoiceDto, pk, sk, appId);
    }

}