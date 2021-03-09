package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(name = "atlas_delivery")
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @OneToOne
    @JoinTable(name = "atlas_devices_delivery",
            joinColumns = { @JoinColumn(name = "delivery_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "devices_Id", referencedColumnName = "device_Id") })
    public Devices devices;

    @Searchable
    @Column(name = "CUSTOMERS",length = 25)
    public String customers;

    @ModifiableField
    @Column(name = "ACTION", nullable = false)
    public String action;

    @Column(name = "DELIVERY_STATUS", length = 20)
    @ModifiableField
    public String deliveryStatus;

    @Filter
    @Column(name = "ACTION_STATUS",length = 15,updatable = false,insertable = false)
    public String actionStatus;

    @ModifiableField
    @Column(name = "INTRASH", length = 3)
    public String intrash;

    @Searchable
    @Column(name = "SERIAL_NUMBER")
    public String serialNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATION_ON",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Filter
    public Date creationOn;

    @ModifiableField
    @Column(name = "LOCATION",length = 30)
    public String location;

    @ModifiableField
    @Column(name = "DELIVERED_BY", length = 30)
    public String deliveredBy;

    @JsonIgnore
    public int week;

    @JsonIgnore
    public int year;

    public Delivery(Devices devices, String customers,String action, String deliveryStatus, String actionStatus, String intrash, String serialNumber, int week, int year) {
        this.devices = devices;
        this.customers = customers;
        this.action = action;
        this.deliveryStatus = deliveryStatus;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.serialNumber = serialNumber;
        this.deliveredBy = deliveredBy;
        this.week = week;
        this.year = year;
    }
}
