package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Nelson
 */

@Entity
@Table( name = "atlas_partsIssued" )
public class PartsIssued {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "partsIssued_Id" )
    private Long id;

    @Searchable
    @Column( name = "parts_issued" )
    private int partsIssued;

    @ModifiableField
    @Searchable
    @Column( name = "part_number" )
    private String partNumber;

    @Searchable
    @Column( name = "date_issued" )
    private String dateIssued;

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

    public PartsIssued() {
    }

    public PartsIssued(Integer partsIssued, String partNumber, String dateIssued, String action, String actionStatus, String intrash, Date creationDate) {
        this.partsIssued = partsIssued;
        this.partNumber = partNumber;
        this.dateIssued = dateIssued;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public PartsIssued setId(Long id) {
        this.id = id;
        return this;
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

    public PartsIssued setPartNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getAction() {
        return action;
    }

    public PartsIssued setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public PartsIssued setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public PartsIssued setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PartsIssued setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public PartsIssued setDateIssued(String dateIssued) {

        this.dateIssued = dateIssued.replace("-" , "");
        return this;
    }

    @Override
    public String toString() {
        return "PartsIssued{" +
                "id=" + id +
                ", partsIssued=" + partsIssued +
                ", partNumber='" + partNumber + '\'' +
                ", dateIssued='" + dateIssued + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
