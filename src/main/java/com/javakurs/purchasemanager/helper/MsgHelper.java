package com.javakurs.purchasemanager.helper;

import javafx.scene.control.Alert;

/**
 * Klasa zawierająca przydatne metody do wyświetlania komunikatów.
 */
public class MsgHelper {
    /**
     * Wyświetla komunikat błędu.
     *
     * @param header nagłówek komunikatu do wyświetlenia
     * @param msg komunikat do wyświetlenia
     */
    public static void showError(String header, String msg) {
        show(header, msg, Alert.AlertType.ERROR, "Wystąpił błąd");
    }

    /**
     * Wyświetla komunikat zawierający informację.
     *
     * @param header nagłówek komunikatu do wyświetlenia
     * @param msg komunikat do wyświetlenia
     */
    public static void showInfo(String header, String msg) {
        show(header, msg, Alert.AlertType.INFORMATION, "Informacja");
    }

    /**
     * Wyświetla alert na ekranie.
     *
     * @param header nagłówek komunikatu do wyświetlenia
     * @param msg komunikat do wyświetlenia
     * @param type typ alertu
     * @param title tytuł okna zawierającego alert
     */
    private static void show(String header, String msg, Alert.AlertType type, String title) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
