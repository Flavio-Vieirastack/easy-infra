module com.infra.easyinfra {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.infra.easyinfra to javafx.fxml;
    exports com.infra.easyinfra;
    exports com.infra.easyinfra.Controllers;
    opens com.infra.easyinfra.Controllers to javafx.fxml;
}