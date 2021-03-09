package com.tracom.atlas.controller;

import com.tracom.atlas.entity.BankRegions;
import com.tracom.atlas.entity.UfsEdittedRecord;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value = "bank_regions")
public class BankRegionController extends ChasisResource<BankRegions, Long, UfsEdittedRecord> {

public BankRegionController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
        }
        }
