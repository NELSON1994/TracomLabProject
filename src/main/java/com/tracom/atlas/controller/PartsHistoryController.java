package com.tracom.atlas.controller;

import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsEdittedRecord;
import com.tracom.atlas.repository.RepairRepository;
import ke.axle.chassis.ChasisResource;
import ke.axle.chassis.utils.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


/**
 * End points for parts history
 *
 * @author momondi
 */


@RestController
@RequestMapping(value = "/parts_history")
public class PartsHistoryController extends ChasisResource<Repair, Long, UfsEdittedRecord> {

    @Autowired
    private RepairRepository repairRepository;

    public PartsHistoryController(LoggerService loggerService, EntityManager entityManager) {

        super(loggerService, entityManager);
    }

//  @GetMapping(value = "/pdf_part_history",
//            produces = MediaType.ALL_VALUE)
//   public ResponseEntity<InputStreamResource> partspdfReport() throws IOException {
//       List<Repair> partsHistory = (List<Repair>) repairRepository.findAll();
//       ByteArrayInputStream bis = PartHistoryReport.partHistoryPDFReport( partsHistory);
//       HttpHeaders headers = new HttpHeaders();
//       headers.setContentType(MediaType.IMAGE_JPEG);
//       headers.add("Content-Disposition", "inline; filename=part_history_report.pdf");
//
//        return ResponseEntity
//                .ok()
//               .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(new InputStreamResource());
//   }

}
