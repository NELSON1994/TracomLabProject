package com.tracom.atlas.repository;

import com.tracom.atlas.entity.PartsRequsition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author Nelson
 */
@Repository
public interface PartsRequsitionRepository extends JpaRepository<PartsRequsition , Long> {

}
