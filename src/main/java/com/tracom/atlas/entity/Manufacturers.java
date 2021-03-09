package com.tracom.atlas.entity;

import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Date;

/**
 * @author Nelson
 * 2019
 */
@Entity
@Table( name = "atlas_manufacturers" )
public class Manufacturers {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "manufacturer_Id" )
    private Long id;

    @ModifiableField
    @Searchable
    @Column( name = "manufacturer_name", unique = true )
    private String manufacturer;

    @ModifiableField
    @Searchable
    @Email
    @Column( name = "email_address", unique = true )
    private String email;

    @ModifiableField
    @Searchable
    @Column( name = "address" )
    private String address;

    @ModifiableField
    @Searchable
    @Column( name = "phone_number", unique = true )
    public String phone_number;

    @ModifiableField
    @Searchable
    @Column( name = "contact_person" )
    private String contact_person;

    @Column( name = "action" )
    private String action;
    @Filter
    @ModifiableField
    @Searchable
    @Column( name = "action_status" )
    private String actionStatus;

    @ModifiableField
    @Searchable
    @Column( name = "comments" )
    private String comment = "success";
    @ModifiableField
    @Searchable
    @Column( name = "intrash" )
    private String intrash;

    @CreatedDate
    @Column( name = "creation_date" )
    private Date creationDate = new Date(System.currentTimeMillis());


    public Manufacturers() {
    }

    public Manufacturers(String manufacturer, String email, String address, String phone_number, String contact_person, String action, String actionStatus, String comment, String intrash, Date creationDate) {
        this.manufacturer = manufacturer;
        this.email = email;
        this.address = address;
        this.phone_number = phone_number;
        this.contact_person = contact_person;
        this.action = action;
        this.actionStatus = actionStatus;
        this.comment = comment;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Manufacturers setId(Long id) {
        this.id = id;
        return this;
    }

    public String getManufacturer() {
        return manufacturer.toUpperCase();
    }

    public Manufacturers setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Manufacturers setEmail(String email) {
        this.email = email;
        return this;
    }


    public String getAddress() {
        return address;
    }

    public Manufacturers setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Manufacturers setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public String getContact_person() {
        return contact_person;
    }

    public Manufacturers setContact_person(String contact_person) {
        this.contact_person = contact_person;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Manufacturers setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public Manufacturers setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public Manufacturers setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Manufacturers setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Manufacturers setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        return "Manufacturers{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", contact_person='" + contact_person + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", comment='" + comment + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

}
