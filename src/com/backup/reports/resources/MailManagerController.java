package com.backup.reports.resources;

import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.services.MailSender;
import com.backup.reports.util.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by ken $ jose on 7/20/2019.
 */
public class MailManagerController implements Initializable {
    /*Admin */
    private AdminData adminData;
    @FXML
    private Button saveSenderDetail;
    @FXML
    private TextField senderEmail;
    @FXML
    private PasswordField senderPassword;

    /* Mail Address tabPane*/
    @FXML
    private AnchorPane tableAnchorPane,reminderTableWrapper,mailAddressTabHolder;
    @FXML
    private TableView<ClientAddress> emailAddressTable;
    @FXML
    private TableColumn<ClientAddress, String> activeCol,equipmentUserCol,emailAddressCol,clientMAchineCol,executionTimeCol;
    @FXML
    private Button newEmailBtn, editEmailAddressBtn, deleteEmailAddressBtn, addressRefreshBtn,testSenderEmail;
    @FXML
    private TextField searchTextField;
    @FXML
    private FontAwesomeIconView searchClear;

    /*reminder and notification tabPane*/
    @FXML
    private TableView<Reminder> reminderTableView;
    @FXML
    private TableColumn<Reminder, String> reminderStatusCol, reminderOccurrenceCol, inventoryOccurrenceCol,
            reminderEquipmentUserCol,reminderClientMachineCol,reminderRecipientCol, reminderSubjectCol,reminderMessageCol;
    @FXML
    private Button newReminderBtn,reminderEditBtn,reminderDeleteBtn,reminderRefresh,mailComposeBtn;
    @FXML
    private ComboBox<String> reminderAcountMailCombo;
    @FXML
    private Label reminderSearchClear;
    @FXML
    private TextField reminderSearchTextField;

    /*Mail log tabPane*/
    @FXML
    private Tab mailLogTab;
    @FXML
    private TableColumn<MailLog, String> logStatusCol,logDateCol,logLevelCol,logRecipientCol,logMachineCol,logSubjectCol,logMessageIdCol,logInventoryFile;
    @FXML
    private TableView<MailLog> logTableView;
    @FXML
    private Button logReSendBtn, logDeleteBtn,mailLogRefresh;
    @FXML
    private TextField logSearchTextFieldBtn;
    @FXML
    private Label logSearchLabel;
    @FXML
    private FontAwesomeIconView logSearchClear,emailUp,emailDown;

