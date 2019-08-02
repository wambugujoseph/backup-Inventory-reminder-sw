package com.backup.reports.resources;

import com.backup.reports.database.DbConnection;
import com.backup.reports.database.MailManagerSchema;
import com.backup.reports.services.*;
import com.backup.reports.util.ClientAddress;
import com.backup.reports.util.DiskSpace;
import com.backup.reports.util.MailMessage;
import com.backup.reports.util.Report;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.backup.reports.util.Report.createReportPDF;
import static com.backup.reports.util.ScreenSize.GetPrefScreenHeight;
import static com.backup.reports.util.ScreenSize.GetPrefScreenWidth;

public class mainController implements Initializable {
    @FXML
    private Label contentTitle, mailManager;
    @FXML
    public ComboBox<String> serverNameCombo;
    @FXML
    private Button settingBtnId;
    @FXML
    private ProgressBar diskProgressBarId;
    @FXML
    private Button fromToDateButton;
    @FXML
    private MenuItem quite;
    @FXML
    private MenuItem help, developer;
    @FXML
    private ScrollPane displayArea;
    @FXML
    private Button fixedDateButtonId;
    @FXML
    private DatePicker datePikerOne;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button printPreview;
    @FXML
    private Button resetFields;
    @FXML
    private Button clearBackupBtn;
    @FXML
    private Button printBackupInventBtn;
    @FXML
    private Button backUpRefreshBtn;
    @FXML
    private Button mysqlShell, instantMailBtn;
    @FXML
    private AnchorPane indicatorPane;

    private int checker;

    private int reportSelector;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //progress Bar
        progressBarSet();
        populateServerNameCombox();
        //close the main window
        backUpRefreshBtn.setOnAction(e -> {
            refreshBackUp();
            progressBarSet();
            populateServerNameCombox();
        });
        //start mysql shel
        mysqlShell.setOnAction(e -> startMySQLShell());

        mailManager.setOnMouseClicked(e -> setMailManager());
        help.setOnAction(e -> showHelpPage());
        clearBackupBtn.setOnAction(e -> openClearBackupDetails());
        quite.setOnAction(e -> Platform.exit());
        settingBtnId.setOnAction(e -> openDbSettings());
        resetFields.setOnAction(e -> ClearInput());
        printBackupInventBtn.setOnAction(e -> openDairyInventoryPrint());
        instantMailBtn.setOnMouseClicked(event -> {
            ContextMenu contextMenu = setInstantMailContextMenu();
            contextMenu.show(instantMailBtn, event.getScreenX(), event.getScreenY());
        });

        fixedDateButtonId.setOnAction(e -> {
            try {
                this.reportSelector = 1;
                checker = 1;
                showTable();
            } catch (ParseException ignored) {
            }
        });
        fromToDateButton.setOnAction(e -> {
            try {
                checker = 2;
                datePikerOne.getEditor().clear();
                showTable();
            } catch (ParseException ignored) {
            }
        });

