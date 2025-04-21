package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Constants.TempConstants;
import com.infra.easyinfra.Helpers.FileOperations;
import com.infra.easyinfra.Helpers.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {
    @FXML
    private Text path;

    @FXML
    private void handleNext(ActionEvent event) {
        Navigation.go(
                SceneConstants.SECOND_PAGE,
                event,
                getClass()
        );
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
            FileOperations.createOrOverwriteFile(TempConstants.PROJECT_ROOT_FOLDER, folderPath);
        }
    }

}