    public static String NonEditedEmail = null;
    private ObservableList<ClientAddress> clientAddresses;
    private ObservableList<Reminder> reminders;
    private ObservableList<MailLog> mailLogs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonActionEvent();
        saveAdmin();
        //setTestSenderEmail();
        emailAddressTable.prefWidthProperty().bind(tableAnchorPane.widthProperty());
        reminderTableView.prefWidthProperty().bind(reminderTableWrapper.widthProperty());
        emailAddressTable.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        reminderTableView.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        logTableView.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);

        try {
            setReminderTableColValue(new MailManagerSchema().getAllReminder());
            setClientAddressColValue(new MailManagerSchema().getAllAddresses());
            setMailLogTableCol(new MailManagerSchema().getAllMailLog());
            //Table filter
            clientAddresses = FXCollections.observableArrayList();
            clientAddresses.addAll(new MailManagerSchema().getAllAddresses());
            FilteredList<ClientAddress> clientAddressFilteredList= new FilteredList<>(clientAddresses, p->true);
            searchTextField.textProperty().addListener((observable,oldValue,newValue)->
                    clientAddressFilteredList.setPredicate(clientAddress -> {
                if (newValue==null|| newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if(clientAddress.getClientMachine().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (clientAddress.getEquipmentUser().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }
                return false;
            }));
            SortedList<ClientAddress> sortedList = new SortedList<>(clientAddressFilteredList);
            sortedList.comparatorProperty().bind(emailAddressTable.comparatorProperty());
            emailAddressTable.setItems(sortedList);

            //reminderTable filter
            reminders = FXCollections.observableArrayList();
            reminders.addAll(new MailManagerSchema().getAllReminder());
            FilteredList<Reminder> reminderFilteredList= new FilteredList<>(reminders, p->true);
            reminderSearchTextField.textProperty().addListener((observable,oldValue,newValue)->
                    reminderFilteredList.setPredicate(reminders -> {
                if (newValue==null|| newValue.isEmpty()){
                    return true;
                }
                String reminderLowerCaseFilter = newValue.toLowerCase();

                if(reminders.getClientMachine().toLowerCase().contains(reminderLowerCaseFilter)){
                    return true;
                } else if (reminders.getEquipmentUser().contains(reminderLowerCaseFilter)) {

                    return true;
                }
                return false;
            }));
            SortedList<Reminder> reminderSortedList = new SortedList<>(reminderFilteredList);
            reminderSortedList.comparatorProperty().bind(reminderTableView.comparatorProperty());
            reminderTableView.setItems(reminderSortedList);

            //mailLog filter
            mailLogs = FXCollections.observableArrayList();
            mailLogs.addAll(new MailManagerSchema().getAllMailLog());
            FilteredList<MailLog> mailLogFilteredList= new FilteredList<>(mailLogs, p->true);
            logSearchTextFieldBtn.textProperty().addListener((observable,oldValue,newValue)->
                    mailLogFilteredList.setPredicate(mailLogs -> {
                        if (newValue==null|| newValue.isEmpty()){
                            return true;
                        }
                        String mailLogFilteredListToLowerCase = newValue.toLowerCase();

                        if(mailLogs.getRecipient().toLowerCase().contains(mailLogFilteredListToLowerCase)){
                            return true;
                        } else if (mailLogs.getClientMachine().contains(mailLogFilteredListToLowerCase)) {

                            return true;
                        }
                        return false;
                    }));
            SortedList<MailLog> mailLogSortedList = new SortedList<>(mailLogFilteredList);
            mailLogSortedList.comparatorProperty().bind(logTableView.comparatorProperty());
            logTableView.setItems(mailLogSortedList);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void buttonActionEvent(){
        //mail address
        newEmailBtn.setOnAction(e->addNewMailAddress());
        editEmailAddressBtn.setOnAction(e->editMailAddress());
        deleteEmailAddressBtn.setOnAction(e->setDeleteEmailAddress());
        //reminder and notification
        newReminderBtn.setOnAction(e->setNewReminder());
        reminderEditBtn.setOnAction(e->setEditMailReminder());
        searchClear.setOnMouseClicked(e->searchTextField.clear());
        reminderDeleteBtn.setOnAction(e-> setDeleteReminder());
        reminderRefresh.setOnAction(e-> setReminderTableColValue(new MailManagerSchema().getAllReminder()));
        addressRefreshBtn.setOnAction(e->setClientAddressColValue(new MailManagerSchema().getAllAddresses()));
        mailLogRefresh.setOnAction(e->setMailLogTableCol(new MailManagerSchema().getAllMailLog()));
        logSearchClear.setOnMouseClicked(e->logSearchTextFieldBtn.clear());
        testSenderEmail.setOnAction(e->setTestSenderEmail());
        mailComposeBtn.setOnAction(e-> setMailCompose());
        logReSendBtn.setOnAction(e->setLogMailReSend());
        logDeleteBtn.setOnAction(e->setDeleteMailLog());
        logTableView.setOnMouseClicked(e->{
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                ContextMenu contextMenu  = mailLOgContextMenu();
                Stage stage = (Stage) logTableView.getScene().getWindow();
                contextMenu.show(stage, e.getScreenX(), e.getScreenY());
            }
        });


    }
    //++++++++++++++ mail reminder section +++++++//____________________________________________________________________
    private void setEditMailReminder(){
        if (reminderTableView.getSelectionModel().getSelectedItem() != null){
                Reminder reminder = reminderTableView.getSelectionModel().getSelectedItem();
                new NewReminderController().setEditReminder(reminder.getEquipmentUser(),reminder.getClientMachine(),reminder.getRecipient());
        }else getAlert("Error Message","No reminder selected for editing");
    }

    /**
     * launch the reminder edit Dashboard
     */
    public void setNewReminder(){
        try {
            Stage newMail = new Stage();
            FXMLLoader fxmlLoader =  new FXMLLoader();
            Parent newMillAddress = fxmlLoader.load(getClass().getResource("newReminderFXML.fxml").openStream());
            Button updateButton = (Button) newMillAddress.lookup("#updateReminder");
            updateButton.setDisable(true);
            Scene scene =  new Scene(newMillAddress);
            newMail.setTitle("New Reminder");
            newMail.setScene(scene);
            newMail.initModality(Modality.APPLICATION_MODAL);
            newMail.setResizable(false);
            newMail.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setMailCompose(){
        try {
            Stage newMail = new Stage();
            FXMLLoader fxmlLoader =  new FXMLLoader();
            Parent newMillAddress = fxmlLoader.load((getClass().getResource("composeMailFXML.fxml")).openStream());
            Scene scene =  new Scene(newMillAddress);
            newMail.setTitle("Mail Compose");
            newMail.setScene(scene);
            newMail.initModality(Modality.APPLICATION_MODAL);
            newMail.setResizable(false);
            newMail.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set delete reminder
     */
    private void setDeleteReminder(){

        if (reminderTableView.getSelectionModel().getSelectedItem() != null){
            Reminder reminder  = reminderTableView.getSelectionModel().getSelectedItem();
            boolean result = new MailManagerSchema().deleteReminder(reminder.getRecipient());
            if (result){
                getAlert("Delete Information", "Reminder Delete");
            }else{
                getAlert("Delete Information", "Could not delete the reminder");
            }
        }else getAlert("Delete Information","No reminder selected for delete !");

    }

    /**
     * set Reminder table
     * @param reminderList
     */
    private void setReminderTableColValue(List<Reminder> reminderList){
        this.reminderStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.reminderOccurrenceCol.setCellValueFactory(new PropertyValueFactory<>("reminderOccurrence"));
        this.inventoryOccurrenceCol.setCellValueFactory(new PropertyValueFactory<>("inventoryOccurrence"));
        this.reminderEquipmentUserCol.setCellValueFactory(new PropertyValueFactory<>("equipmentUser"));
        this.reminderClientMachineCol.setCellValueFactory(new PropertyValueFactory<>("clientMachine"));
        this.reminderRecipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        this.reminderSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        this.reminderMessageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        this.reminders = FXCollections.observableArrayList();
        this.reminders.addAll(reminderList);

        this.reminderTableView.setItems(null);
        this.reminderTableView.setItems(reminders);
    }
    //+++++++++ mail address section+++++++++//______________________________________________________________________
    private void setDeleteEmailAddress(){

        if (emailAddressTable.getSelectionModel().getSelectedItem() != null){
            ClientAddress clientAddress  = emailAddressTable.getSelectionModel().getSelectedItem();
            boolean result = new MailManagerSchema().deleteClient(clientAddress.getEmail());
            if (result){
                getAlert("Delete Information", "Address Delete");
            }else{
                getAlert("Delete Information", "Problem Occurred While Try To Delete");
            }

        }else getAlert("Delete Information","Nothing selected for delete");

    }
    /**
     * Populate the client address table
     * @param clientAddressList
     */
    public void setClientAddressColValue(List<ClientAddress> clientAddressList){
        this.activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        this.equipmentUserCol.setCellValueFactory(new PropertyValueFactory<>("equipmentUser"));
        this.emailAddressCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.clientMAchineCol.setCellValueFactory(new PropertyValueFactory<>("clientMachine"));
        this.executionTimeCol.setCellValueFactory(new PropertyValueFactory<>("executionTime"));
        clientAddresses = FXCollections.observableArrayList();
        clientAddresses.addAll(clientAddressList);

        this.emailAddressTable.setItems(null);
        this.emailAddressTable.setItems(this.clientAddresses);
    }

    /**
     * new email address
     */
    private void addNewMailAddress(){
        try {
            Button update;
            Stage newMail = new Stage();
            FXMLLoader dbSettingLoader =  new FXMLLoader();
            Parent newMillAddress = dbSettingLoader.load(getClass().getResource("newMailFXML.fxml").openStream());
            update = (Button) newMillAddress.lookup("#update");
            update.setDisable(true);
            Scene scene =  new Scene(newMillAddress);
            newMail.setTitle("New Client");
            newMail.setScene(scene);
            newMail.initModality(Modality.APPLICATION_MODAL);
            newMail.setResizable(false);
            newMail.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * edit the mail address
     */

    private void editMailAddress(){
        if (emailAddressTable.getSelectionModel().getSelectedItem() != null) {
           ClientAddress clientAddress  = emailAddressTable.getSelectionModel().getSelectedItem();
           NonEditedEmail = clientAddress.getEmail();
            try {
                TextField name,email,time;
                Button save;
                ComboBox<String> clientMachine;
                Stage newMail = new Stage();
                FXMLLoader dbSettingLoader =  new FXMLLoader();
                Parent newMailPane = dbSettingLoader.load(getClass().getResource("newMailFXML.fxml").openStream());
                Scene scene =  new Scene(newMailPane);

                name = (TextField) newMailPane.lookup("#name");
                name.setText(clientAddress.getEquipmentUser());
                email = (TextField) newMailPane.lookup("#email");
                email.setText(clientAddress.getEmail());
                time = (TextField) newMailPane.lookup("#time");
                time.setText(clientAddress.getExecutionTime());
                clientMachine= (ComboBox<String>) newMailPane.lookup("#clientMachine");
                clientMachine.getEditor().setText(clientAddress.getClientMachine());
                save = (Button) newMailPane.lookup("#save");
                save.setDisable(true);

                newMail.setTitle("Edit Client Details");
                newMail.setScene(scene);
                newMail.initModality(Modality.APPLICATION_MODAL);
                newMail.setResizable(false);
                newMail.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getAlert("Information Alert","Nothing selected for edit");
        }
    }

    //+++++++++++++++++mail logging section+++++++++++++++++++++++++++++++

    private void setMailLogTableCol(List<MailLog> mailLogList){
        logStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        logDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        logLevelCol.setCellValueFactory(new PropertyValueFactory<>("log_Level"));
        logRecipientCol.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        logMachineCol.setCellValueFactory(new PropertyValueFactory<>("clientMachine"));
        logSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        logMessageIdCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        logInventoryFile.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        mailLogs= FXCollections.observableArrayList();
        mailLogs.addAll(mailLogList);
        this.logTableView.setItems(null);
        this.logTableView.setItems(this.mailLogs);

    }
    //..........admin.............//

    private void saveAdmin(){
        saveSenderDetail.setOnAction(event ->{
            if (!senderEmail.getText().isEmpty()&&!senderPassword.getText().isEmpty()){
                this.adminData = new AdminData(senderEmail.getText(), senderPassword.getText());
                if (new MailManagerSchema().getAdmin()!=null){
                    new MailManagerSchema().updateAdmin(new AdminData(senderEmail.getText(),senderPassword.getText()),
                            1);
                }else new MailManagerSchema().addAdmin(adminData);
            }else {
                if (senderEmail.getText().isEmpty()) {
                    senderEmail.setStyle("-fx-border-color: red");
                }else senderPassword.setStyle("-fx-border-color: red");
            }
        });
        mailAddressTabHolder.setOnMouseClicked(e->{
            if (!senderEmail.isFocused()){
                senderEmail.setStyle("-fx-border-color: #e6e6ff");
            }
            if (!senderPassword.isFocused())
                senderPassword.setStyle("-fx-border-color: #e6e6ff");
        });
    }

    private void setTestSenderEmail(){
        boolean onOff=false;
        if (!senderEmail.getText().isEmpty() && !senderPassword.getText().isEmpty()){
            String body = "<p>Dear User</p><br>" +
                    "<p>You Email is currently being used by <b>BACKUP INVENTORY SOFTWARE</b> to send reminder and backup inventory files<br>" +
                    "to the registered clients</p><br><br><p style=\"text-align: center; font-size: 9pt;\">***********--For Queries Call/Email--***************<br>" +
                    "Joseph Wambugu: phone:0791510069 email: kibew.joseph@gmail.com<br> Kennedy Mutugi: phone:0704345654 email:ruteremutugi@gmail.com<br>" +
                    "************__________________*****************</p>";
            onOff= new MailSender().sendMail(
                    "NOTIFICATION FROM BACKUP INVENTORY  SOFTWARE",
                    body,senderEmail.getText(),false,"none");
        }
        if (onOff){
            emailUp.setFill(Color.valueOf("#1ACC12"));
            emailDown.setFill(Color.valueOf("#000000"));
        }else {
            emailDown.setFill(Color.valueOf("#EA221F"));
            emailUp.setFill(Color.valueOf("#000000"));
        }
    }

    /**
     * delete mail log
     */
    private void setDeleteMailLog(){
        if (logTableView.getSelectionModel().getSelectedItem() != null){
            MailLog mailLog  = logTableView.getSelectionModel().getSelectedItem();
            //not++++++++++++++
            boolean result = new MailManagerSchema().deleteMailLog(mailLog.getDate());
            if (result){
                getAlert("Delete Information", "Mail log Delete");
            }else{
                getAlert("Delete Information", "Problem Occurred While Try To Delete");
            }

        }else getAlert("Delete Information","Nothing selected for delete");
    }

    /**
     * Log Mail Resend
     */
    private void setLogMailReSend(){
        if (logTableView.getSelectionModel().getSelectedItem() != null){
            MailLog mailLog  = logTableView.getSelectionModel().getSelectedItem();
            if (mailLog.getInventory().equalsIgnoreCase("none")){
                boolean mailSent =new MailSender().sendMail(
                        mailLog.getSubject(),
                        getMessageBody(mailLog.getMessage()),
                        mailLog.getRecipient(),
                        false,
                        "none"
                );
                if (mailSent){
                    getAlert("Mail send notification","You email has been sent");
                }else getAlert("Mail send notification","You email has not been sent");
            }else{
                boolean mailSent =new MailSender().sendMail(
                        mailLog.getSubject(),
                        getMessageBody(mailLog.getMessage()),
                        mailLog.getRecipient(),
                        true,
                        mailLog.getInventory()
                );
                if (mailSent){
                    getAlert("Mail send notification","You email has been sent");
                }else getAlert("Mail send notification","You email has not been  sent");
            }

        }else{
            getAlert("Delete Information","Nothing selected for re-send");
        }
    }

    private ContextMenu mailLOgContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem itemOne = new MenuItem("Resend Selected Log");
        MenuItem itemTwo = new MenuItem("Delete Selected Log");
        MenuItem itemThree = new MenuItem("Delete All Logs");
        MenuItem itemFour = new MenuItem("Cancel");
        itemOne .setOnAction(e-> setLogMailReSend());
        itemTwo.setOnAction(e->setDeleteMailLog());
        itemThree.setOnAction(e->{
            if (new MailManagerSchema().deleteAllMailLog()) {
                getAlert("Logs Delete","All Mail Logs Deleted");
            }else getAlert("Logs Delete", "Failed To Delete The Logs");

        });
        contextMenu.getItems().addAll(itemOne,itemTwo,itemThree,itemFour);

        return contextMenu;
    }

    /**
     * Alert box
     * @param title
     * @param context
     */
    public void getAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

    private String getMessageBody(String messageId){
        List<MailMessage> mailMessageList = new  MailManagerSchema().getAllMailMessage();
        for (MailMessage mailMessage : mailMessageList){
            if (mailMessage.getMessageId().equalsIgnoreCase(messageId)) {
                return mailMessage.getMessage();
            }
        }
        return messageId;
    }

}
