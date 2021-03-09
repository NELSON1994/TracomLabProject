package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


/**
 * @author Nelson
 */
@Repository
public interface DevicesRepository extends JpaRepository<Devices, Long> {
    Optional<Devices> findById(Long id);
    Optional<Devices> findDevicesBySerialnumber(String serialNumber);
    Optional<Devices> findDevicesByDeviceownerAndSerialnumber(String deviceowner, String serialnumber);
    Optional<List<Devices>> findDevicesByDeviceownerAndSerialnumberIsStartingWith(String deviceowner, int year);
    long countDevicesByDeviceownerAndDeviceContractStatus(String deviceowner, String contractStatus);
   Optional<List<Devices>> findDevicesByDeviceownerAndDeviceContractStatus(String deviceowner, String contractstatus);
   Optional<List<Devices>> findDevicesByIntrash(String intrash);
   long countDevicesByDeviceownerAndDeviceWarantyStatus(String name, String warrantyYes);
   Optional<List<Devices>> findByDeviceownerAndActionStatus(String status, String deviceowner);
   Optional<List<Devices>> findDevicesByDeviceownerAndCreationDateBetween(String deviceowner,Date from , Date to);
   Optional<List<Devices>>findDevicesByCreationDateBetween(Date from , Date to);
   Optional<List<Devices>> findByActionStatus(String actionstatus);
   long countDevicesByActionStatusAndIntrash(String actionstatus, String intrash);
}
