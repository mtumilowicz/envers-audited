package com.example.envers.audited.rest.customer.controller.assembler;

import com.example.envers.audited.domain.customer.model.Customer;
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto;
import org.modelmapper.ModelMapper;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
public class CustomerAssembler {
    private static ModelMapper mapper = new ModelMapper();
    
    public static Customer toEntity(CustomerDto dto) {
        return mapper.map(dto, Customer.class);
    }
    
    public static Customer merge(Customer customer, CustomerDto dto) {
        mapper.map(dto, customer);
        
        return customer;
    }
}
