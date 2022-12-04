module com.javakurs.purchasemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.javakurs.purchasemanager to javafx.fxml;
    exports com.javakurs.purchasemanager;
    exports com.javakurs.purchasemanager.controller;
    opens com.javakurs.purchasemanager.controller to javafx.fxml;
}