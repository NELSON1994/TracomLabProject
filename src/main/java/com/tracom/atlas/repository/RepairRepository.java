package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Delivery;
import com.tracom.atlas.entity.Devices;
import com.tracom.atlas.entity.Repair;
import com.tracom.atlas.entity.UfsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair,Long> {

    List<Repair> findRepairsByRepairStatusIsNotAndIntrash(String action, String inTrash);
    Optional<List<Repair>> findRepairsByIntrashAndCustomers(String inTrash, String customers);
    Optional<List<Repair>> findRepairsByCreatedOnBetween(Date startDate, Date stopDate);
    Optional<List<Repair>> findRepairsByIntrash( String inTrash);
    Optional<List<Repair>> findRepairsBySerialNumber(String serialNumber);
    Page<Repair> findAllByCreatedOnBetween(Date mfrom, Date mto,Pageable pg);
    List<Repair> findAllBySerialNumberAndIntrash(String serialNumber,String intrash);
    Page<Repair> findAllByCurrentUser(Long id, Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndCustomers(Date mfrom, Date mto, String customers,Pageable pg);
    Page<Repair> findAllByCustomersAndLevelsAndUsersAndCreatedOnBetween(String customers, String levels, UfsUser user, Date mfrom,Date mto,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndUsers(Date mfrom,Date mto,UfsUser user,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndLevels(Date mfrom,Date mto,String levels,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndRepairStatus(Date mfrom, Date mto, String repairStatus,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndRepairStatusAndUsers(Date mfrom, Date mto, String repairStatus,UfsUser user,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndRepairStatusAndCustomers(Date mfrom, Date mto, String repairStatus,String customer,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndLevelsAndCustomers(Date mfrom, Date mto, String repairStatus,String customer,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndLevelsAndUsers(Date mfrom, Date mto, String repairStatus,UfsUser user,Pageable pg);
    Page<Repair> findAllByCreatedOnBetweenAndUsersAndCustomers(Date mfrom, Date mto, UfsUser user,String customers,Pageable pg);
    List<Repair> findAllByCreatedOnBetweenAndUsers(Date mfrom,Date mto,UfsUser user);
    List<Repair> findAllBySerialNumberAndRepairStatus(String serialno,String repairStatus);
    Long countAllByRepairCentre(String repairCentre);
    Long countAllByRepairStatus(String repairStatus);
    Long countAllByLevels(String levels);
    Long countAllByLevelsAndRepairCentre(String levels,String repaircentre);
    Long countAllByCustomersAndLevels(String customers,String levels);
    Long countAllByLevelsAndRepairCentreAndCustomers(String levels,String repaircentre,String customer);
}
