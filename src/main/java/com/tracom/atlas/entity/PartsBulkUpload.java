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
@Table( name = "atlas_partsRequsition" )
public class PartsBulkUpload {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "parts_Id" )
    private Long id;

    @ModifiableField
    @Searchable
    @Column( name = "model" )
    private String model;

    @ModifiableField
    @Column( name = "quantity" )
    private int quantity;

    @ModifiableField
    @Searchable
    @Column( name = "part_number" )
    private String partnumber;

    @ModifiableField
    @Searchable
    @Column( name = "description" )
    private String description;

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

    public PartsBulkUpload() {
    }

    public PartsBulkUpload(String model, int quantity, String partnumber, String description, String action, String actionStatus, String intrash, Date creationDate) {
        this.model = model;
        this.quantity = quantity;
        this.partnumber = partnumber;
        this.description = description;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public PartsBulkUpload setId(Long id) {
        this.id = id;
        return this;
    }

    public String getModel() {
        return model;
    }

    public PartsBulkUpload setModel(String model) {
        this.model = model;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public PartsBulkUpload setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public PartsBulkUpload setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PartsBulkUpload setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAction() {
        return action;
    }

    public PartsBulkUpload setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public PartsBulkUpload setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public PartsBulkUpload setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PartsBulkUpload setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "PartsBulkUpload{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", quantity=" + quantity +
                ", partnumber='" + partnumber + '\'' +
                ", description='" + description + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
