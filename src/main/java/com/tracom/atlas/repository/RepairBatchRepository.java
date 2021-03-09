package com.tracom.atlas.repository;

import com.tracom.atlas.entity.RepairBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RepairBatchRepository extends JpaRepository<RepairBatch,Long> {

    Optional<RepairBatch> findRepairBatchByClientName(String clientName);
}
