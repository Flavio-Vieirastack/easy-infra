package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.Navigation;
import com.infra.easyinfra.Writers.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class SixthView {
    @FXML
    private CheckBox checkBox;

    @FXML
    private Button button;

    @FXML
    private void handleFinish() {
        Platform.runLater(() -> button.setText("Await..."));
        new Thread(() -> {
            sleep();
            Platform.runLater(() -> {
                var infraData = new InfraData();
                while (infraData.isAllNull()) {
                    sleep();
                }
                new TfS3().write(infraData);
                new TfMain().write(infraData);
                new TfElasticIp().write(infraData);
                new TfDb().write(infraData);
                new TfBackend().write(infraData);
                new GithubActions().write(infraData);
                if(checkBox.isSelected()) {
                    new DockerCompose().write(infraData);
                    new DockerFile().write(infraData);
                }
            });
        }).start();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
