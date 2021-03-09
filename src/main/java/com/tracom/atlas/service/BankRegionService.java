package com.tracom.atlas.service;


import com.tracom.atlas.repository.BankRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankRegionService {

    @Autowired
    private BankRegionRepository bankRegionRepository;
}
