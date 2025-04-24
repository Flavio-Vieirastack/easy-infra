module com.infra.easyinfra {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.infra.easyinfra to javafx.fxml;
    exports com.infra.easyinfra;
    exports com.infra.easyinfra.Controllers;
    opens com.infra.easyinfra.Controllers to javafx.fxml;
}