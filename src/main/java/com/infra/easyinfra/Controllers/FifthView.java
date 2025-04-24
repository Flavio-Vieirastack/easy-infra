package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Entity.InfraData;
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
            var infraData = InfraData.getInstance();
            infraData.setEcsTask(ecsTask.getText());
            infraData.setContainerName(containerName.getText());
            infraData.setEcsServiceName(ecsServiceName.getText());
            infraData.setEcsClusterName(ecsClusterName.getText());
            infraData.setApplicationPort(applicationPort.getText());
            infraData.setSubDomainUrl(subDomainUrl.getText());
            infraData.setUserEmail(userEmail.getText());
            Navigation
                    .go(
                            SceneConstants.SIXTH_PAGE,
                            event,
                            getClass()
                    );
        }
    }
}