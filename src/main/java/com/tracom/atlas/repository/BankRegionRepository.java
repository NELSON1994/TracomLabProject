package com.tracom.atlas.repository;

import com.tracom.atlas.entity.BankRegions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRegionRepository extends JpaRepository<BankRegions,Long> {
}
