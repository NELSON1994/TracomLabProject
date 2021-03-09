package com.tracom.atlas.controller;

import com.tracom.atlas.entity.RepairBatch;
import com.tracom.atlas.entity.UfsEdittedRecord;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value="/batch")
public class RepairBatchController extends ChasisResource<RepairBatch, Long, UfsEdittedRecord> {

    public RepairBatchController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

}
