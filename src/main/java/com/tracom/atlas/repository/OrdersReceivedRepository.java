package com.tracom.atlas.repository;

import com.tracom.atlas.entity.OrdersReceived;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author momondi  maurice
 * order repository
 * find order by order number(ponumber)
 *
 */
@Repository
public interface OrdersReceivedRepository extends CrudRepository<OrdersReceived,Long> {
  List<OrdersReceived> findAllByIntrashAndPonumber(String Intrash, String ponumber);

}
