/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.repository;


import com.tracom.atlas.entity.UfsUserWorkgroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author eli.muraya
 */
@Repository
public interface UfsUserWorkgroupRepository extends CrudRepository<UfsUserWorkgroup, BigDecimal> {
    void deleteAllByUserId(Long userId);
    List<UfsUserWorkgroup> findAllByUserId(Long userId);

}
