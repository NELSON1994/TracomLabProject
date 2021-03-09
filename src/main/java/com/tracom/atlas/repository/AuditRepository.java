package com.tracom.atlas.repository;

import com.tracom.atlas.entity.UfsAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AuditRepository extends JpaRepository<UfsAuditLog, BigDecimal> {
}
