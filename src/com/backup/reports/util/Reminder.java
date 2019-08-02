package com.backup.reports.util;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by ken on 7/21/2019.
 */
public class Reminder {

    private SimpleStringProperty status;
    private SimpleStringProperty reminderOccurrence;
    private SimpleStringProperty inventoryOccurrence;
    private SimpleStringProperty recipient;
    private SimpleStringProperty message;
    private SimpleStringProperty equipmentUser;
    private SimpleStringProperty clientMachine;
    private SimpleStringProperty subject;


    public Reminder(String status, String reminderOccurrence, String inventoryOccurrence, String recipient,String subject,
                    String message, String equipmentUser, String clientMachine) {
        this.status = new SimpleStringProperty(status);
        this.reminderOccurrence = new SimpleStringProperty(reminderOccurrence);
        this.inventoryOccurrence = new SimpleStringProperty(inventoryOccurrence);
        this.recipient = new SimpleStringProperty(recipient);
        this.message = new SimpleStringProperty(message);
        this.equipmentUser = new SimpleStringProperty(equipmentUser);
        this.clientMachine = new SimpleStringProperty(clientMachine);
        this.subject= new SimpleStringProperty(subject);
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

    public String getEquipmentUser() {
        return equipmentUser.get();
    }

    public SimpleStringProperty equipmentUserProperty() {
        return equipmentUser;
    }

    public void setEquipmentUser(String equipmentUser) {
        this.equipmentUser.set(equipmentUser);
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

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getReminderOccurrence() {
        return reminderOccurrence.get();
    }

    public SimpleStringProperty reminderOccurrenceProperty() {
        return reminderOccurrence;
    }

    public void setReminderOccurrence(String reminderOccurrence) {
        this.reminderOccurrence.set(reminderOccurrence);
    }

    public String getInventoryOccurrence() {
        return inventoryOccurrence.get();
    }

    public SimpleStringProperty inventoryOccurrenceProperty() {
        return inventoryOccurrence;
    }

    public void setInventoryOccurrence(String inventoryOccurrence) {
        this.inventoryOccurrence.set(inventoryOccurrence);
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

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }
}
