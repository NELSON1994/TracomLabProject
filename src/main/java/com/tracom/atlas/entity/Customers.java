package com.tracom.atlas.entity;


import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Date;

/**
 * @author Nelson
 */

@Entity
@Table( name = "atlas_customers" )

public class Customers {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "customer_Id" )
    private Long id;

    @ModifiableField
    @Searchable
    @Column( name = "customer_name", unique = true )
    private String name;

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
    @Column( name = "country" )
    private String country;

    @ModifiableField
    @Column( name = "phone_number", unique = true )
    public String phone_number;

    @ModifiableField
    @Searchable
    @Column( name = "contact_person" )
    private String contact_person;

    @Searchable
    @Column( name = "comments" )
    private String comment = "success";

    @Searchable
    @Column( name = "action" )
    private String action;
    @Filter
    @Searchable
    @Column( name = "action_status" )
    private String actionStatus;
    @Searchable
    @Column( name = "intrash" )
    private String intrash;
    @Filter
    @Basic( optional = false )
    @Column( name = "creation_date" )
    private Date creationDate = new Date(System.currentTimeMillis());

    public Customers() {

    }

    public Customers(String name,  String email, String address, String country, String phone_number, String contact_person, String comment, String action, String actionStatus, String intrash, Date creationDate) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.country = country;
        this.phone_number = phone_number;
        this.contact_person = contact_person;
        this.comment = comment;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Customers setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public Customers setName(String name) {
        this.name = name.toUpperCase();
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Customers setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Customers setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Customers setCountry(String country) {
        this.country = country.toUpperCase();
        return this;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Customers setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public String getContact_person() {
        return contact_person;
    }

    public Customers setContact_person(String contact_person) {
        this.contact_person = contact_person;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Customers setAction(String action) {
        this.action = action;
        return this;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public Customers setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public String getIntrash() {
        return intrash;
    }

    public Customers setIntrash(String intrash) {
        this.intrash = intrash;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Customers setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Customers setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", contact_person='" + contact_person + '\'' +
                ", comment='" + comment + '\'' +
                ", action='" + action + '\'' +
                ", actionStatus='" + actionStatus + '\'' +
                ", intrash='" + intrash + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
