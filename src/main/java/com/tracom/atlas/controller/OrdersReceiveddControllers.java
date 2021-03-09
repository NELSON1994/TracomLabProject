package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Orders;
import com.tracom.atlas.entity.OrdersReceiveddd;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.OrdersReceiveddRepository;
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
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/ordersReceivedd")
public class OrdersReceiveddControllers extends ChasisResource<OrdersReceiveddd, Long, UfsEdittedRecord> {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    @Autowired
    private OrdersRepository ordersRepository;

    private OrdersReceiveddRepository ordersReceiveddRepository;


    private static final Logger logger = LoggerFactory.getLogger(OrdersReceiveddControllers.class);

    public OrdersReceiveddControllers(LoggerService loggerService, EntityManager entityManager, OrdersReceiveddRepository ordersReceiveddRepository) {

        super(loggerService, entityManager);
        this.ordersReceiveddRepository = ordersReceiveddRepository;
    }


    @RequestMapping(value = "/receivedOrders", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<Orders>> receivedOrders(@RequestParam("File") MultipartFile file,
                                                                  @RequestParam("pordernumberr") int pordernumberr,
                                                                  @RequestParam("receiveddate") String receiveddate) throws IOException {
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String po = String.valueOf(pordernumberr);
        fileStorageService.storeFile(file);
        String fileName = file.getOriginalFilename();
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
                partnumber = d.formatCellValue(row.getCell(1));
                quantityreceived = String.valueOf(d.formatCellValue(row.getCell(2)));
            }
            Orders orders = ordersRepository.findByPartnumberAndPonumber(partnumber, po).get();
            OrdersReceiveddd ordersReceiveddd = new OrdersReceiveddd();
            ordersReceiveddd.setActionStatus(Constants.STATUS_UNAPPROVED);
            ordersReceiveddd.setAction(Constants.ACTION_UPDATE);
            ordersReceiveddd.setIntrash(Constants.INTRASH_NO);
            ordersReceiveddd.setDatereceived(receiveddate);
            ordersReceiveddd.setPartnumber(partnumber);
            ordersReceiveddd.setPonumber(po);
            ordersReceiveddd.setQtreceived(quantityreceived);
            ordersReceiveddd.setQtpurchased(orders.getQtpurchased());
            ordersReceiveddd.setBalance(Integer.valueOf(ordersReceiveddd.getQtpurchased()) - Integer.valueOf(quantityreceived));
            ordersReceiveddRepository.save(ordersReceiveddd);
            responseWrapper.setData(ordersReceiveddRepository.save(ordersReceiveddd));
            responseWrapper.setMessage("The orders received  updated succesfully");
            responseWrapper.setCode(HttpStatus.OK.value());

        }
        workbook.close();

        return ResponseEntity.ok().body(responseWrapper);
    }

    @GetMapping("/SampleExcell")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("ReceivedOrdersSample.xlsx");

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
