package com.tracom.atlas.repository;

import com.tracom.atlas.entity.UfsEdittedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdittedRepository extends JpaRepository<UfsEdittedRecord,Long> {

}
