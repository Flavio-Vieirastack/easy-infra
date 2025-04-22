package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
import com.infra.easyinfra.Helpers.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class ThirdViewController {
    @FXML
    TextField textField;

    @FXML
    Text errorText;

    @FXML
    public void handleCaptureText(ActionEvent event) {
        String inputText = textField.getText();
        if (inputText.contains("-") || !inputText.contains("_")) {
            errorText.setText("Invalid db name. The database name must contains underscore '_'");
        } else {
            FileOperations.createOrOverwriteFile(
                    TempKeys.DB_NAME.getKey(),
                    inputText
            );
            Navigation
                    .go(
                            SceneConstants.FOURTH_PAGE,
                            event,
                            getClass()
                    );
        }
    }
}
