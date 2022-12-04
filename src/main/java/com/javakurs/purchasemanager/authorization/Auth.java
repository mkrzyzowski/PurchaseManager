package com.javakurs.purchasemanager.authorization;

import com.javakurs.purchasemanager.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Klasa obsługująca logowanie użytkownika.
 */
public class Auth {
    /** Aktualnie zalogowany użytkownik */
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Sprawdza, czy istnieje użytkownik o przekazanym loginie i haśle.
     *
     * @param login    login użytkownika
     * @param password hasło użytkownika
     * @return {@code true} - dane logowania są poprawne, {@code false} - dane logowania są błędne
     */
    public static boolean isCorrectUser(String login, String password) {
        byte[] encodedPassword = Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8));
        loggedInUser = User.getByLoginPassword(login, new String(encodedPassword));

        return loggedInUser != null;
    }
}