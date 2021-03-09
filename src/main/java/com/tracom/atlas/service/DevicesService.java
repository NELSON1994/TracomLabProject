package com.tracom.atlas.service;

import com.tracom.atlas.entity.Devices;
import com.tracom.atlas.repository.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DevicesService {
    @Autowired
    private DevicesRepository devicesRepository;

    public Optional<List<Devices>> findStatus(String status, String devowner){
        return devicesRepository.findByDeviceownerAndActionStatus(devowner , status);
    }

    public Optional<List<Devices>> findActionStatus(String status){
        return devicesRepository.findByActionStatus(status);
    }
    public Optional<List<Devices>> findByContractStatus(String status, String devowner){
        return devicesRepository.findDevicesByDeviceownerAndDeviceContractStatus(status,devowner);
    }

    public Optional<List<Devices>> findByWarrantyStatus(String status, String devowner){
        return devicesRepository.findDevicesByDeviceownerAndDeviceContractStatus(status,devowner);
    }



    public Optional<List<Devices>> findDevicesBetweenDate(Date from , Date to){
        return devicesRepository.findDevicesByCreationDateBetween(from , to);
    }

    public Optional<List<Devices>> findDevicesByDeviceownerAndDateBetweenDate(String deviceowner , Date from , Date to){
        return devicesRepository.findDevicesByDeviceownerAndCreationDateBetween(deviceowner , from , to);
    }

}
