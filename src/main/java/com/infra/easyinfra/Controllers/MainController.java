package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.Navigation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label clickableLabel;

    @FXML
    private Button button;

    @FXML
    private Text path;

    @FXML
    private void handleNext(ActionEvent event) {
        if (path != null) {
            Platform.runLater(() -> button.setText("Await..."));
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> Navigation.go(
                        SceneConstants.SECOND_PAGE,
                        event,
                        getClass()
                ));
            }).start();
        }
    }

    @FXML
    private void getProjectRootFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the project root folder");

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String folderPath = selectedDirectory.getAbsolutePath();
            path.setText("Path: " + folderPath);
            InfraData.getInstance().setProjectRootFolder(folderPath);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickableLabel.setOnMouseClicked(event -> {
            try {
                String docUrl = "https://github.com/Flavio-Vieirastack/easy-infra";
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(docUrl));
                }
            } catch (URISyntaxException | java.io.IOException e) {
                e.printStackTrace();
            }
        });
    }
}