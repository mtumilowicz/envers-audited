package com.example.envers.audited.rest.customer.controller;

import com.example.envers.audited.domain.customer.model.Customer;
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto;
import com.example.envers.audited.domain.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@RestController
@RequestMapping("customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;
    
    @PostMapping
    public void save(@RequestBody CustomerDto dto) {
        service.save(dto);
    }

    @GetMapping
    public List<Customer> findAll() {
        return service.findAll();
    }

    @PutMapping("{id}")
    public void update(@RequestBody CustomerDto dto, @PathVariable("id") Long id) {
        service.update(dto, id);
    }

    @GetMapping("{id}")
    public Customer findPersonById(@PathVariable("id") Long id) {
        return service.findById(id).orElse(null);
    }
    
    @GetMapping("{id}/history")
    public List<Customer> getHistoryById(@PathVariable("id") Long id) {
        return service.getHistory(id);
    }
    
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
