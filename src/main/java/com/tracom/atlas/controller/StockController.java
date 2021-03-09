package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.*;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.*;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

/**
 * @author Nelson
 */
@RestController
@RequestMapping(value = "/stocks")
public class StockController extends ChasisResource<Stocks, Long, UfsEdittedRecord> {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private PartsMaxMinConfigRepository partsMaxMinConfigRepository;
    @Autowired
    private PartsIssuedRepository partsIssuedRepository;

    @Autowired
    private OrdersReceiveddRepository ordersReceiveddRepository;

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    public StockController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @RequestMapping(value = "/viewStocks", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Stocks>> stocks(Pageable pg) throws ResourceNotFoundException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Stocks> stocks = new ArrayList<>();
        List<PartsMaxMinConfig> partsMaxMinConfigs = partsMaxMinConfigRepository.findAll();
        for (PartsMaxMinConfig p : partsMaxMinConfigs) {
            Stocks stocks1 = new Stocks();
            List<OrdersReceiveddd> ordersReceiveddd = ordersReceiveddRepository.findByPartnumberAndIntrashOrderByDatereceivedDesc(p.getPartnumber(), Constants.INTRASH_NO);
            List<PartsIssued> partsIssueds = partsIssuedRepository.findByPartNumberAndIntrashOrderByDateIssuedDesc(p.getPartnumber(), Constants.INTRASH_NO);

            OrdersReceiveddd op = ordersReceiveddd.get(0);
            PartsIssued ppp = partsIssueds.get(0);
            stocks1.setIssuedparts(String.valueOf(ppp.getPartsIssued()));
            stocks1.setAction(Constants.ACTION_CREATED);
            stocks1.setActionStatus(Constants.STATUS_UNAPPROVED);
            stocks1.setIntrash(Constants.INTRASH_NO);
            stocks1.setMaxlimit(p.getMaximumlimit());
            stocks1.setMinlimit(p.getMinimumlimit());
            stocks1.setPartnumber(p.getPartnumber());
            stocks1.setPartdescription(p.getPartdescription());
            stocks1.setCreationDate(new Date(System.currentTimeMillis()));
            stocks1.setOpeningstock(stocks1.getAvailablestock());
            stocks1.setStockin(Double.valueOf(op.getQtreceived()));
            Calendar cal = Calendar.getInstance();
            int maximum = cal.getActualMaximum(Calendar.DATE);
            int minimum = cal.getActualMinimum(Calendar.DATE);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            if (minimum == dayOfMonth) {

                double available = stocks1.getOpeningstock() + stocks1.getStockin() - Double.valueOf(stocks1.getIssuedparts());
                stocks1.setAvailablestock(available);
            } else {
                double available = stocks1.getStockin() - Double.valueOf(stocks1.getIssuedparts());
                ;
                stocks1.setAvailablestock(available);
            }
            double available = stocks1.getAvailablestock();
            double min = stocks1.getMinlimit();
            double max = stocks1.getMaxlimit();
            if (available < min) {
                stocks1.setReoderStatus(Constants.RE_ORDER_YES);
            } else if (available <= max) {
                stocks1.setReoderStatus(Constants.RE_ORDER_SOON);
            } else {
                stocks1.setReoderStatus(Constants.RE_ORDER_NO);
            }
            stocks.add(stocks1);
            stockRepository.save(stocks1);
        }
        stockRepository.saveAll(stocks);

        Map<String, Object> st = new HashMap<>();
        st.put("content", stocks);
        responseWrapper.setMessage("success stock  viewed");
        responseWrapper.setCode(HttpStatus.OK.value());
        Page<Stocks> pageResponse = new PageImpl<>(stocks, pg, stocks.size());
        responseWrapper.setData(pageResponse);
        return ResponseEntity.ok().body(responseWrapper);
    }

    @RequestMapping(value = "/partsToBeReordered", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Stocks>> getPrtsToBeReordered(Pageable pg) {
        List<Stocks> st = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Stocks> stocks = stockRepository.findByReoderStatusOrderByCreationDateDesc(Constants.RE_ORDER_YES);
        Page<Stocks> pageResponse = new PageImpl<>(stocks, pg, stocks.size());
        responseWrapper.setData(pageResponse);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("Success ");
        return ResponseEntity.ok().body(responseWrapper);
    }
}
