package com.javakurs.purchasemanager;

import com.javakurs.purchasemanager.enums.WindowType;
import com.javakurs.purchasemanager.helper.WindowHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Główna klasa aplikacji.
 */
public class MainApplication extends Application {
    /**
     * Otwiera ekran główny.
     */
    @Override
    public void start(Stage stage) throws IOException {
        WindowHelper.openWindow(WindowType.FRM_MAIN);
    }

    /**
     * Metoda startowa aplikacji.
     */
    public static void main(String[] args) {
        launch();
    }
}