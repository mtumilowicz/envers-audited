package com.example.envers.audited.customer.audit;

import com.example.envers.audited.customer.domain.Customer;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mtumilowicz on 2018-07-14.
 */
@Repository
@Transactional
@AllArgsConstructor
public class CustomerHistoryRepository {
    
    private final EntityManager entityManager;

    public List<Customer> getHistory(@NotNull Long id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = ListUtils.emptyIfNull(auditReader.getRevisions(Customer.class, id));

        return revisions.stream()
                .map(rev -> auditReader.find(Customer.class, id, rev))
                .collect(Collectors.toList());
    }
}
