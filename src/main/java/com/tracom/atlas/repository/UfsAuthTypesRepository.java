/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.repository;

import com.tracom.atlas.entity.UfsAuthenticationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *
 * @author eli.muraya
 */
@Repository
public interface UfsAuthTypesRepository extends CrudRepository<UfsAuthenticationType, BigDecimal> {
    
}
