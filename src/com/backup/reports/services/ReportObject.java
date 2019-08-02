package com.backup.reports.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ReportObject {

    private  File dairyReport = new File("src\\com\\backup\\reports\\jasper\\dailyReport.jasper");
    private File depBasedReport = new File("src\\com\\backup\\reports\\jasper\\depBasedReport.jasper");
    private File  depReportFromTo =  new File("src\\com\\backup\\reports\\jasper\\depReportFromTo.jasper");
    private  File dairyInventoryFromTo = new File("src\\com\\backup\\reports\\jasper\\dailyReportFromTo.jasper");
    private  File dairyGroupByDepInvent = new File("src\\com\\backup\\reports\\jasper\\depInventoryFromTo.jasper");


    public  InputStream getDairyGroupDep(){
        return getInventory(dairyGroupByDepInvent);
    }

    public InputStream getDailyReport(){
        return getInventory(dairyReport);
    }

    public InputStream getDepBasedReport(){
       return getInventory(depBasedReport);
    }

    public InputStream getDepReportFromTo(){
       return getInventory(depReportFromTo);
    }
    public  InputStream getDairyInventoryFromTo(){
        return getInventory(dairyInventoryFromTo);
    }

    private InputStream getInventory( File file){
        InputStream inputStream;
        try {
            inputStream  = new FileInputStream(file);
            return  inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("reading file error");
            return null;
        }
    }
}
