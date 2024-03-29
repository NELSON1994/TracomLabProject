/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.TreeRoot;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author ASUS
 */
@Entity
@Table(name = "UFS_BANK_REGION")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UfsBankRegion.findAll", query = "SELECT u FROM UfsBankRegion u")
        , @NamedQuery(name = "UfsBankRegion.findById", query = "SELECT u FROM UfsBankRegion u WHERE u.id = :id")
        , @NamedQuery(name = "UfsBankRegion.findByBranchName", query = "SELECT u FROM UfsBankRegion u WHERE u.branchName = :branchName")
        , @NamedQuery(name = "UfsBankRegion.findByCode", query = "SELECT u FROM UfsBankRegion u WHERE u.code = :code")
        , @NamedQuery(name = "UfsBankRegion.findByIsParent", query = "SELECT u FROM UfsBankRegion u WHERE u.isParent = :isParent")
        , @NamedQuery(name = "UfsBankRegion.findByCreationDate", query = "SELECT u FROM UfsBankRegion u WHERE u.creationDate = :creationDate")
        , @NamedQuery(name = "UfsBankRegion.findByAction", query = "SELECT u FROM UfsBankRegion u WHERE u.action = :action")
        , @NamedQuery(name = "UfsBankRegion.findByActionStatus", query = "SELECT u FROM UfsBankRegion u WHERE u.actionStatus = :actionStatus")
        , @NamedQuery(name = "UfsBankRegion.findByIntrash", query = "SELECT u FROM UfsBankRegion u WHERE u.intrash = :intrash")})
public class UfsBankRegion implements Serializable {

    @Size(max = 100)
    @Column(name = "BRANCH_NAME")
    private String branchName;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 15)
    @Column(name = "ACTION")
    private String action;
    @Size(max = 15)
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Size(max = 3)
    @Column(name = "INTRASH")
    private String intrash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankRegionId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<UfsBankBranches> ufsBankBranchesSet;
    @JoinColumn(name = "BANK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsBanks bankId;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GenericGenerator(
            name = "UFS_BANK_REGION_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "UFS_BANK_REGION_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )

    @GeneratedValue(generator = "UFS_BANK_REGION_SEQ")
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "IS_PARENT")
    private Short isParent;
    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @OneToMany(mappedBy = "parentId")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UfsBankRegion> ufsBankRegionList;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsBankRegion parentId;
    @JoinColumn(name = "TENANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private UfsOrganizationUnits tenantId;
    @Column(name = "PARENT_ID")
    @TreeRoot
    private Long parentIds;
    @Column(name = "TENANT_ID")
    private Long tenantIds;

    @Transient
    private List<UfsBankRegion> children;

    public UfsBankRegion() {
    }

    public UfsBankRegion(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    public Short getIsParent() {
        return isParent;
    }

    public void setIsParent(Short isParent) {
        this.isParent = isParent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }


    @XmlTransient
    @JsonIgnore
    public List<UfsBankRegion> getUfsBankRegionList() {
        return ufsBankRegionList;
    }

    public void setUfsBankRegionList(List<UfsBankRegion> ufsBankRegionList) {
        this.ufsBankRegionList = ufsBankRegionList;
    }

    public UfsBankRegion getParentId() {
        return parentId;
    }

    public void setParentId(UfsBankRegion parentId) {
        this.parentId = parentId;
    }

    public UfsOrganizationUnits getTenantId() {
        return tenantId;
    }

    public void setTenantId(UfsOrganizationUnits tenantId) {
        this.tenantId = tenantId;
    }

    public Long getParentIds() {
        return parentIds;
    }

    public void setParentIds(Long parentIds) {
        this.parentIds = parentIds;
    }

    public Long getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(Long tenantIds) {
        this.tenantIds = tenantIds;
    }


    public List<UfsBankRegion> getChildren() {
        return ufsBankRegionList;
    }

    public void setChildren(List<UfsBankRegion> children) {
        this.children = children;
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
        if (!(object instanceof UfsBankRegion)) {
            return false;
        }
        UfsBankRegion other = (UfsBankRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ke.tracom.ufs.entities.UfsBankRegion[ id=" + id + " ]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    @XmlTransient
    @JsonIgnore
    public Set<UfsBankBranches> getUfsBankBranchesSet() {
        return ufsBankBranchesSet;
    }

    public void setUfsBankBranchesSet(Set<UfsBankBranches> ufsBankBranchesSet) {
        this.ufsBankBranchesSet = ufsBankBranchesSet;
    }

    public UfsBanks getBankId() {
        return bankId;
    }

    public void setBankId(UfsBanks bankId) {
        this.bankId = bankId;
    }

}
