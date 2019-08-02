package com.backup.reports.util;

/**
 * Created by ken & joseph on 7/27/2019.
 */
public class ReminderNotificationMsg {
    private String reminder_occurrence;
    private String inventory_occurrence;
    private String recipient, subject, content,equipment_user,execution_time,client_machine;

    public ReminderNotificationMsg() {
    }

    public ReminderNotificationMsg(String reminder_occurrence, String inventory_occurrence, String recipient, String subject, String content, String equipment_user, String execution_time, String client_machine) {
        this.reminder_occurrence = reminder_occurrence;
        this.inventory_occurrence = inventory_occurrence;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.equipment_user = equipment_user;
        this.execution_time = execution_time;
        this.client_machine = client_machine;
    }

    public String getReminder_occurrence() {
        return reminder_occurrence;
    }

    public void setReminder_occurrence(String reminder_occurrence) {
        this.reminder_occurrence = reminder_occurrence;
    }

    public String getInventory_occurrence() {
        return inventory_occurrence;
    }

    public void setInventory_occurrence(String inventory_occurrence) {
        this.inventory_occurrence = inventory_occurrence;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEquipment_user() {
        return equipment_user;
    }

    public void setEquipment_user(String equipment_user) {
        this.equipment_user = equipment_user;
    }

    public String getExecution_time() {
        return execution_time;
    }

    public void setExecution_time(String execution_time) {
        this.execution_time = execution_time;
    }

    public String getClient_machine() {
        return client_machine;
    }

    public void setClient_machine(String client_machine) {
        this.client_machine = client_machine;
    }
}
