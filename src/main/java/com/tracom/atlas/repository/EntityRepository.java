/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.repository;


import com.tracom.atlas.entity.UfsEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author eli.muraya
 */
public interface EntityRepository extends CrudRepository<UfsEntity, Short> {
    
}
