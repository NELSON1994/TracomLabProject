package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.entity.WhiteList;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.WhiteListRepository;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
/**
 * @author Nelson
 */

@RestController
@RequestMapping(value="/whitelist")
public class WhiteListController  extends ChasisResource<WhiteList, Long, UfsEdittedRecord> {

    public WhiteListController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
    @Autowired
    private WhiteListRepository whiteListRepository;
    @RequestMapping(value ="/uploadNewdevices" , method = RequestMethod.POST)
    public void uploadNewDevices(@RequestParam("Filee") MultipartFile file) throws ResourceNotFoundException, IOException {
        if (file.isEmpty()) {
            new ResourceNotFoundException("Select a file to upload please");
        }
        InputStream stream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(stream);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 0; i <= 10; i += 1)
        {
            XSSFRow row = worksheet.getRow(i);
            if (row.getRowNum() == 0)
            {
                continue;
            }
            String partnumber = row.getCell(3).getStringCellValue();
            int serialnumberr = ((int) row.getCell(4).getNumericCellValue());
            String articlenumber = row.getCell(2).getStringCellValue();
            String description = row.getCell(7).getStringCellValue();
            int batchnumber = ((int) row.getCell(6).getNumericCellValue());
            String deliverydate = row.getCell(5).getStringCellValue();
            WhiteList whiteList;
            whiteList = new WhiteList();
            whiteList.setPartnumber(partnumber);
            whiteList.setSerialnumber(serialnumberr);
            whiteList.setBatchnumber(batchnumber);
            whiteList.setDescription(description);
            whiteList.setArticle_number(articlenumber);
            whiteList.setDeliverydate(deliverydate);
            whiteList.setActionStatus(Constants.STATUS_UNAPPROVED);
            whiteList.setAction(Constants.ACTION_CREATED);
            whiteList.setIntrash(Constants.INTRASH_NO);
            whiteList.setCreationDate(new Date());
            whiteListRepository.save(whiteList);
        }

    }


}
