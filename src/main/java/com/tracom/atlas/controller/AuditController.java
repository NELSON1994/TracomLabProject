package com.tracom.atlas.controller;

import com.tracom.atlas.entity.UfsAuditLog;
import com.tracom.atlas.entity.UfsEdittedRecord;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RestController
@RequestMapping("/audit-logs")
public class AuditController extends ChasisResource<UfsAuditLog, BigDecimal, UfsEdittedRecord> {

    public AuditController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
