package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Orders;
import com.tracom.atlas.entity.PartsIssued;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * aurthor Nelson
 */
@Repository
public interface OrdersRepository extends CrudRepository<Orders,Long> {
    List<Orders> findByPonumberAndIntrash(String ponumber , String intrash);
    Optional<List<Orders>> findByDtpurchasedStartingWithOrDtpurchasedStartingWithOrDtpurchasedStartingWith(String one , String two ,String three );
    Optional<Orders> findByPartnumberAndPonumber(String partnumber , String ponumber );
}
