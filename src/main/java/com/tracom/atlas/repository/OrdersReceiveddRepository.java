package com.tracom.atlas.repository;

import com.tracom.atlas.entity.OrdersReceiveddd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * aurthor Nelson
 */
@Repository
public interface OrdersReceiveddRepository extends JpaRepository<OrdersReceiveddd,Long>  {
    Optional<OrdersReceiveddd> findByPonumberAndPartnumber(String po , String pa);
    List<OrdersReceiveddd> findByPartnumberAndIntrashOrderByDatereceivedDesc(String a, String b);
}
