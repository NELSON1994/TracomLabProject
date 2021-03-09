package com.tracom.atlas.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Delivery;
import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.DeliveryRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.service.DeliveryService;
import com.tracom.atlas.wrapper.DeliveryWrapper;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.exceptions.ExpectationFailed;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@Transactional
@RequestMapping("/delivery")
public class DeliveryController extends ChasisResource<Delivery,Long, UfsEdittedRecord> {

    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;
    private final RepairRepository repairRepository;

    public DeliveryController(LoggerService loggerService, EntityManager entityManager, DeliveryService deliveryService, DeliveryRepository deliveryRepository, RepairRepository repairRepository) {
        super(loggerService, entityManager);
        this.deliveryService = deliveryService;
        this.deliveryRepository = deliveryRepository;
        this.repairRepository = repairRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper> findDevicesByClient(@RequestParam("client") String client){
        ResponseWrapper responseWrapper = deliveryService.findDevicesByClient(client);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseWrapper<Delivery>> updateEntity(@Valid @RequestBody Delivery delivery) throws IllegalAccessException, JsonProcessingException, ExpectationFailed {
        ResponseWrapper wrapper = new ResponseWrapper();

        Delivery delivery_ = deliveryRepository.findById(delivery.getId()).get();

        if (delivery.getDeliveryStatus().equalsIgnoreCase(Constants.DELIVERED)) {
            delivery_.setIntrash(AppConstants.NO);
        }

        delivery_.setDeliveryStatus(delivery.getDeliveryStatus());
        delivery_.setDeliveredBy(delivery.getDeliveredBy());
        delivery_.setLocation(delivery.getLocation());
        delivery_.setAction(AppConstants.ACTIVITY_UPDATE);
        deliveryRepository.save(delivery_);

        wrapper.setData(delivery_);
        changeRepairStatus(delivery.getSerialNumber());

        return new ResponseEntity<>(wrapper,HttpStatus.OK);
    }

    @Async
    @Transactional
    public void changeRepairStatus(String serialNumber) {
        List<Repair> repairs = repairRepository.findAllBySerialNumberAndIntrash(serialNumber,AppConstants.NO);

        for (Repair repair: repairs) {
            repair.setIntrash(AppConstants.YES);
        }

        repairRepository.saveAll(repairs);
    }

    @PutMapping(value = "/update-multiple")
    @Transactional
    public ResponseEntity<ResponseWrapper> updateMultiple(@RequestBody DeliveryWrapper wrapper) {
        ResponseWrapper response = new ResponseWrapper();

        List<Delivery> deliveryList = new ArrayList<>();
        for (String id: wrapper.getIds()) {
            Optional<Delivery> t = deliveryRepository.findById(Long.valueOf(id));

            if (t.isPresent()) {
                Delivery delivery = t.get();

                if (Objects.nonNull(wrapper.getDeliveryStatus())) {
                    delivery.setDeliveryStatus(wrapper.getDeliveryStatus());

                    if (wrapper.getDeliveryStatus().equalsIgnoreCase(Constants.DELIVERED)){
                        delivery.setIntrash(AppConstants.YES);
                    }
                }

                if (Objects.nonNull(wrapper.getLocation())) {
                    delivery.setLocation(wrapper.getLocation());
                }

                if (Objects.nonNull(wrapper.getDeliveredBy())) {
                    delivery.setDeliveredBy(wrapper.getDeliveredBy());
                }

                deliveryList.add(delivery);
            }
        }

        deliveryRepository.saveAll(deliveryList);
        response.setData("Saved successfully");


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
