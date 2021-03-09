package com.tracom.atlas.service;

import com.tracom.atlas.entity.RepairBatch;
import com.tracom.atlas.repository.RepairBatchRepository;
import ke.axle.chassis.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
@Transactional
public class RepairBatchService {

    @Autowired
    private RepairBatchRepository repairBatchRepository;

    public int findCurrentBatchNumber(String clientName) {

        Optional<RepairBatch> opt = repairBatchRepository.findRepairBatchByClientName(clientName);

        if (opt.isPresent()) {
            RepairBatch repairBatch = opt.get();
            repairBatch.setBatchNumber(repairBatch.getBatchNumber() + 1);
            repairBatch.setActions(AppConstants.ACTIVITY_UPDATE);
            repairBatch.setActionStatus(AppConstants.STATUS_APPROVED);
            repairBatchRepository.save(repairBatch);

            return  repairBatch.getBatchNumber();
        }

        RepairBatch repairBatch = new RepairBatch();
        repairBatch.setBatchNumber(1);
        repairBatch.setClientName(clientName);
        repairBatch.setIntrash(AppConstants.NO);
        repairBatch.setActions(AppConstants.ACTIVITY_CREATE);
        repairBatch.setActionStatus(AppConstants.STATUS_APPROVED);
        repairBatchRepository.save(repairBatch);

        return  repairBatch.getBatchNumber();
    }
}
