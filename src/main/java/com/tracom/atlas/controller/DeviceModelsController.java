package com.tracom.atlas.controller;

import com.tracom.atlas.entity.DevicesModel;

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
@RequestMapping( value = "/deviceModel" )
public class DeviceModelsController  extends ChasisResource <DevicesModel, Long, UfsEdittedRecord> {

    public DeviceModelsController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

}
