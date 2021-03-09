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
@Table(name = "atlas_shipped")
@NoArgsConstructor
@AllArgsConstructor
public class ShippedRepair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Searchable
    @Column(name = "SERIAL_NUMBER")
    public String serialNumber;

    @ModifiableField
    @Searchable
    @Column(name = "PCBA_PN", length = 30)
    public String pcba_pn;

    @ModifiableField
    @Searchable
    @Column(name = "PCBA_SN", length = 30)
    public String pcba_sn;

    @ModifiableField
    @Searchable
    @Column(name = "BOARD_DEFECT", length = 50)
    public String board_defect;

    @ModifiableField
    @Searchable
    @Column(name = "PKI_VERSION", length = 40)
    public String pki_version;

    @ModifiableField
    @Searchable
    @Column(name = "MAC_ID", length = 50)
    public String mac_id;

    @ModifiableField
    @Searchable
    @Column(name = "BT_ADDRESS", length = 50)
    public String bt_address;

    @ModifiableField
    @Searchable
    @Column(name = "WIFI" , length =  50)
    public String wifi;

    @ModifiableField
    @Searchable
    @Column(name = "PRINTER_TYPE", length = 40)
    public String printer_type;

    @ModifiableField
    @Searchable
    @Column(name = "NOTE", length = 60)
    private String note;

    @ModifiableField
    @Searchable
    @Column(name = "SHIPPED_STATUS", length = 60)
    private String shippedStatus;

    @OneToOne
    @JoinTable(name = "atlas_devices_shippeddevices",
            joinColumns = { @JoinColumn(name = "shipped_id", referencedColumnName = "Id") },
            inverseJoinColumns = { @JoinColumn(name = "atlas_devices", referencedColumnName = "device_Id") })
    public Devices devices;

    @ModifiableField
    @Column(name = "ACTION", nullable = false, length = 20)
    public String action;

    @Filter
    @Column(name = "ACTION_STATUS", length = 15,updatable = false,insertable = false)
    @ModifiableField
    public String actionStatus;

    @ModifiableField
    @Column(name = "INTRASH", length = 3)
    public String intrash;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CREATION_ON",insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date creationOn;

    public ShippedRepair(String serialNumber,Devices devices, String action, String actionStatus, String intrash) {
        this.serialNumber = serialNumber;
        this.devices = devices;
        this.action = action;
        this.actionStatus = actionStatus;
        this.intrash = intrash;
    }
}
