package com.danb.dca.product_serivce.exceptions;

import com.danb.dca.product_serivce.models.response.RestResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonErrorResponse extends RestResponse {
    private Error error;
}
