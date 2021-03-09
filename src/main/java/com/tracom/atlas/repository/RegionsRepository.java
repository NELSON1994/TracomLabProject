package com.tracom.atlas.repository;

import com.tracom.atlas.entity.Regions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionsRepository extends JpaRepository<Regions,Long> {



}
