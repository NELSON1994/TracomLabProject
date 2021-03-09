package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.*;
import com.tracom.atlas.repository.PartsMaxMinConfigRepository;
import com.tracom.atlas.repository.PartsRequsitionRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.service.FileStorageService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.*;

/**
 * @author Nelson
 */
@RestController
@RequestMapping( value = "/requestParts" )
public class PartsRequsitionController  extends ChasisResource<PartsRequsition, Long, UfsEdittedRecord> {
    public PartsRequsitionController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    @Autowired
    private PartsRequsitionRepository partsRequsitionRepository;

    @Autowired
    private PartsMaxMinConfigRepository partsMaxMinConfigRepository;

    @RequestMapping( value = "/Bulkrequest", method = RequestMethod.POST )
    public ResponseEntity<ResponseWrapper<PartsRequsition>> partsBulkRequest(@RequestParam( "File" ) MultipartFile file,
                                                                             @RequestParam("dateRequested") String dateRequested) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> partsIssued = new HashMap<>();
        String fileName = file.getOriginalFilename();
        fileStorageService.storeFile(file);
        String uploadPath = environment.getProperty("file.upload-dir");
        String fullPathh = uploadPath + "/" + fileName;
        XSSFWorkbook workbookk = new XSSFWorkbook(fullPathh);
        XSSFSheet worksheett = workbookk.getSheetAt(0);
        int lenn = worksheett.getPhysicalNumberOfRows();
        for (int i = 0; i < lenn ; i++ ) {
            XSSFRow row = worksheett.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String partnumber;
            String partdescription;
            String quantity;
            DataFormatter d = new DataFormatter();
            try {
                partnumber = d.formatCellValue(row.getCell(0));
                partdescription = d.formatCellValue(row.getCell(1));
                quantity = d.formatCellValue(row.getCell(2));
            } catch (NullPointerException e) {
                partnumber = d.formatCellValue(row.getCell(0));
                partdescription = d.formatCellValue(row.getCell(1));
                quantity = String.valueOf(d.formatCellValue(row.getCell(2)));
            }
            PartsRequsition partsRequsition = new PartsRequsition();
            partsRequsition.setDateRequested(dateRequested);
            partsRequsition.setAction(Constants.ACTION_CREATED);
            partsRequsition.setPartnumber(partnumber);
            partsRequsition.setQuantity(quantity);
            partsRequsition.setPartdescription(partdescription);
            partsRequsition.setActionStatus(Constants.STATUS_UNAPPROVED);
            partsRequsition.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
            partsRequsition.setIntrash(Constants.INTRASH_NO);

            partsRequsitionRepository.save(partsRequsition);
            partsIssued.put("content" , partsRequsitionRepository.save(partsRequsition));

            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Parts Requesition successfull");
            responseWrapper.setData(partsIssued);
            workbookk.close();
        }
            return  ResponseEntity.ok().body(responseWrapper);

    }

    @GetMapping("/SampleExcell")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("PartsRequsitionSample.xlsx");

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
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
