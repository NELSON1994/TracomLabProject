package com.tracom.atlas.wrapper;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

public class SearchWrapper {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date to;
    private String customer;
    private Long technician;
    private String level;
    private String serialNo;
    private String repairStatus;

    public SearchWrapper() {
        this.from = new Date(0);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        this.to = cal.getTime();
        this.customer = customer;
        this.technician = technician;
        this.level = level;
        this.serialNo = serialNo;
        this.repairStatus = repairStatus;
    }

    public Date getFrom() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.add(Calendar.DAY_OF_WEEK, -1);
        return cal.getTime();
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(to);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Long getTechnician() {
        return technician;
    }

    public void setTechnician(Long technician) {
        this.technician = technician;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }
}
