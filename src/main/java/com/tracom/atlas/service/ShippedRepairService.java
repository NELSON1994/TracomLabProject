package com.tracom.atlas.service;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.ShippedRepair;
import com.tracom.atlas.entity.UfsAuditLog;
import com.tracom.atlas.entity.UfsUser;
import com.tracom.atlas.repository.AuditRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.repository.ShippedRepairRepository;
import com.tracom.atlas.repository.UserRepository;
import com.tracom.atlas.wrapper.LogExtras;
import ke.axle.chassis.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShippedRepairService {

    private final Logger logger = LoggerFactory.getLogger(ShippedRepair.class);

    private final RepairRepository repairRepository;
    private final LoggerServiceTemplate loggerServiceTemplate;
    private final ShippedRepairRepository shippedRepairRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    public ShippedRepairService(RepairRepository repairRepository, ShippedRepairRepository shippedRepairRepository, LogExtras logExtras, LoggerServiceTemplate loggerServiceTemplate, AuditRepository auditRepository, UserRepository userRepository) {
        this.repairRepository = repairRepository;
        this.shippedRepairRepository = shippedRepairRepository;
        this.loggerServiceTemplate = loggerServiceTemplate;
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void populateShippedRepair(Long id) {
        List<ShippedRepair> shippedRepairList = shippedRepairRepository.findShippedRepairsByIntrash(AppConstants.NO);

        Repair repair = repairRepository.findById(id).get();

        if (!shippedRepairList.contains(repair.getDevices())) {

            ShippedRepair shippedRepair = new ShippedRepair(repair.getDevices().getSerialnumber(),repair.getDevices(),AppConstants.ACTIVITY_CREATE,
                    AppConstants.STATUS_APPROVED,AppConstants.NO);

            ShippedRepair x = shippedRepairRepository.save(shippedRepair);

            //Logger Service
            loggerServiceTemplate.log(Constants.DESCRIPTION,Constants.SHIPPED_REPAIR,x.getId(),AppConstants.ACTIVITY_CREATE,AppConstants.STATUS_COMPLETED,Constants.NOTES);


        }
    }
}
