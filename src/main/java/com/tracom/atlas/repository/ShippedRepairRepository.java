package com.tracom.atlas.repository;

import com.tracom.atlas.entity.ShippedRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ShippedRepairRepository extends JpaRepository<ShippedRepair,Long> {
        List<ShippedRepair> findShippedRepairsByIntrash(String intrash);
}
