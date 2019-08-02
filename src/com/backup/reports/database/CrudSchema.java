package com.backup.reports.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudSchema {

    public boolean deleteRecordByDepartment(String serverName, Date fromDate , Date toDate){

        String deleteQuery= "DELETE FROM file_record WHERE server_name = ? AND date_modified >= ? AND date_modified <= ?";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                PreparedStatement preparedStatement = DbConnection.connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1,serverName);
                preparedStatement.setDate(2,fromDate);
                preparedStatement.setDate(3,toDate);
                preparedStatement.execute();
                return  true;
            } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
            }
        }else {
            return false;
        }
    }

    public  boolean deleteRecordByDate(Date fromDate, Date toDate){
        String deleteQuery = "DELETE FROM file_record WHERE date_modified >= ? AND date_modified <= ?";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                PreparedStatement preparedStatement =  DbConnection.connection.prepareStatement(deleteQuery);
                preparedStatement.setDate(1,fromDate);
                preparedStatement.setDate(2,toDate);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
