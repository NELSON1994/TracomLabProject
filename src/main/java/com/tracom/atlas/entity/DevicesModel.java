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
@Table( name = "atlas_devicetypes" )
public class DevicesModel {


    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "device_Id" )
    private Long id;

    @ModifiableField
    @Searchable
    @Column( name = "device_model" , unique = true)
    private String name;
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

    public DevicesModel() {
    }

    public DevicesModel(String name, String action, String actionStatus, String intrash, Date creationDate) {
        this.name = name;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public DevicesModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public DevicesModel setName(String name) {
        this.name = name.toUpperCase();
        return this;
    }

    public String getAction() {
        return action;
    }

    public DevicesModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public DevicesModel setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public DevicesModel setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public DevicesModel setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "DevicesModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
