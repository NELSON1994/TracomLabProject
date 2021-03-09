package com.tracom.atlas.entity;


import ke.axle.chassis.annotations.Searchable;

import javax.persistence.*;
import java.util.*;

/**
 * @author Nelson
 */

@Entity
@Table(name = "atlas_whitelist")
public class WhiteList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "whitelist_Id")
    private Long id;
    @Searchable
    @Column(name = "article_number" , unique = true)
    private String article_number;

    @Column(name = "serial_number" , unique = true)
    private int serialnumber;
    @Searchable
    @Column(name = "part_number")
    private String partnumber;

    @Column(name = "batch_number")
    private int batchnumber;
    @Searchable
    @Column(name = "description")
    private String description;
    @Searchable
    @Column(name = "deliverydate")
    private String deliverydate;
    @Searchable
    @Column(name = "action")
    private String action;
    @Searchable
    @Column(name = "action_status")
    private String actionStatus;
    @Searchable
    @Column(name = "intrash")
    private String intrash;
    @Basic(optional = false)

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate =  new Date();
    // one to many relationship between whitelist and devices
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ATLAS_WHITELIST_DEVICES",
            joinColumns = {@JoinColumn(name = "whitelist_id" , referencedColumnName = "whitelist_Id")},
            inverseJoinColumns = {@JoinColumn(name = "devices_id" , referencedColumnName = "device_Id")}
    )
    private List<Devices> devices;

    public WhiteList() {
    }

    public WhiteList(String article_number, int serialnumber, String partnumber, int batchnumber, String description, String deliverydate, String action, String actionStatus, String intrash, Date creationDate, List<Devices> devices) {
        this.article_number = article_number;
        this.serialnumber = serialnumber;
        this.partnumber = partnumber;
        this.batchnumber = batchnumber;
        this.description = description;
        this.deliverydate = deliverydate;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public WhiteList setId(Long id) {
        this.id = id;
        return this;
    }

    public String getArticle_number() {
        return article_number;
    }

    public WhiteList setArticle_number(String article_number) {
        this.article_number = article_number;
        return this;
    }


    public String getPartnumber() {
        return partnumber;
    }

    public WhiteList setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WhiteList setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public WhiteList setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
        return this;
    }

    public String getAction() {
        return action;
    }

    public WhiteList setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public WhiteList setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public WhiteList setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public WhiteList setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public List<Devices> getDevices() {
        return devices;
    }

    public WhiteList setDevices(List<Devices> devices) {
        this.devices = devices;
        return this;
    }

    public int getSerialnumber() {
        return serialnumber;
    }

    public WhiteList setSerialnumber(int serialnumber) {
        this.serialnumber = serialnumber;
        return this;
    }

    public int getBatchnumber() {
        return batchnumber;
    }

    public WhiteList setBatchnumber(int batchnumber) {
        this.batchnumber = batchnumber;
        return this;
    }

    @Override
    public String toString() {
        return "WhiteList{" +
                "id=" + id +
                ", article_number=" + article_number +
                ", serialnumber='" + serialnumber + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", batchnumber='" + batchnumber + '\'' +
                ", description='" + description + '\'' +
                ", deliverydate='" + deliverydate + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                ", devices=" + devices +
                '}';
    }
}
