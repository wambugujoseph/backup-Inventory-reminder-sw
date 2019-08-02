package com.backup.reports.resources;

import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.services.MailSender;
import com.backup.reports.util.ClientAddress;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by ken on 7/28/2019.
 */
public class ComposeMailController implements Initializable {
    @FXML
    private Label resultLabel;
    @FXML
    private ComboBox<String> toCombo;
    @FXML
    private TextField subjectTextField,attachmentTextField;
    @FXML
    private HTMLEditor mailBodyEditor;
    @FXML
    private MaterialIconView attachmentFile;
    @FXML
    private Button send,reset,exit;

    private MaterialIconView icon = new MaterialIconView(MaterialIcon.DONE_ALL);
    MaterialIconView iconCancel = new MaterialIconView(MaterialIcon.CLOSE);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateCombo();
        exit.setOnAction(e->resetFields());
        attachmentFile.setOnMouseClicked(e->attachmentFileChooser());
        send.setOnAction(e->invokeMailSend());
        reset.setOnAction(e->resetFields());
        resultLabel.setOnMouseClicked(event ->{
            resultLabel.setText("");
            resultLabel.setGraphic(null);
        });
        exit.setOnAction(event ->{
            Stage stage = (Stage) attachmentFile.getScene().getWindow();
            stage.close();
        });
    }

    private void populateCombo(){
        try {
            List<ClientAddress> clientAddressList =  new MailManagerSchema().getAllAddresses();
            ObservableList<String> emails   = FXCollections.observableArrayList();
            for (ClientAddress clientAddress : clientAddressList){
                emails.add(clientAddress.getEmail());
            }
            try {
                toCombo.getItems().addAll(emails);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFields(){

        subjectTextField.clear();
        mailBodyEditor.setHtmlText("");
        attachmentTextField.clear();
        resultLabel.setGraphic(null);
        resultLabel.setText("");

    }

    private void attachmentFileChooser(){
        Stage stage = (Stage) attachmentFile.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selecteFile = fileChooser.showOpenDialog(stage);
        if(selecteFile==null){
            attachmentTextField.setText("No directory selected");
        }else
            attachmentTextField.setText(selecteFile.getAbsolutePath());
    }

    private void invokeMailSend(){
        boolean bl;
        if (!subjectTextField.getText().isEmpty() && !mailBodyEditor.getHtmlText().isEmpty()& !toCombo.getValue().isEmpty() &&!attachmentTextField.getText().isEmpty()){
            File file = new File(attachmentTextField.getText());
            if (file.isFile()) {
                bl =  new MailSender().sendMail(subjectTextField.getText(),
                          mailBodyEditor.getHtmlText(),
                          toCombo.getValue(),
                          true,
                        file.getName());
                setResultTPane(bl);
            }else{
                new MailManagerController().getAlert("Invalid file", "Please select a valid file");
            }

        }else if (!subjectTextField.getText().isEmpty() && !mailBodyEditor.getHtmlText().isEmpty()& !toCombo.getValue().isEmpty()){
           bl = new MailSender().sendMail(subjectTextField.getText(),
                    mailBodyEditor.getHtmlText(),
                    toCombo.getValue(),
                    false,
                    "none");
           setResultTPane(bl);
        }
    }

    public void setResultTPane(boolean bl) {
        icon.setSize("60");
        iconCancel.setSize("60");
        if (bl){
            resultLabel.setGraphic(icon);
            resultLabel.setText("Sent");
            icon.setFill(Color.valueOf("#0A6E0E"));
        }else{
            resultLabel.setGraphic(iconCancel);
            iconCancel.setFill(Color.valueOf("#E9262D"));
            resultLabel.setText("Failed");
        }
    }
    private void modifyPath(String absolutePath){

    }
}
