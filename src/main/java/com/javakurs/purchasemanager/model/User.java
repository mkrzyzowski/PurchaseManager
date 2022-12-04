package com.javakurs.purchasemanager.model;

import com.javakurs.purchasemanager.database.DatabaseManager;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca użytkownika.
 */
public class User extends BaseEntity {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean isActive;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    /**
     * Pobiera dane użytkownika o określonym loginie i haśle.
     *
     * @param login    login użytkownika
     * @param password hasło użytkownika zaszyfrowane Base64
     * @return dane użytkownika lub {@code null}, jeśli użytkownik nie istnieje
     */
    public static User getByLoginPassword(String login, String password) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("login", login));
        params.add(new Pair<>("password", password));
        params.add(new Pair<>("isActive", true));
        return DatabaseManager.findOne(User.class, params);
    }
}
