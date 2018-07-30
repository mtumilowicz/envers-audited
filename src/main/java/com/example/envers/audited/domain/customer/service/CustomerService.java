package com.example.envers.audited.domain.customer.service;

import com.example.envers.audited.domain.customer.history.repository.CustomerHistoryRepository;
import com.example.envers.audited.domain.customer.model.Customer;
import com.example.envers.audited.rest.customer.controller.assembler.CustomerAssembler;
import com.example.envers.audited.rest.customer.controller.dto.CustomerDto;
import com.example.envers.audited.infrastructure.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by mtumilowicz on 2018-07-13.
 */
@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerHistoryRepository historyRepository;

    public void save(@NotNull CustomerDto dto) {
        repository.save(CustomerAssembler.toEntity(dto));
    }
    
    public Optional<Customer> findById(@NotNull Long i) {
        return repository.findById(i);
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }
    
    public void update(@NotNull CustomerDto dto, @NotNull  Long id) {
        Customer one = Objects.requireNonNull(repository.getOne(id));
        CustomerAssembler.merge(one, dto);
        repository.save(one);
    }
    
    public void deleteById(@NotNull Long id) {
        repository.deleteById(id);
    }
    
    public List<Customer> getHistory(@NotNull Long id) {
        return historyRepository.getHistory(id);
    }
}
