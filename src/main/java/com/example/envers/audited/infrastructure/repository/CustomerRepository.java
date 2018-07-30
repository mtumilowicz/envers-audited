package com.example.envers.audited.infrastructure.repository;

import com.example.envers.audited.domain.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
