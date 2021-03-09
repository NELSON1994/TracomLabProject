package com.tracom.atlas.entity;

import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author maurice omondi
 * parts entity which is  mapped to atlas_parts table
 */

@Data
@Entity
@Table(name = "atlas_parts")
public class Parts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long parts_id;

    @ModifiableField
    @Searchable
    @Column(name = "part_number", unique = true)
    private String partNumber;

    @ModifiableField
    @Searchable
    @Column(name = "part_model")
    private String partModel;

    @ModifiableField
    @Searchable
    @Column(name = "part_name")
    private String partName;

    @ModifiableField
    @Searchable
    @Column(name = "description")
    private String description;
    @ModifiableField
    @Searchable
    @Column(name = "manufacturer_name")
    private String manufacturerName;



    @ModifiableField
    @Searchable
    @Column(name = "created_on")
    private Date createdOn = new Date(System.currentTimeMillis());
    @ModifiableField

    @Column(name = "intrash")
    private String intrash;

    @ModifiableField
    @Column(name = "action")
    private String action;


    @ModifiableField
    @Searchable
    @Column(name = "action_status")
    private String actionStatus;



    public Parts() {
    }

    public Parts(String partNumber, String partModel, String partName, String description, String manufacturerName, Date createdOn, String intrash, String action, String actionStatus) {
        this.partNumber = partNumber;
        this.partModel = partModel;
        this.partName = partName;
        this.description = description;
        this.manufacturerName = manufacturerName;
        this.createdOn = createdOn;
        this.intrash = intrash;
        this.action = action;
        this.actionStatus = actionStatus;
    }
}
