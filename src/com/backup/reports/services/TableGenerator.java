package com.backup.reports.services;

import com.backup.reports.database.FileRecordSchema;
import com.backup.reports.util.BackUpFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.time.LocalDate;

public class TableGenerator{

    private TableView<BackUpFile> table;

    public TableView<BackUpFile> getBackUpInfoByDate(Date date){

        TableColumn<BackUpFile, Integer> columnCount = new TableColumn<>("#");
        columnCount.setMinWidth(40);
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<BackUpFile, String> serverNameColumn = new TableColumn<>("Server_Name");
        serverNameColumn.setMinWidth(200);
        serverNameColumn.setCellValueFactory(new PropertyValueFactory<>("serverName"));

        TableColumn<BackUpFile, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setMinWidth(250);
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<BackUpFile,String > backupType = new TableColumn<>("Backup_Type");
        backupType.setMinWidth(200);
        backupType.setCellValueFactory(new PropertyValueFactory<>("backType"));

        TableColumn<BackUpFile,LocalDate> dateModified = new TableColumn<>("Date_Modified");
        dateModified.setMinWidth(200);
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));

        TableColumn<BackUpFile,Long > size = new TableColumn<>("Size (KB)");
        size.setMinWidth(200);
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        ObservableList<BackUpFile> backUpFiles = FXCollections.observableArrayList(new FileRecordSchema().getBackUpFilesSchema(date));
        table =new TableView<>();
        table.setItems(backUpFiles);
        table.getColumns().addAll(columnCount, serverNameColumn,fileNameColumn,backupType,size, dateModified);

        return table;
    }


    public TableView<BackUpFile> getSeverBackupInfoFromTo(String serverName, Date fromDate, Date toDate) {

        TableColumn<BackUpFile, Integer> columnCount = new TableColumn<>("#");
        columnCount.setMinWidth(40);
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<BackUpFile, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setMinWidth(250);
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<BackUpFile,String > backupType = new TableColumn<>("Backup_Type");
        backupType.setMinWidth(200);
        backupType.setCellValueFactory(new PropertyValueFactory<>("backType"));

        TableColumn<BackUpFile,LocalDate> dateModified = new TableColumn<>("Date_Modified");
        dateModified.setMinWidth(200);
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));

        TableColumn<BackUpFile,Long > size = new TableColumn<>("Size (KB)");
        size.setMinWidth(200);
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        ObservableList<BackUpFile> backUpFiles = FXCollections.observableArrayList(new FileRecordSchema().getServerBackUpFile(serverName,fromDate,toDate));
        table =new TableView<>();
        table.setItems(backUpFiles);
        table.getColumns().addAll(columnCount, fileNameColumn,backupType,size,dateModified);

        return table;
    }

    public TableView<BackUpFile> getAllSeverBackUpInfo(String serverName) {

        TableColumn<BackUpFile, Integer> columnCount = new TableColumn<>("#");
        columnCount.setMinWidth(40);
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        TableColumn<BackUpFile, String> fileNameColumn = new TableColumn<>("File Name");
        fileNameColumn.setMinWidth(250);
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<BackUpFile,String > backupType = new TableColumn<>("Backup_Type");
        backupType.setMinWidth(200);
        backupType.setCellValueFactory(new PropertyValueFactory<>("backType"));

        TableColumn<BackUpFile,LocalDate> dateModified = new TableColumn<>("Date_Modified");
        dateModified.setMinWidth(200);
        dateModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));

        TableColumn<BackUpFile,Long > size = new TableColumn<>("Size (KB)");
        size.setMinWidth(200);
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        ObservableList<BackUpFile> backUpFiles = FXCollections.observableArrayList(new FileRecordSchema().getAllServerBackFileInfo(serverName));
        table =new TableView<>();
        table.setItems(backUpFiles);
        table.getColumns().addAll(columnCount,fileNameColumn,backupType,size,dateModified);

        return table;
    }
}