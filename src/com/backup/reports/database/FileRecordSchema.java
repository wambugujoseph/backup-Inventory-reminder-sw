package com.backup.reports.database;

import com.backup.reports.util.BackUpFile;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileRecordSchema {
    private PreparedStatement preparedStatement;
    //private BackUpFile backUpFile = new BackUpFile();
    private List<String> serverNames = new ArrayList<>();
    private Connection connection = DbConnection.connection;

    public boolean clientBackDiretoryUpdate(String serverName, String absolutePath){
        String updateClient = "INSERT IGNORE INTO backup_client (server_name, logical_path) VALUES(?,?)";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                preparedStatement = connection.prepareStatement(updateClient);
                preparedStatement.setString(1,serverName );
                preparedStatement.setString(2,absolutePath);
                return preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {e.printStackTrace();}
            }
        }else
            return false;
    }

    /**
     * update new  back up file Records
     * @param backUpFile
     * @return true if files are successfully updated
     */
    public boolean fileRecordUpdate(BackUpFile backUpFile){
        String fileRecordQuery = "INSERT IGNORE INTO file_record (file_name, server_name, date_modified, backup_type,size) VALUES(?,?,?,?,?)";
        if (new DbConnection().isDatabaseConnected()) {
            try{
                try {
                    preparedStatement = connection.prepareStatement(fileRecordQuery);
                    preparedStatement.setString(1,backUpFile.getFileName());
                    preparedStatement.setString(2,backUpFile.getServerName());
                    preparedStatement.setDate(3, java.sql.Date.valueOf(backUpFile.getDateModified()));
                    preparedStatement.setString(4,backUpFile.getBackType());
                    preparedStatement.setLong(5,backUpFile.getSize());
                    return preparedStatement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }finally {
                    if (!connection.isClosed())
                        connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Get the list of all BackUp Server_Name
     */
    public List<String> getServerNamesSchema(){
        String query = "SELECT server_name FROM backup_client";
        if (new DbConnection().isDatabaseConnected()) {
            try {
                PreparedStatement preparedStatement = DbConnection.connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    serverNames.add(resultSet.getString("server_name"));
                }
               // connection.close();
                return serverNames;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    if (!connection.isClosed())connection.close();
                    return serverNames;
                } catch (SQLException ignored) {return serverNames;}
            }
        }else
           // System.out.println("problem in you connection");
        return serverNames;
    }

    /**
     * @param date
     * @return List of BackUpFile object
     */
    public List<BackUpFile> getBackUpFilesSchema(Date date){
        String selectQuery ="SELECT * FROM file_record WHERE date_modified = ? ";
        List<BackUpFile> backUpFiles = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()) {
            try {
                preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setDate(1, date);
                ResultSet resultSet = preparedStatement.executeQuery();
                return getBackUpFilesList(resultSet);
            } catch (SQLException e) {
                return backUpFiles;
            }finally {
                try {
                    if(! connection.isClosed())connection.close();
                } catch (SQLException ignored) {}
            }
        }else
            return backUpFiles;
    }
    /**
     * @param serverName
     * @param backUpFrom
     * @param backUpTo
     * @return List of file selected between a range of dates
     */
    public List<BackUpFile> getServerBackUpFile(String serverName, Date backUpFrom, Date backUpTo){
        String selectQuery = "SELECT * FROM file_record WHERE server_name=? AND date_modified BETWEEN ? AND ?";
        List<BackUpFile> backUpFiles = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setString(1, serverName);
                preparedStatement.setDate(2,backUpFrom);
                preparedStatement.setDate(3,backUpTo);
                ResultSet resultSet = preparedStatement.executeQuery();
                return getBackUpFilesList(resultSet);
            } catch (SQLException e) {
                return backUpFiles;
            }finally {
                try {
                    if (!connection.isClosed())connection.close();
                } catch (SQLException ignored) {}
            }
        }else
            return backUpFiles;
    }

    /**
     *
     * @param serverName
     * @return list of all backed up file of a particular server
     */

    public List<BackUpFile> getAllServerBackFileInfo(String serverName){
        String selectQuery = "SELECT * FROM file_record WHERE server_name = ?";
        List<BackUpFile> backUpFiles = new ArrayList<>();
        if (new DbConnection().isDatabaseConnected()){
            try {
                preparedStatement=connection.prepareStatement(selectQuery);
                preparedStatement.setString(1,serverName);
                ResultSet resultSet =preparedStatement.executeQuery();
                return getBackUpFilesList(resultSet);
            } catch (SQLException e) {
                return backUpFiles;
            }
        }
        return backUpFiles;
    }

    /**
     * @param resultSet
     * @return List of backUpFile
     */
    private List<BackUpFile> getBackUpFilesList(ResultSet resultSet){
        List<BackUpFile> backUpFiles = new ArrayList<>();
        try {
            int count = 1;
            while(resultSet.next()){
                BackUpFile backUpFile = new BackUpFile();
                backUpFile.setCount(count);
                backUpFile.setFileName(resultSet.getString("file_name"));
                backUpFile.setServerName(resultSet.getString("server_name"));
                backUpFile.setDateModified(LocalDate.parse(resultSet.getDate("date_modified").toString()));
                backUpFile.setSize(resultSet.getLong("size"));
                backUpFile.setBackType(resultSet.getString("backup_type"));
                backUpFiles.add(backUpFile);
                count++;
            }
            return backUpFiles;
        } catch (SQLException e) {
            return backUpFiles;
        }
    }
}
