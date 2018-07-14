package com.example.envers.audited.customer.controller;

import com.example.envers.audited.customer.domain.Customer;
import com.example.envers.audited.customer.domain.CustomerDto;
import com.example.envers.audited.customer.repository.CustomerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by mtumilowicz on 2018-07-14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CustomerRepository repository;

    @LocalServerPort
    private int port;

    @Before
    public void before() {
        repository.deleteAll();
    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void save() {
//        when
        restTemplate.postForEntity(
                createURLWithPort("/customers"),
                new Customer(),
                Void.class);
//        then
        assertThat(repository.findAll().size(), is(1));
    }

    @Test
    public void findAll() {
//        when
        repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

//        then
        assertThat(restTemplate
                        .getForObject(
                                createURLWithPort("/customers"),
                                List.class).size(),
                is(1));
    }


    @Test
    public void update_found() {
//        given
        Customer save = repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

//        and
        Long id = save.getId();

//        and
        String editedFirstName = "editedFirstName";
        String editedLastName = "editedLastName";

//        and
        Customer expected = new Customer(id, editedFirstName, editedLastName);

//        when
        restTemplate.put(createURLWithPort("/customers/" + id), CustomerDto.builder()
                .firstName(editedFirstName)
                .lastName(editedLastName)
                .build());

//        then
        Optional<Customer> byId = repository.findById(id);
        assertTrue(byId.isPresent());
        assertThat(expected, is(byId.get()));
    }

    @Test
    public void update_notFound() {
//        when
        restTemplate.put(createURLWithPort("/customers/1"), CustomerDto.builder()
                .firstName("editedFirstName")
                .lastName("editedLastName")
                .build());
        
//        then
        assertThat(repository.findAll().size(), is(0));
    }


    @Test
    public void findPersonById() {
//        given
        Customer expected = repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

//        when
        Customer customer = restTemplate
                .getForObject(
                        createURLWithPort("/customers/" + expected.getId()),
                        Customer.class);

//        then
        assertThat(expected, is(customer));
    }

    @Test
    public void getHistoryById_sizeCheck() {
//        given
        Customer customer = repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

//        and
        customer.setFirstName("firstNameEdited");

//        and
        repository.save(customer);

//        expect
        assertThat(restTemplate
                .getForObject(
                        createURLWithPort("/customers/" + customer.getId() + "/history"),
                        List.class).size(), is(2));
    }

    @Test
    public void getHistoryById_elementCheck() {
//        given
        Customer customer = repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

        Long id = customer.getId();

//        and
        customer.setFirstName("firstNameEdited");
        
//        and
        Customer expected_customerHistory1 = Customer.builder()
                .id(id)
                .firstName("firstName")
                .lastName("lastName")
                .build();

        Customer expected_customerHistory2 = Customer.builder()
                .id(id)
                .firstName("firstNameEdited")
                .lastName("lastName")
                .build();
        
//        and
        List<Customer> expected_history = Arrays.asList(expected_customerHistory1, expected_customerHistory2);
        
//        and
        repository.save(customer);

//        and
        ParameterizedTypeReference<List<Customer>> typeRef = new ParameterizedTypeReference<List<Customer>>() {
        };

//        when
        List<Customer> customerHistory = restTemplate
                .exchange(createURLWithPort("/customers/" + id + "/history"),
                        HttpMethod.GET,
                        null,
                        typeRef).getBody();
        
//        then
        assertThat(customerHistory, is(expected_history));
    }
    
    @Test
    public void deleteById() {
//        given
        Customer customer = repository.save(Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build());

        Long id = customer.getId();
        
//        when
        restTemplate.delete(createURLWithPort("/customers/" + id));
        
//        then
        assertThat(repository.findAll().size(), is(0));
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
