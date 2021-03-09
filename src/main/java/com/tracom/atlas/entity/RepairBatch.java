package com.tracom.atlas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ke.axle.chassis.annotations.Filter;
import ke.axle.chassis.annotations.ModifiableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "atlas_repair_batch")
@NoArgsConstructor
@AllArgsConstructor
public class RepairBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long batch_id;

    @Column(name = "CLIENT_NAME", length = 50)
    public String clientName;

    @Column(name = "BATCH_NUMBER")
    public int batchNumber;

    @Column(name = "INTRASH",length = 3)
    public String intrash;

    @ModifiableField
    @Column(name = "ACTION", nullable = false, length = 20)
    public String actions;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATION_ON",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdOn;

    @Filter
    @Column(name = "ACTION_STATUS", length = 15)
    public String actionStatus;
}
