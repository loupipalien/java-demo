package com.ltchen.demo.spring.data.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author: 01139983
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    /**
     * 按 lastName 查找
     * @param lastName
     * @return
     */
    List<Customer> findByLastName(String lastName);
}
