package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import ke.axle.chassis.annotations.Unique;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Nelson
 */
@Entity
@Table( name = "atlas_partsRequsition" )
public class PartsRequsition {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "Id" )
    private Long id;

    @Basic( optional = false )
    @Searchable
    @Column( name = "part_number")
    private String partnumber;

    @Searchable
    @Column( name = "part_description")
    private String partdescription;

    @Basic( optional = false )
    @Column( name = "quantity" )
    private String quantity;

    @Searchable
    @Column( name = "dateRequested" )
    private String dateRequested ;

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

    public PartsRequsition() {
    }

    public PartsRequsition(String partnumber, String quantity, String dateRequested, String action, String partdescription, String actionStatus, String intrash, Date creationDate) {
        this.partnumber = partnumber;
        this.partdescription = partdescription;
        this.quantity = quantity;
        this.dateRequested = dateRequested;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public PartsRequsition setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public PartsRequsition setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public PartsRequsition setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getAction() {
        return action;
    }

    public PartsRequsition setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public PartsRequsition setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public PartsRequsition setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public String getPartdescription() {
        return partdescription;
    }

    public void setPartdescription(String partdescription) {
        this.partdescription = partdescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public PartsRequsition setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public PartsRequsition setDateRequested(String dateRequested) {

        this.dateRequested = dateRequested.replace("-" , "");
        return this;
    }

    @Override
    public String toString() {
        return "PartsRequsition{" +
                "id=" + id +
                ", partnumber='" + partnumber + '\'' +
                ", partdescription='" + partdescription + '\'' +
                ", quantity=" + quantity +
                ", dateRequested=" + dateRequested +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
