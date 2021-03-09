package com.tracom.atlas.entity;

import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "atlas_bankregions")
public class BankRegions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bankRegions_id;

    @Column(nullable = false, name = "branch_name")
    @Searchable
    @ModifiableField
    private String branchName;


    @Column(name = "code",unique = true,nullable = false)
    @ModifiableField
    @Searchable
    private String code;

    @ModifiableField
    @Column(name = "created_on", nullable = false, updatable = false)
    private Date createdOn = new Date();

    @ModifiableField
    @Column(name = "intrash")
    private String intrash;

    @ModifiableField
    @Column(name = "action", nullable = false)
    private String action;

    @ModifiableField
    @Column(name = "action_status", nullable = false)
    private String actionStatus;

}
