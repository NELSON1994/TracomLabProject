package com.tracom.atlas.repository;

import com.tracom.atlas.entity.PartsMaxMinConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nelson
 */
@Repository
public interface PartsMaxMinConfigRepository   extends JpaRepository<PartsMaxMinConfig, Long> {

    Optional<PartsMaxMinConfig> findByPartnumberAndIntrash(String partno , String intrash);
}
