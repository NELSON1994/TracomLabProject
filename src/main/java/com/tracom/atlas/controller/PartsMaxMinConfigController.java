package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.PartsMaxMinConfig;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.PartsMaxMinConfigRepository;
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
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nelson
 */

@RestController
@RequestMapping(value = "/partsMaxMinConfigs")
public class PartsMaxMinConfigController extends ChasisResource<PartsMaxMinConfig, Long, UfsEdittedRecord> {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    @Autowired
    private PartsMaxMinConfigRepository partsMaxMinConfigRepository;
    private static final Logger logger = LoggerFactory.getLogger(PartsMaxMinConfigController.class);

    public PartsMaxMinConfigController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<PartsMaxMinConfig>> upload(@RequestParam("File") MultipartFile file) throws ResourceNotFoundException, IOException {
        Map<String, Object> map = new HashMap<>();
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
            String Min;
            String Max;

            DataFormatter d = new DataFormatter();
            try {
                description = d.formatCellValue(row.getCell(1));
                part_number = d.formatCellValue(row.getCell(0));
                Min = d.formatCellValue(row.getCell(2));
                Max = d.formatCellValue(row.getCell(3));
            } catch (NumberFormatException e) {
                description = d.formatCellValue(row.getCell(1));
                part_number = d.formatCellValue(row.getCell(2));
                Min = String.valueOf(d.formatCellValue(row.getCell(2)));
                Max = String.valueOf(d.formatCellValue(row.getCell(3)));
            }
            PartsMaxMinConfig p = new PartsMaxMinConfig();
            p.setPartnumber(part_number);
            p.setPartdescription(description);
            p.setMinimumlimit(Integer.valueOf(Min));
            p.setMaximumlimit(Integer.valueOf(Max));
            p.setActionStatus(Constants.STATUS_UNAPPROVED);
            p.setAction(Constants.ACTION_CREATED);
            p.setIntrash(Constants.INTRASH_NO);
            Date f = new java.sql.Date(System.currentTimeMillis());
            p.setCreationDate(f).toString();
            map.put("content", partsMaxMinConfigRepository.save(p));
            responseWrapper.setData(map);
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Uploaded Successfully");
            workbook.close();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }

    @GetMapping("/SampleExcell")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("PartsMaxMinSample.xlsx");

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
