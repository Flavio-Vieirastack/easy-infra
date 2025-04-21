package com.infra.easyinfra.Helpers;

import com.infra.easyinfra.Constants.SceneConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public abstract class Navigation {

    public static void go(String fxml, ActionEvent event, Class<?> originalClass) {
        try {
            URL resource = originalClass.getResource(fxml);
            Objects.requireNonNull(resource, "FXML file not found: " + fxml);
            Parent root = FXMLLoader.load(Objects.requireNonNull(originalClass.getResource(fxml)));
            Scene novaCena = new Scene(root, SceneConstants.SIZE, SceneConstants.SIZE);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(novaCena);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
