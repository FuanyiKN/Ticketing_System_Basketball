module com.febs24.ticketing {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jbcrypt;

    opens com.febs24.ticketing to javafx.fxml;
    exports com.febs24.ticketing;
    exports com.febs24.ticketing.models;
    opens com.febs24.ticketing.models to javafx.fxml;
    exports com.febs24.ticketing.dao;
    opens com.febs24.ticketing.dao to javafx.fxml;
    exports com.febs24.ticketing.controllers;
    opens com.febs24.ticketing.controllers to javafx.fxml;
}