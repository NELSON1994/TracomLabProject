package com.tracom.atlas.controller;

import com.tracom.atlas.Model.ContractModel;
import com.tracom.atlas.Model.WarrantyModel;
import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Customers;
import com.tracom.atlas.entity.Devices;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.exception.ResourceNotFoundException;
import com.tracom.atlas.repository.CustomersRepository;
import com.tracom.atlas.repository.DevicesRepository;
import com.tracom.atlas.service.DevicesService;
import com.tracom.atlas.service.FileStorageService;

import io.swagger.models.auth.In;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

/**
 * @author Nelson
 */
@RestController
@RequestMapping( value = "/devices" )
public class DevicesController extends ChasisResource<Devices, Long, UfsEdittedRecord> {

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private CustomersRepository customersRepository;
    private static final Logger logger = LoggerFactory.getLogger(DevicesController.class);

    public DevicesController(LoggerService loggerService, EntityManager entityManager) {
        super(loggerService, entityManager);
    }

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private Environment environment;
    @Autowired
    private DevicesService devicesService;


    //uploading the excel to the DATABASE and saving the file to the directory

    @Transactional
    @RequestMapping( value = "/uploaddevices", method = RequestMethod.POST )
    public ResponseWrapper uploadDevice(@RequestParam( "File" ) MultipartFile file,
                                        @RequestParam( "deviceowner" ) String deviceowner,
                                        @RequestParam( "manufacturer" ) String manufacturer,
                                        @RequestParam( "devicemodel" ) String devicemodel) throws ResourceNotFoundException, IOException, Exception {
        String fileName = file.getOriginalFilename();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> map = new HashMap<>();
        String uploadPath = environment.getProperty("file.upload-dir");
        String fileName_ = fileStorageService.storeFile(file);
        String fullFilePath = uploadPath + "/" + fileName_;
        XSSFWorkbook workbook = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        int len = worksheet.getPhysicalNumberOfRows();
        int duplicates = 0,bad_entries = 0;
        for (int i = 0; i < len; i++) {
            XSSFRow row = worksheet.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String partnumber;
            String serialnumber;
            String imei;
            DataFormatter d = new DataFormatter();
            try {
                partnumber = d.formatCellValue(row.getCell(1));
                serialnumber = d.formatCellValue(row.getCell(2));
                imei = d.formatCellValue(row.getCell(3));
            } catch (NullPointerException e) {
                partnumber = d.formatCellValue(row.getCell(1));
                serialnumber = d.formatCellValue(row.getCell(2));
                imei = d.formatCellValue(row.getCell(3));
            }
            Optional<Devices> dev = devicesRepository.findDevicesBySerialnumber(serialnumber);
            if (dev.isPresent()) {
                duplicates++;
                Devices n = dev.get();
                map.put("Repeated Records  :", duplicates);
                map.put("serial numbers repeated :+==:::", n.getSerialnumber());
                responseWrapper.setCode(HttpStatus.OK.value());
                responseWrapper.setMessage("Some devices in the file are already onboarded!!!!" + duplicates + "   Devices");
                responseWrapper.setData(map);
            }
            else {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+serialnumber);
                Devices devices = new Devices();
                devices.setPartnumber(partnumber);
                devices.setSerialnumber(serialnumber);
                devices.setImeinumber(imei);
                devices.setActionStatus(Constants.STATUS_UNAPPROVED);
                devices.setAction(Constants.ACTION_CREATED);
                devices.setIntrash(Constants.INTRASH_NO);
                devices.setDeviceowner(deviceowner.toUpperCase());
                Date f = new java.sql.Date(System.currentTimeMillis());
                devices.setCreationDate(f).toString();
                devices.setContractperiod(0);
                devices.setWarrantyperiod(0);
                devices.setContract_starts(f).toString();
                devices.setContract_expires(f).toString();
                devices.setDeviceWarantyStatus(Constants.WARRANTY_NO);
                devices.setWarranty_starts(f).toString();
                devices.setWarrantyExpire(f).toString();
                devices.setDeviceContractStatus(Constants.DEVICE_NOTINCONTRACT);
                devices.setDeviceModels(devicemodel);
                devices.setManufacturer(manufacturer);
                devices.setSeller(Constants.SELLER1);
                ResponseEntity<ResponseWrapper<Devices>> x = create(devices);
                map.put("save devices  :", devicesRepository.save(devices));

                responseWrapper.setData(map);
                responseWrapper.setCode(HttpStatus.OK.value());
                responseWrapper.setMessage("Devices for :" + deviceowner + " Uploaded Successfully");
            }

            workbook.close();
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/oneOffRepair", method = RequestMethod.POST )
    public ResponseWrapper uploadDevice(@RequestParam( "File" ) MultipartFile filee,
                                        @RequestParam( "deviceownerr" ) String deviceownerr,
                                        @RequestParam( "status" ) String status) throws ResourceNotFoundException, IOException, ParseException {
        String fileName = filee.getOriginalFilename();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> map = new HashMap<>();
        int alreadyExist = 0;
        String uploadPath = environment.getProperty("file.upload-dir");
        String fileName_ = fileStorageService.storeFile(filee);
        String fullFilePath = uploadPath + "/" + fileName_;
        XSSFWorkbook workbookk = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheett = workbookk.getSheetAt(0);
        int lenn = worksheett.getPhysicalNumberOfRows();
        for (int i = 0; i < lenn; i++) {
            XSSFRow row = worksheett.getRow(i);
            if (row.getRowNum() == 0) {
                continue;
            }
            String partnumber = "";
            String serialnumber ="";
            String imei = "";
            try {
                partnumber = row.getCell(1).getStringCellValue();
                serialnumber = row.getCell(2).getStringCellValue();
                imei = row.getCell(3).getStringCellValue();
            }catch (IllegalStateException e) {
                partnumber = ""+row.getCell(1).getNumericCellValue();
                serialnumber = ""+row.getCell(2).getNumericCellValue();
                imei = ""+row.getCell(3).getNumericCellValue();
            } catch (NullPointerException e) {

            }

            Optional<Devices> dev = devicesRepository.findDevicesBySerialnumber(serialnumber);
            if (dev.isPresent()) {
                alreadyExist++;
                map.put("Repeated Records  :", alreadyExist);
                map.put("serial numbers repeated :+==:::", serialnumber);
                responseWrapper.setCode(HttpStatus.OK.value());
                responseWrapper.setMessage("Some devices in the file are already onboarded!!!!" + alreadyExist + "Devices");
                responseWrapper.setData(map);
            } else {
                Devices device = new Devices();
                device.setSerialnumber(serialnumber);
                device.setPartnumber(partnumber);
                device.setImeinumber(imei);
                device.setSeller(Constants.SELLER2);
                device.setManufacturer("UNDEFINED");
                device.setDeviceModels("UNDEFINED");
                device.setDeviceowner(deviceownerr.toUpperCase());
                device.setAction(Constants.ACTION_CREATED);
                device.setActionStatus(Constants.STATUS_UNAPPROVED);
                Date f = new java.sql.Date(System.currentTimeMillis());
                device.setDeviceWarantyStatus(status);
                device.setContractperiod(0);
                device.setWarrantyperiod(0);
                device.setCreationDate(f).toString();
                device.setWarranty_starts(f).toString();
                device.setWarrantyExpire(f).toString();
                device.setIntrash(Constants.INTRASH_NO);
                device.setContract_expires(f).toString();
                device.setContract_starts(f).toString();
                device.setDeviceContractStatus(status);
                device.setContract_expires(f).toString();
                map.put("saving the devices  :", devicesRepository.save(device));
                responseWrapper.setCode(HttpStatus.OK.value());
                responseWrapper.setMessage("Successfull file Upload !!!!");
                responseWrapper.setData(map);
            }
            workbookk.close();
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/setContract", method = RequestMethod.PUT )
    public ResponseWrapper setDeviceContract(@RequestBody ContractModel c) throws ParseException

                                             {
                                             ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Devices> dd = devicesRepository.findDevicesByDeviceownerAndSerialnumberIsStartingWith(c.getDeviceowner(), c.getYear()).get();
        Map<String, Object> map1 = new HashMap<>();
        List<Devices> dh = new ArrayList<>();
        if (dd.isEmpty()) {
            responseWrapper.setMessage("No devices for  :" + c.getDeviceowner() + " found for the year :20" + c.getYear());
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
        } else {
            int i = 0;
            while (i < dd.size()) {
                Devices d = dd.get(i);
                d.setContractperiod(c.getContractperiod());
                String pattern = "yyyy-MM-dd";
                java.util.Date contract = new SimpleDateFormat(pattern).parse(c.getContract_starts());
                Calendar cal = Calendar.getInstance();
                cal.setTime(contract);
                cal.add(Calendar.MONTH, c.getContractperiod());
                java.util.Date date = cal.getTime();
                d.setContract_expires(date).toString();
                d.setContract_starts(contract).toString();
                d.setAction(Constants.ACTION_UPDATE);
                java.util.Date date2 = new java.util.Date();
                if (date2.before(date)) {
                    d.setDeviceContractStatus(Constants.DEVICE_ONCONTRACT);
                } else if (date2.after(date)) {
                    d.setDeviceContractStatus(Constants.DEVICE_EXPIRED);
                } else if (date.equals(date2)) {
                    d.setDeviceContractStatus(Constants.DEVICE_TODAY);
                } else {
                    d.setDeviceContractStatus(Constants.DEVICE_EXPIRED);
                }
                map1.put("content :", devicesRepository.save(d));
                i++;

            }
            responseWrapper.setMessage("Contract set succesfull");
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setData(map1);
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/setWarranty", method = RequestMethod.PUT )
    public ResponseWrapper setDeviceWarranty(@RequestBody WarrantyModel warrantyModel) throws ParseException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> map = new HashMap<>();
        List<Devices> d = new ArrayList<>();
        List<Devices> devices = devicesRepository.findDevicesByDeviceownerAndSerialnumberIsStartingWith(warrantyModel.getDeviceowner(), warrantyModel.getYear()).get();
        if (devices.isEmpty()) {
            responseWrapper.setMessage("No devices for  :" + warrantyModel.getDeviceowner() + " found for the year :20" + warrantyModel.getYear());
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
        } else {
            for (int i = 0; i < devices.size(); i++) {
                Devices device = devices.get(i);
                device.setWarrantyperiod(warrantyModel.getWarrantyperiod());
                String pattern = "yyyy-MM-dd";
                java.util.Date warranty = new SimpleDateFormat(pattern).parse(warrantyModel.getWarranty_starts());
                Calendar cal = Calendar.getInstance();
                cal.setTime(warranty);
                cal.add(Calendar.MONTH, warrantyModel.getWarrantyperiod());
                java.util.Date date = cal.getTime();
                 device.setWarrantyExpire(date).toString();
                device.setWarranty_starts(warranty).toString();

                device.setAction(Constants.ACTION_UPDATE);
                java.util.Date date2 = new java.util.Date();
                if (date2.before(date)) {
                    device.setDeviceWarantyStatus(Constants.WARRANTY_YES);
                }
                 else {
                   device.setDeviceWarantyStatus(Constants.WARRANTY_NO);
                }

               devicesRepository.save(device);
                 d =devicesRepository.saveAll(devices);
                 map.put("content" , d);
                responseWrapper.setMessage("Warranty set succesfully");
                responseWrapper.setCode(HttpStatus.OK.value());
                responseWrapper.setData(map);
            }

        }
        return responseWrapper;
    }

    @RequestMapping( value = "/removeDevicesContract", method = RequestMethod.PUT )
    public ResponseWrapper removeContract(@RequestParam( "contract_ends" ) String contract_ends,
                                          @RequestParam( "deviceowner" ) String deviceowner,
                                          @RequestParam( "year" ) int year) throws ParseException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        List<Devices> devices = devicesRepository.findDevicesByDeviceownerAndSerialnumberIsStartingWith(deviceowner, year).get();
        if (devices.isEmpty()) {
            responseWrapper.setMessage("Devices Not found");
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
        } else {
            for (int i = 0; i <= devices.size(); i++) {
                Devices device = devices.get(i);
                String pattern = "yyyy/MM/dd";
                Date contractExpire = (Date) new SimpleDateFormat(pattern).parse(contract_ends);
                map.put("cntract expires", device.setContract_expires(contractExpire));
                map.put("contract status", device.setDeviceContractStatus(Constants.DEVICE_REMOVED));
                map.put("action status", device.setAction(Constants.ACTION_UPDATE));
                map2.put("removed out of contract", devicesRepository.save(device));
            }
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Devices Removed out of contract");
            responseWrapper.setData(map2);
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/removeBrokenDeviceContract", method = RequestMethod.PUT )
    public ResponseWrapper removeBrokenDeviceContract(@RequestParam( "serialnumber" ) String serialnumber,
                                                      @RequestParam( "deviceowner" ) String deviceowner) {
        Devices device = devicesRepository.findDevicesByDeviceownerAndSerialnumber(deviceowner, serialnumber).get();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        if (device == null) {
            responseWrapper.setMessage("no such device found");
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
        } else {
            if (device.getActionStatus() == Constants.STATUS_APPROVED) {
                device.setActionStatus(Constants.STATUS_UNAPPROVED);
            }
            if (device.getDeviceWarantyStatus() == Constants.WARRANTY_YES) {
                device.setDeviceWarantyStatus(Constants.WARRANTY_REMOVED);
                device.setContract_expires(new Date(System.currentTimeMillis()));
            }
            if (device.getDeviceContractStatus() == Constants.DEVICE_ONCONTRACT) {
                device.setDeviceContractStatus(Constants.DEVICE_REMOVED);
                device.setContract_expires(new Date(System.currentTimeMillis()));
            }
            device.setActionStatus(Constants.STATUS_UNAPPROVED);
            device.setAction(Constants.ACTION_UPDATE);

            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setMessage("Successfully removed on contract");
            responseWrapper.setData(devicesRepository.save(device));
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/devicesStatus/notOnWarranty", method = RequestMethod.GET )
    public ResponseWrapper devicesNotOnWarranty(@RequestParam( "warrantystatus" ) String warrantystatus, @RequestParam( "deviceowner" ) String deviceowner) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Devices> devices = devicesService.findByWarrantyStatus(Constants.WARRANTY_NO, deviceowner).get();
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("Succesfull");
        responseWrapper.setData(devices);
        return responseWrapper;
    }

    @RequestMapping( value = "/devicesStatus/onwarranty", method = RequestMethod.GET )
    public ResponseWrapper devicesOnWarranty(@RequestParam( "warrantystatus" ) String warrantystatus,
                                             @RequestParam( "deviceowner" ) String deviceowner) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Devices> devices = devicesService.findByWarrantyStatus(Constants.WARRANTY_YES, deviceowner).get();
        responseWrapper.setMessage("Success");
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setData(devices);
        return responseWrapper;
    }

    @RequestMapping( value = "/devicesStatus/onContract", method = RequestMethod.GET )
    public ResponseWrapper devicesOnContract(@RequestParam( "contractstatus" ) String contractstatus,
                                             @RequestParam( "deviceowner" ) String deviceowner) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Devices> devices = devicesService.findByContractStatus(Constants.DEVICE_ONCONTRACT, deviceowner).get();
        responseWrapper.setMessage("Success");
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setData(devices);
        return responseWrapper;
    }

    @RequestMapping( value = "/devicesStatus/NotOnContract", method = RequestMethod.GET )
    public ResponseWrapper devicesNotOnContract(@RequestParam( "contractstatus" ) String contractstatus,
                                                @RequestParam( "deviceowner" ) String deviceowner) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<Devices> devices = devicesService.findByContractStatus(Constants.DEVICE_NOTINCONTRACT, deviceowner).get();
        responseWrapper.setMessage("Success");
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setData(devices);
        return responseWrapper;
    }

    @GetMapping( value = "/reports/filterByDate" )
    public ResponseWrapper devicesBetweenDates(@RequestParam( "from" ) String from,
                                               @RequestParam( "to" ) String to) throws ParseException {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        String pattern = "yyyy/MM/dd";
        java.util.Date too = new SimpleDateFormat(pattern).parse(to);
        java.util.Date fromm = new SimpleDateFormat(pattern).parse(from);
        Date one = new Date(too.getTime());
        Date two = new Date(fromm.getTime());
        List<Devices> devices = devicesService.findDevicesBetweenDate(two, one).get();
        if (devices.isEmpty()) {
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage("No devices found for the specified date interval");
        } else {
            responseWrapper.setMessage("Successfully filtered by date");
            responseWrapper.setCode(HttpStatus.OK.value());
            responseWrapper.setData(devices);
        }
        return responseWrapper;
    }

    @RequestMapping( value = "/count/devicesOnContract/perClient", method = RequestMethod.GET )
    public ResponseWrapper countContractYes() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> cus = new HashMap<>();
        List<Customers> customer = customersRepository.findCustomersByIntrash(Constants.INTRASH_NO).get();
        for (Customers customers : customer) {
            long dex = devicesRepository.countDevicesByDeviceownerAndDeviceContractStatus(customers.getName(), Constants.DEVICE_ONCONTRACT);
            cus.put(customers.getName(), dex);
        }
        responseWrapper.setData(cus);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success ");
        return responseWrapper;
    }

    @RequestMapping( value = "/count/devicesNotOnContract/perClient", method = RequestMethod.GET )
    public ResponseWrapper countContractno() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> cus = new HashMap<>();
        List<Customers> customer = customersRepository.findCustomersByIntrash(Constants.INTRASH_NO).get();
        for (Customers customers : customer) {
            long dex = devicesRepository.countDevicesByDeviceownerAndDeviceContractStatus(customers.getName(), Constants.DEVICE_NOTINCONTRACT);
            cus.put(customers.getName(), dex);
        }
        responseWrapper.setData(cus);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success ");
        return responseWrapper;
    }

    @RequestMapping( value = "/count/devicesOnWarranty/perClient", method = RequestMethod.GET )
    public ResponseWrapper countWarrantyyes() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> cus = new HashMap<>();
        List<Customers> customer = customersRepository.findCustomersByIntrash(Constants.INTRASH_NO).get();
        for (Customers customers : customer) {
            long dex = devicesRepository.countDevicesByDeviceownerAndDeviceWarantyStatus(customers.getName(), Constants.WARRANTY_YES);
            cus.put(customers.getName(), dex);
        }
        responseWrapper.setData(cus);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success ");
        return responseWrapper;
    }

