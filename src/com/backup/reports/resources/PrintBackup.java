package com.backup.reports.resources;

import com.backup.reports.database.DbConnection;
import com.backup.reports.services.ReportObject;
import com.backup.reports.util.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class PrintBackup implements Initializable {
    @FXML
    private Button printBtn,closeBtn;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private RadioButton groupByDate;
    @FXML
    private RadioButton groupByDepartment;

    private ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        printBtn.setOnAction((ActionEvent e) ->{
            String fromDate = fromDatePicker.getEditor().getText();
            String toDate = toDatePicker.getEditor().getText();
            if (group.getSelectedToggle() == groupByDate) {
                if (!fromDatePicker.getEditor().getText().isEmpty() && !toDatePicker.getEditor().getText().isEmpty()){
                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
                    try {
                        printInventoryFromTo(
                                Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                                Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    alertMessages("Information","Please select From And To date");
                }
            } else {
                if (!fromDatePicker.getEditor().getText().isEmpty() && !toDatePicker.getEditor().getText().isEmpty()){
                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
                    try {
                        printGroupDepInvent(
                                Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                                Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    alertMessages("Information","Please select From And To date");
                }
            }
        });
        closeBtn.setOnAction(e->closeCurrentWindow());

        radioButtonGroup();
    }

    private void radioButtonGroup(){
        groupByDate.setToggleGroup(group);
        groupByDepartment.setToggleGroup(group);
    }

    private void  printInventoryFromTo( Date fromDate, Date toDate){
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        Report.createReport(connection, map, new ReportObject().getDairyInventoryFromTo());
        Report.showReport();
    }
private  void printGroupDepInvent(Date fromDate, Date toDate){
    Connection connection = DbConnection.connection;
    Map<String, Object> map = new HashMap<>();
    map.put("fromDate", fromDate);
    map.put("toDate", toDate);
    Report.createReport(connection, map, new ReportObject().getDairyGroupDep());
    Report.showReport();

}
    /**
     * information alert
     * @param title
     * @param headerText
     */
    private void alertMessages(String title, String headerText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.show();
    }
    private void closeCurrentWindow(){
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
