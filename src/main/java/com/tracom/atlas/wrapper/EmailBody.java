/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tracom.atlas.wrapper;

/**
 *
 * @author emuraya
 */
public class EmailBody {
    
    private String sendTo;
    private String subject;
    private String message;
    private String pathToAttachment;
    private String type;

    public EmailBody(){}

    public EmailBody(String sendTo, String subject, String message, String pathToAttachment, String type) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.message = message;
        this.pathToAttachment = pathToAttachment;
        this.type = type;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPathToAttachment() {
        return pathToAttachment;
    }

    public void setPathToAttachment(String pathToAttachment) {
        this.pathToAttachment = pathToAttachment;
    }
}
