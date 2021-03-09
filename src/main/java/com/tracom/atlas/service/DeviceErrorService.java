package com.tracom.atlas.service;

import com.tracom.atlas.entity.DeviceError;
import com.tracom.atlas.entity.UfsAuditLog;
import com.tracom.atlas.entity.UfsUser;
import com.tracom.atlas.repository.AuditRepository;
import com.tracom.atlas.repository.DeviceErrorRepository;
import com.tracom.atlas.repository.UserRepository;
import com.tracom.atlas.wrapper.LogExtras;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeviceErrorService {

    private final Logger logger = LoggerFactory.getLogger(DeviceErrorService.class);

    private final FileStorageService fileStorageService;
    private final AuditRepository auditRepository;
    private final LogExtras logExtras;
    private final UserRepository userRepository;
    private final DeviceErrorRepository deviceErrorRepository;
    private final Environment environment;

    public DeviceErrorService(FileStorageService fileStorageService, AuditRepository auditRepository, LogExtras logExtras, UserRepository userRepository, DeviceErrorRepository deviceErrorRepository, Environment environment) {
        this.fileStorageService = fileStorageService;
        this.auditRepository = auditRepository;
        this.logExtras = logExtras;
        this.userRepository = userRepository;
        this.deviceErrorRepository = deviceErrorRepository;
        this.environment = environment;
    }

    @Transactional
    public ResponseWrapper uploadFile(MultipartFile file) throws IOException {
        ResponseWrapper wrapper = new ResponseWrapper();

        String uploadDirectory = environment.getProperty("file.upload-dir");
        String fileName = fileStorageService.storeFile(file);
        String fullFilePath = uploadDirectory + "/" + fileName;

        XSSFWorkbook workbook = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheet = workbook.getSheetAt(0);

        int i = 1;
        for (i = 1; i < worksheet.getPhysicalNumberOfRows(); i ++) {
            XSSFRow row = worksheet.getRow(i);


            List<Cell> cells = new ArrayList<>();
            int lastColumn = Math.max(row.getLastCellNum(), 4);

            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = row.getCell(cn, MissingCellPolicy.RETURN_BLANK_AS_NULL);
                cells.add(c);
            }

            if (cells.get(3) != null ) {
                addToDb(cells);
            }

        }

        wrapper.setData(i);
        wrapper.setMessage("Data saved successfully");
        return wrapper;
    }

    private void addToDb(List<Cell> cells) {
        UfsAuditLog log = new UfsAuditLog();
        DeviceError deviceError = new DeviceError();

        Cell description = cells.get(0);
        if (description != null) {
            description.setCellType(CellType.STRING);
            deviceError.setDescription(description.getStringCellValue());
        } else {
            deviceError.setDescription("xxx");
        }

        Cell codeName = cells.get(1);
        if (codeName != null ) {
            codeName.setCellType(CellType.STRING);
            deviceError.setCodeName(codeName.getStringCellValue());
        } else {
            deviceError.setCodeName("xxx");
        }


        Cell level = cells.get(2);
        if (level != null ) {
            level.setCellType(CellType.STRING);
            deviceError.setLevel(level.getStringCellValue());
        } else {
            deviceError.setLevel("xxx");
        }


        Cell code = cells.get(3);
        code.setCellType(CellType.STRING);



        deviceError.setCode(code.getStringCellValue());
        deviceError.setAction(AppConstants.ACTIVITY_CREATE);
        deviceError.setActionStatus(AppConstants.STATUS_UNAPPROVED);
        deviceError.setIntrash(AppConstants.NO);

        DeviceError deviceError1 = deviceErrorRepository.findByCode(code.getStringCellValue());
        if ( deviceError1 == null) {

            DeviceError x = deviceErrorRepository.save(deviceError);

            log.setActivityType(AppConstants.ACTIVITY_CREATE);
            log.setDescription("Created Record successfully");
            log.setEntityName("DeviceError");
            log.setIpAddress(logExtras.getIpAddress());
            log.setNotes("Record Creation");
            log.setSource(logExtras.getSource());
            log.setClientId(logExtras.getClientId());
            log.setStatus(AppConstants.STATUS_COMPLETED);
            UfsUser user1 = userRepository.findUfsUserByUserId(logExtras.getUserId());
            log.setUserId(user1);
            log.setIntrash(AppConstants.NO);
            log.setOccurenceTime(new Date(System.currentTimeMillis()));
            log.setEntityId(String.valueOf(x.getError_id()));

            auditRepository.save(log);

        }

    }
}
