package com.example.envers.audited.customer.controller

import com.example.envers.audited.customer.domain.CustomerDto
import com.example.envers.audited.customer.service.CustomerRepository
import com.example.envers.audited.customer.service.CustomerService
import spock.lang.Specification

import javax.persistence.EntityManager
/**
 * Created by mtumilowicz on 2018-07-14.
 */
class CustomerControllerTest extends Specification {

    def repository = Mock(CustomerRepository)
    def service = new CustomerService(repository, Mock(EntityManager))
    def controller = new CustomerController(service)
    def dto = CustomerDto.builder().build()

    def "save"() {
        when:
        controller.save(dto)

        then:
        1 * service.save(_)
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
        1 * service.update(_, 1)
    }
    
    def "getHistoryById"() {
        when:
        controller.getHistoryById(1)

        then:
        1 * service.getHistory(1)
    }
}