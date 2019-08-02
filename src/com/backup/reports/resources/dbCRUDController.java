package com.backup.reports.resources;

import com.backup.reports.database.CrudSchema;
import com.backup.reports.services.BackupFileService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class dbCRUDController implements Initializable {

    @FXML
    private ComboBox<String> departmentCombo;
    @FXML
    private Button clearBtn;
    @FXML
    private  Button resetBtn;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
   private VBox radioGroup;

    private RadioButton allRadio = new RadioButton("All");
    private RadioButton departmentRadio  = new RadioButton("Department");
    private ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        populateServerNameCombox();
        toggleGroup();

        clearBtn.setOnAction(event ->deleteRecord());
        resetBtn.setOnAction(e->resetField());

    }
    private void populateServerNameCombox(){
        if (!(new BackupFileService().getServerName().isEmpty())) {
            ObservableList<String> serverNames =
                    FXCollections.observableArrayList(new BackupFileService().getServerName());
            departmentCombo.getItems().addAll(serverNames);
        }else
            alertMessages("Database Notification ","Ensure you are connected to the database");
    }

    private void toggleGroup(){
       allRadio.setPadding(new Insets(2,0,5,2));
       allRadio.setToggleGroup(group);
       departmentRadio.setToggleGroup(group);
       departmentRadio.setPadding(new Insets(2,0,2,2));
       departmentRadio.setOnAction(e-> departmentCombo.setDisable(false));
       allRadio.setOnAction(e->departmentCombo.setDisable(true));
        radioGroup.getChildren().addAll(allRadio,departmentRadio);

    }

    private void deleteRecord(){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        String fromDate =  fromDatePicker.getEditor().getText();
        String  toDate = toDatePicker.getEditor().getText();
       // boolean result;

        if (group.getSelectedToggle() == allRadio ){
            if (confirmAlert("Confirm","Are you sure you want to clear the records!","")) {
                try {
                      boolean result = new CrudSchema().deleteRecordByDate(
                            Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                            Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                     if (result){
                         alertMessages("Delete Confirmation","Successfully Cleared");
                     }else {
                         alertMessages("Delete Confirmation","Error Occurred During Deletion");
                     }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else if(group.getSelectedToggle() == departmentRadio){
           String department = departmentCombo.getValue();

            try {
                if (confirmAlert("Confirm","Are you sure you want to clear the records!","")) {
                    boolean result  =  new CrudSchema().deleteRecordByDepartment(department,
                               Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                               Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    if (result){
                        alertMessages("Delete Confirmation","Successfully Cleared");
                    }else {
                        alertMessages("Delete Confirmation","Error Occurred During Deletion");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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

    /**
     * @param title
     * @param context
     * @param msg
     * @return
     * handles the confirmation dialogs
     */
    private boolean confirmAlert(String title, String msg,String context){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.setContentText(context);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            return true;
        }else if (result.get() == ButtonType.CANCEL){
            return false;
        }
        return false;
    }

    private void resetField(){
     fromDatePicker.getEditor().clear();
     toDatePicker.getEditor().clear();
    }
}
