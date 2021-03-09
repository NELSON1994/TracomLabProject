package com.tracom.atlas.service;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.*;
import com.tracom.atlas.repository.*;
import com.tracom.atlas.util.SharedMethods;
import com.tracom.atlas.wrapper.*;
import ke.axle.chassis.utils.AppConstants;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RepairService {

    private static final Logger logger = LoggerFactory.getLogger(RepairService.class);

    private final RepairRepository repairRepository;
    private final LoggerServiceTemplate loggerServiceTemplate;
    private final PartsIssuedService partsIssuedService;
    private final FileStorageService fileStorageService;
    private final CustomersRepository customersRepository;
    private final DeviceErrorRepository deviceErrorRepository;
    private final UserRepository userRepository;
    private final PartsRepository partsRepository;
    private final RepairBatchService repairBatchService;
    private final DeliveryRepository deliveryRepository;
    private final EdittedRepository edittedRepository;
    private final NotifyServiceTemplate notifyService;
    private final UfsSysConfigRepository ufsSysConfigRepository;
    private final LogExtras logExtras;
    private final AuditRepository auditRepository;
    private final Environment environment;
    private final DevicesRepository deviceRepository;
    private final ShippedRepairService shippedRepairService;
    private final DeliveryService deliveryService;
    private final SharedMethods sharedMethods;

    public RepairService(RepairRepository repairRepository, LoggerServiceTemplate loggerServiceTemplate, PartsIssuedService partsIssuedService, FileStorageService fileStorageService, CustomersRepository customersRepository, DeviceErrorRepository deviceErrorRepository, LogExtras logExtras, AuditRepository auditRepository, DevicesRepository deviceRepository, UserRepository userRepository, PartsRepository partsRepository, RepairBatchService repairBatchService, DeliveryRepository deliveryRepository, EdittedRepository edittedRepository, NotifyServiceTemplate notifyService, Environment environment, UfsSysConfigRepository ufsSysConfigRepository,
                         ShippedRepairService shippedRepairService, DeliveryService deliveryService, SharedMethods sharedMethods) {
        this.repairRepository = repairRepository;
        this.loggerServiceTemplate = loggerServiceTemplate;
        this.partsIssuedService = partsIssuedService;
        this.fileStorageService = fileStorageService;
        this.customersRepository = customersRepository;
        this.deviceErrorRepository = deviceErrorRepository;
        this.logExtras = logExtras;
        this.auditRepository = auditRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.partsRepository = partsRepository;
        this.repairBatchService = repairBatchService;
        this.deliveryRepository = deliveryRepository;
        this.edittedRepository = edittedRepository;
        this.notifyService = notifyService;
        this.environment = environment;
        this.ufsSysConfigRepository = ufsSysConfigRepository;
        this.shippedRepairService = shippedRepairService;
        this.deliveryService = deliveryService;
        this.sharedMethods = sharedMethods;
    }

    @Transactional
    public ResponseWrapper loadAndReadFile(MultipartFile file, String comments, String clientName, String receivedFrom) throws IOException {
        UfsUser user = userRepository.findUfsUserByUserId(logExtras.getUserId());
        String uploadDirectory = environment.getProperty("file.upload-dir");
        String fileName = fileStorageService.storeFile(file);
        String fullFilePath = uploadDirectory + "/" + fileName;

        int random = 0;
        List<String> unBoarded = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(fullFilePath);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        List<Repair> repairs = new ArrayList<>();

        int i = 0,unboardedpos = 0,badEntries = 0;
        while (i <= worksheet.getPhysicalNumberOfRows()) {
            XSSFRow row = worksheet.getRow(i);

            String serialNumber = "";

            try {
                serialNumber = row.getCell(2).getStringCellValue();
            } catch (IllegalStateException e) {
                DataFormatter d = new DataFormatter();
                serialNumber = d.formatCellValue(row.getCell(2));
            }catch (NullPointerException e) {
                badEntries++;
            }


            Optional<Devices> opt = deviceRepository.findDevicesBySerialnumber(serialNumber);
            List<Repair> repairList = repairRepository.findAllBySerialNumberAndRepairStatus(serialNumber,Constants.STATUS_PENDING);
            if (repairList.size() > 0) {
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}",serialNumber);
                i++;
                continue;

            }
            if (opt.isPresent()) {

                Devices devices = opt.get();

                if (random == 0) {
                    random = repairBatchService.findCurrentBatchNumber(devices.getDeviceowner());
                }

                String batches = String.format("BATCH %06d", random);
                Repair repair = new Repair(devices,devices.getDeviceowner(),AppConstants.ACTIVITY_APPROVE,Constants.TRACOM_CENTRE,
                        batches,AppConstants.NO,AppConstants.ACTIVITY_CREATE,comments,AppConstants.STATUS_PENDING,devices.getSerialnumber(),
                        receivedFrom,user.getFullName());


                Repair x = repairRepository.save(repair);
                repairs.add(repair);

                //Logger Service
                loggerServiceTemplate.log(Constants.DESCRIPTION,Constants.REPAIR,x.getId(),AppConstants.ACTIVITY_CREATE,AppConstants.STATUS_COMPLETED,Constants.NOTES);

            }else {
                unboardedpos++;
                unBoarded.add(serialNumber);
            }
            i +=1;

        }
        workbook.close();

        if ( !unBoarded.isEmpty()) {
            sendEmail(unBoarded);
        }


        Map<String,Object> data = new HashMap<>();

        data.put("Unboarded Devices",unboardedpos);
        data.put("Devices",repairs);
        data.put("Bad Entries ",badEntries);

        logger.info("Unboarded Devices {} Devices {} Bad Entries {}",unboardedpos,repairs,badEntries);

        ResponseWrapper response = new ResponseWrapper();
        response.setData(data);

        return response;
    }

    public RepairResponse repairStatus(){

        RepairResponse response = new RepairResponse();
        List<Object> list = new ArrayList<>();
        Map<String,Object> client = new HashMap<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        List<Customers> customers;
        customers = customersRepository.findAll();

        if (!customers.isEmpty()) {
            for (Customers customer:customers) {
                Map<String,Integer> status = new HashMap<>();

                int tracom = 0, ingenico = 0,pending = 0, progress = 0,delivered = 0,pending_delivery = 0,unrepairable = 0,repaired = 0;

                Optional<List<Repair>> optional = repairRepository.findRepairsByIntrashAndCustomers(AppConstants.NO,customer.getName());
                List<Delivery> deliveryList = deliveryRepository.findAllByCustomers(customer.getName());

                if (Objects.nonNull(deliveryList)) {
                    for (Delivery delivery: deliveryList) {
                        if (delivery.getYear() == year && delivery.getWeek() == week ) {
                            if (delivery.getDeliveryStatus().equalsIgnoreCase(AppConstants.STATUS_PENDING)) {
                                pending_delivery++;
                            } else {
                                delivered++;
                            }
                        }
                    }
                }

                if (optional.isPresent()) {
                    List<Repair> repairs = optional.get();
                    for (Repair repair: repairs) {

                        if (Objects.nonNull(repair.getRepairCentre())) {
                            if (repair.getRepairCentre().equalsIgnoreCase(Constants.TRACOM_CENTRE)) {
                                tracom++;
                            }else {
                                ingenico++;
                            }
//
                            if (repair.getRepairStatus().equalsIgnoreCase(AppConstants.STATUS_PENDING)) {
                                pending++;
                            }else if (repair.getRepairStatus().equalsIgnoreCase(Constants.STATUS_PROGRESS)){
                                progress++;
                            }else if(repair.getRepairStatus().equalsIgnoreCase(Constants.STATUS_UNREPAIRABLE)) {
                                unrepairable++;
                            }else{
                                repaired++;
                            }

                        }
                    }
//
                    status.put("Tracom",tracom);
                    status.put("Unrepairable", unrepairable);
                    status.put("Ingenico",ingenico);
                    status.put("Repaired",repaired);
                    status.put("Pending_repair", pending);
                    status.put("repair_in_progress",progress);
                    status.put("delivered_devices",delivered);
                    status.put("pending_delivery", pending_delivery);

                    client.put(customer.getName(),status);
                }


            }
        }

        list.add(client);

        response.setContent(list);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("data");
        return response;

    }

    public ResponseWrapper findRepairByLevel() {
        ResponseWrapper response = new ResponseWrapper();
        List<Repair> repairList = new ArrayList<>();
        repairList  = repairRepository.findRepairsByIntrash(AppConstants.NO).get();
        Map<String,Integer> status = new HashMap<>();
        int level_1 = 0, level_2 = 0, level_3  = 0, level_4 = 0, level_ingenico = 0, delivered = 0, repaired = 0,
                devices = 0, repairlist_ = 0;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        if (!repairList.isEmpty()) {
            for (Repair repair: repairList) {
                if (Objects.nonNull(repair.getLevels() )) {
                    if (repair.getLevels().equals(Constants.LEVEL_1)){
                        level_1++;
                    } else if (repair.getLevels().equals(Constants.LEVEL_2)) {
                        level_2++;
                    } else if (repair.getLevels().equals(Constants.LEVEL_3)) {
                        level_3++;
                    }else {
                        if (repair.getRepairCentre().equals(Constants.TRACOM_INGENICO)) {
                            level_ingenico++;
                        }else {
                            level_4 ++;
                        }
                    }
                }
            }
        }

        List<Devices> devicesList = deviceRepository.findDevicesByIntrash(AppConstants.NO).get();
        List<Delivery> deliveryList = deliveryRepository.findAll();

        if (!deliveryList.isEmpty()) {
            for (Delivery delivery: deliveryList) {
                if (delivery.getYear() == year && delivery.getWeek() == week) {
                    if (delivery.getDeliveryStatus().equals(Constants.STATUS_DELIVERED)) {
                        delivered++;
                    }
                    repaired++;
                }
            }
        }

        if (!devicesList.isEmpty()) {
            devices = devicesList.size();
        }

        if (!repairList.isEmpty()){
            repairlist_ = repairList.size();
        }

        status.put("level 1", level_1);
        status.put("level 2", level_2);
        status.put("level 3", level_3);
        status.put("level 4", level_4);
        status.put("delivered", delivered);
        status.put("repaired", repaired);
        status.put("onboarded_devices",devices);
        status.put("received_devices",repairlist_ );
        status.put("level_ingenico", level_ingenico);

        response.setData(status);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("data");
        response.setTimestamp(Calendar.getInstance().getTimeInMillis());
        return response;
    }

    @Transactional
    public ResponseWrapper updateRepair(RepairWrapper repairWrapper) {
        List<Repair> repairs = new ArrayList<>();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UfsUser user1 = userRepository.findUfsUserByUserId(logExtras.getUserId());

        RepairModel repairModel = repairWrapper.getRepairModel();
        for (Long id : repairWrapper.getIds()) {
            Optional<Repair> opt = repairRepository.findById(id);
            if (opt.isPresent()) {
                Repair repair = opt.get();

                //Update Reported Defects
                if (!repairModel.getReportedDefects().isEmpty()) {
                    repair.setReportedDefects(repairModel.getReportedDefects());
                }

                //Update Repair Status
                if (!repairModel.getRepairStatus().isEmpty()) {
                    repair.setRepairCentre(repairModel.getRepairCentre());
                }

                //Update Parts
                if (!repairModel.getParts().isEmpty()) {
                    for (String partNumber: repairModel.getParts()) {
                        Parts part = partsRepository.findByPartNumber(partNumber).get();
                        partsIssuedService.issueParts(part);
                        repair.getParts().add(part);
                        repair.setDatePartsAdded(new Date(System.currentTimeMillis()));

                    }
                }

                //Update Users
                if (!repairModel.getUsers().isEmpty()) {
                        long Id = Long.valueOf(repairModel.getUsers());
                        UfsUser user = userRepository.findUfsUserByUserId(Id);
                        repair.getUsers().add(user);
                        repair.setCurrentUser(Id);
                        repair.setDateUserAdded(new Date(System.currentTimeMillis()));
                }

                //Update device Error
                if (!repairModel.getDeviceErrors().isEmpty()) {
                    for (String deviceName: repairModel.getDeviceErrors()) {
                        DeviceError deviceError =deviceErrorRepository.findByCode(deviceName);
                        repair.getDeviceErrors().add(deviceError);
                        if (Objects.isNull(repair.getLevels())) {

                            repair.setLevels(deviceError.getLevel());
                        }else {
                            String level_split = repair.getLevels().split(" ")[1];

                            if (Integer.parseInt(level_split) < Integer.parseInt(deviceError.getLevel().split(" ")[1])){
                                repair.setLevels(deviceError.getLevel());
                            }
                        }

                    }
                }

                //Update comments
                if ( !repairModel.getComments().isEmpty()) {
                    repair.setComments(repairModel.getComments());
                    logger.info("COMMENTS {} ", repairModel.getComments());
                }

                //Update Repair Centre
                if (!repairModel.getRepairCentre().isEmpty()) {
                    repair.setRepairCentre(repairModel.getRepairCentre());
                    if (repairModel.getRepairCentre().equalsIgnoreCase(Constants.TRACOM_INGENICO)) {
                        shippedRepairService.populateShippedRepair(repair.getId());
                    }
                    logger.info("REPAIR CENTRE {}", repairModel.getRepairCentre());
                }

                //Update QA Test
                if (!repairModel.getQaTest().isEmpty()) {
                    repair.setQaTest(repairModel.getQaTest());
                    repair.setQaDoneBy(user1.getFullName());
                    repair.setQaTestPassedDate(new Date(System.currentTimeMillis()));
                    logger.info("QA TEST {} ", repairModel.getQaTest());
                }

                //Update RepairStatus
                if (!repairModel.getRepairStatus().isEmpty()) {
                    repair.setRepairStatus(repairModel.getRepairStatus());
                    if (repair.getRepairStatus().equalsIgnoreCase(Constants.STATUS_REPAIRED) && repair.getQaTest().equalsIgnoreCase(Constants.QA_PASSED)) {
                        deliveryService.populateDelivery(repair.getId());
                    }
                    logger.info("REPAIR STATUS {} ", repairModel.getRepairStatus());
                }

                repair.setAction(AppConstants.ACTIVITY_UPDATE);
                repair.setActionStatus(AppConstants.ACTIVITY_APPROVE);

                repairs.add(repair);
                Repair x = repairRepository.save(repair);

                //Logger Service
                loggerServiceTemplate.log(Constants.DESCRIPTION,Constants.REPAIR,x.getId(),AppConstants.ACTIVITY_CREATE,AppConstants.STATUS_COMPLETED,Constants.NOTES);

            }

        }

        responseWrapper.setData(repairs);
        responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("Content Updated Successfully");

        return responseWrapper;
    }

    @Transactional
    public ResponseWrapper findRepairByUser(Pageable pg) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        Long id = logExtras.getUserId();

        Page<Repair> repairs = repairRepository.findAllByCurrentUser(id,pg);

        if (Objects.isNull(repairs)) {
            responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
            responseWrapper.setCode(HttpStatus.NOT_FOUND.value());
            responseWrapper.setMessage("Content not found");

            return responseWrapper;
        }


        responseWrapper.setData(repairs);
        responseWrapper.setCode(HttpStatus.OK.value());
        return responseWrapper;

    }

    @Transactional
    public ResponseWrapper searchRepairsByCombinedQuery(SearchWrapper wrapper, Pageable pg) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        Page<Repair> pageResponse = null;

        if (Objects.nonNull(wrapper.getCustomer()) && Objects.nonNull(wrapper.getLevel()) && Objects.nonNull(wrapper.getTechnician()) && Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo())) {
            UfsUser ufsUser = userRepository.findUfsUserByUserId(wrapper.getTechnician());
            pageResponse = repairRepository.findAllByCustomersAndLevelsAndUsersAndCreatedOnBetween(wrapper.getCustomer(),wrapper.getLevel(),ufsUser,wrapper.getFrom(),wrapper.getTo(),pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getRepairStatus()) && Objects.nonNull(wrapper.getTechnician())) {
            UfsUser ufsUser = userRepository.findUfsUserByUserId(wrapper.getTechnician());
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndRepairStatusAndUsers(wrapper.getFrom(), wrapper.getTo(), wrapper.getRepairStatus(),ufsUser, pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getRepairStatus()) && Objects.nonNull(wrapper.getCustomer())) {
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndRepairStatusAndCustomers(wrapper.getFrom(), wrapper.getTo(), wrapper.getRepairStatus(),wrapper.getCustomer(), pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getLevel()) && Objects.nonNull(wrapper.getCustomer())) {
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndLevelsAndCustomers(wrapper.getFrom(), wrapper.getTo(), wrapper.getLevel(),wrapper.getCustomer(), pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getTechnician()) && Objects.nonNull(wrapper.getCustomer())) {
            UfsUser ufsUser = userRepository.findUfsUserByUserId(wrapper.getTechnician());
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndUsersAndCustomers(wrapper.getFrom(), wrapper.getTo(),ufsUser,wrapper.getCustomer(), pg);
        } else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getLevel()) && Objects.nonNull(wrapper.getTechnician())) {
            UfsUser ufsUser = userRepository.findUfsUserByUserId(wrapper.getTechnician());
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndLevelsAndUsers(wrapper.getFrom(), wrapper.getTo(), wrapper.getLevel(),ufsUser, pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getTechnician())){
            UfsUser ufsUser = userRepository.findUfsUserByUserId(wrapper.getTechnician());
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndUsers(wrapper.getFrom(),wrapper.getTo(),ufsUser,pg);
        } else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getLevel())) {
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndLevels(wrapper.getFrom(),wrapper.getTo(),wrapper.getLevel(),pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getCustomer())) {
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndCustomers(wrapper.getFrom(), wrapper.getTo(), wrapper.getCustomer(), pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo()) && Objects.nonNull(wrapper.getRepairStatus())) {
            pageResponse = repairRepository.findAllByCreatedOnBetweenAndRepairStatus(wrapper.getFrom(), wrapper.getTo(), wrapper.getRepairStatus(), pg);
        }else if (Objects.nonNull(wrapper.getFrom()) && Objects.nonNull(wrapper.getTo())) {
            pageResponse = repairRepository.findAllByCreatedOnBetween(wrapper.getFrom(), wrapper.getTo(), pg);
        }

        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setData(pageResponse);
        responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
        return responseWrapper;

    }

    @Transactional
    @Async
    public void sendEmail(List<String> devices){
        String path = "../atlas/uploads/" + createExcelFile(devices);
        UfsSysConfig ufsSysConfig = ufsSysConfigRepository.getConfiguration("Global Configuration","respondantMail");
        notifyService.sendEmailWithAttachment(ufsSysConfig.getValue(),"Unboarded Devices", "Please Onboard the following devices first", path);
    }

    @Transactional
    public String createExcelFile(List<String> devices) {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Unboarded Devices");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Serial Number");
        cell.setCellStyle(headerCellStyle);

        int rowNum = 1;
        for (String device: devices) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(device);
        }

        sheet.autoSizeColumn(0);

        String file_title = RandomStringUtils.random(10, true, false);

        try {

            String uploadDirectory = environment.getProperty("file.upload-dir");
            String file = file_title + ".xlsx";
            String fullFilePath = uploadDirectory + "/" + file;

            FileOutputStream fileOut = new FileOutputStream(fullFilePath);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseWrapper findRepairBySerialNumber(SearchWrapper wrapper, Pageable pg) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        Optional<List<Repair>> optional = repairRepository.findRepairsBySerialNumber(wrapper.getSerialNo());
        List<Repair> repairArrayList;

        if (optional.isPresent()) {
            repairArrayList = optional.get();

            Page<Repair> pageResponse = new PageImpl<>(repairArrayList, pg, repairArrayList.size());

            responseWrapper.setMessage("Repairs fetched successfully");
            responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
            responseWrapper.setData(pageResponse);
            responseWrapper.setCode(HttpStatus.OK.value());

            return responseWrapper;
        } else {
            Page<Repair> pageResponse = repairRepository.findAllByCreatedOnBetween(wrapper.getFrom(),wrapper.getTo(),pg);
            responseWrapper.setMessage("Repairs fetched successfully");
            responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
            responseWrapper.setData(pageResponse);
            responseWrapper.setCode(HttpStatus.OK.value());

            return responseWrapper;
        }
    }

    public void generateReportFile(SearchWrapper wrapper) {
        generateFile(wrapper);
    }

    @Async
    @Transactional
    public void generateFile(SearchWrapper wrapper){
        List<UfsUser> technicians = userRepository.findAll().stream().filter(x -> x.getUserType().getUserType().equalsIgnoreCase("Technician")).collect(Collectors.toList());
        List<String> parts = partsRepository.findAll().stream().map(Parts::getPartName).collect(Collectors.toList());

        //Headers
        List<String> columns_ = new ArrayList<>();
        columns_.addAll(Arrays.asList("TECHNICIANS", "WORK ASSIGNED", "DIAGNONSED", "OTHER ACTIVITIES", "REPAIRED", "TIME TAKEN (hrs)",
                "TARGET", " ", "Level 1", "Level 2", "Level 3", "Level 4", " "));

        columns_.addAll(parts);
        columns_.add("Total Parts Used");
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Repair Data");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns_.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns_.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with repair data
        int rowNum = 1;
        LocalDateTime from = wrapper.getFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime to = wrapper.getTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        for (LocalDateTime date = from;date.isBefore(to);date = date.plusDays(1)) {

            for(UfsUser technician: technicians) {
                Map<String, Integer> map = new HashMap<>();
                for (String part_ : parts) {
                    map.put(part_, 0);
                }
                Row row = sheet.createRow(rowNum++);
                DateFromToWrapper dateFromToWrapper = sharedMethods.fromTo(date,PeriodRange.DAYS);

                row.createCell(0)
                        .setCellValue(technician.getFullName());

                List<Repair> repairList=repairRepository.findAllByCreatedOnBetweenAndUsers(dateFromToWrapper.getFrom(),dateFromToWrapper.getTo(),technician);
                if (repairList.size() > 0) {
                    row.createCell(1)
                            .setCellValue(repairList.get(0).getCustomers());

                    int repaired = 0;
                    int level_1 = 0;
                    int level_2 = 0;
                    int level_3 = 0;
                    int level_4 = 0;

                    for (Repair repair_: repairList) {
                        if (repair_.getRepairStatus().equalsIgnoreCase(Constants.STATUS_REPAIRED)) {
                            repaired++;

                            if (repair_.getLevels().equalsIgnoreCase(Constants.LEVEL_1)) {
                                level_1++;
                            } else if(repair_.getLevels().equalsIgnoreCase(Constants.LEVEL_2)) {
                               level_2++;
                            }else  if (repair_.getLevels().equalsIgnoreCase(Constants.LEVEL_3)) {
                                level_3++;
                            }else {
                                level_4++;
                            }

                            int value = 0;
                            for (Parts parts1: repair_.getParts()){
                                map.put(parts1.getPartName(),value++);
                            };


                        }
                    }

                    row.createCell(4)
                            .setCellValue(repaired);

                    row.createCell(8)
                            .setCellValue(level_1);

                    row.createCell(9)
                            .setCellValue(level_2);

                    row.createCell(10)
                            .setCellValue(level_3);

                    row.createCell(11)
                            .setCellValue(level_4);

                    AtomicInteger cell_ = new AtomicInteger(13);
                    map.keySet().stream().forEach(x -> {
                        row.createCell(cell_.get())
                                .setCellValue(map.get(x));
                        cell_.getAndIncrement();
                    });
                }

            }

            sheet.createRow(rowNum++);
        }



        // Resize all columns to fit the content size
        for(int i = 0; i < columns_.size() - 1; i++) {
            sheet.autoSizeColumn(i);
        }


        try {
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
            workbook.write(fileOut);

            fileOut.close();

            // Closing the workbook
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

