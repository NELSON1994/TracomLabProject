package com.tracom.atlas.repository;



import com.tracom.atlas.entity.UfsCurrency;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UfsCurrencyRepository extends CrudRepository<UfsCurrency, BigDecimal> {
}
