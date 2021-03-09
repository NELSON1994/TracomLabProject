package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.PartsIssued;
import com.tracom.atlas.entity.PartsRequsition;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.PartsIssuedRepository;
import com.tracom.atlas.repository.PartsRequsitionRepository;
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
import java.util.HashMap;
import java.util.Map;


/**
 * @author Nelson
 */

@RestController
@RequestMapping( value = "/issueParts" )
public class PartsIssuedController  extends ChasisResource<PartsIssued, Long, UfsEdittedRecord> {

    public PartsIssuedController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }
    @Autowired
    private PartsRequsitionRepository partsRequsitionRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    @Autowired
    private PartsIssuedRepository partsIssuedRepository;

    @RequestMapping( value = "/partsIssued", method = RequestMethod.POST )
    public ResponseEntity<ResponseWrapper<PartsRequsition>> partsIssued(@RequestParam( "File" ) MultipartFile file,
                                                                             @RequestParam("dateIssued") String dateIssued) throws IOException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> partsIssued = new HashMap<>();

        String uploadPath = environment.getProperty("file.upload-dir");
        String fileName_ = fileStorageService.storeFile(file);
        String fullFilePath = uploadPath + "/" + fileName_;

        XSSFWorkbook workbookk = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheett = workbookk.getSheetAt(0);
        int lenn = worksheett.getPhysicalNumberOfRows();
        for (int i = 0; i < lenn ; i++ ) {
            XSSFRow row = worksheett.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String partnumber;
            String quantity;
            DataFormatter d = new DataFormatter();
            try {
                partnumber = d.formatCellValue(row.getCell(0));
                quantity = d.formatCellValue(row.getCell(1));
            } catch (NullPointerException e) {
                partnumber = String.valueOf(d.formatCellValue(row.getCell(0)));
                quantity = String.valueOf(d.formatCellValue(row.getCell(1)));
            }

            PartsIssued partsRequsition = new PartsIssued();
            partsRequsition.setDateIssued(dateIssued);
            partsRequsition.setPartsIssued(Integer.parseInt(quantity));
            partsRequsition.setPartNumber(partnumber);
            partsRequsition.setAction(Constants.ACTION_CREATED);
            partsRequsition.setActionStatus(Constants.STATUS_UNAPPROVED);
            partsRequsition.setCreationDate(new java.sql.Date(System.currentTimeMillis()));
            partsRequsition.setIntrash(Constants.INTRASH_NO);
            partsIssuedRepository.save(partsRequsition);
            partsIssued.put("content" , partsIssuedRepository.save(partsRequsition));

            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Parts Issued Upload successfull");
            responseWrapper.setData(partsIssued);
            workbookk.close();
        }
        return  ResponseEntity.ok().body(responseWrapper);

    }
    @GetMapping("/SampleFile")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("PartsIssuedSample.xlsx");

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
