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
@Table( name = "atlas_parts_minmax_configs" )
public class PartsMaxMinConfig {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "partsminmax_Id" )
    private Long id;

    @Basic( optional = false )
    @Searchable
    @Column( name = "partnumber" )
    private String partnumber;

    @Searchable
    @Basic( optional = false )
    @Column( name = "part_description" )
    private String partdescription;

    @Searchable
    @Column( name = "minimum_limit" )
    private int minimumlimit;

    @ModifiableField
    @Searchable
    @Column( name = "maximum_limit" )
    private int maximumlimit;

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


    public PartsMaxMinConfig() {
    }

    public PartsMaxMinConfig(String partnumber, String partdescription, int minimumlimit, int maximumlimit, String action, String actionStatus, String intrash, Date creationDate) {
        this.partnumber = partnumber;
        this.partdescription = partdescription;
        this.minimumlimit = minimumlimit;
        this.maximumlimit = maximumlimit;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public PartsMaxMinConfig setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public PartsMaxMinConfig setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getPartdescription() {
        return partdescription;
    }

    public PartsMaxMinConfig setPartdescription(String partdescription) {
        this.partdescription = partdescription;
        return this;
    }

    public int getMinimumlimit() {
        return minimumlimit;
    }

    public PartsMaxMinConfig setMinimumlimit(int minimumlimit) {
        this.minimumlimit = minimumlimit;
        return this;
    }

    public int getMaximumlimit() {
        return maximumlimit;
    }

    public PartsMaxMinConfig setMaximumlimit(int maximumlimit) {
        this.maximumlimit = maximumlimit;
        return this;
    }

    public String getAction() {
        return action;
    }

    public PartsMaxMinConfig setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public PartsMaxMinConfig setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public PartsMaxMinConfig setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PartsMaxMinConfig setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "PartsMaxMinConfig{" +
                "id=" + id +
                ", partnumber='" + partnumber + '\'' +
                ", partdescription='" + partdescription + '\'' +
                ", minimumlimit=" + minimumlimit +
                ", maximumlimit=" + maximumlimit +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
