package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tracom.atlas.config.Constants;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import ke.axle.chassis.annotations.Unique;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author Nelson
 */
@Entity
@Table( name = "atlas_devices" )
public class Devices implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "device_Id" )
    private Long id;

    @Basic( optional = false )
    @Searchable
    @Column( name = "serial_number",unique = true)
    @Unique
    private String serialnumber;

    @Searchable
    @Basic( optional = false )
    @Column( name = "part_number" )
    private String partnumber;

    @Searchable
    @Column( name = "imei_number" )
    private String imeinumber;
    @ModifiableField

    @Searchable
    @Column( name = "device_model" )
    private String deviceModels;
    @ModifiableField
    @Searchable
    @Column( name = "manufacturer" )
    private String manufacturer;

    @ModifiableField
    @Searchable
    @Basic( optional = false )
    @Column( name = "device_owner" )
    private String deviceowner;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "contract_period" )
    private int contractperiod;

    @ModifiableField
    @Searchable
    @Column( name = "device_contract" )
    private String deviceContractStatus;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "warranty_period" )
    private int warrantyperiod;

    @ModifiableField
    @Searchable
    @Basic( optional = false )
    @Column( name = "device_warranty" )
    private String deviceWarantyStatus;

    @ModifiableField
    @Basic( optional = false )
    @JsonFormat( pattern = "yyyy/MM/dd" )
    @Column( name = "warrantyexpires" )
    private java.util.Date warrantyExpire;

    @ModifiableField
    @JsonFormat( pattern = "yyyy/MM/dd" )
    @Column( name = "warranty_starts" )
    private java.util.Date warranty_starts;

    @ModifiableField
    @JsonFormat( pattern = "yyyy/MM/dd" )
    @Column( name = "contract_starts" )
    private java.util.Date contract_starts;

    @JsonFormat( pattern = "yyyy/MM/dd" )
    @Column( name = "contract_expires" )
    private java.util.Date contract_expires;

    @ModifiableField
    @Searchable
    @Column( name = "seller" )
    private String seller;

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

    public Devices() {
    }

    public Devices(String serialnumber, String partnumber, String imeinumber, String deviceModels, String manufacturer, String deviceowner, int contractperiod, String deviceContractStatus, int warrantyperiod, String deviceWarantyStatus, java.util.Date warrantyExpire, java.util.Date warranty_starts, java.util.Date contract_starts, java.util.Date contract_expires, String seller, String action, String actionStatus, String intrash, Date creationDate) {
        this.serialnumber = serialnumber;
        this.partnumber = partnumber;
        this.imeinumber = imeinumber;
        this.deviceModels = deviceModels;
        this.manufacturer = manufacturer;
        this.deviceowner = deviceowner;
        this.contractperiod = contractperiod;
        this.deviceContractStatus = deviceContractStatus;
        this.warrantyperiod = warrantyperiod;
        this.deviceWarantyStatus = deviceWarantyStatus;
        this.warrantyExpire = warrantyExpire;
        this.warranty_starts = warranty_starts;
        this.contract_starts = contract_starts;
        this.contract_expires = contract_expires;
        this.seller = seller;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Devices setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public Devices setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
        return this;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public Devices setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getImeinumber() {
        return imeinumber;
    }

    public Devices setImeinumber(String imeinumber) {
        this.imeinumber = imeinumber;
        return this;
    }

    public String getDeviceModels() {
        return deviceModels;
    }

    public Devices setDeviceModels(String deviceModels) {
        this.deviceModels = deviceModels;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Devices setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getDeviceowner() {
        return deviceowner;
    }

    public Devices setDeviceowner(String deviceowner) {
        this.deviceowner = deviceowner;
        return this;
    }

    public int getContractperiod() {
        return contractperiod;
    }

    public Devices setContractperiod(int contractperiod) {
        this.contractperiod = contractperiod;
        return this;
    }

    public String getDeviceContractStatus() {
        return deviceContractStatus;
    }

    public Devices setDeviceContractStatus(String deviceContractStatus) {
        this.deviceContractStatus =deviceContractStatus;
        return this;
    }

    public int getWarrantyperiod() {
        return warrantyperiod;
    }

    public Devices setWarrantyperiod(int warrantyperiod) {
        this.warrantyperiod = warrantyperiod;
        return this;
    }

    public String getDeviceWarantyStatus() {
        return deviceWarantyStatus;
    }

    public Devices setDeviceWarantyStatus(String deviceWarantyStatus) {
        this.deviceWarantyStatus = deviceWarantyStatus;
        return this;
    }

    public java.util.Date getWarrantyExpire() {
        return warrantyExpire;
    }

    public Devices setWarrantyExpire(java.util.Date warrantyExpire) {
        this.warrantyExpire =  warrantyExpire;
        return this;
    }

    public java.util.Date getWarranty_starts() {
        return warranty_starts;
    }

    public Devices setWarranty_starts(java.util.Date warranty_starts) {
        this.warranty_starts = warranty_starts;
        return this;
    }

    public java.util.Date getContract_starts() {
        return contract_starts;
    }

    public Devices setContract_starts(java.util.Date contract_starts) {
        this.contract_starts = contract_starts;
        return this;
    }

    public java.util.Date getContract_expires() {
        return contract_expires;
    }

    public Devices setContract_expires(java.util.Date contract_expires) {
        this.contract_expires = contract_expires;
        return this;
    }

    public String getSeller() {
        return seller;
    }

    public Devices setSeller(String seller) {
        this.seller = seller;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Devices setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public Devices setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public Devices setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Devices setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "Devices{" +
                "id=" + id +
                ", serialnumber='" + serialnumber + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", imeinumber='" + imeinumber + '\'' +
                ", deviceModels='" + deviceModels + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", deviceowner='" + deviceowner + '\'' +
                ", contractperiod=" + contractperiod +
                ", deviceContractStatus='" + deviceContractStatus + '\'' +
                ", warrantyperiod=" + warrantyperiod +
                ", deviceWarantyStatus='" + deviceWarantyStatus + '\'' +
                ", warrantyExpire=" + warrantyExpire +
                ", warranty_starts=" + warranty_starts +
                ", contract_starts=" + contract_starts +
                ", contract_expires=" + contract_expires +
                ", seller='" + seller + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
