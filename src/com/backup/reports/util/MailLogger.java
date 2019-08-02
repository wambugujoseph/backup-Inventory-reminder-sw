package com.backup.reports.util;

/**
 * Created by ken on 7/25/2019.
 */
public class MailLogger {
    private  String recipient;
    private  String clientMachine;
    private  String subject;
    private  String message;
    private  String inventory;

    public MailLogger(String recipient, String clientMachine, String subject, String message, String inventory) {
        this.recipient = recipient;
        this.clientMachine = clientMachine;
        this.subject = subject;
        this.message = message;
        this.inventory = inventory;
    }

    public MailLogger() {
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getClientMachine() {
        return clientMachine;
    }

    public void setClientMachine(String clientMachine) {
        this.clientMachine = clientMachine;
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

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
}
