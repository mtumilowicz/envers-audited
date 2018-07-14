package com.example.envers.audited.customer.service

import com.example.envers.audited.customer.audit.repository.CustomerHistoryRepository
import com.example.envers.audited.customer.domain.Customer
import com.example.envers.audited.customer.domain.CustomerDto
import com.example.envers.audited.customer.repository.CustomerRepository
import spock.lang.Specification
/**
 * Created by mtumilowicz on 2018-07-13.
 */
class CustomerServiceTest extends Specification {
    def "test save"() {
        given:
        def repository = Mock(CustomerRepository)

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        and:
        def customer = new Customer(
                firstName: _firstName,
                lastName: _lastName
        )

        and:
        def dto = CustomerDto.builder()
                .firstName("1")
                .lastName("2")
                .build()

        when:
        service.save(dto)

        then:
        1 * repository.save({ it == customer } as Customer)

        where:
        _firstName | _lastName
        "1"        | "2"
    }

    def "test findPersonById"() {
        given:
        def customer = Customer.builder()
                .id(1)
                .firstName("firstName")
                .lastName("lastName")
                .build()

        def repository = Mock(CustomerRepository)

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        when:
        def found = service.findById(1)

        then:
        1 * repository.findById(1) >> Optional.of(customer)
        found.isPresent()
        found.get() == customer
    }

    def "test findPersonById - notFound"() {
        given:
        def repository = Mock(CustomerRepository) {
            findById(_) >> Optional.empty()
        }

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        when:
        def notFound = service.findById(1)

        then:
        !notFound.isPresent()
    }

    def "test findPersonById - found"() {
        given:
        def customer = new Customer(
                id: 1,
                firstName: "firstName",
                lastName: "lastName"
        )

        and:
        def repository = Mock(CustomerRepository) {
            findById(1) >> Optional.of(customer)
        }

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        when:
        def found = service.findById(1)

        then:
        found.isPresent()
        found.get() == customer
    }

    def "test findAll"() {
        given:
        def repository = Mock(CustomerRepository)

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        when:
        service.findAll()

        then:
        1 * repository.findAll()
    }


    def "test update"() {
        given:
        def customer = new Customer(
                id: 1,
                firstName: _firstName,
                lastName: _lastName
        )

        and:
        def repository = Mock(CustomerRepository) {
            getOne(_) >> customer
        }

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        and:
        def dto = CustomerDto.builder()
                .firstName(_firstNameDto)
                .lastName(_lastNameDto)
                .build()

        when:
        service.update(dto, 1)

        then:
        1 * repository.save({ it == customer } as Customer)

        where:
        _firstNameDto  | _lastNameDto  | _firstName  | _lastName
        "firstNameDto" | "lastNameDto" | "firstName" | "lastName"
    }
    
    def "test getHistory"() {
        given:
        def historyRepository = Mock(CustomerHistoryRepository)

        and:
        def service = new CustomerService(Mock(CustomerRepository), historyRepository)

        when:
        service.getHistory(1)

        then:
        1 * historyRepository.getHistory(1)
    }

    def "test delete"() {
        given:
        def repository = Mock(CustomerRepository)

        and:
        def service = new CustomerService(repository, Mock(CustomerHistoryRepository))

        when:
        service.deleteById(1)

        then:
        1 * repository.deleteById(1)
    }
}
