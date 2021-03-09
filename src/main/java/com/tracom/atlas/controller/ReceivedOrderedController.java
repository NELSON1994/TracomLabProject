package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Orders;
import com.tracom.atlas.entity.OrdersReceived;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.OrdersReceivedRepository;
import com.tracom.atlas.repository.OrdersRepository;
import com.tracom.atlas.service.FileStorageService;
import com.tracom.atlas.service.OrdersReceivedService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maurice
 * process order sent and recived. Get the total remainig order not received from the supplier
 */

@RestController
@RequestMapping(path = "/received_orders")
public class ReceivedOrderedController extends ChasisResource<OrdersReceived, Long, UfsEdittedRecord> {
    public ReceivedOrderedController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Autowired
    public OrdersReceivedService ordersReceivedService;

    @Autowired
    public OrdersRepository ordersRepository;
    @Autowired
    public OrdersReceivedRepository ordersReceivedRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    Logger logger = LoggerFactory.getLogger(ReceivedOrderedController.class);

    /**
     *
     * @param file
     * @param pordernumberr
     * @param receiveddate
     * @return saves order received
     * @throws IOException
     */


    @RequestMapping(value = "/received_Orders", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<OrdersReceived>> receivedOrders(@RequestParam("File") MultipartFile file,
                                                                          @RequestParam("pordernumberr") int pordernumberr,
                                                                          @RequestParam("receiveddate") String receiveddate) throws IOException {
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String po = String.valueOf(pordernumberr);
        fileStorageService.storeFile(file);
        String fileName = file.getOriginalFilename();
        logger.info("=========filename========={}", fileName);
        logger.info("=========poreeb||||||||||||||@@@@@@@@@@@@@@@@========={}", pordernumberr);
        String uploadPath = environment.getProperty("file.upload-dir");
        String fullPath = uploadPath + "/" + fileName;
        XSSFWorkbook workbook = new XSSFWorkbook(fullPath);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        int len = worksheet.getPhysicalNumberOfRows();
        for (int i = 0; i < len; i++) {
            XSSFRow row = worksheet.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String partnumber;
            String quantityreceived;

            DataFormatter d = new DataFormatter();
            try {
                partnumber = d.formatCellValue(row.getCell(0));
                quantityreceived = d.formatCellValue(row.getCell(1));

            } catch (NullPointerException e) {
                partnumber = d.formatCellValue(row.getCell(0));
                quantityreceived = String.valueOf(d.formatCellValue(row.getCell(1)));
            }

            OrdersReceived orders = new OrdersReceived();
            // OrdersReceived orders = ordersReceivedRepository.findAllByIntrash(AppConstants.NO).get();
            orders.setActionStatus(Constants.STATUS_UNAPPROVED);
            orders.setAction(Constants.ACTION_UPDATE);
            orders.setDateReceived(receiveddate);
            orders.setQtreceived(quantityreceived);
            orders.setIntrash(Constants.INTRASH_NO);
            orders.setPartnumber(partnumber);
            ordersReceivedRepository.save(orders);
            responseWrapper.setData(ordersReceivedRepository.save(orders));
            responseWrapper.setMessage("The orders received  updated succesfully");
            responseWrapper.setCode(HttpStatus.OK.value());

        }
        workbook.close();

        return ResponseEntity.ok().body(responseWrapper);
    }

    /**
     *
     * @param ordernum
     * @return orders not received from the supplier
     */

    @RequestMapping("/orders_not_received/{ordernum}")
    public ResponseEntity<ResponseWrapper> getOrderBalance(@RequestParam String ordernum) {
        ResponseWrapper response = new ResponseWrapper();
    List<OrdersReceived> receivedord = ordersReceivedRepository.findAllByIntrashAndPonumber(AppConstants.NO, ordernum);
        List<Orders> ordernotreceived = new ArrayList<>();
    List<Orders> orders = ordersRepository.findByPonumberAndIntrash(ordernum, AppConstants.NO);

    if(receivedord.size()==orders.size()){
        response.setData(receivedord);
        response.setMessage("We are pleased to inform you that all your orders were delivered.Thank you");
        response.setCode(200);
    }
    else if(orders.size()>receivedord.size()){

        for(Orders or: orders){
            if(!receivedord.contains(or)){
                ordernotreceived.add(or);
            }
        }

        response.setData(ordernotreceived);
        response.setCode(200);
        response.setMessage("The following orders were not received "+ordernotreceived.size()+" Note more details provided");

    }

    else if(orders.size()==0 && receivedord.size()==0){

        response.setData("Sorry, the order cant be found!");
        response.setMessage("Contact logistics manager  for more information");
        response.setCode(200);


    }


        return ResponseEntity.ok().body(response);
}


}
