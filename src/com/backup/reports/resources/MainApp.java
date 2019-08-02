package com.backup.reports.resources;

import com.backup.reports.database.DbConnection;
import com.backup.reports.services.MailSenderScheduler;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.backup.reports.services.MailSenderScheduler.mailTimer;
import static com.backup.reports.util.ScreenSize.GetPrefScreenHeight;
import static com.backup.reports.util.ScreenSize.GetPrefScreenWidth;

public class MainApp extends Application {

    private static Logger logger = Logger.getLogger(MainApp.class);
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        if (new DbConnection().isDatabaseConnected()) {
            lunchApplication(primaryStage);
        } else {
            MaterialIconView iconView= new MaterialIconView(MaterialIcon.WARNING);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            iconView.setSize("50");
            iconView.setFill(Color.valueOf("#f1cd16"));
            alert.setTitle("Database Connection Exception");
            alert.setHeaderText(null);
            alert.setGraphic(iconView);
            alert.setContentText("You Are Not Connected to The Database");
            ButtonType buttonContinue = new ButtonType("Continue");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonContinue, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonContinue){
                lunchApplication(primaryStage);
            }

        }

    }

    private void lunchApplication(Stage primaryStage){
        try {
            //mail scheduler call+++++++++++++++++++++++++++
            new MailSenderScheduler().mailSendSchedule();
            //+++++++++++++++++++++++++++++++++++++++++++++
        } catch (Exception e) {
            logger.error("Failed to start the mail scheduler "+ e.getMessage());
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("mainFXML.fxml"));
            Scene scene =  new Scene(root,GetPrefScreenWidth(),GetPrefScreenHeight());
            scene.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);
            primaryStage.setTitle("KenSeph Inventory");
            primaryStage.setMaximized(true);
            primaryStage.setOnCloseRequest(e -> {
                mailTimer.cancel();
                Platform.exit();
            });
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
