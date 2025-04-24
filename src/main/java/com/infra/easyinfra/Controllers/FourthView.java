package com.infra.easyinfra.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Entity.InfraData;
import com.infra.easyinfra.Helpers.FileOperations;
import com.infra.easyinfra.Helpers.Navigation;
import com.infra.easyinfra.Helpers.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;

public class FourthView {
    @FXML
    TextField textField;

    @FXML
    @SneakyThrows
    public void handleCaptureText(ActionEvent event) {
        String inputText = textField.getText();
        if (!inputText.isBlank()) {
            var infraData = InfraData.getInstance();
            infraData.setS3Buckets(StringUtils.splitByComma(inputText));
        }
        Navigation
                .go(
                        SceneConstants.FIFTH_PAGE,
                        event,
                        getClass()
                );
    }
}
