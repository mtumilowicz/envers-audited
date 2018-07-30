package com.example.envers.audited.rest.customer.controller;

import com.example.envers.audited.domain.customer.model.Customer;
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

    @Test
    public void save() {
        assertThat(restTemplate
                        .postForEntity(
                                createURLWithPort("/customers"),
                                new Customer(),
                                Void.class)
                        .getStatusCode(),
                is(HttpStatus.OK));
    }

    @Test
    public void findAll() {
        assertThat(restTemplate
                        .getForEntity(
                                createURLWithPort("/customers"),
                                List.class)
                        .getStatusCode(),
                is(HttpStatus.OK));
    }


    @Test
    public void update() {
        restTemplate.put(createURLWithPort("/customers/1"), CustomerDto.builder().build());
    }


    @Test
    public void findPersonById() {
        assertThat(restTemplate
                        .getForEntity(
                                createURLWithPort("/customers/1"),
                                Customer.class)
                        .getStatusCode(),
                is(HttpStatus.OK));
    }

    @Test
    public void getHistoryById() {
        assertThat(restTemplate
                        .getForEntity(
                                createURLWithPort("/customers/1/history"),
                                List.class)
                        .getStatusCode(),
                is(HttpStatus.OK));
    }

    @Test
    public void deleteById() {
        restTemplate.delete(createURLWithPort("/customers/1"));
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}