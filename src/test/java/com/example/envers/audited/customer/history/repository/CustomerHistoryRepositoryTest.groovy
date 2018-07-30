package com.example.envers.audited.customer.history.repository

import com.example.envers.audited.infrastructure.audit.reader.CustomerAuditReader
import com.example.envers.audited.domain.customer.model.Customer
import com.example.envers.audited.domain.customer.history.repository.CustomerHistoryRepository
import org.hibernate.envers.AuditReader
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2018-07-14.
 */
class CustomerHistoryRepositoryTest extends Specification {
    def "test getHistory"() {
        given:
        def customer1 = new Customer(1, "a1", "b1")
        def customer2 = new Customer(2, "a2", "b2")
        def customer3 = new Customer(3, "a3", "b3")

        def auditReader = Mock(AuditReader) {
            getRevisions(*_) >> [1, 2, 3]
            find(_, 1, 1) >> customer1
            find(_, 1, 2) >> customer2
            find(_, 1, 3) >> customer3
        }

        and:
        def customerAuditReader = Mock(CustomerAuditReader) {
            get() >> auditReader
        }

        and:
        def repository = new CustomerHistoryRepository(customerAuditReader)

        expect:
        repository.getHistory(1) == [customer1, customer2, customer3]
    }
}
