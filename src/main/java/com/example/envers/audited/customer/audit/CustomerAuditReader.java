package com.example.envers.audited.customer.audit;

import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Created by mtumilowicz on 2018-07-14.
 */
@Component
@AllArgsConstructor
public class CustomerAuditReader {
    
    private final EntityManager entityManager;

    public AuditReader get() {
        return AuditReaderFactory.get(entityManager);
    }
}
