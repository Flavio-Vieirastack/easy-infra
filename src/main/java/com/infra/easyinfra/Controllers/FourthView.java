package com.infra.easyinfra.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
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
        var mapper = new ObjectMapper();
        String inputText = textField.getText();
        if(!inputText.isBlank()) {
            FileOperations.createOrOverwriteFile(
                    TempKeys.S3_BUCKETS.getKey(),
                    mapper.writeValueAsString(
                            StringUtils.splitByComma(inputText)
                    )
            );// add navegação
        }
    }
}
