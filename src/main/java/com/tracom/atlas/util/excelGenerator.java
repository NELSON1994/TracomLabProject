package com.tracom.atlas.util;

import com.tracom.atlas.entity.Parts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * excel report generator
 * @author maurice momondi
 */
public class excelGenerator {

    public static ByteArrayInputStream partsToExcel(List<Parts> parts) throws IOException {
        String[] COLUMNs = {"Part name", "Part Model", "Part Description", "Part Number"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Parts");

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


            CellStyle partnumCellStyle = workbook.createCellStyle();
            partnumCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (Parts part1 : parts) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(part1.getPartName());
                row.createCell(1).setCellValue(part1.getPartModel());
                row.createCell(2).setCellValue(part1.getDescription());

                Cell partNumberCell = row.createCell(3);
                partNumberCell.setCellValue(part1.getPartNumber());
                partNumberCell.setCellStyle(partnumCellStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
