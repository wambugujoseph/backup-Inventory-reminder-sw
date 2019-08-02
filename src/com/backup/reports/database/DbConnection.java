package com.backup.reports.database;

import com.backup.reports.util.ReadConfigFile;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DbConnection {

    public static Connection connection;
    private String url;
    private String user = "root";
    private String password;
    final static Logger logger = Logger.getLogger(DbConnection.class);




    public DbConnection() {
        Map<String, String> dbConfigInfo = new ReadConfigFile().readDbConfigFile();
        this.url = "jdbc:mysql://" + dbConfigInfo.get("host") +"/" + dbConfigInfo.get("dbname");
        this.password = dbConfigInfo.get("password");
        connection =  getConnection();
        //System.out.println(this.url +""+ password);

    }

    private  Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(this.url, this.user, this.password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.error("Error connecting to DB"+ e.getMessage());
            return null;
        }
    }

    public boolean isDatabaseConnected(){
        return  connection != null;
    }
}
