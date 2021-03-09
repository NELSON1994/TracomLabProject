package com.tracom.atlas.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tracom.atlas.entity.Repair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
    /**
     * @author  maurice momondi
     *
     * pdf report generator
     */
    public class PartHistoryReport {

        private static Logger logger = LoggerFactory.getLogger(Repair.class);

        public static ByteArrayInputStream partHistoryPDFReport(List<Repair> partHistory) throws IOException {

            ByteArrayOutputStream out = new ByteArrayOutputStream() ;

            try {
                Document doc = new Document();
                doc.open();

                PdfPTable t = new PdfPTable(5);
                t.setWidthPercentage(100);
                t.setWidths(new int[]{2, 3, 3,2,2});

                Font hFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

                PdfPCell hc;
                hc = new PdfPCell(new Phrase("Device Serial", hFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                t.addCell(hc);

                hc = new PdfPCell(new Phrase("Part used", hFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                t.addCell(hc);


                hc = new PdfPCell(new Phrase("Batch Number", hFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                t.addCell(hc);

                hc = new PdfPCell(new Phrase("Device Owner", hFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                t.addCell(hc);

                hc = new PdfPCell(new Phrase("Repair Center", hFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                t.addCell(hc);

                for (Repair parthist : partHistory) {

                    PdfPCell cell;

                    cell = new PdfPCell(new Phrase(parthist.getDevices().getSerialnumber()));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    t.addCell(cell);

                    cell = new PdfPCell(new Phrase(parthist.getDevices().getPartnumber()));
                    cell.setPaddingLeft(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    t.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(parthist.getBatchNumber())));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    t.addCell(cell);


                    cell = new PdfPCell(new Phrase(parthist.getDevices().getDeviceowner()));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    t.addCell(cell);

                    cell = new PdfPCell(new Phrase(parthist.repairCentre));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    t.addCell(cell);
                }

                PdfWriter.getInstance(doc, out);
                doc.open();
                doc.add( t);

                doc.close();

            } catch (DocumentException ex) {

                logger.error("Error occurred: {0}", ex);
            }

            return new ByteArrayInputStream(out.toByteArray());
        }




}
