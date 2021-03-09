package com.tracom.atlas.service;


import com.tracom.atlas.entity.Parts;
import com.tracom.atlas.repository.PartsRepository;
import ke.axle.chassis.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author momondi
 * parts Service
 */

@Service
@Transactional(propagation= Propagation.REQUIRED)
public class PartsService {


    @Autowired
    private PartsRepository partsRepository;

    Date created;
    int totalparts = 0;

    List<Parts> quarterpart = new ArrayList<>();
    List<Parts> quarterpart2 = new ArrayList<>();
    List<Parts> quarterpart3 = new ArrayList<>();


    /**
     * first quarter(January-March)
     *
     * @return list of parts first quarter parts only
     */
    public Page<Parts> getAllQuarter1(Pageable pg) {

        List<Parts> parts = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts part : parts) {
            created = part.getCreatedOn();
            LocalDate localDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int monthquarter = 1 + localDate.getMonthValue();
            if (monthquarter < 5) {
                quarterpart.add(part);

            }
        }

        Page<Parts> pageResponse = new PageImpl<>(quarterpart, pg, quarterpart.size());
        return pageResponse;
    }

    /**
     * second quarter(April-July)
     *
     * @return list of parts ordered for second quarter
     */
    public Page<Parts> getAllQuarter2(Pageable pg) {


        List<Parts> parts = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts part : parts) {
            created = part.getCreatedOn();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(created);
            int monthquarter = 1 + calendar.get(Calendar.MONTH);
            if (monthquarter > 4 && monthquarter < 9) {

                quarterpart2.add(part);
            }

        }

        Page<Parts> pageResponse = new PageImpl<>(quarterpart2, pg, quarterpart2.size());
        return pageResponse;
    }

    /**
     * third quarter(August- December)
     *
     * @return a list of only parts ordered for third quarter
     */

    public Page<Parts> getAllQuarter3(Pageable pg) {


        List<Parts> parts = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts part : parts) {
            created = part.getCreatedOn();
            LocalDate localDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int monthquarter = 1 + localDate.getMonthValue();
            if (monthquarter > 8 && monthquarter < 13) {

                quarterpart3.add(part);
            }


        }

        Page<Parts> pageResponse = new PageImpl<>(quarterpart3, pg, quarterpart3.size());
        return pageResponse;

    }


    public Page<Integer> totalPartscount(Pageable pg) {


        int totalparts = 0;
        String message=" ";
        List<Parts> all = partsRepository.findPartsByIntrash(AppConstants.NO);
        totalparts = all.size();
        List<Integer> data = new ArrayList<>();
        if(totalparts<1000) {
            data.add(totalparts);
            message="Hello, You are running out of parts  you currently have "+totalparts+" .Please DO A REORDER!";
            int result = Integer.parseInt(message);
            data.add(result);
        }

        else{
            message="Hello, You still have "+totalparts+" parts remaining";
            int result = Integer.parseInt(message);
            data.add(result);
        }


        Page<Integer> pageResponse = new PageImpl<>(data, pg, data.size());
        return pageResponse;
    }




    public Page<Parts> getUnapproved(Pageable pg) {
        List<Parts> unapproved = partsRepository.findAllByIntrashAndActionStatus(AppConstants.NO, AppConstants.STATUS_UNAPPROVED);


        Page<Parts> pageResponse = new PageImpl<>(unapproved, pg, unapproved.size());

        return pageResponse;
    }


    public List<Parts> getAllQuarters2() {


        List<Parts> partsq = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts partq : partsq) {
            created = partq.getCreatedOn();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(created);
            int monthquarter = 1 + calendar.get(Calendar.MONTH);
            if (monthquarter > 4 && monthquarter < 9) {

                quarterpart2.add(partq);


            }


        }
        return quarterpart2;
    }


    public List<Parts> getAllQuarter1() {

        List<Parts> parts1 = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts part1 : parts1) {
            created = part1.getCreatedOn();
            LocalDate localDate1 = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int monthquarter = 1 + localDate1.getMonthValue();
            if (monthquarter < 5) {
                quarterpart.add(part1);

            }
        }

        return quarterpart;
    }


    public List<Parts> getAllQuarter3() {

        List<Parts> parts3 = partsRepository.findPartsByIntrash(AppConstants.NO);

        for (Parts part3 : parts3) {
            created = part3.getCreatedOn();
            LocalDate localDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int monthquarter = 1 + localDate.getMonthValue();
            if (monthquarter > 8 && monthquarter < 13) {

                quarterpart3.add(part3);
            }


        }
        return quarterpart3;
    }



    public Optional<List<Parts>> findPartsBetweenDate(java.sql.Date from , java.sql.Date to){
        return partsRepository.findPartsByCreatedOnBetween(from,to);
    }

    public List<Parts> findByDescription(String description){
        return partsRepository.findPartsByDescriptionIsContaining(description);
    }

    public List<Parts> findByManufacturer(String manufacturer){
        return partsRepository.findPartsByManufacturerNameContaining(manufacturer);
    }

}
