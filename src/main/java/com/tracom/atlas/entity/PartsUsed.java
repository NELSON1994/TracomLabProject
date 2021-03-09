package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Nelson
 */

@Entity
@Table( name = "atlas_partsUsed" )
public class PartsUsed {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "partsIssued_Id" )
    private Long id;


    @Searchable
    @Column( name = "parts_used" )
    private int partsIssued;
    @ModifiableField

    @Searchable
    @Column( name = "part_number" )
    private String partNumber;

    @ModifiableField
    @Searchable
    @Column( name = "action" )
    private String action;

    @ModifiableField
    @Filter
    @Searchable
    @Column( name = "action_status" )
    private String actionStatus;

    @Searchable
    @Column( name = "intrash" )
    private String intrash;

    @CreatedDate
    @JsonFormat( pattern = "yyyy/MM/dd" )
    @Basic( optional = false )
    @Column( name = "creation_date", updatable = false )
    private Date creationDate = new Date(System.currentTimeMillis());

    public PartsUsed() {
    }

    public PartsUsed(int partsIssued, String partNumber, String action, String actionStatus, String intrash, Date creationDate) {
        this.partsIssued = partsIssued;
        this.partNumber = partNumber;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPartsIssued() {
        return partsIssued;
    }

    public void setPartsIssued(int partsIssued) {
        this.partsIssued = partsIssued;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "PartsUsed{" +
                "id=" + id +
                ", partsIssued=" + partsIssued +
                ", partNumber='" + partNumber + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
