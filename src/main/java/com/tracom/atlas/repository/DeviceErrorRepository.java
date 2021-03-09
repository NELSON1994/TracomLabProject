package com.tracom.atlas.repository;

import com.tracom.atlas.entity.DeviceError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DeviceErrorRepository extends JpaRepository<DeviceError,Long> {
    DeviceError findByCode(String code);

}
