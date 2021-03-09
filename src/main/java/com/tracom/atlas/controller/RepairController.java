package com.tracom.atlas.controller;

import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.service.FileStorageService;
import com.tracom.atlas.service.RepairService;
import com.tracom.atlas.wrapper.RepairResponse;
import com.tracom.atlas.wrapper.RepairWrapper;
import com.tracom.atlas.wrapper.SearchWrapper;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;



@RestController
@RequestMapping("/repair")
public class RepairController extends ChasisResource<Repair,Long, UfsEdittedRecord> {

    private static final Logger logger = LoggerFactory.getLogger(RepairController.class);

    private final RepairService repairService;
    private final FileStorageService fileStorageService;
    private final RepairRepository repairRepository;

    public RepairController(LoggerService loggerService, EntityManager entityManager, RepairService repairService, FileStorageService fileStorageService, RepairRepository repairRepository) {
        super(loggerService, entityManager);

        this.repairService = repairService;
        this.fileStorageService = fileStorageService;
        this.repairRepository = repairRepository;

    }

    //upload an excel file to board devices for repair
    @PostMapping("/upload")
    public ResponseEntity<ResponseWrapper> excelToDB(@RequestParam("File") MultipartFile file, @RequestParam("comments") String comments,
                                                    @RequestParam("clientName") String clientName,
                                                    @RequestParam("from") String receivedFrom) throws IOException {

        ResponseWrapper responseWrapper = repairService.loadAndReadFile(file, comments, clientName, receivedFrom);

        return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
    }


    //status
    @GetMapping("/status")
    public  ResponseEntity<RepairResponse> getRepairCentre() {

        RepairResponse response = repairService.repairStatus();

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper> search(SearchWrapper wrapper,Pageable pageable){

        ResponseWrapper response = repairService.searchRepairsByCombinedQuery(wrapper,pageable);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PutMapping(value = "/update")
    public ResponseEntity<ResponseWrapper<Repair>> updateRepair(@RequestBody RepairWrapper repairWrapper) throws IOException{
        ResponseWrapper responseWrapper = repairService.updateRepair(repairWrapper);

        return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<ResponseWrapper> findRepairByUser(Pageable pg){
        ResponseWrapper responseWrapper = repairService.findRepairByUser(pg);

        return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
    }

    @GetMapping(value = "/level/status")
    public ResponseEntity<ResponseWrapper> findRepairByLevel(){
        ResponseWrapper responseWrapper = repairService.findRepairByLevel();

        return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
    }

    @GetMapping(value = "/serialnumber")
    public ResponseEntity<ResponseWrapper> findRepairBySerialNumber(SearchWrapper wrapper,Pageable pg){
        ResponseWrapper responseWrapper = repairService.findRepairBySerialNumber(wrapper,pg);

        return new ResponseEntity<>(responseWrapper,HttpStatus.OK);
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("upload_template.xlsx");

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

    @GetMapping(value = "/generate-report")
    public ResponseEntity<ResponseWrapper> generateReportFile(SearchWrapper wrapper) {
        ResponseWrapper  response = new ResponseWrapper();
        repairService.generateReportFile(wrapper);

        response.setData("Response will be email to you");

        return  new ResponseEntity<>(response,HttpStatus.OK);
    }


}