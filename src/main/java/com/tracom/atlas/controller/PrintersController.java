package com.tracom.atlas.controller;

import com.tracom.atlas.entity.Printers;
import com.tracom.atlas.entity.UfsEdittedRecord;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

/**
 * @author Nelson
 */
@RestController
@RequestMapping( value = "/printers" )
public class PrintersController  extends ChasisResource<Printers, Long, UfsEdittedRecord> {

    public PrintersController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
}
