/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tracom.atlas.repository;

import com.tracom.atlas.entity.UfsWorkgroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author eli.muraya
 */
public interface WorkgroupRepository extends CrudRepository<UfsWorkgroup, Long> {

    Page<UfsWorkgroup> findAll(Pageable pg);

    @Override
    Optional<UfsWorkgroup> findById(Long aLong);
}
