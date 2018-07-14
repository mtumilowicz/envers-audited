package com.example.envers.audited.customer.controller;

import com.example.envers.audited.customer.domain.Customer;
import com.example.envers.audited.customer.domain.CustomerDto;
import com.example.envers.audited.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@RestController
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;
    
    @PostMapping("customers")
    public void save(@RequestBody CustomerDto dto) {
        service.save(dto);
    }

    @GetMapping("customers")
    public List<Customer> findAll() {
        return service.findAll();
    }

    @PutMapping("customers/{id}")
    public void update(@RequestBody CustomerDto dto, @PathVariable("id") Long id) {
        service.update(dto, id);
    }

    @GetMapping("customers/{id}")
    public Customer findPersonById(@PathVariable("id") Long id) {
        return service.findById(id).orElse(null);
    }
    
    @GetMapping("customers/{id}/history")
    public List<Customer> getHistoryById(@PathVariable("id") Long id) {
        return service.getHistory(id);
    }
    
    @DeleteMapping("customers/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
