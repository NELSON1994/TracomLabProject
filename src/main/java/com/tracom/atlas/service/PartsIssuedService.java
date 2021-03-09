package com.tracom.atlas.service;

import com.tracom.atlas.entity.Parts;
import com.tracom.atlas.entity.PartsIssued;
import com.tracom.atlas.entity.UfsSysConfig;
import com.tracom.atlas.repository.PartsIssuedRepository;
import com.tracom.atlas.repository.UfsSysConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartsIssuedService {

    private final Logger logger = LoggerFactory.getLogger(PartsIssuedService.class);
    private final PartsIssuedRepository partsIssuedRepository;
    private final UfsSysConfigRepository ufsSysConfigRepository;
    private final NotifyService notifyService;

    public PartsIssuedService(PartsIssuedRepository partsIssuedRepository, UfsSysConfigRepository ufsSysConfigRepository, NotifyService notifyService) {
        this.partsIssuedRepository = partsIssuedRepository;
        this.ufsSysConfigRepository = ufsSysConfigRepository;
        this.notifyService = notifyService;
    }

    @Transactional
    public void issueParts(Parts parts) {
            PartsIssued partsIssued = partsIssuedRepository.findAllByPartNumber(parts.getPartNumber());
            if ((partsIssued != null) && (partsIssued.getPartsIssued() == 0)) {
                int partexisting = partsIssued.getPartsIssued();
                partexisting-=1;
                partsIssued.setPartsIssued(partexisting);
                partsIssuedRepository.save(partsIssued);
            }
    }

}
