package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Nelson
 */

@Repository
public interface CustomersRepository  extends JpaRepository<Customers, Long> {
    Optional<List<Customers>> findCustomersByActionStatus(String actionStatus);
    Optional<Customers> findById(Long id);
    Optional<List<Customers>> findCustomersByIntrash(String intrash);
    long count();
    Optional<List<Customers>>findCustomersByCreationDateBetween(Date from , Date to);
}
