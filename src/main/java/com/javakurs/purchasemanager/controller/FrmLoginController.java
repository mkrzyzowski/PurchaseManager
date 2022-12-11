package com.javakurs.purchasemanager.controller;

import com.javakurs.purchasemanager.authorization.Auth;
import com.javakurs.purchasemanager.enums.WindowType;
import com.javakurs.purchasemanager.helper.MsgHelper;
import com.javakurs.purchasemanager.helper.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class FrmLoginController {
    private int incorrectLoginCount = 0;
    private PasswordField txtPassword;

    private TextField txtUser;

    /**
     * Metoda sprawdza czy dane logowania są poprawne, jeśli sa to przechodzi do ekranu głównego, jeśli nie to wyświetla
     * komunikat, jeśli komunikat pokaże się 3 razy okienko logowania się wyłącza
     */
    public void btnLoginClicked(ActionEvent actionEvent) {
        String password = txtPassword.getText();
        String login = txtUser.getText();

        do {
            if (Auth.isCorrectUser(login, password)) {
                WindowHelper.openWindow(WindowType.FRM_MAIN);
            } else {
                MsgHelper.showError("Złe informacje", "Podaj poprawne dane logowania");
                incorrectLoginCount++;
            }
        } while (incorrectLoginCount < 3);

        ((Stage) txtPassword.getScene().getWindow()).close();
    }

    /**
     * Metoda pozwala na przejście do logowania  za pomoca klawisza enter
     */
    public void initialize() {
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnLoginClicked(null);
            }
        });
    }
}
