package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
import com.infra.easyinfra.Helpers.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FifthView {
    @FXML
    private TextField ecsTask;
    @FXML
    private TextField containerName;
    @FXML
    private TextField ecsServiceName;
    @FXML
    private TextField ecsClusterName;
    @FXML
    private TextField applicationPort;
    @FXML
    private TextField subDomainUrl;
    @FXML
    private TextField userEmail;

    @FXML
    private void handleNext(ActionEvent event) {
        if(ecsTask.getText() != null
                && containerName.getText() != null
                && ecsServiceName.getText() != null
                && ecsClusterName.getText() != null
                && applicationPort.getText() != null
                && subDomainUrl.getText() != null
                && userEmail.getText() != null
        ) {
            FileOperations.createOrOverwriteFile(TempKeys.ECS_TASK.getKey(), ecsTask.getText());
            FileOperations.createOrOverwriteFile(TempKeys.CONTAINER_NAME.getKey(), containerName.getText());
            FileOperations.createOrOverwriteFile(TempKeys.ECS_SERVICE_NAME.getKey(), ecsServiceName.getText());
            FileOperations.createOrOverwriteFile(TempKeys.ECS_CLUSTER_NAME.getKey(), ecsClusterName.getText());
            FileOperations.createOrOverwriteFile(TempKeys.APPLICATION_PORT.getKey(), applicationPort.getText());
            FileOperations.createOrOverwriteFile(TempKeys.SUBDOMAIN_URL.getKey(), subDomainUrl.getText());
            FileOperations.createOrOverwriteFile(TempKeys.USER_EMAIL.getKey(), userEmail.getText());
            Navigation
                    .go(
                            SceneConstants.SIXTH_PAGE,
                            event,
                            getClass()
                    );
        }
    }
}