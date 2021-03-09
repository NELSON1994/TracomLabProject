package com.tracom.atlas.controller;


import com.tracom.atlas.entity.Regions;
import com.tracom.atlas.entity.UfsEdittedRecord;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
@RequestMapping(value = "regions")
public class RegionsController extends ChasisResource<Regions, Long, UfsEdittedRecord> {

public RegionsController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
        }
        }
