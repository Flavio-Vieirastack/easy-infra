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

import java.net.URL;
import java.util.ResourceBundle;

public class SecondViewController implements Initializable {
    @FXML
    private ComboBox<AwsRegion> comboBox = new ComboBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll(
                new AwsRegion("US East (N. Virginia)", "us-east-1"),
                new AwsRegion("US East (Ohio)", "us-east-2"),
                new AwsRegion("US West (N. California)", "us-west-1"),
                new AwsRegion("US West (Oregon)", "us-west-2"),
                new AwsRegion("Africa (Cape Town)", "af-south-1"),
                new AwsRegion("Asia Pacific (Hong Kong)", "ap-east-1"),
                new AwsRegion("Asia Pacific (Hyderabad)", "ap-south-2"),
                new AwsRegion("Asia Pacific (Jakarta)", "ap-southeast-3"),
                new AwsRegion("Asia Pacific (Melbourne)", "ap-southeast-4"),
                new AwsRegion("Asia Pacific (Mumbai)", "ap-south-1"),
                new AwsRegion("Asia Pacific (Osaka)", "ap-northeast-3"),
                new AwsRegion("Asia Pacific (Seoul)", "ap-northeast-2"),
                new AwsRegion("Asia Pacific (Singapore)", "ap-southeast-1"),
                new AwsRegion("Asia Pacific (Sydney)", "ap-southeast-2"),
                new AwsRegion("Asia Pacific (Tokyo)", "ap-northeast-1"),
                new AwsRegion("Canada (Central)", "ca-central-1"),
                new AwsRegion("Europe (Frankfurt)", "eu-central-1"),
                new AwsRegion("Europe (Ireland)", "eu-west-1"),
                new AwsRegion("Europe (London)", "eu-west-2"),
                new AwsRegion("Europe (Milan)", "eu-south-1"),
                new AwsRegion("Europe (Paris)", "eu-west-3"),
                new AwsRegion("Europe (Spain)", "eu-south-2"),
                new AwsRegion("Europe (Stockholm)", "eu-north-1"),
                new AwsRegion("Europe (Zurich)", "eu-central-2"),
                new AwsRegion("Middle East (Bahrain)", "me-south-1"),
                new AwsRegion("Middle East (UAE)", "me-central-1"),
                new AwsRegion("South America (São Paulo)", "sa-east-1"),
                new AwsRegion("AWS GovCloud (US-East)", "us-gov-east-1"),
                new AwsRegion("AWS GovCloud (US-West)", "us-gov-west-1"),
                new AwsRegion("Israel (Tel Aviv)", "il-central-1")
        );
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AwsRegion region) {
                return region != null ? region.name() + " - " + region.code() : "";
            }
            @Override
            public AwsRegion fromString(String string) {
                return null;
            }
        });
        comboBox.setOnAction(e -> {
            AwsRegion selected = comboBox.getValue();
            FileOperations.createOrOverwriteFile(TempKeys.AWS_REGION.getKey(), selected.code());
        });
    }

    @FXML
    public void next(ActionEvent event) {
        AwsRegion selectedRegion = comboBox.getValue();
        if(selectedRegion != null) {
            Navigation.go(SceneConstants.THIRD_PAGE, event, getClass());
        }
    }
}
