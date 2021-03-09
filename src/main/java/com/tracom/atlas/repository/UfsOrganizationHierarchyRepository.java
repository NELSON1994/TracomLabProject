package com.tracom.atlas.repository;


import com.tracom.atlas.entity.UfsOrganizationHierarchy;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface UfsOrganizationHierarchyRepository extends CrudRepository<UfsOrganizationHierarchy, BigDecimal> {

    /**
     * @param isRootTenant
     * @param intrash
     * @return
     */
    public UfsOrganizationHierarchy findByIsRootTenantAndIntrash(Short isRootTenant, String intrash);



}
