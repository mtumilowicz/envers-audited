package com.example.envers.audited.customer.service;

import com.example.envers.audited.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
