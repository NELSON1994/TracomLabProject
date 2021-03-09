package com.tracom.atlas.repository;

import com.tracom.atlas.entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nelson
 */

@Repository
public interface WhiteListRepository  extends JpaRepository<WhiteList, Long> {
    List<WhiteList> findAll();
}
