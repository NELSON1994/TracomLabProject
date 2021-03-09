package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Delivery;
import com.tracom.atlas.entity.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

    List<Delivery> findAllByCustomers(String customers);

    List<Delivery> findDeliveriesByIntrash(String intrash);

}
