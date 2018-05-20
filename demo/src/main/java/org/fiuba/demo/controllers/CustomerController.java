package org.fiuba.demo.controllers;

import org.fiuba.demo.domain.Customer;
import org.fiuba.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return this.repository.save(customer);
    }

    @GetMapping
    public List<Customer> getAll(@RequestParam(required = false) String lastName) {
        if (Objects.isNull(lastName)) {
            return this.repository.findAll();
        }
        return this.repository.findByLastName(lastName);
    }

    @GetMapping("/{customerId}")
    public Customer findById(@PathVariable Long customerId) {
        return this.repository.findById(customerId).orElse(null);
    }

    @DeleteMapping("/{customerId}")
    public void deleteById(@PathVariable Long customerId) {
        this.repository.deleteById(customerId);
    }
}
