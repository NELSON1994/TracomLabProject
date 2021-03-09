package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Parts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author momondi
 * JpaRepository  class  extended
 */

@Repository
public interface PartsRepository extends JpaRepository<Parts, Long> {

    Optional<Parts> findByPartNumber(String partnumber);

    List<Parts> findPartsByIntrash(String intrash);

    List<Parts> findAllByIntrashAndActionStatus(String intrash, String actionStatus);

    Optional<List<Parts>> findPartsByCreatedOnBetween(java.sql.Date from, java.sql.Date to);
    List<Parts> findPartsByDescriptionIsContaining(String description);
    List<Parts> findPartsByManufacturerNameContaining(String manufactucturer);

}
