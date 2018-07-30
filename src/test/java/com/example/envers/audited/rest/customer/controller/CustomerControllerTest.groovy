package com.example.envers.audited.rest.customer.controller

import com.example.envers.audited.domain.customer.model.Customer
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto
import com.example.envers.audited.domain.customer.service.CustomerService
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2018-07-14.
 */
class CustomerControllerTest extends Specification {

    def service = Mock(CustomerService)
    def controller = new CustomerController(service)
    def dto = CustomerDto.builder().build()

    def "save"() {
        when:
        controller.save(dto)

        then:
        1 * service.save(dto)
    }

    def "findAll"() {
        when:
        controller.findAll()

        then:
        1 * service.findAll()
    }

    def "update"() {
        when:
        controller.update(dto, 1)

        then:
        1 * service.update(dto, 1)
    }

    def "findPersonById - found"() {
        given:
        def customer = new Customer(
                firstName: _firstName,
                lastName: _lastName
        )

        when:
        def found = controller.findPersonById(1)

        then:
        1 * service.findById(1) >> Optional.of(customer)

        and:
        found
        with(found) {
            firstName == _firstName
            lastName == _lastName
        }

        where:
        _firstName  | _lastName
        "firstName" | "lastName"
    }

    def "findPersonById - notFound"() {
        when:
        def notFound = controller.findPersonById(1)

        then:
        1 * service.findById(1) >> Optional.empty()
        !notFound
    }

    def "getHistoryById"() {
        when:
        controller.getHistoryById(1)

        then:
        1 * service.getHistory(1)
    }
    
    def "deleteById"() {
        when:
        controller.deleteById(1)

        then:
        1 * service.deleteById(1)
    }
}