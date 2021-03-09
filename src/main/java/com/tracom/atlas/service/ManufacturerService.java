package com.tracom.atlas.service;


import com.tracom.atlas.entity.Manufacturers;
import com.tracom.atlas.repository.ManufacturersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturersRepository manufacturersRepository;


    //get manufacturers by action status
    public Optional<List<Manufacturers>> findStatus(String status){
        return manufacturersRepository.findManufacturersByActionStatus(status);
    }

    //get manufacturers by action status
    public Optional<List<Manufacturers>> findIntrash(String intrash){
        return manufacturersRepository.findManufacturersByIntrash(intrash);
    }


    public Optional<List<Manufacturers>> findManufacturersBetweenDate(Date from , Date to){
        return manufacturersRepository.findManufacturersByCreationDateBetween(from , to);
    }

}
