package com.tracom.atlas.util;

import com.tracom.atlas.entity.Devices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DevicesExcellReport {

    public static ByteArrayInputStream DevicesExcel(List<Devices> devices) throws IOException {

        String[] COLUMNs = {"SerialNumber","PartNumber","ImeiNumber","WarrantyStatus","ContractStatus","DeviceStatus" , "DeviceOwner"};

        try(Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Devices");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            // Row for Header
            Row headerRow = sheet.createRow(0);
            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }
            CellStyle devicesCellStyle = workbook.createCellStyle();
            devicesCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
            int rowIdx = 1;
            for (Devices dev : devices) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dev.getSerialnumber());
                row.createCell(1).setCellValue(dev.getPartnumber());
                row.createCell(2).setCellValue(dev.getImeinumber());
                row.createCell(3).setCellValue(dev.getDeviceWarantyStatus());
                row.createCell(4).setCellValue(dev.getDeviceContractStatus());
                row.createCell(5).setCellValue(dev.getActionStatus());
                row.createCell(6).setCellValue(dev.getDeviceowner());

                Cell  devicesNumberCell = row.createCell(3);
                devicesNumberCell.setCellValue(dev.getDeviceowner());
                devicesNumberCell.setCellStyle(devicesCellStyle);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }
}
