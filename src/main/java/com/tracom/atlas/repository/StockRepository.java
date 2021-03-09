package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nelson
 */
@Repository
public interface StockRepository   extends JpaRepository<Stocks, Long> {

    Optional<Stocks> findByIntrash(String intrash);

    Optional<Stocks> findAllByPartnumber(String partNumber);
List<Stocks> findByReoderStatusOrderByCreationDateDesc(String reorderStatus);
}
