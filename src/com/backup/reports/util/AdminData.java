package com.backup.reports.util;

/**
 * Created by ken on 7/26/2019.
 */
public class AdminData {
    private String userName;
    private String adminPassword;

    public AdminData(String userName, String adminPassword) {
        this.userName = userName;
        this.adminPassword = adminPassword;
    }

    public AdminData() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