    @RequestMapping( value = "/count/devicesNotOnWarranty/perClient", method = RequestMethod.GET )
    public ResponseWrapper countDevicesNotOnWarranty() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> cus = new HashMap<>();
        List<Customers> customer = customersRepository.findCustomersByIntrash(Constants.INTRASH_NO).get();
        for (Customers customers : customer) {
            long dex = devicesRepository.countDevicesByDeviceownerAndDeviceWarantyStatus(customers.getName(), Constants.WARRANTY_NO);
            cus.put(customers.getName(), dex);
        }
        responseWrapper.setData(cus);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success for devices not on warranty");
        return responseWrapper;
    }
    @RequestMapping( value = "/countAllDevices", method = RequestMethod.GET )
    public ResponseWrapper countAllDevices() {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Map<String, Object> cus = new HashMap<>();

        long all = devicesRepository.countDevicesByActionStatusAndIntrash(Constants.STATUS_APPROVED , Constants.INTRASH_NO);
        cus.put("content" , all);
        responseWrapper.setData(cus);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("success for devices not on warranty");
        return responseWrapper;

    }

    @GetMapping("/sampleFile")
    public ResponseEntity<Resource> downloadTemplate(HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileStorageService.loadFileAsResource("onBoardingTemplate.xlsx");

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


