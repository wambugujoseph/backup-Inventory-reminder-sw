package com.backup.reports.resources;

import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NewReminderController implements Initializable {

    @FXML
    private Pane emailEditorSection;
    @FXML
    private ComboBox<InventoryOccurrenceOption> reminderInventoryOccurrenceCombo, reminderOccurrenceCombo;
    @FXML
    private ComboBox<String> reminderEquipmentUserCombo, reminderCustomCombo;
    @FXML
    private TextField reminderClientMachine, reminderRecipientEmail, reminderMailSubjectTextField,msgId;
    @FXML
    private RadioButton defaultRadioBtn, customRadioButton, activeRadioBtn, inactiveBtn;
    @FXML
    private HTMLEditor reminderMessageBodyEditor;
    @FXML
    private Button reminderDefaultPreviewBtn, reminderSaveBtn, reminderDoneBtn, reminderExitBtn,messageUpdate,reminderClearBtn;

    private ToggleGroup editorRadioButtonToggleGroup = new ToggleGroup();
    private ToggleGroup activeInactiveRadioBtnToggleGroup = new ToggleGroup();
    private Map<String, ClientAddress> clientAddressMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getResourceMethod();
            reminderEquipmentUserCombo.setOnAction(e->setTextToTextField());
            reminderSaveBtn.setOnAction(e->saveNewMailMessage());
            reminderDoneBtn.setOnAction(e-> createReminder());
            reminderDefaultPreviewBtn.setOnAction(e->{
                showMailMessage("default");
                reminderMailSubjectTextField.setDisable(false);
                reminderMessageBodyEditor.setDisable(false);
                reminderSaveBtn.setDisable(false);
                reminderClearBtn.setDisable(false);
                msgId.setDisable(false);
                messageUpdate.setDisable(false);
            });
            messageUpdate.setOnAction(e->setMessageUpdate());
            reminderClearBtn.setOnAction(e-> clearMailEditor());
            reminderCustomCombo.setOnAction(event -> showMailMessage(reminderCustomCombo.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        reminderExitBtn.setOnAction(e->closeCurrentWindow());
    }

    private void showMailMessage(String msgID){
        List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
        for (MailMessage mailMessage: mailMessageList){
            if(mailMessage.getMessageId().contains(msgID)){
                reminderMessageBodyEditor.setHtmlText(mailMessage.getMessage());
                reminderMailSubjectTextField.setText(mailMessage.getSubject());
                msgId.setText(mailMessage.getMessageId());
            }
        }

    }

    private void createReminder() {
        String msgID;
        String active;
        try {
            if (!reminderEquipmentUserCombo.getValue().isEmpty()&& !reminderOccurrenceCombo.getValue().toString().isEmpty()&&
                    !reminderInventoryOccurrenceCombo.getValue().toString().isEmpty()) {
                String equipmentUser = reminderEquipmentUserCombo.getValue();
                String inventoryOccurrence = reminderInventoryOccurrenceCombo.getValue().value();
                String reminderOccurrence = reminderOccurrenceCombo.getValue().value();
                String recipient = reminderRecipientEmail.getText();
                if(editorRadioButtonToggleGroup.getSelectedToggle() == customRadioButton ){
                    msgID= reminderCustomCombo.getValue();
                }else msgID = "default";
                if (activeInactiveRadioBtnToggleGroup.getSelectedToggle()== inactiveBtn){
                    active= "Inactive";
                }else active = "Active";
               boolean result = new MailManagerSchema().setMailReminder(
                       new Reminder(active,reminderOccurrence,inventoryOccurrence,recipient,"",msgID,equipmentUser,""));
               if (result){
                   new MailManagerController().getAlert("Success Message" ,"Reminder successfully created");
               }
            } else {
                new MailManagerController().getAlert("Error Message","Some fields are empty");
            }
        } catch (Exception e) {
            new MailManagerController().getAlert("Error Message","Please ensure fields are not empty");
        }
    }

    private void clearMailEditor(){
        reminderMessageBodyEditor.setHtmlText("");
        reminderMailSubjectTextField.clear();
        msgId.clear();
    }

    /**
     * Call the first method to be loaded before
     */
    private void getResourceMethod() {
        setRadioButtonToggleGroup();
        setEmailEditorSectionDisable();
        populateComboBox();

    }

    /**
     * set Radio Button
     */
    private void setRadioButtonToggleGroup() {
        defaultRadioBtn.setToggleGroup(editorRadioButtonToggleGroup);
        customRadioButton.setToggleGroup(editorRadioButtonToggleGroup);
        defaultRadioBtn.setSelected(true);
        activeRadioBtn.setToggleGroup(activeInactiveRadioBtnToggleGroup);
        inactiveBtn.setToggleGroup(activeInactiveRadioBtnToggleGroup);
        activeRadioBtn.setSelected(true);
    }

    /**
     * Disable and enable the mailEditor component
     */
    private void setEmailEditorSectionDisable() {
        reminderMailSubjectTextField.setDisable(true);
        reminderMessageBodyEditor.setDisable(true);
        reminderSaveBtn.setDisable(true);
        reminderClearBtn.setDisable(true);
        msgId.setDisable(true);
        reminderCustomCombo.setDisable(true);
        messageUpdate.setDisable(true);

        customRadioButton.setOnMouseClicked(e -> {
            reminderMailSubjectTextField.setDisable(false);
            reminderMessageBodyEditor.setDisable(false);
            reminderSaveBtn.setDisable(false);
            reminderClearBtn.setDisable(false);
            msgId.setDisable(false);
            reminderCustomCombo.setDisable(false);
            messageUpdate.setDisable(false);
        });

        defaultRadioBtn.setOnMouseClicked(e -> {
            reminderMailSubjectTextField.setDisable(true);
            reminderMessageBodyEditor.setDisable(true);
            reminderSaveBtn.setDisable(true);
            reminderClearBtn.setDisable(true);
            msgId.setDisable(true);
            reminderCustomCombo.setDisable(true);
        });
    }

    private void populateComboBox() {
        this.reminderInventoryOccurrenceCombo.setItems(FXCollections.observableArrayList(InventoryOccurrenceOption.values()));
        this.reminderOccurrenceCombo.setItems(FXCollections.observableArrayList(InventoryOccurrenceOption.values()));
        List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
        List<ClientAddress> clientAddressList = new MailManagerSchema().getAllAddresses();
        List<String> clientNames = new ArrayList<>();
        List<String> emailMassageName = new ArrayList<>();
        for (ClientAddress address : clientAddressList) {
            clientNames.add(address.getEquipmentUser());
        }
        for (MailMessage mailMessage : mailMessageList){
            emailMassageName.add(mailMessage.getMessageId());
        }
        reminderEquipmentUserCombo.setItems(FXCollections.observableArrayList(clientNames));
        reminderCustomCombo.setItems(FXCollections.observableArrayList(emailMassageName));

    }

    public void setEditReminder(String equipmentUser, String clientMachine, String recipient){
        //launch reminder edit modal
        TextField client, recipientEmail;
        ComboBox  user,inventory,reminder,customCombo;
        RadioButton active, inactive,defaultMsg,customMsg;
        Button reminderDoneButton;
        try {
            Stage newMail = new Stage();
            FXMLLoader fxmlLoader =  new FXMLLoader();
            Pane newMillAddress = fxmlLoader.load(getClass().getResource("newReminderFxml.fxml").openStream());

            reminderDoneButton = (Button) newMillAddress.lookup("#reminderDoneBtn");
            reminderDoneButton.setDisable(true);
            client= (TextField) newMillAddress.lookup("#reminderClientMachine");
            client.setText(clientMachine);
            client.setDisable(true);
            recipientEmail = (TextField) newMillAddress.lookup("#reminderRecipientEmail");
            recipientEmail.setText(recipient);
            recipientEmail.setDisable(true);
            user = (ComboBox) newMillAddress.lookup("#reminderEquipmentUserCombo");
            user.setValue(equipmentUser);
            customCombo = (ComboBox) newMillAddress.lookup("#reminderCustomCombo");
            inventory = (ComboBox) newMillAddress.lookup("#reminderInventoryOccurrenceCombo");
            reminder = (ComboBox) newMillAddress.lookup("#reminderOccurrenceCombo");
            active = (RadioButton) newMillAddress.lookup("#activeRadioBtn");
            inactive = (RadioButton) newMillAddress.lookup("#inactiveBtn");
            defaultMsg = (RadioButton) newMillAddress.lookup("#defaultRadioBtn");
            customMsg = (RadioButton) newMillAddress.lookup("#customRadioButton");
            user.setDisable(true);
            Button update  = (Button) newMillAddress.lookup("#updateReminder");

            update.setOnAction(event -> {
                try {
                    if (!inventory.getValue().toString().isEmpty()&&!reminder.getValue().toString().isEmpty()){
                        MutableString isActive =  new MutableString();
                        MutableString msg = new MutableString();
                        if (active.isSelected()){
                            isActive.setMyString("Active");
                        }else if
                                (inactive.isSelected()) isActive.setMyString("Inactive");
                        if (defaultMsg.isSelected()){
                            msg.setMyString("Default");
                        }else if (customMsg.isSelected())
                            msg.setMyString(customCombo.getValue().toString());

                        boolean result = new MailManagerSchema().editReminder(
                                new Reminder(isActive.getMyString(),reminder.getValue().toString(),inventory.getValue().toString(),recipientEmail.getText(),"", msg.getMyString(),user.getValue().toString(),
                                        client.getText()),recipientEmail.getText());
                        if (result){
                            new MailManagerController().getAlert("Success Message","Reminder updated successfully");
                        }else
                            new MailManagerController().getAlert("Error Message","Reminder update failed !");
                    }else
                        new MailManagerController().getAlert("Error Message","Some fields are empty");
                } catch (Exception e) {
                    new MailManagerController().getAlert("Failure Message","Please ensure fields are not empty");
                }

            });
            Scene scene =  new Scene(newMillAddress);
            newMail.setTitle("Edit Reminder");
            newMail.setScene(scene);
            newMail.initModality(Modality.APPLICATION_MODAL);
            newMail.setResizable(false);
            newMail.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setTextToTextField() {
        List<ClientAddress> clientAddressList = new MailManagerSchema().getAllAddresses();
        for (ClientAddress clientAddress : clientAddressList) {
            clientAddressMap.put(clientAddress.getEquipmentUser(), clientAddress);
        }
        if (!reminderEquipmentUserCombo.getValue().isEmpty()) {
            reminderClientMachine.setText(clientAddressMap.get(reminderEquipmentUserCombo.getValue()).getClientMachine());
            reminderClientMachine.setDisable(true);
            reminderRecipientEmail.setText(clientAddressMap.get(reminderEquipmentUserCombo.getValue()).getEmail());
            reminderRecipientEmail.setDisable(true);
        }
    }

    private void saveNewMailMessage(){
        if (!reminderMessageBodyEditor.getHtmlText().isEmpty() && !reminderMailSubjectTextField.getText().isEmpty()){
            if (!msgId.getText().isEmpty()) {
                boolean result = new MailManagerSchema().CreateMAilMessage(new MailMessage(
                        reminderMessageBodyEditor.getHtmlText(),msgId.getText(),reminderMailSubjectTextField.getText()
                ));
                msgId.setStyle("-fx-border-color: gainsboro");
                checkFiled(result);

            } else {
                new MailManagerController().getAlert("Error Message","Please enter the name of you Custom Mail");
                msgId.setStyle("-fx-border-color: red");
            }
        }else{
            new MailManagerController().getAlert("Error Message","Ensure you have create the subject and message body");
        }

    }

    private void setMessageUpdate(){
        if (!reminderMessageBodyEditor.getHtmlText().isEmpty() && !reminderMailSubjectTextField.getText().isEmpty()){
            if (!msgId.getText().isEmpty()) {
                boolean result =new MailManagerSchema().updateMailMessage(new MailMessage(
                        reminderMessageBodyEditor.getHtmlText(),msgId.getText(),reminderMailSubjectTextField.getText()),
                        msgId.getText());
                msgId.setStyle("-fx-border-color: gainsboro");
                checkFiled(result);
            }
        }else{
            new MailManagerController().getAlert("Error Message","Ensure you have create the subject and message body");
        }
    }

    private void checkFiled(boolean result){
        if (result){
            new MailManagerController().getAlert("Success Message","Massage Saved As: "+msgId.getText());
            populateComboBox();
            reminderCustomCombo.setValue(msgId.getText());
        }else {
            new MailManagerController().getAlert("Success Message","Massage Not Save. Please change message name and save again");
            msgId.clear();
            msgId.setStyle("-fx-border-color: red");
        }
    }

    private void closeCurrentWindow(){
        Stage stage = (Stage) reminderExitBtn.getScene().getWindow();
        stage.close();
    }
}
