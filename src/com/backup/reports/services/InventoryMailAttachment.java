package com.backup.reports.services;

import com.backup.reports.database.DbConnection;
import com.backup.reports.util.Report;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by ken & joseph on 7/27/2019.
 */
public class InventoryMailAttachment {

    public InventoryMailAttachment() {
        try {
            removeOlderPDFFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String  createDepInventoryPDFFromTo(String serverName, Date fromDate, Date toDate){
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("serverName", serverName);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        Report.createReport(connection, map, new ReportObject().getDepReportFromTo());
        return Report.createReportPDF();
    }
    public  String createPDFDepBasedReport(String serverName ){
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("server_name", serverName);
        Report.createReport(connection, map, new ReportObject().getDepBasedReport());
        return Report.createReportPDF();
    }
    public String  createPDFDailyReport(Date date){
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("Date", date);
        Report.createReport(connection, map, new ReportObject().getDailyReport());
       return Report.createReportPDF();
    }

    private void removeOlderPDFFiles(){
        File folder = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/SentBackupInventory/");
        File[] listOfFiles = folder.listFiles();
        try {
            assert listOfFiles != null;
            int size = listOfFiles.length;
            if (listOfFiles.length == 150){
                for (int i = 60; i<size; i++){
                    if (LocalDateTime.ofInstant(Instant.ofEpochMilli(listOfFiles[60].lastModified()), TimeZone
                                    .getDefault().toZoneId())
                            .isAfter(LocalDateTime.ofInstant(Instant.ofEpochMilli(listOfFiles[i].lastModified()), TimeZone
                                            .getDefault().toZoneId())
                            )){
                        listOfFiles[i].delete();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
