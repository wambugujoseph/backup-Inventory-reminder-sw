package com.backup.reports.resources;

import com.backup.reports.database.DbConnection;
import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.services.BackupFileService;
import com.backup.reports.util.ClientAddress;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.backup.reports.resources.MailManagerController.NonEditedEmail;

public class NewMailController implements Initializable{

    @FXML
    private TextField name,email,time;
    @FXML
    private ComboBox<String> clientMachine;
    @FXML
    private RadioButton active,inactive;
    @FXML
    private Button update, save,close;

    private ToggleGroup toggleRadio = new ToggleGroup();
    private ClientAddress clientAddress;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Method call
        radioGroupToggle();
        active.setSelected(true);
        update.setOnAction(e->editClientAddress());
        save.setOnAction(e->addClientAddress());
        populateClientMachineCombo();
        close.setOnAction(event -> closeCurrentWindow());
    }

    /**
     * ToggleRadio Button
     */
    private void radioGroupToggle(){
        active.setToggleGroup(toggleRadio);
        inactive.setToggleGroup(toggleRadio);
    }

    private void addClientAddress(){
        String editedActive;
        if(!name.getText().isEmpty()&& !email.getText().isEmpty()&& !time.getText().isEmpty()&& !clientMachine.getValue().isEmpty()){
            if (toggleRadio.getSelectedToggle() == active){
                editedActive  = "YES";
            }else editedActive = "NO";

            this.clientAddress = new ClientAddress(editedActive,name.getText(),email.getText(),clientMachine.getValue(),time.getText());
            boolean result = new MailManagerSchema().saveEmail(clientAddress);//schema and ui connection
            if (result){
                getAlert("Success Message","Client Details Created Successfully");
            }else getAlert("Failure Message", "Client Not created");
        }else{
            getAlert("Information Alert","Ensure all the field are not empty");
        }
    }

    /**
     * editClient address details
     */
    private void editClientAddress(){
        String editedActive;
        if(!name.getText().isEmpty()&& !email.getText().isEmpty()&& !time.getText().isEmpty()&& !clientMachine.getValue().isEmpty()){
            if (toggleRadio.getSelectedToggle() == active){
                editedActive  = "YES";
            }else editedActive = "NO";

            this.clientAddress = new ClientAddress(editedActive,name.getText(),email.getText(),clientMachine.getEditor().getText(),time.getText());
            boolean result = new MailManagerSchema().editAddress(clientAddress,NonEditedEmail);
            if (result){
                getAlert("Success Message","Client Details Update Successfully");
            }else getAlert("Failure Message", "Client Details Not Edited");
        }else{
            getAlert("Information Alert","Ensure all the field are not empty");
        }
    }

    /**
     * Alert box
     * @param title
     * @param context
     */
    private void getAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

    /**
     * populate the client Machine combo with the available sever names
     */
    private void populateClientMachineCombo(){
        if (new DbConnection().isDatabaseConnected()) {
            if (!(new BackupFileService().getServerName().isEmpty())) {
                ObservableList<String> serverNames =
                        FXCollections.observableArrayList(new BackupFileService().getServerName());
                clientMachine.getItems().addAll(serverNames);
            }
        }
    }

    private void closeCurrentWindow(){
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
