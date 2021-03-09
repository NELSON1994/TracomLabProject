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
@Table( name = "atlas_orders" )
public class Orders {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "order_Id" )
    private Long id;

    @Basic( optional = false )
    @Searchable
    @Column( name = "description" )
    private String description;

    @Searchable
    @Basic( optional = false )
    @Column( name = "dt_purchased" )
    private String dtpurchased;
    @ModifiableField
    @Column( name = "po_number" )
    private String ponumber;
    @ModifiableField
    @Searchable
    @Column( name = "part_no" )
    private String partnumber;
    @ModifiableField
    @Searchable
    @Column( name = "qt_purchased" )
    private String qtpurchased;

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

    public Orders() {
    }

    public Orders(String description, String dtpurchased, String ponumber, String partnumber, String qtpurchased, String action, String actionStatus, String intrash, Date creationDate) {
        this.description = description;
        this.dtpurchased = dtpurchased;
        this.ponumber = ponumber;
        this.partnumber = partnumber;
        this.qtpurchased = qtpurchased;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDtpurchased() {
        return dtpurchased;
    }

    public void setDtpurchased(String dtpurchased) {
        this.dtpurchased = dtpurchased.replace("-" , "");
    }

    public String getPonumber() {
        return ponumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getQtpurchased() {
        return qtpurchased;
    }

    public void setQtpurchased(String qtpurchased) {
        this.qtpurchased = qtpurchased;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getIntrash() {
        return intrash;
    }

    public void setIntrash(String intrash) {
        this.intrash = intrash;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dtpurchased='" + dtpurchased + '\'' +
                ", ponumber='" + ponumber + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", qtpurchased='" + qtpurchased + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
