package com.tracom.atlas.controller;


import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Customers;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.CustomersRepository;
import com.tracom.atlas.service.CustomerService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RequestMapping( value = "/customers" )
public class CustomersController extends ChasisResource<Customers, Long, UfsEdittedRecord> {

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CustomerService customerService;

    public CustomersController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @RequestMapping( value = "customersUnapproved", method = RequestMethod.GET)
    public ResponseWrapper findunapproved(){
      List <Customers> customers = customerService.findStatus(Constants.STATUS_UNAPPROVED).get();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(customers);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }
    @RequestMapping( value = "customersApproved", method = RequestMethod.GET)
    public ResponseWrapper findapproved(){
        List <Customers> customers = customerService.findStatus(Constants.STATUS_APPROVED).get();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(customers);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }
    @RequestMapping( value = "customersIntrash", method = RequestMethod.GET)
    public ResponseWrapper findIntrash(){
        List <Customers> customers = customerService.findIntrash(Constants.INTRASH_YES).get();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(customers);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success");
        return responseWrapper;
    }


    @GetMapping( value = "/filterByDate" )
    public ResponseWrapper customersBetweenDates(@RequestParam( "from" ) String from,
                                               @RequestParam( "to" ) String to) throws ParseException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String pattern = "yyyy-MM-dd";
        java.util.Date too = new SimpleDateFormat(pattern).parse(to);
        java.util.Date fromm = new SimpleDateFormat(pattern).parse(from);
        Date one = new Date(too.getTime());
        Date two = new Date(fromm.getTime());
        List<Customers> customers = customerService.findCustomersBetweenDate(two , one).get();
        if (customers.isEmpty()) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage("No customers found for the specified date interval");
        } else {
            responseWrapper.setMessage("Successfully filtered by date");
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setData(customers);
        }
        return responseWrapper;
    }

}
