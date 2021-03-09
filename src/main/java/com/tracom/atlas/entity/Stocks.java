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
@Table( name = "atlas_stocks" )
public class Stocks {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "stock_Id" )
    private Long id;
    @ModifiableField
    @Column( name = "opening_stock" )
    private double openingstock;
    @ModifiableField
    @Searchable
    @Column( name = "part_number" )
    private String partnumber;

    @ModifiableField
    @Searchable
    @Column( name = "part_description" )
    private String partdescription;

    @ModifiableField
    @Column( name = "stock_in" )
    private double stockin;

    @ModifiableField
    @Column( name = "issued_parts" )
    private String issuedparts;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "available_stock" )
    private double availablestock;
    @ModifiableField
    @Column( name = "max_limit" )
    private double maxlimit;

    @ModifiableField
    @Column( name = "min_limit" )
    private double minlimit;

    @ModifiableField
    @Filter
    @Column( name = "reoder_status" )
    private String reoderStatus;

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

    public Stocks() {
    }

    public Stocks(double openingstock, String partnumber, String partdescription, double stockin, String issuedparts, double availablestock, double maxlimit, double minlimit, String reoderStatus, String action, String actionStatus, String intrash, Date creationDate) {
        this.openingstock = openingstock;
        this.partnumber = partnumber;
        this.partdescription = partdescription;
        this.stockin = stockin;
        this.issuedparts = issuedparts;
        this.availablestock = availablestock;
        this.maxlimit = maxlimit;
        this.minlimit = minlimit;
        this.reoderStatus = reoderStatus;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Stocks setId(Long id) {
        this.id = id;
        return this;
    }

    public double getOpeningstock() {
        return openingstock;
    }

    public Stocks setOpeningstock(double openingstock) {
        this.openingstock = openingstock;
        return this;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public Stocks setPartnumber(String partnumber) {
        this.partnumber = partnumber;
        return this;
    }

    public String getPartdescription() {
        return partdescription;
    }

    public Stocks setPartdescription(String partdescription) {
        this.partdescription = partdescription;
        return this;
    }

    public double getStockin() {
        return stockin;
    }

    public Stocks setStockin(double stockin) {
        this.stockin = stockin;
        return this;
    }

    public String getIssuedparts() {
        return issuedparts;
    }

    public Stocks setIssuedparts(String issuedparts) {
        this.issuedparts = issuedparts;
        return this;
    }

    public double getAvailablestock() {
        return availablestock;
    }

    public Stocks setAvailablestock(double availablestock) {
        this.availablestock = availablestock;
        return this;
    }

    public double getMaxlimit() {
        return maxlimit;
    }

    public Stocks setMaxlimit(double maxlimit) {
        this.maxlimit = maxlimit;
        return this;
    }

    public double getMinlimit() {
        return minlimit;
    }

    public Stocks setMinlimit(double minlimit) {
        this.minlimit = minlimit;
        return this;
    }

    public String getReoderStatus() {
        return reoderStatus;
    }

    public Stocks setReoderStatus(String reoderStatus) {
        this.reoderStatus = reoderStatus;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Stocks setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public Stocks setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public Stocks setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Stocks setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "id=" + id +
                ", openingstock=" + openingstock +
                ", partnumber='" + partnumber + '\'' +
                ", partdescription='" + partdescription + '\'' +
                ", stockin=" + stockin +
                ", issuedparts=" + issuedparts +
                ", availablestock=" + availablestock +
                ", maxlimit=" + maxlimit +
                ", minlimit=" + minlimit +
                ", reoderStatus='" + reoderStatus + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
