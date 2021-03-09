/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.repository;


import com.tracom.atlas.entity.UfsRole;
import com.tracom.atlas.entity.UfsRolePermission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author eli.muraya
 */
public interface RolePermissionRepository extends CrudRepository<UfsRolePermission, Long> {

    List<UfsRolePermission> findByRole(UfsRole role);

}
