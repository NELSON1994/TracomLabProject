package com.tracom.atlas.controller;

import com.tracom.atlas.entity.DeviceError;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.service.DeviceErrorService;
import com.tracom.atlas.service.FileStorageService;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/device_error")
public class DeviceErrorController extends ChasisResource<DeviceError,Long, UfsEdittedRecord> {

    @Autowired
    private FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(DeviceErrorController.class);
    private final DeviceErrorService deviceErrorService;

    public DeviceErrorController(LoggerService loggerService, EntityManager entityManager, DeviceErrorService deviceErrorService) {
        super(loggerService, entityManager);
        this.deviceErrorService = deviceErrorService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseWrapper> uploadFile(@RequestParam("File")MultipartFile file) throws IOException {
        ResponseWrapper wrapper =deviceErrorService.uploadFile(file);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/sampleFile")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("errorCodeTemplate.xlsx");

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
