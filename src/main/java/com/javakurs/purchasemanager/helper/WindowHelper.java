package com.javakurs.purchasemanager.helper;

import com.javakurs.purchasemanager.MainApplication;
import com.javakurs.purchasemanager.enums.WindowType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasa zawierająca metody związane z obsługą okien.
 */
public class WindowHelper {

    /**
     * Otwiera wybrane okno.
     *
     * @param windowType typ okna do otwarcia
     */
    public static void openWindow(WindowType windowType) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(windowType.getViewPath()));
            Scene scene = new Scene(fxmlLoader.load(), windowType.getWidth(), windowType.getHeight());
            Stage stage = new Stage();
            stage.setTitle(windowType.getTitle());
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            MsgHelper.showError(String.format("Błąd otwierania ekranu %s", windowType.getViewPath()), ex.getLocalizedMessage());
        }
    }

    /**
     * Metoda ta zamyka okno po przekazaniu jednego z jego komponentów.
     *
     * @param cmp komponent z ekrenu do zamknięcia
     */
    public static void closeWindow(Node cmp) {
        try {
            ((Stage) cmp.getScene().getWindow()).close();
        } catch (Exception ex) {
            ex.printStackTrace();
            MsgHelper.showError(String.format("Błąd otwierania ekranu ",ex.getLocalizedMessage()),"Spróbuj ponownie później bo teraz ci nie idzie");
        }
    }
}
