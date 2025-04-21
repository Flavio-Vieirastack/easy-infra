package com.infra.easyinfra;

import com.infra.easyinfra.Constants.SceneConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EasyInfra extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EasyInfra.class.getResource(SceneConstants.MAIN));
        Scene scene = new Scene(fxmlLoader.load(), SceneConstants.SIZE, SceneConstants.SIZE);
        stage.setTitle("Easy Infra");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}