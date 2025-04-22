package com.infra.easyinfra.Controllers;

import com.infra.easyinfra.Constants.SceneConstants;
import com.infra.easyinfra.Dtos.AwsRegion;
import com.infra.easyinfra.Enum.TempKeys;
import com.infra.easyinfra.Helpers.FileOperations;
import com.infra.easyinfra.Helpers.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SecondViewController implements Initializable {
    @FXML
    private ComboBox<AwsRegion> comboBox = new ComboBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<AwsRegion> regions = getAwsRegionsFromCLI();
        comboBox.getItems().addAll(regions);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AwsRegion region) {
                return region != null ? region.name() : "";
            }
            @Override
            public AwsRegion fromString(String string) {
                return null;
            }
        });
        comboBox.setOnAction(e -> {
            AwsRegion selected = comboBox.getValue();
            FileOperations.createOrOverwriteFile(TempKeys.AWS_REGION.getKey(), selected.name());
        });
    }

    private List<AwsRegion> getAwsRegionsFromCLI() {
        List<AwsRegion> awsRegions = new ArrayList<>();

        try {
            // Executando o comando AWS CLI
            ProcessBuilder processBuilder = new ProcessBuilder("aws", "ec2", "describe-regions", "--query", "Regions[*].RegionName", "--output", "text");
            Process process = processBuilder.start();

            // Lendo a saída do processo
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // A linha retornada contém todas as regiões separadas por espaço
                String[] regions = line.split("\\s+");  // Divide as regiões com base em espaços em branco

                // Para cada região, cria um objeto AwsRegion e adiciona à lista
                for (String region : regions) {
                    awsRegions.add(new AwsRegion(region));
                }
            }

            // Espera o processo terminar
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return awsRegions;
    }


    @FXML
    public void next(ActionEvent event) {
        AwsRegion selectedRegion = comboBox.getValue();
        if(selectedRegion != null) {
            Navigation.go(SceneConstants.THIRD_PAGE, event, getClass());
        }
    }
}
