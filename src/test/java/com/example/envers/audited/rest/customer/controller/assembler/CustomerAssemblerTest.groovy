package com.example.envers.audited.rest.customer.controller.assembler

import com.example.envers.audited.domain.customer.model.Customer
import com.example.envers.audited.rest.customer.controller.assembler.CustomerAssembler
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2018-07-13.
 */
class CustomerAssemblerTest extends Specification {
    def "test toEntity"() {
        given:
        def dto = CustomerDto.builder()
                .firstName(_firstName)
                .lastName(_lastName)
                .build()

        and:
        def expected = new Customer(
                firstName: _firstName,
                lastName: _lastName
        )

        when:
        def entity = CustomerAssembler.toEntity(dto)

        then:
        expected == entity

        where:
        _firstName | _lastName
        "1"        | "2"
        null       | null
    }

    def "test merge"() {
        given:
        def dto = CustomerDto.builder()
                .firstName(_firstNameDto)
                .lastName(_lastNameDto)
                .build()

        and:
        def customer = new Customer(
                firstName: _firstName,
                lastName: _lastName
        )

        and:
        def expected = new Customer(
                firstName: _expectedFirstName,
                lastName: _expectedLastName
        )

        when:
        def entity = CustomerAssembler.merge(customer, dto)

        then:
        expected == entity

        where:
        _firstNameDto  | _lastNameDto  | _firstName  | _lastName  || _expectedFirstName || _expectedLastName
        "firstNameDto" | "lastNameDto" | "firstName" | "lastName" || "firstNameDto" | "lastNameDto"
    }
}
