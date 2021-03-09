package com.tracom.atlas.controller;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Parts;
import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.PartsRepository;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.service.FileStorageService;
import com.tracom.atlas.service.PartsService;
import com.tracom.atlas.util.PartsReportpdf;
import com.tracom.atlas.util.excelGenerator;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author maurice momondi
 * parts end points
 */


@RestController
@RequestMapping(value = "/parts")
public class PartsController extends ChasisResource<Parts, Long, UfsEdittedRecord> {

    private final PartsService partsService;
    private final RepairRepository repairRepository;
    private final PartsRepository partsRepository;
    private final FileStorageService fileStorageService;
    private final Environment environment;


    public PartsController(LoggerService loggerService, EntityManager entityManager, PartsService partsService, RepairRepository repairRepository, PartsRepository partsRepository, FileStorageService fileStorageService, Environment environment) {
        super(loggerService, entityManager);
        this.partsService = partsService;
        this.repairRepository = repairRepository;
        this.partsRepository = partsRepository;
        this.fileStorageService = fileStorageService;
        this.environment = environment;
    }

//    private static final Logger logger = LoggerFactory.getLogger(PartsController.class);

    /**
     * @return quarter one parts
     */

    @GetMapping("/q1")
    public ResponseEntity<ResponseWrapper<Object>> getQuarter1Parts(Pageable pg) {
        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.partsService.getAllQuarter1(pg));
        return ResponseEntity.ok().body(response);
    }


    /**
     * @return quarter two parts
     */
    @GetMapping("/q2")
    public ResponseEntity<ResponseWrapper<Object>> getQuarter2Parts(Pageable pg) {

        ResponseWrapper wrap = new ResponseWrapper();
        wrap.setData(this.partsService.getAllQuarter2(pg));
        return ResponseEntity.ok().body(wrap);
    }

    /**
     * @return quarter three parts
     */

    @GetMapping("/q3")
    public ResponseEntity<ResponseWrapper<Object>> getQuarter3Parts(Pageable pg) {

        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.partsService.getAllQuarter3(pg));
        return ResponseEntity.ok().body(response);
    }


    /**
     * @param pg
     * @return un approved parts
     */


    @GetMapping("/unapproved")
    public ResponseEntity<ResponseWrapper<Object>> getUnapprovedParts(Pageable pg) {

        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.partsService.getUnapproved(pg));
        return ResponseEntity.ok().body(response);
    }

    /**
     * @return pdf report
     */
    @GetMapping(value = "/pdf_part",
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> PartsReport() throws IOException {
        List<Parts> parts = (List<Parts>) partsRepository.findPartsByIntrash(AppConstants.NO);
        ByteArrayInputStream bis = PartsReportpdf.partsPDFReport(parts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=parts.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    /**
     * @return excel sheet containing parts
     * @throws IOException
     */
    @GetMapping(value = "/xl")
    public ResponseEntity<InputStreamResource> excelPartsReport() throws IOException {
        List<Parts> parts = (List<Parts>) partsRepository.findPartsByIntrash(AppConstants.NO);
        ByteArrayInputStream in = excelGenerator.partsToExcel(parts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Allparts.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * @return quarter 1 report
     * @throws IOException
     */
    @GetMapping(value = "/q1_excel_report")
    public ResponseEntity<InputStreamResource> excelQuater1Report() throws IOException {
        List<Parts> parts1 = (List<Parts>) partsService.getAllQuarter1();
        ByteArrayInputStream in = excelGenerator.partsToExcel(parts1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Quarter One.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * @return quarter 2 parts excel
     * @throws IOException
     */

    @GetMapping(value = "/q2_excel_report")
    public ResponseEntity<InputStreamResource> excelQuater2Report() throws IOException {
        List<Parts> parts1 = (List<Parts>) partsService.getAllQuarters2();
        ByteArrayInputStream in = excelGenerator.partsToExcel(parts1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Quarter_Two.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * @return quarter 3 excel report
     * @throws IOException
     */
    @GetMapping(value = "/q3_excel_report")
    public ResponseEntity<InputStreamResource> excelQuater3Report() throws IOException {
        List<Parts> parts1 = (List<Parts>) partsService.getAllQuarter3();
        ByteArrayInputStream in = excelGenerator.partsToExcel(parts1);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Last quarter_three_parts.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * @return
     */

    @GetMapping(value = "/total_repairs")
    public ResponseWrapper repairs() {
        List<Repair> repairs = repairRepository.findAll();
        int partnumbers = 0;
        for (Repair repair : repairs) {

            partnumbers++;


        }
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setMessage("Successful");
        responseWrapper.setCode(200);
        responseWrapper.setData(partnumbers);

        return responseWrapper;
    }

    /**
     * @param from
     * @param to
     * @return
     * @throws ParseException
     */


    @GetMapping(value = "/filter_by_date")
    public ResponseWrapper partsBetweenDates(@RequestParam("from") String from, @RequestParam("to") String to) throws ParseException {
        ResponseWrapper response = new ResponseWrapper();
        log.info("start date----------------- 11" + from);
        log.info("end date-------------- 12 " + to);

        String datepartern = "yyyy/MM/dd";
        java.util.Date too = new SimpleDateFormat(datepartern).parse(to);
        java.util.Date fro = new SimpleDateFormat(datepartern).parse(from);
        Date S = new Date(too.getTime());
        Date E = new Date(fro.getTime());
        List<Parts> parts = partsService.findPartsBetweenDate(S, E).get();
        log.info("parts---------------------" + parts);
        if (parts.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Part Not found withing the specified date range");
        } else {
            response.setMessage("Success");
            response.setCode(HttpStatus.OK.value());
            response.setData(parts);
        }
        return response;
    }

    /**
     * @param description
     * @return part by description
     * @throws IOException
     */
    @GetMapping(value = "/filter_by_description")
    public ResponseWrapper partsByDescription(@RequestParam("description") String description) throws IOException {
        ResponseWrapper response = new ResponseWrapper();
        List<Parts> parts = partsService.findByDescription(description);
        if (parts.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No part found with such description!");
        } else {
            response.setMessage("Success");
            response.setCode(HttpStatus.OK.value());
            response.setData(parts);
        }
        return response;
    }

    /**
     * @param manufacturer
     * @return part by manufacturer
     * @throws IOException
     */

    @GetMapping(value = "/filter_by_manufacturer")
    public ResponseWrapper partsByManufacturer(@RequestParam("manufacturer") String manufacturer) throws IOException {
        ResponseWrapper response = new ResponseWrapper();
        List<Parts> partmanufacturer = partsService.findByManufacturer(manufacturer);
        if (partmanufacturer.isEmpty()) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Part By the given manufacturer not found please check manufacturer name correctly!");
        } else {
            response.setMessage("Success");
            response.setCode(HttpStatus.OK.value());
            response.setData(partmanufacturer);
        }
        return response;
    }

    /**
     * @param pg
     * @return
     */

    @GetMapping("/parts_quantity")
    public ResponseEntity<ResponseWrapper<Object>> getWarningParts(Pageable pg) {

        ResponseWrapper response = new ResponseWrapper();
        response.setData(this.partsService.totalPartscount(pg));
        response.setMessage("success!");
        response.setCode(200);
        return ResponseEntity.ok().body(response);
    }

    //parts bulk upload

    @RequestMapping(value = "/uploadParts", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<Parts>> upload(@RequestParam("File") MultipartFile file) throws ResourceNotFoundException, IOException {
        Map<String, Object> map = new HashMap<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();

        String uploadPath = environment.getProperty("file.upload-dir");
        String fileName_ = fileStorageService.storeFile(file);
        String fullFilePath = uploadPath + "/" + fileName_;

        XSSFWorkbook workbook = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        int len = worksheet.getPhysicalNumberOfRows();
        for (int i = 0; i < len; i++) {
            XSSFRow row = worksheet.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String description = "";
            String part_number = "";
            String manufacturer = "";
            String model = "";
            String partname = "";
            try {
                description = row.getCell(1).getStringCellValue();
                part_number = row.getCell(0).getStringCellValue();
                manufacturer = row.getCell(4).getStringCellValue();
                model = row.getCell(3).getStringCellValue();
                partname = row.getCell(2).getStringCellValue();
            }catch (IllegalStateException e){
                description = "" +row.getCell(1).getNumericCellValue();
                part_number = "" +row.getCell(0).getNumericCellValue();
                manufacturer = "" +row.getCell(4).getNumericCellValue();
                model = "" +row.getCell(3).getNumericCellValue();
                partname = "" +row.getCell(2).getNumericCellValue();

            } catch (NumberFormatException e) {

            }
            Parts p = new Parts();
            p.setDescription(description);
            p.setPartModel(model);
            p.setManufacturerName(manufacturer);
            p.setPartName(partname);
            p.setPartNumber(part_number);
            p.setActionStatus(Constants.STATUS_UNAPPROVED);
            p.setAction(Constants.ACTION_CREATED);
            p.setIntrash(Constants.INTRASH_NO);
            Date f = new java.sql.Date(System.currentTimeMillis());
            p.setCreatedOn(f);
            map.put("content", partsRepository.save(p));
            responseWrapper.setData(map);
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Uploaded Successfully");
            workbook.close();
        }
        return ResponseEntity.ok().body(responseWrapper);
    }
    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("PartsSample.xlsx");

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




