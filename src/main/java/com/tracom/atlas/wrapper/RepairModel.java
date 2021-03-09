
package com.tracom.atlas.wrapper;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

public class RepairModel {

    @Expose
    private String comments;
    @Expose
    private List<String> deviceErrors;
    @Expose
    private String failureFound;
    @Expose
    private List<String> parts;

    private String qaTest;

    private String repairCentre;

    private String repairStatus;

    private String reportedDefects;
    @Expose
    private String users;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<String> getDeviceErrors() {
        return deviceErrors;
    }

    public void setDeviceErrors(List<String> deviceErrors) {
        this.deviceErrors = deviceErrors;
    }

    public String getFailureFound() {
        return failureFound;
    }

    public void setFailureFound(String failureFound) {
        this.failureFound = failureFound;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }

    public String getQaTest() {
        return qaTest;
    }

    public void setQaTest(String qaTest) {
        this.qaTest = qaTest;
    }

    public String getRepairCentre() {
        return repairCentre;
    }

    public void setRepairCentre(String repairCentre) {
        this.repairCentre = repairCentre;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public String getReportedDefects() {
        return reportedDefects;
    }

    public void setReportedDefects(String reportedDefects) {
        this.reportedDefects = reportedDefects;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

}
