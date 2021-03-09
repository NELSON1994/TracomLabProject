package com.tracom.atlas.service;

import com.tracom.atlas.repository.RegionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionsService {

    @Autowired
    private RegionsRepository regionsRepository;


}
