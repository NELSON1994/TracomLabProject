package com.tracom.atlas.service;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.*;
import com.tracom.atlas.repository.AuditRepository;
import com.tracom.atlas.repository.DeliveryRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.repository.UserRepository;
import com.tracom.atlas.wrapper.LogExtras;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DeliveryService {

    private final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    public final RepairRepository repairRepository;
    private final LoggerServiceTemplate loggerServiceTemplate;
    public final DeliveryRepository deliveryRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    public DeliveryService(RepairRepository repairRepository, LoggerServiceTemplate loggerServiceTemplate, DeliveryRepository deliveryRepository, AuditRepository auditRepository, UserRepository userRepository) {
        this.repairRepository = repairRepository;
        this.loggerServiceTemplate = loggerServiceTemplate;
        this.deliveryRepository = deliveryRepository;
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void populateDelivery(Long id){
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByIntrash(AppConstants.NO);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        Repair repair = repairRepository.findById(id).get();

        if (!deliveryList.contains(repair.getDevices())) {

            Delivery delivery = new Delivery(repair.getDevices(),repair.getCustomers(),AppConstants.ACTIVITY_CREATE,
                    AppConstants.STATUS_PENDING,AppConstants.ACTIVITY_APPROVE,AppConstants.NO,repair.getDevices().getSerialnumber(),week,year);

            //save delivery
            Delivery x = deliveryRepository.save(delivery);

            //Logger Service
            loggerServiceTemplate.log(Constants.DESCRIPTION,Constants.DELIVERY,x.getId(),AppConstants.ACTIVITY_CREATE,AppConstants.STATUS_COMPLETED,Constants.NOTES);

        }

    }

    public ResponseWrapper findDevicesByClient(String client) {
        ResponseWrapper wrapper = new ResponseWrapper();
        List<Delivery> optional = deliveryRepository.findAllByCustomers(client);

        if (Objects.nonNull(optional)) {

            wrapper.setMessage("devices fetched successfully");
            wrapper.setCode(HttpStatus.OK.value());
            wrapper.setData(optional);
            wrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());

            return wrapper;
        }

        wrapper.setMessage("devices not found");
        wrapper.setCode(HttpStatus.NOT_FOUND.value());
        wrapper.setData(null);
        wrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());

        return wrapper;
    }


}
