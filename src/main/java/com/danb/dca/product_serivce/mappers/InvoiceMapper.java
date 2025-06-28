package com.danb.dca.product_serivce.mappers;

import com.danb.dca.product_serivce.models.dto.CompanyDto;
import com.danb.dca.product_serivce.models.dto.InvoiceDto;
import com.danb.dca.product_serivce.models.dto.PaymentInfoDto;
import com.danb.dca.product_serivce.models.dto.ProductDto;
import com.danb.dca.product_serivce.models.po.CompanyPO;
import com.danb.dca.product_serivce.models.po.InvoicePO;
import com.danb.dca.product_serivce.models.po.PaymentInfoPO;
import com.danb.dca.product_serivce.models.po.ProductPO;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, java.time.Instant.class})
public interface InvoiceMapper {

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(target = "pk", expression = "java(pk)")
    @Mapping(target = "sk", expression = "java(sk)")
    @Mapping(target = "applicationId", expression = "java(applicationId)")
    @Mapping(target = "creationDate", expression = "java(Instant.now().toString())")
    @Mapping(target = "invoiceUuid", expression = "java(UUID.randomUUID().toString())")
    InvoicePO toPO(InvoiceDto dto, String pk, String sk, String applicationId);
    InvoiceDto fromPoToDTO(InvoicePO po);

    CompanyPO fromDtoToPO(CompanyDto dto);
    CompanyDto fromPoToDTO(CompanyPO po);

    PaymentInfoPO fromDtoToPO(PaymentInfoDto dto);
    PaymentInfoDto fromPoToDTO(PaymentInfoPO po);

    ProductPO fromDtoToPO(ProductDto dto);
    ProductDto fromPoToDTO(ProductPO po);
}
