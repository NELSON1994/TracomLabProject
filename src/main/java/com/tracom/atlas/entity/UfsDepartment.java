/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "UFS_DEPARTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UfsDepartment.findAll", query = "SELECT u FROM UfsDepartment u")
    , @NamedQuery(name = "UfsDepartment.findById", query = "SELECT u FROM UfsDepartment u WHERE u.id = :id")
    , @NamedQuery(name = "UfsDepartment.findByDepartmentName", query = "SELECT u FROM UfsDepartment u WHERE u.departmentName = :departmentName")
    , @NamedQuery(name = "UfsDepartment.findByDescription", query = "SELECT u FROM UfsDepartment u WHERE u.description = :description")
    , @NamedQuery(name = "UfsDepartment.findByCreationDate", query = "SELECT u FROM UfsDepartment u WHERE u.creationDate = :creationDate")
    , @NamedQuery(name = "UfsDepartment.findByAction", query = "SELECT u FROM UfsDepartment u WHERE u.action = :action")
    , @NamedQuery(name = "UfsDepartment.findByActionStatus", query = "SELECT u FROM UfsDepartment u WHERE u.actionStatus = :actionStatus")
    , @NamedQuery(name = "UfsDepartment.findByIntrash", query = "SELECT u FROM UfsDepartment u WHERE u.intrash = :intrash")})
public class UfsDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 10)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 10)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID")
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsOrganizationUnits tenantId;
    @OneToMany(mappedBy = "departmentId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsUser> ufsUserList;

    public UfsDepartment() {
    }

    public UfsDepartment(BigDecimal id) {
        this.id = id;
    }

    public UfsDepartment(BigDecimal id, String departmentName, String description) {
        this.id = id;
        this.departmentName = departmentName;
        this.description = description;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    @XmlTransient
    @JsonIgnore
    public List<UfsUser> getUfsUserList() {
        return ufsUserList;
    }

    public void setUfsUserList(List<UfsUser> ufsUserList) {
        this.ufsUserList = ufsUserList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UfsDepartment)) {
            return false;
        }
        UfsDepartment other = (UfsDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsDepartment[ id=" + id + " ]";
    }
    
}
