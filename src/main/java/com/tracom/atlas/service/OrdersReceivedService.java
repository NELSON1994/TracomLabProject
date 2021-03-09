package com.tracom.atlas.service;


import com.tracom.atlas.controller.OrderingController;
import com.tracom.atlas.repository.OrdersReceivedRepository;
import com.tracom.atlas.repository.OrdersRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

/**
 * @author momondi maurice
 *
 */

@Service
public class OrdersReceivedService {

    @Autowired
    public OrdersRepository ordersRepository;

    @Autowired
    public OrdersReceivedRepository ordersReceivedRepository;


    private static final Logger logger = LoggerFactory.getLogger(OrderingController.class);



    }
