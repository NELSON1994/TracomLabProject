package com.tracom.atlas.util;

import com.tracom.atlas.config.Constants;
import com.tracom.atlas.entity.Devices;
import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsSysConfig;
import com.tracom.atlas.entity.UfsUser;
import com.tracom.atlas.repository.RepairRepository;
import com.tracom.atlas.repository.UfsSysConfigRepository;
import com.tracom.atlas.service.NotifyServiceTemplate;
import ke.axle.chassis.utils.AppConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
public class Scheduler {

    private final Environment environment;
    private final NotifyServiceTemplate notifyService;
    private final RepairRepository repairRepository;
    private final UfsSysConfigRepository ufsSysConfigRepository;

    public Scheduler(Environment environment, NotifyServiceTemplate notifyService, RepairRepository repairRepository, UfsSysConfigRepository ufsSysConfigRepository) {
        this.environment = environment;
        this.notifyService = notifyService;
        this.repairRepository = repairRepository;
        this.ufsSysConfigRepository = ufsSysConfigRepository;
    }

    @Scheduled(cron = "0 0/60 * * * ?")
    @Transactional
    @Async
    public void timerLevel() {

        List<Repair> repairs = repairRepository.findRepairsByRepairStatusIsNotAndIntrash(Constants.STATUS_REPAIRED, AppConstants.NO);
        Calendar calendar = Calendar.getInstance();
        List<String> check_devices = new ArrayList<>();
        for (Repair repair: repairs) {
            String levels = repair.getLevels();
            List<Repair> devices = new ArrayList<>();
            //Duration 24hrs
            if (levels != null ) {
                if (levels.equals(Constants.LEVEL_1) && !repair.getUsers().isEmpty()) {

                    Date past_date = repair.getDateUserAdded();
                    calendar.add(Calendar.HOUR_OF_DAY,24);
                    Instant past = past_date.toInstant();
                    Instant future = calendar.toInstant();
                    Duration duration = Duration.between(future,past);

                    if (duration.isZero() || !duration.isNegative()) {
                        repair.setRepairStatus(Constants.STATUS_IMPORTANT);
                        devices.add(repair);
                        check_devices.add(repair.getDevices().getSerialnumber());
                    }
                }else if (levels.equals(Constants.LEVEL_2) && !repair.getUsers().isEmpty()) {
                    Date past_date = repair.getDateUserAdded();
                    calendar.add(Calendar.HOUR_OF_DAY,48);
                    Instant past = past_date.toInstant();
                    Instant future = calendar.toInstant();
                    Duration duration = Duration.between(future,past);

                    if (duration.isZero() || !duration.isNegative()) {
                        repair.setRepairStatus(Constants.STATUS_IMPORTANT);
                        devices.add(repair);
                        check_devices.add(repair.getDevices().getSerialnumber());
                    }
                } else if (levels.equals(Constants.LEVEL_3) && !repair.getUsers().isEmpty()) {

                    Date past_date = repair.getDateUserAdded();
                    calendar.add(Calendar.HOUR_OF_DAY,72);
                    Instant past = past_date.toInstant();
                    Instant future = calendar.toInstant();
                    Duration duration = Duration.between(future,past);

                    if (duration.isZero() || !duration.isNegative()) {
                        devices.add(repair);
                        repair.setRepairStatus(Constants.STATUS_IMPORTANT);
                        check_devices.add(repair.getDevices().getSerialnumber());
                    }
                } else if (levels.equals(Constants.LEVEL_4) && !repair.getUsers().isEmpty()) {
                    Date past_date = repair.getDateUserAdded();
                    calendar.add(Calendar.HOUR_OF_DAY,96);
                    Instant past = past_date.toInstant();
                    Instant future = calendar.toInstant();
                    Duration duration = Duration.between(future,past);

                    if (duration.isZero() || !duration.isNegative()) {
                        devices.add(repair);
                        repair.setRepairStatus(Constants.STATUS_IMPORTANT);
                        check_devices.add(repair.getDevices().getSerialnumber());
                    }
                }
            }

            repairRepository.save(repair);

        }

        if ( !check_devices.isEmpty()) {
            sendEmail(check_devices);
        }

    }

    @Transactional
    public String createExcelFile(List<String> devices) {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("UnRepaired Devices");

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
            String fullFilePath = uploadDirectory + "/" + file_title + ".xlsx";

            FileOutputStream fileOut = new FileOutputStream(fullFilePath);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();

            return fullFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    @Async
    public void sendEmail(List<String> devices){
        String path = "../atlas/uploads/" + createExcelFile(devices);
        UfsSysConfig ufsSysConfig = ufsSysConfigRepository.getConfiguration("Global Configuration","respondantMail");
        notifyService.sendEmailWithAttachment(ufsSysConfig.getValue(),"Unboarded Devices", "Please Onboard the following devices first", path);
    }
}
