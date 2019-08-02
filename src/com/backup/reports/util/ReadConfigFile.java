package com.backup.reports.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadConfigFile {
    private File  backUpPathfile,dbConfigFile;
    private Properties properties = new Properties();

    public ReadConfigFile() {

        this.dbConfigFile = new File("src/com/backup/reports/config/dbconfig.properties");
        this.backUpPathfile = new File("src/com/backup/reports/config/backUpPath.properties");
    }

    /**
     * @param host
     * @param dbName
     * @param password
     * @return
     * update the database configuration details
     */
    public boolean updateDbConfigFile(String host, String dbName, String password){

        try {
            properties.setProperty("HOST",host);
            properties.setProperty("DATABASE_NAME",  dbName);
            properties.setProperty("DATABASE_PASSWORD",password);

            FileOutputStream fileOutputStream = new FileOutputStream(dbConfigFile);

            properties.store(fileOutputStream,"properties");

            fileOutputStream.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * @return
     *  Reads the properties file
     */
    public Map<String, String> readDbConfigFile(){
        try {
            FileInputStream fileInputStream =  new FileInputStream(dbConfigFile);
            properties.load(fileInputStream);
            Map<String, String> dbMap =  new HashMap<>();

            dbMap.put("host",  properties.getProperty("HOST"));
            dbMap.put("dbname",properties.getProperty("DATABASE_NAME"));
            dbMap.put("password", properties.getProperty("DATABASE_PASSWORD"));

            fileInputStream.close();

            return dbMap;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * @param str
     * @return
     *  updates the  backUp path
     */
    public boolean updateBackUpPath(String str){
        try {
            properties.setProperty("BACK_UP_PATH",str );
            FileOutputStream fileOutputStream = new FileOutputStream(backUpPathfile);
            properties.store(fileOutputStream,"properties");
            fileOutputStream.close();

            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return
     *  Read the backUp Path
     */
    public String readBackUpPath(){
        try {
            FileInputStream fileInputStream = new FileInputStream(backUpPathfile);
            properties.load(fileInputStream);
            fileInputStream.close();
            return  properties.getProperty("BACK_UP_PATH");
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
