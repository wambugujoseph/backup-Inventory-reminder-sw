package com.backup.reports.resources;

import com.backup.reports.database.DbConnection;
import com.backup.reports.util.ReadConfigFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class settingController implements Initializable {


    @FXML
    private TextField hostNameTxtId;
    @FXML
    private TextField dbNameTxtId;
    @FXML
    private PasswordField passwordTxtId;
    @FXML
    private TextField pathTxtId;
    @FXML
    private Pane dbStatus;
    @FXML
    private Button extBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDbStatus();
        extBtn.setOnAction(event -> closeCurrentWondow());
    }

    @FXML
    public  void dbConfigSaveId(ActionEvent event){
        dbConfigs();
    }

    @FXML
    public void pathBtnId(ActionEvent event){
        selectBackupPath();
        //extBtn.setOnAction(e->openDbSettings());

    }
    @FXML
    private void  pathSaveBtnId(ActionEvent event){
        updateBackUpPath();
    }
    @FXML
    private void clearPath(ActionEvent event){
        pathTxtId.setText("");
    }
    @FXML
    private  void  clearField(ActionEvent event){
        hostNameTxtId.clear();
        dbNameTxtId.clear();
        passwordTxtId.clear();
    }


    public  void updateBackUpPath(){
      String myPath = pathTxtId.getText();
        String alertTitle = "Backup Path Setting Confirmation";
        String message = "This will alter you backup path\n" +
                "Note: change it only when the back configuration on the server has changed";
        String context =  "Are you sure you want to do this?";

        if (confirmAlert(alertTitle,message,context)) {
            boolean response = new ReadConfigFile().updateBackUpPath(myPath);

            if (response) {
                Text txt = new Text(10, 40, "BackUpPath set");
                txt.setStroke(Color.RED);
                txt.setFont(Font.font(10));
                dbStatus.getChildren().clear();
                dbStatus.getChildren().add(txt);
                showDbStatus();
            } else {
                Text txt = new Text(10, 40, "BackUp path not set");
                txt.setStroke(Color.RED);
                txt.setFont(Font.font(10));
                dbStatus.getChildren().clear();
                dbStatus.getChildren().add(txt);
            }
        }
    }

    /**
     * Updates the configuration file of the database
     */

    private  void dbConfigs(){

       String host = hostNameTxtId.getText();
       String dbName =dbNameTxtId.getText();
       String password = passwordTxtId.getText();
       String alertTitle = "Database Configuration Confirmation";
       String message = "This will alter you database configuration \n" +
               "You may not be able to access the database";
       String context =  "Are you sure you want to do this?";


       if (confirmAlert(alertTitle,message,context)){

           boolean response = new ReadConfigFile().updateDbConfigFile(host,dbName,password);
           if (response){
               Text txt =  new Text(10,10,"Database configuration set");
               txt.setStroke(Color.GREEN);
               txt.setFont(Font.font(10));
               dbStatus.getChildren().clear();
               dbStatus.getChildren().add(txt);
               showDbStatus();
           }else{
               Text txt =  new Text(10,10,"Database configuration not set");
               txt.setStroke(Color.GREEN);
               txt.setFont(Font.font(10));
               dbStatus.getChildren().clear();
               dbStatus.getChildren().add(txt);
           }
       }


    }

    /**
     *  get the path  of the backup directory
     */
    public void selectBackupPath(){
        Stage dbSetting = new Stage();
        FXMLLoader dbSettingLoader =  new FXMLLoader();
        Pane dbSettingsRoot = null;
        try {
            dbSettingsRoot = dbSettingLoader.load(getClass().getResource("settingsFXML.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene =  new Scene(dbSettingsRoot);
        dbSetting.setScene(scene);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(dbSetting);
        if(selectedDirectory==null){
            pathTxtId.setText("No directory selected");
        }else
            pathTxtId.setText(selectedDirectory.getAbsolutePath());
    }

    /**
     *  checks whether the database is connected and updates the UI
     */

    public void showDbStatus(){

        if (new DbConnection().isDatabaseConnected()){
            Text txt =  new Text(10,20,"DataBase connected");
            txt.setStroke(Color.GREEN);
            txt.setFont(Font.font(12));

            dbStatus.getChildren().add(txt);
            //System.out.println("database connected");

        }else{
            Text txt =  new Text(10,10,"DataBase Not connected");
            txt.setStroke(Color.GREEN);
            txt.setFont(Font.font(12));
            dbStatus.getChildren().add(txt);
        }
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

    private void closeCurrentWondow(){
        Stage stage = (Stage) extBtn.getScene().getWindow();
        stage.close();
    }

}
