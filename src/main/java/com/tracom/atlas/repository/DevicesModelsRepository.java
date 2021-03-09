package com.tracom.atlas.repository;

import com.tracom.atlas.entity.DevicesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Nelson
 */
@Repository
public interface DevicesModelsRepository  extends JpaRepository<DevicesModel, Long> {

}
