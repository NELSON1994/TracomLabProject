/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.repository;

import com.tracom.atlas.entity.UfsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author eli.muraya
 */
@Repository
public interface UserRepository extends JpaRepository<UfsUser, Long> {
    UfsUser findByUserId(Long id);

    UfsUser findByfullName(String name);

    UfsUser findByuserId(Long id);

    public void save(Optional<UfsUser> there);

    public UfsUser findByuserId(long id);

    UfsUser findUfsUserByUserId(Long id);

}
