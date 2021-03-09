package com.tracom.atlas.service;

import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.repository.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PartHistoryService {

    @Autowired
    private RepairRepository repairRepository;

    public Optional<List<Repair>> findPartsBetweenDate(Date from , Date to){
        return repairRepository.findRepairsByCreatedOnBetween(from, to);
    }

}
