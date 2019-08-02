package com.backup.reports.util;

import java.time.LocalDate;

public class BackUpFile {

    private int count;
   private String fileName;
   private String serverName;
   private LocalDate dateModified;
   private String backType;
   private long size;

   public BackUpFile(){

   }

    public BackUpFile(String fileName, String serverName, LocalDate dateModified, long size) {
        this.fileName = fileName;
        this.serverName = serverName;
        this.dateModified = dateModified;
        this.backType = generateBackUpType(fileName);
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    private String generateBackUpType(String fileName){
       if ((fileName.lastIndexOf("Full")>0)){
           return "Full";
       }else{
           return "Differential";
       }
    }

}
