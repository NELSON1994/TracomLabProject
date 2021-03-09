package com.tracom.atlas.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tracom.atlas.entity.Parts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class inventoryReport {

        private static final Logger logger = LoggerFactory.getLogger(com.tracom.atlas.util.inventoryReport.class);

        public static ByteArrayInputStream PartsReport(List<Parts> parts){

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Font f = new Font();


            try {

                PdfPTable tb = new PdfPTable(4);
                tb.setWidthPercentage(100);
                tb.setWidths(new int[]{4, 3, 5,5,4,4});

                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase("part name", headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tb.addCell(hcell);

                hcell = new PdfPCell(new Phrase("part description", headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tb.addCell(hcell);

                hcell = new PdfPCell(new Phrase("part number", headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tb.addCell(hcell);

                hcell = new PdfPCell(new Phrase("approved/unapproved", headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tb.addCell(hcell);
                for (Parts part : parts) {
                    PdfPCell cellr;
                    cellr = new PdfPCell(new Phrase(part.getPartName()));
                    cellr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tb.addCell(cellr);

                    cellr = new PdfPCell(new Phrase(part.getDescription()));
                    cellr.setPaddingLeft(2);
                    cellr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellr.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tb.addCell(cellr);

                    cellr = new PdfPCell(new Phrase(part.getPartNumber()));
                    cellr.setPaddingLeft(2);
                    cellr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellr.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tb.addCell(cellr);

                    cellr = new PdfPCell(new Phrase(part.getPartName()));
                    cellr.setPaddingLeft(2);
                    cellr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellr.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tb.addCell(cellr);

                    cellr = new PdfPCell(new Phrase(part.getActionStatus()));
                    cellr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellr.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cellr.setPaddingRight(2);
                    tb.addCell(cellr);

                }

                PdfWriter.getInstance(document, out);
                document.open();
                document.add(tb);

                document.close();

            } catch (DocumentException ex) {
                logger.error("Error  the datails: {0}", ex);
            }
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


