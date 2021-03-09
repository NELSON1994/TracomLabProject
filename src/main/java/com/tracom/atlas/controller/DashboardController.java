package com.tracom.atlas.controller;


import com.tracom.atlas.service.DashboardService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "repair-cards",method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> repairCards() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(dashboardService.getRepairCars());

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @RequestMapping(value = "repair-level",method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> repairLevels() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(dashboardService.getRepairLevels());

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }

    @RequestMapping(value = "repair-per-customer",method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper> repairPerCustomer() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setData(dashboardService.getRepairPerCustomers());

        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
}
