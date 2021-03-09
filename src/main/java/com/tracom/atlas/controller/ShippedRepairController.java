package com.tracom.atlas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.ShippedRepair;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.ShippedRepairRepository;
import com.tracom.atlas.service.DeliveryService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("/shipped_repair")
public class ShippedRepairController extends ChasisResource<ShippedRepair,Long, UfsEdittedRecord> {

    public final DeliveryService shippedRepairService;
    public final ShippedRepairRepository shippedRepairRepository;

    public ShippedRepairController(LoggerService loggerService, EntityManager entityManager, DeliveryService shippedRepairService, ShippedRepairRepository shippedRepairRepository) {
        super(loggerService, entityManager);
        this.shippedRepairService = shippedRepairService;
        this.shippedRepairRepository = shippedRepairRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<ShippedRepair>> updateEntity(@RequestBody @Valid ShippedRepair shippedRepair) throws IllegalAccessException, JsonProcessingException, ExpectationFailed {
        if (shippedRepair.getShippedStatus().equalsIgnoreCase(Constants.RETURNED)) {
            shippedRepair.setIntrash(AppConstants.YES);
        }
        return super.updateEntity(shippedRepair);
    }
}
