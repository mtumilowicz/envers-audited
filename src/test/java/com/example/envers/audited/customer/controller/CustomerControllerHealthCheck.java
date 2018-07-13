package com.example.envers.audited.customer.controller;

import com.example.envers.audited.customer.domain.Customer;
import com.example.envers.audited.customer.service.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerHealthCheck {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    CustomerRepository repository;

    @Test
    public void save() {
        repository.save(Customer.builder()
                .firstName("a")
                .build());
        
        System.out.println(restTemplate.getForEntity(createURLWithPort("/customers"), List.class));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}