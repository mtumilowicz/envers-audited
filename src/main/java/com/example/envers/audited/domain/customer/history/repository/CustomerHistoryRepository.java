package com.example.envers.audited.domain.customer.history.repository;

import com.example.envers.audited.infrastructure.audit.reader.CustomerAuditReader;
import com.example.envers.audited.domain.customer.model.Customer;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.hibernate.envers.AuditReader;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mtumilowicz on 2018-07-14.
 */
@Repository
@Transactional(readOnly = true)
@AllArgsConstructor
public class CustomerHistoryRepository {
    
    private final CustomerAuditReader customerAuditReader;

    public List<Customer> getHistory(@NotNull Long id) {
        AuditReader auditReader = customerAuditReader.get();
        List<Number> revisions = ListUtils.emptyIfNull(auditReader.getRevisions(Customer.class, id));

        return revisions.stream()
                .map(rev -> auditReader.find(Customer.class, id, rev))
                .collect(Collectors.toList());
    }
}
