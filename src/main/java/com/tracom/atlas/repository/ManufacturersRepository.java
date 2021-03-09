package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Manufacturers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * @author Nelson
 * 2019
 */

@Repository
public interface ManufacturersRepository  extends JpaRepository<Manufacturers, Long> {
    Optional<List<Manufacturers>> findManufacturersByIntrash(String intrash);
    Optional<List<Manufacturers>> findManufacturersByActionStatus(String actionStatus);
    Optional<List<Manufacturers>>findManufacturersByCreationDateBetween(Date from , Date to);
    long count();
}
