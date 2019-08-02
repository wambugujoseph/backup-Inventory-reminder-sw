package com.backup.reports.util;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by ken on 7/21/2019.
 */
public class ClientAddress {

    private SimpleStringProperty active;
    private SimpleStringProperty equipmentUser;
    private SimpleStringProperty email;
    private SimpleStringProperty clientMachine;
    private SimpleStringProperty executionTime;

    public ClientAddress(String active, String equipmentUser, String email, String clientMachine, String executionTime) {
        this.active = new SimpleStringProperty(active);
        this.equipmentUser = new SimpleStringProperty(equipmentUser);
        this.email = new SimpleStringProperty(email);
        this.clientMachine = new SimpleStringProperty(clientMachine);
        this.executionTime = new SimpleStringProperty(executionTime);
    }
    public ClientAddress() {
    }

    public String getActive() {
        return active.get();
    }

    public SimpleStringProperty activeProperty() {
        return active;
    }

    public void setActive(String active) {
        this.active.set(active);
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

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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

    public String getExecutionTime() {
        return executionTime.get();
    }

    public SimpleStringProperty executionTimeProperty() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime.set(executionTime);
    }
}
