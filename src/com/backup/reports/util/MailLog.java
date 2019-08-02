package com.backup.reports.util;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by ken on 7/21/2019.
 */
public class MailLog {

    private SimpleStringProperty status;
    private SimpleStringProperty date;
    private SimpleStringProperty log_Level;
    private SimpleStringProperty recipient;
    private SimpleStringProperty clientMachine;
    private SimpleStringProperty subject;
    private SimpleStringProperty message;
    private SimpleStringProperty inventory;

    public MailLog(String status, String date, String log_Level, String recipient, String clientMachine, String subject, String message, String inventory) {
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
        this.log_Level = new SimpleStringProperty(log_Level);
        this.recipient = new SimpleStringProperty(recipient);
        this.clientMachine = new SimpleStringProperty(clientMachine);
        this.subject = new SimpleStringProperty(subject);
        this.message = new SimpleStringProperty(message);
        this.inventory = new SimpleStringProperty(inventory);
    }

    public MailLog() {
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getLog_Level() {
        return log_Level.get();
    }

    public SimpleStringProperty log_LevelProperty() {
        return log_Level;
    }

    public void setLog_Level(String log_Level) {
        this.log_Level.set(log_Level);
    }

    public String getRecipient() {
        return recipient.get();
    }

    public SimpleStringProperty recipientProperty() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient.set(recipient);
    }

    public String getClientMachine() {
        return clientMachine.get();
    }

    public SimpleStringProperty clientMachineProperty() {
        return clientMachine;
    }

    public void setClientMachine(String clientMachine) {
        this.clientMachine.set(clientMachine);
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getInventory() {
        return inventory.get();
    }

    public SimpleStringProperty inventoryProperty() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory.set(inventory);
    }
}
