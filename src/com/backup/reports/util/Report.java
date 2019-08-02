package com.backup.reports.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class  Report {
    private static JasperPrint jPrint;

    public  static void createReport(Connection connect, Map<String, Object> map, InputStream by){
        try {
            JasperReport jReport = (JasperReport) JRLoader.loadObject(by);
            jPrint = JasperFillManager.fillReport(jReport,map,connect);
            by.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static  void showReport(){
        JasperViewer jViewer = new JasperViewer(jPrint, false);
        jViewer.setTitle("ICT Data Backup Inventory");
        jViewer.setVisible(true);
    }

    public static String createReportPDF(){
        String name = LocalDateTime.now().toString().replace(":","");
        String rootPath =  "C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\SentBackupInventory\\";
        File theDir = new File(rootPath);
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException ignored) {}
        }
        String path =name+".pdf";
        try {
            JasperExportManager.exportReportToPdfFile(jPrint,rootPath+path);
            return path;
        } catch (JRException e) {
            e.printStackTrace();
        }
        return null;
    }

}
