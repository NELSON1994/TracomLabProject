package com.tracom.atlas.controller;


import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Manufacturers;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.ManufacturersRepository;
import com.tracom.atlas.service.ManufacturerService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Nelson
 */
@RestController
@RequestMapping(value = "/manufacturers")
public class ManufacturersController extends ChasisResource<Manufacturers, Long, UfsEdittedRecord> {

    @Autowired
    private ManufacturersRepository manufacturersRepository;

    @Autowired
    private ManufacturerService manufacturerService;

    public ManufacturersController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }


    @RequestMapping(value = "manufacturerUnpproved", method = RequestMethod.GET)
    public ResponseWrapper findunapproved() {
        List<Manufacturers> manufacturer = manufacturerService.findStatus(Constants.STATUS_UNAPPROVED).get();

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(manufacturer.iterator());
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }

    @RequestMapping(value = "manufacturerAnpproved", method = RequestMethod.GET)
    public ResponseWrapper findapproved() {
        List<Manufacturers> manufacturer = manufacturerService.findStatus(Constants.STATUS_APPROVED).get();

        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(manufacturer);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }

    @RequestMapping(value = "manufacturerIntrash", method = RequestMethod.GET)
    public ResponseWrapper findIntrash() {
        List<Manufacturers> manufacturer = manufacturerService.findIntrash(Constants.INTRASH_YES).get();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(manufacturer);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }

    @GetMapping(value = "/filterByDate")
    public ResponseWrapper manufacturersBetweenDates(@RequestParam("from") String from,
                                                     @RequestParam("to") String to) throws ParseException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String pattern = "yyyy-MM-dd";
        java.util.Date too = new SimpleDateFormat(pattern).parse(to);
        java.util.Date fromm = new SimpleDateFormat(pattern).parse(from);
        Date one = new Date(too.getTime());
        Date two = new Date(fromm.getTime());
        List<Manufacturers> manufacturer = manufacturerService.findManufacturersBetweenDate(two, one).get();
        if (manufacturer.isEmpty()) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage("No manufacturers found for the specified date interval");
        } else {
            responseWrapper.setMessage("Successfully filtered by date");
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setData(manufacturer);
        }
        return responseWrapper;
    }


}





