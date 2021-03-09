package com.tracom.atlas.entity;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Nelson
 */

@Entity
@Table( name = "atlas_printers" )
public class Printers {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "printer_Id" )
    private Long id;

    @ModifiableField
    @Searchable
    @Column( name = "printer_name", unique = true )
    private String name;

    @ModifiableField
    @Searchable
    @Column( name = "manufacturer")
    private String manufacturer;

    @ModifiableField
    @Searchable
    @Column( name = "serialNumber", unique = true )
    private String serialnumber;

    @Searchable
    @Column( name = "action" )
    private String action;
    @Filter
    @Searchable
    @Column( name = "action_status" )
    private String actionStatus;
    @Searchable
    @Column( name = "intrash" )
    private String intrash;
    @Filter
    @Basic( optional = false )
    @Column( name = "creation_date" )
    private Date creationDate = new Date(System.currentTimeMillis());

    public Printers() {
    }

    public Printers(String name, String manufacturer, String serialnumber, String action, String actionStatus, String intrash, Date creationDate) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.serialnumber = serialnumber;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Printers setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Printers setName(String name) {
        this.name = name;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Printers setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public Printers setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Printers setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public Printers setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public Printers setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Printers setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "Printers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