        printPreview.setOnAction(e -> {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            if (this.reportSelector == 1) {
                String date = datePikerOne.getEditor().getText();
                try {
                    printDailyReport(Date.valueOf(dateFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else if (this.reportSelector == 2) {
                String fromDate = fromDatePicker.getEditor().getText();
                String toDate = toDatePicker.getEditor().getText();
                try {
                    printDepReportFromTo(serverNameCombo.getValue(),
                            Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                            Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else if (this.reportSelector == 3) {
                printDepBasedReport(serverNameCombo.getValue());
                createReportPDF();
            }
        });

        developer.setOnAction(e -> developerPane());
    }

    private ContextMenu setInstantMailContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        CheckMenuItem relatedClient = new CheckMenuItem("Send Related client");
        relatedClient.setSelected(true);
        MenuItem itemTwo = new MenuItem("Send To A Different Email");
        SeparatorMenuItem s1 = new SeparatorMenuItem();
        MenuItem itemThree = new MenuItem("Cancel");
        contextMenu.getItems().addAll(relatedClient, itemTwo, s1, itemThree);
        relatedClient.setOnAction(e -> {
            List<String> emailList = getClientEmail();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
            //System.out.println("clicked");
            if (this.reportSelector == 2) {
                String fromDate = fromDatePicker.getEditor().getText();
                String toDate = toDatePicker.getEditor().getText();
                try {
                    String path5 = new InventoryMailAttachment().createDepInventoryPDFFromTo(serverNameCombo.getValue(),
                            Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                            Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                    boolean rs = false;

                    for (String clientEmail : emailList) {
                        if (new MailSender().sendMail(
                                getMailSubject(), getMailMessage(), clientEmail, true, path5))
                            rs = true;
                        else rs = false;
                    }
                    if (rs) {
                        new MailManagerController().getAlert("Mail Sent", "Email Sent");
                    } else new MailManagerController().getAlert("Failure Message", "Failed to send the email");

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else if (this.reportSelector == 3) {
                String path6;
                path6 = new InventoryMailAttachment().createPDFDepBasedReport(serverNameCombo.getValue());
                boolean rs = false;
                for (String clientEmail : emailList) {
                    if (new MailSender().sendMail(
                            getMailSubject(), getMailMessage(), clientEmail, true, path6)) rs = true;
                    else rs = false;
                }
                if (rs) {
                    new MailManagerController().getAlert("Mail Sent", "Email Sent");
                } else new MailManagerController().getAlert("Failure Message", "Failed to send the email");
            }

        });
        itemTwo.setOnAction(event -> {
            Popup popup = new Popup();
            AnchorPane pane = new AnchorPane();
            TextField emailTextField = new TextField();
            emailTextField.setLayoutX(130);
            emailTextField.setLayoutY(10);
            emailTextField.setFont(Font.font(10));
            emailTextField.setPrefWidth(250);
            Label email = new Label("Enter Recipient Email:");
            email.setLayoutX(10);
            email.setLayoutY(10);
            Label notification = new Label();
            notification.setPrefWidth(250);
            notification.setLayoutY(40);
            notification.setLayoutX(10);
            Button sentBtn = new Button("Send");
            sentBtn.setLayoutX(250);
            sentBtn.setLayoutY(50);
            Button closeBtn = new Button("Close");
            closeBtn.setLayoutY(50);
            closeBtn.setLayoutX(300);
            closeBtn.setOnAction(e -> popup.hide());
            pane.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    popup.hide();
                }
            });
            sentBtn.setOnAction(e -> {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
                if (this.reportSelector == 1) {
                    String date = datePikerOne.getEditor().getText();
                    try {
                        String path = new InventoryMailAttachment().createPDFDailyReport(Date.valueOf(dateFormat.parse(date).toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate()));
                        boolean rs = new MailSender().sendMail(
                                getMailSubject(), getMailMessage(), emailTextField.getText(), true, path);
                        if (rs) {
                            notification.setText("Email sent");
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else if (this.reportSelector == 2) {
                    String fromDate = fromDatePicker.getEditor().getText();
                    String toDate = toDatePicker.getEditor().getText();
                    try {
                        if (!emailTextField.getText().isEmpty()) {
                            String path1 = new InventoryMailAttachment().createDepInventoryPDFFromTo(serverNameCombo.getValue(),
                                    Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                                    Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                            boolean rs = new MailSender().sendMail(
                                    getMailSubject(), getMailMessage(), emailTextField.getText(), true, path1);
                            if (rs) {
                                notification.setText("Email sent");
                            }
                        }

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else if (this.reportSelector == 3) {
                    String path2 = new InventoryMailAttachment().createPDFDepBasedReport(serverNameCombo.getValue());
                    boolean rs = new MailSender().sendMail(
                            getMailSubject(), getMailMessage(), emailTextField.getText(), true, path2);
                    if (rs) {
                        notification.setText("Email sent");
                    }
                }
            });
            pane.getChildren().addAll(email, notification, emailTextField, sentBtn, closeBtn);
            pane.setStyle("-fx-border-color:#737373; -fx-background-color:#cccccc;");
            popup.getContent().addAll(pane);
            Stage stage = (Stage) instantMailBtn.getScene().getWindow();
            popup.show(stage);
        });
        return contextMenu;
    }

    private void setMailManager() {
        try {
            Stage mailManager = new Stage();
            FXMLLoader dbSettingLoader = new FXMLLoader();
            Parent mailManagerPane = dbSettingLoader.load(getClass().getResource("mailManagerFXML.fxml").openStream());
            Scene scene = new Scene(mailManagerPane, GetPrefScreenWidth(), GetPrefScreenHeight());
            scene.getStylesheets().add(getClass().getResource("css/mailManager.css").toExternalForm());
            mailManager.setTitle("Mail Manager");
            mailManager.setScene(scene);
            mailManager.initModality(Modality.APPLICATION_MODAL);
            mailManager.setResizable(true);
            mailManager.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBackUp() {
        Executor executor = Executors.newFixedThreadPool(1);
        if (new DbConnection().isDatabaseConnected()) {
            ProgressIndicator pb = new ProgressIndicator();
            pb.setStyle("-fx-progress-color:#0eaa09");
            //indicatorPane.getChildren().add(pb);
            Thread extractFile = new Thread(new BackupFileService());
            executor.execute(extractFile);
            while (extractFile.isAlive()) {
                indicatorPane.getChildren().add(pb);
            }
            alertMessages("Confirmation", "Successfully updated all the files", "Click Ok to continue");
            //indicatorPane.getChildren().clear();
        } else {
            alertMessages("Confirmation", "You are not connected to the database", "Connect to the database");
        }
    }

    /**
     * affects the preogress bar
     */
    private void progressBarSet() {
        diskProgressBarId.setProgress(new DiskSpace().diskUsage());
        diskProgressBarId.setStyle("-fx-control-inner-background:beige;");
        if (new DiskSpace().diskUsage() > 0.01 && new DiskSpace().diskUsage() <= 0.5) {
            diskProgressBarId.setStyle("-fx-accent:green;");
        } else if (new DiskSpace().diskUsage() < 0.8 && new DiskSpace().diskUsage() >= 0.5) {
            diskProgressBarId.setStyle("-fx-accent:blue;");
        } else
            diskProgressBarId.setStyle("-fx-accent:red;");
        diskProgressBarId.setProgress(new DiskSpace().diskUsage());
        new DiskSpace().diskUsage();
    }

    /**
     * opening the setting box
     */
    public void openDbSettings() {
        try {
            Stage dbSetting = new Stage();
            FXMLLoader dbSettingLoader = new FXMLLoader();
            Pane dbSettingsRoot = dbSettingLoader.load(getClass().getResource("settingsFXML.fxml").openStream());
            Scene scene = new Scene(dbSettingsRoot);
            dbSetting.setTitle("Database setting");
            dbSetting.setScene(scene);
            dbSetting.initModality(Modality.APPLICATION_MODAL);
            dbSetting.setResizable(false);
            dbSetting.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDairyInventoryPrint() {
        try {
            Stage printBackup = new Stage();
            FXMLLoader dbSettingLoader = new FXMLLoader();
            Pane printBackupRoot = dbSettingLoader.load(getClass().getResource("printBackup.fxml").openStream());
            Scene scene = new Scene(printBackupRoot);

            printBackup.setScene(scene);
            printBackup.setTitle("Daily Report");
            printBackup.setResizable(false);
            printBackup.initModality(Modality.APPLICATION_MODAL);
            printBackup.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openClearBackupDetails() {
        try {
            Stage dbCRUD = new Stage();
            FXMLLoader dbSettingLoader = new FXMLLoader();
            Pane dbCRUDRoot = dbSettingLoader.load(getClass().getResource("dbCRUD.fxml").openStream());
            Scene scene = new Scene(dbCRUDRoot);
            dbCRUD.setTitle("Delete Records");
            dbCRUD.setScene(scene);
            dbCRUD.initModality(Modality.APPLICATION_MODAL);
            dbCRUD.setResizable(false);
            dbCRUD.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateServerNameCombox() {

        if (new DbConnection().isDatabaseConnected()) {
            if (!(new BackupFileService().getServerName().isEmpty())) {
                ObservableList<String> serverNames =
                        FXCollections.observableArrayList(new BackupFileService().getServerName());
                serverNameCombo.getItems().addAll(serverNames);
            }
        } else {
            Text text = new Text(
                    "ENSURE YOU ARE CONNECTED TO THE DATABASE, CHECK WHETHER THE DATABASE IS RUNNING" +
                            "\nOR G0 TO SETTINGS >> DATABASE THEN CHANGE THE DATABASE CONFIGURATION AND REFRESH THE APPLICATION");
            text.setFill(Color.RED);
            displayArea.setContent(text);
        }
    }

    /**
     * @throws ParseException displays produces  and Display the structured table
     */
    private void showTable() throws ParseException {
        TableView<com.backup.reports.util.BackUpFile> table;
        String contentInfo;
        String toDate = toDatePicker.getEditor().getText();
        String fromDate = fromDatePicker.getEditor().getText();
        String dateString = datePikerOne.getEditor().getText();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        if (checker == 1) {
            if (!dateString.isEmpty()) {
                table = new TableGenerator().getBackUpInfoByDate(
                        Date.valueOf(dateFormat.parse(dateString).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                this.displayArea.setContent(table);
                table.prefWidthProperty().bind(this.datePikerOne.widthProperty());
                table.prefHeightProperty().bind(this.displayArea.heightProperty());
                this.contentTitle.setText(" " + "All Departments on  " + dateString);
            } else
                alertMessages("Fields Empty", "Ensure the Fields are not Empty", "");
        } else if (checker == 2) {
            if ((!toDate.isEmpty()) && (!fromDate.isEmpty()) && (!serverNameCombo.getValue().isEmpty())) {
                //set report selector
                String serverName = serverNameCombo.getValue();
                this.reportSelector = 2;
                table = new TableGenerator().getSeverBackupInfoFromTo(serverName,
                        Date.valueOf(dateFormat.parse(fromDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                        Date.valueOf(dateFormat.parse(toDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                this.displayArea.setContent(table);
                contentInfo = " " + serverName.toUpperCase() + " From " + fromDate + " To " + toDate;
                contentTitle.setText(contentInfo);
            } else if (!(serverNameCombo.getValue() == null)) {
                String serverName = serverNameCombo.getValue();
                //set report selector
                this.contentTitle.setText(serverNameCombo.getValue() + " Department");
                this.reportSelector = 3;
                table = new TableGenerator().getAllSeverBackUpInfo(serverName);
                this.displayArea.setContent(table);
                table.prefWidthProperty().bind(this.datePikerOne.widthProperty());
                table.prefHeightProperty().bind(this.displayArea.heightProperty());

            } else {
                alertMessages("Fields Empty", "Ensure the Fields are not Empty", "");
            }
        }
    }

    /**
     * information alert
     *
     * @param title
     * @param headerText
     */
    private void alertMessages(String title, String headerText, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(context);
        alert.setHeaderText(headerText);
        alert.show();
    }

    private void printDailyReport(Date date) {
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("Date", date);
        Report.createReport(connection, map, new ReportObject().getDailyReport());
        Report.showReport();
    }

    private void printDepBasedReport(String serverName) {
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("server_name", serverName);
        Report.createReport(connection, map, new ReportObject().getDepBasedReport());
        Report.showReport();
    }

    private void printDepReportFromTo(String serverName, Date fromDate, Date toDate) {
        Connection connection = DbConnection.connection;
        Map<String, Object> map = new HashMap<>();
        map.put("serverName", serverName);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        Report.createReport(connection, map, new ReportObject().getDepReportFromTo());
        Report.showReport();
    }

    private void ClearInput() {
        this.fromDatePicker.getEditor().clear();
        this.toDatePicker.getEditor().clear();
    }

    private void showHelpPage() {
        File f = new File("src/com/backup/reports/config/help.html");
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(f.toURI().toString());
        displayArea.setContent(webView);
    }

    public String getMailSubject() {
        List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
        for (MailMessage mailMessage : mailMessageList) {
            if (mailMessage.getMessageId().equalsIgnoreCase("instant")) return mailMessage.getSubject();
        }
        return "";
    }

    public String getMailMessage() {
        List<MailMessage> mailMessageList = new MailManagerSchema().getAllMailMessage();
        for (MailMessage mailMessage : mailMessageList) {
            if (mailMessage.getMessageId().equalsIgnoreCase("instant")) return mailMessage.getMessage();
        }
        return "";
    }

    public List<String> getClientEmail() {
        List<ClientAddress> clientAddressList = new MailManagerSchema().getAllAddresses();
        List<String> emailList = new ArrayList<>();
        for (ClientAddress clientAddress : clientAddressList) {
            if (clientAddress.getClientMachine().equalsIgnoreCase(serverNameCombo.getValue())) {
                emailList.add(clientAddress.getEmail());
            }
        }
        return emailList;
    }

    private void developerPane() {
        try {
            Stage dev = new Stage();
            FXMLLoader dbSettingLoader = new FXMLLoader();
            Pane mailManagerPane = dbSettingLoader.load(getClass().getResource("dev.fxml").openStream());
            Scene scene = new Scene(mailManagerPane);
            dev.setScene(scene);
            dev.initModality(Modality.APPLICATION_MODAL);
            dev.setResizable(false);
            dev.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startMySQLShell(){

        try {
            ProcessBuilder builder = new ProcessBuilder("C:\\Program Files\\MySQL\\MySQL Workbench 8.0 CE\\MySQLWorkbench.exe");
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
