package com.backup.reports.resources;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ken on 8/1/2019.
 */
public class dbExceptionController implements Initializable {

    @FXML
    private Button continueBtn,closeBtn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        closeBtn.setOnAction(e-> Platform.exit());
//        Stage stage = (Stage) closeBtn.getScene().getWindow();
//        continueBtn.setOnAction(e->stage.close());
    }
}
