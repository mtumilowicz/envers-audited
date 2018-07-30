package com.example.envers.audited.rest.customer.controller.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@Value
@Builder
public class CustomerDto {
    private String firstName;
    private String lastName;
}
