package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.Navigation;
import com.infra.easyinfra.Writers.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class SixthView {
    @FXML
    private CheckBox checkBox;

    @FXML
    private void handleFinish(ActionEvent event) {
        InfraData infraData = InfraData.getInstance();
        new TfMain().write(infraData);
        new TfElasticIp().write(infraData);
        new TfDb().write(infraData);
        new TfBackend().write(infraData);
        new GithubActions().write(infraData);
        new TfVariables().write(infraData);
        new TfS3().write(infraData);
        if (checkBox.isSelected()) {
            new DockerCompose().write(infraData);
            new DockerFile().write(infraData);
        }
        Navigation.go(
                SceneConstants.FINAL_PAGE,
                event,
                getClass()
        );
    }
}
