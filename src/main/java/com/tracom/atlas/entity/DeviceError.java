package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import ke.axle.chassis.annotations.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "atlas_device_error")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceError implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long error_id;

    @ModifiableField
    @Searchable
    @Column(name = "CODE", length = 30)
    public String code;

    @ModifiableField
    @Searchable
    @Column(name = "CODE_NAME", length = 50)
    public String codeName;

    @ModifiableField
    @Searchable
    @Column(name = "DESCRIPTION", length = 100)
    public String description;

    @ModifiableField
    @Searchable
    @Column(name = "LEVELS", length = 30)
    public String level;

    @Filter
    @Column(name = "ACTION_STATUS", length = 15)
    public String actionStatus;

    @ModifiableField
    @Column(name = "ACTION", nullable = false, length = 20)
    public String action;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATION_ON",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @ModifiableField
    @Column(name = "INTRASH", length = 3)
    public String intrash;
}
