package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Printers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nelson
 */
@Repository
public interface PrintersRepository  extends JpaRepository<Printers,Long> {


}
