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
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "ATLAS_REPAIR")
@NoArgsConstructor
@AllArgsConstructor
public class  Repair implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long Id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "atlas_devices_repair", joinColumns = { @JoinColumn(name = "repair_id", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "device_id", referencedColumnName = "device_Id") })
    public Devices devices;

    @Searchable
    @Column(name = "CUSTOMERS")
    public String customers;

    @ModifiableField
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "atlas_repair_errors",joinColumns = @JoinColumn(name = "repair_id",referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "error_id",referencedColumnName = "ID"))
    public Set<DeviceError> deviceErrors;

    @ModifiableField
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "atlas_repair_parts",joinColumns = @JoinColumn(name = "repair_id",referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "part_id",referencedColumnName = "parts_id"))
    public Set<Parts> parts;

    @ModifiableField
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "atlas_repair_users",joinColumns = @JoinColumn(name = "repair_id",referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "USER_ID"))
    public Set<UfsUser> users;

    @Filter
    @Column(name = "ACTION_STATUS",updatable = false,insertable = false)
    public String actionStatus;

    @ModifiableField
    @Column(name = "REPORTED_DEFECTS", length = 60)
    public String reportedDefects;

    @Column(name = "DATE_TEST_PASSED")
    public Date qaTestPassedDate;

    @Size(max = 8)
    @ModifiableField
    @Column(name = "QA_TEST_STATUS")
    public String qaTest;

    @ModifiableField
    @Searchable
    @Column(name = "REPAIR_CENTRE" ,length = 20)
    public String repairCentre;

    @ModifiableField
    @Searchable
    @Column(name = "BATCH_NUMBER" , length = 20)
    public String batchNumber;

    @ModifiableField
    @Column(name = "INTRASH")
    public String intrash;

    @ModifiableField
    @Size(max = 20)
    @Column(name = "ACTION", nullable = false)
    private String action;

    @ModifiableField
    @Column(name = "DATE_USER_ADDED")
    public Date dateUserAdded;


    @ModifiableField
    @Column(name = "COMMENTS", length = 60)
    public String comments;

    @ModifiableField
    @Searchable
    @Column(name = "REPAIR_STATUS")
    public String repairStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATED_ON",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @Searchable
    @Column(name = "SERIAL_NUMBER")
    public String serialNumber;

    @Searchable
    @Column(name = "RECEIVED_FROM")
    public String receivedFrom;

    @Searchable
    @Column(name = "RECEIVED_BY",length = 50)
    public String receivedBy;

    @Searchable
    @Column(name = "SCREENED_BY",length = 50)
    public String screenedBy;

    @Searchable
    @Column(name = "QA_DONE_BY",length = 50)
    public String qaDoneBy;

    @Column(name = "LEVELS")
    public String levels;

    @JsonIgnore
    @Column(name = "DATE_PARTS_ADDED")
    public Date datePartsAdded;

    @JsonIgnore
    @Column(name = "CURRENT_USER")
    public Long currentUser;

    public Repair(Devices devices, String customers, String actionStatus, String repairCentre, String batchNumber, String intrash, String action, String comments, String repairStatus, String serialNumber, String receivedFrom, String receivedBy) {
        this.devices = devices;
        this.customers = customers;
        this.actionStatus = actionStatus;
        this.repairCentre = repairCentre;
        this.batchNumber = batchNumber;
        this.intrash = intrash;
        this.action = action;
        this.comments = comments;
        this.repairStatus = repairStatus;
        this.serialNumber = serialNumber;
        this.receivedFrom = receivedFrom;
        this.receivedBy = receivedBy;

    }
}
