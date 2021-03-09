package com.tracom.atlas.repository;

import com.tracom.atlas.entity.PartsIssued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nelson
 */
@Repository
public interface PartsIssuedRepository extends JpaRepository<PartsIssued , Long> {
    List<PartsIssued> findByPartNumberAndIntrashOrderByDateIssuedDesc(String partnumber, String  intrash);

    PartsIssued findAllByPartNumber(String partNumber);
}
