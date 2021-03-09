package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Orders;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.OrdersRepository;
import com.tracom.atlas.service.FileStorageService;
import ke.axle.chassis.ChasisResource;
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
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nelson
 */
@RestController
@RequestMapping(value = "/orders")
public class OrderingController extends ChasisResource<Orders, Long, UfsEdittedRecord> {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;

    private OrdersRepository ordersRepository;


    private static final Logger logger = LoggerFactory.getLogger(OrderingController.class);

    public OrderingController(LoggerService loggerService, EntityManager entityManager, OrdersRepository ordersRepository) {

        super(loggerService, entityManager);
        this.ordersRepository = ordersRepository;
    }

    @RequestMapping(value = "/sentOrders", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<Orders>> uploadOrders(@RequestParam("File") MultipartFile file,
                                                                @RequestParam("pordernumberr") int pordernumberr,
                                                                @RequestParam("datepurchased") String datepurchased) throws ResourceNotFoundException, IOException {
        Map<String, Object> map = new HashMap<>();
        String poNo = String.valueOf(pordernumberr);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        fileStorageService.storeFile(file);
        String fileName = file.getOriginalFilename();
        logger.info("=========filename========={}", fileName);
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
            String description;
            String part_number;
            String qt_purchased;

            DataFormatter d = new DataFormatter();
            try {
                description = d.formatCellValue(row.getCell(3));
                part_number = d.formatCellValue(row.getCell(0));
                qt_purchased = d.formatCellValue(row.getCell(2));
            } catch (NumberFormatException e) {
                description = d.formatCellValue(row.getCell(5));
                part_number = d.formatCellValue(row.getCell(1));
                qt_purchased = String.valueOf(d.formatCellValue(row.getCell(3)));
            }
            Orders orders = new Orders();
            orders.setDescription(description);
            orders.setPartnumber(part_number);
            orders.setPonumber(poNo);
            orders.setDtpurchased(datepurchased);
            orders.setQtpurchased(qt_purchased);
            orders.setActionStatus(Constants.STATUS_UNAPPROVED);
            orders.setAction(Constants.ACTION_CREATED);
            orders.setIntrash(Constants.INTRASH_NO);
            Date f = new java.sql.Date(System.currentTimeMillis());
            orders.setCreationDate(f);
            map.put("content", ordersRepository.save(orders));
            responseWrapper.setData(map);
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Uploaded Successfully");
            workbook.close();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }

    @RequestMapping(value = "/firstQuater", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Orders>> getFirstQuater(Pageable pg) {
        Calendar cal = Calendar.getInstance();
        int yearr = cal.get(Calendar.YEAR);
        String year = String.valueOf(yearr);
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String first = "01";
        String middle = "02";
        String last = "03";
        String start = year.concat(first);
        String middlee = year.concat(middle);
        String end = year.concat(last);
        List<Orders> orders1 = null;
        try {
            orders1 = ordersRepository.findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(start, middlee, end).get();
            map.put("content", orders1.iterator());
            //responseWrapper.setData(map);
            Page<Orders> pageResponse = new PageImpl<>(orders1, pg, orders1.size());
            responseWrapper.setData(pageResponse);
            responseWrapper.setMessage(" First quarter orders generated for the year :" + year);
            responseWrapper.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage(" First quarter orders for the year :" + year + " not found");
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }

    @RequestMapping(value = "/secondQuater", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Orders>> getSecondQuater(Pageable pg) {
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Calendar cal = Calendar.getInstance();
        int yearr = cal.get(Calendar.YEAR);
        String year = String.valueOf(yearr);
        String first = "04";
        String middle = "05";
        String last = "06";
        String start = year.concat(first);
        String middlee = year.concat(middle);
        String end = year.concat(last);
        List<Orders> orderss = null;
        try {
            orderss = ordersRepository.findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(start, middlee, end).get();
            map.put("content", orderss.iterator());
           // responseWrapper.setData(map);
            Page<Orders> pageResponse = new PageImpl<>(orderss, pg, orderss.size());
            responseWrapper.setData(pageResponse);
            responseWrapper.setMessage(" Second quarter orders generated for the year :" + year);
            responseWrapper.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage(" Second quarter orders for the year :" + year + " not found");
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }

    @RequestMapping(value = "/thirdQuater", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Orders>> getThirdQuater(Pageable pg) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Calendar cal = Calendar.getInstance();
        int yearr = cal.get(Calendar.YEAR);
        String year = String.valueOf(yearr);
        Map<String, Object> map = new HashMap<>();
        String first = "07";
        String middle = "08";
        String last = "09";
        String start = year.concat(first);
        String middlee = year.concat(middle);
        String end = year.concat(last);
        List<Orders> orders = null;
        try {
            orders = ordersRepository.findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(start, middlee, end).get();
            map.put("content", orders.iterator());
            Page<Orders> pageResponse = new PageImpl<>(orders, pg, orders.size());
            responseWrapper.setData(pageResponse);

            responseWrapper.setMessage(" Third quarter orders generated for the year :" + year);
            responseWrapper.setCode(HttpStatus.OK.value());
        } catch (Exception e) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage(" Third quarter orders for the year :" + year + " not found");
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }

    @RequestMapping(value = "/fourthQuater", method = RequestMethod.GET)
    public ResponseEntity<ResponseWrapper<Orders>> getFourthQuater(Pageable pg) {
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Calendar cal = Calendar.getInstance();
        int yearr = cal.get(Calendar.YEAR);
        String year = String.valueOf(yearr);
        String first = "10";
        String middle = "11";
        String last = "12";
        String start = year.concat(first);
        String middlee = year.concat(middle);
        String end = year.concat(last);
        //List<Orders> orders = ordersRepository.findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(start, middlee, end).get();
        List<Orders> orders = null;
       try {
           orders = ordersRepository.findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(start, middlee, end).get();
            Page<Orders> pageResponse = new PageImpl<>(orders, pg, orders.size());
            responseWrapper.setData(pageResponse);
            responseWrapper.setMessage(" Fourth quarter orders generated for the year :" + year);
            responseWrapper.setCode(HttpStatus.OK.value());

      } catch (Exception e) {
           responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
           responseWrapper.setMessage(" Fourth quarter orders for the year :" + year + " not found");
        }
//        if (orders.size() < 0) {
//            Page<Orders> pageResponse = new PageImpl<>(orders, pg, orders.size());
//            responseWrapper.setData(pageResponse);
//            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
//            responseWrapper.setMessage(" Fourth quarter orders for the year :" + year + " not found");
//        } else {
//            Page<Orders> pageResponse = new PageImpl<>(orders, pg, orders.size());
//            responseWrapper.setData(pageResponse);
//            responseWrapper.setMessage(" Fourth quarter orders generated for the year :" + year);
//            responseWrapper.setCode(HttpStatus.OK.value());
//        }


        return ResponseEntity.ok().body(responseWrapper);
    }

    @GetMapping("/SampleFile")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("SentOrdersSample.xlsx");

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
