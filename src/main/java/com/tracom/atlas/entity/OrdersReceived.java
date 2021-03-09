package com.tracom.atlas.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Maurice
 */
@Data
@Entity
@Table( name = "atlas_ordersreceived" )
public class OrdersReceived {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "order_Id" )
    private Long id;


    @Searchable
    @Basic( optional = false )
    @Column( name = "date_received" )
    private String dateReceived;
    @ModifiableField
    @Column( name = "po_number" )
    private String ponumber;

    @ModifiableField
    @Searchable
    @Column( name = "part_no" )
    private String partnumber;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "qt_received" )
    private String qtreceived;

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

    public OrdersReceived() {
    }

    public OrdersReceived(String dateReceived, String ponumber, String partnumber, String qtreceived, String action, String actionStatus, String intrash, Date creationDate) {
        this.dateReceived = dateReceived;
        this.ponumber = ponumber;
        this.partnumber = partnumber;
        this.qtreceived = qtreceived;
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


    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
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

    public String getQtreceived() {
        return qtreceived;
    }

    public void setQtreceived(String qtreceived) {
        this.qtreceived = qtreceived;
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
}

