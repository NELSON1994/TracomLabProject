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
@Table( name = "atlas_ordersReceivedd" )
public class OrdersReceiveddd {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "received_Id" )
    private Long id;
    @ModifiableField
    @Column( name = "po_number" )
    private String ponumber;

    @Searchable
    @Column( name = "date_received" )
    private String datereceived;
    @ModifiableField
    @Searchable
    @Column( name = "part_no" )
    private String partnumber;
    @ModifiableField
    @Searchable
    @Column( name = "qt_purchased" )
    private String qtpurchased;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "qt_received" )
    private String qtreceived;

    @ModifiableField
    @Basic( optional = false )
    @Column( name = "balance_ingenico" )
    private int balance;


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

    public OrdersReceiveddd() {
    }

    public OrdersReceiveddd(String ponumber, String datereceived, String partnumber, String qtpurchased, String qtreceived, int balance, String action, String actionStatus, String intrash, Date creationDate) {
        this.ponumber = ponumber;
        this.datereceived = datereceived;
        this.partnumber = partnumber;
        this.qtpurchased = qtpurchased;
        this.qtreceived = qtreceived;
        this.balance = balance;
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

    public String getPonumber() {
        return ponumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public String getDatereceived() {
        return datereceived;
    }

    public void setDatereceived(String datereceived) {
        this.datereceived = datereceived.replace("-" , "");
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

    public String getQtreceived() {
        return qtreceived;
    }

    public void setQtreceived(String qtreceived) {
        this.qtreceived = qtreceived;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        int a = Integer.valueOf(qtpurchased);
        int b = Integer.valueOf(qtreceived);
        this.balance = a - b;
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
        return "OrdersReceiveddd{" +
                "id=" + id +
                ", ponumber='" + ponumber + '\'' +
                ", datereceived='" + datereceived + '\'' +
                ", partnumber='" + partnumber + '\'' +
                ", qtpurchased='" + qtpurchased + '\'' +
                ", qtreceived='" + qtreceived + '\'' +
                ", balance=" + balance +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
