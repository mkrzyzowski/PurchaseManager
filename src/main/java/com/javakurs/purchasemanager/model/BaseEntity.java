package com.javakurs.purchasemanager.model;

import com.javakurs.purchasemanager.database.DatabaseManager;
import com.javakurs.purchasemanager.helper.MsgHelper;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakcyjna klasa bazowa reprezentująca byty istniejące w bazie danych.
 * Klasy modelu powinny dziedziczyć po tej klasie.
 */
public abstract class BaseEntity {
    private Integer id;
    private Integer insertUserId;
    private LocalDateTime insertDate;
    private Integer modifyUserId;
    private LocalDateTime modifyDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(Integer insertUserId) {
        this.insertUserId = insertUserId;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(LocalDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * Dodaje nowy obiekt do bazy danych.
     *
     * @return identyfikator utworzonego rekordu lub {@code null} w przypadku błędu tworzenia rekordu
     */
    public Integer insert() {
        if (id != null) {
            MsgHelper.showError("Rekord istnieje już w bazie danych", "Nastąpiła próba ponownego dodania rekordu do bazy danych");
            return null;
        }

        return DatabaseManager.insertRecord(this);
    }

    /**
     * Zapisuje zmodyfikowany obiekt w bazie danych.
     *
     * @return {@code true} - operacja zakończona powodzeniem, {@code false} - wystąpił błąd modyfikacji obiektu
     */
    public boolean modify() {
        if (id == null) {
            MsgHelper.showError("Rekord nie istnieje w bazie danych", "Nastąpiła próba usunięcia rekordu, który nie istnieje w bazie danych");
            return false;
        }

        return DatabaseManager.modifyRecord(this);
    }

    /**
     * Usuwa wybrany obiekt z bazy danych.
     *
     * @return {@code true} - operacja zakończona powodzeniem, {@code false} - wystąpił błąd usuwania obiektu
     */
    public boolean delete() {
        if (id == null) {
            MsgHelper.showError("Rekord nie istnieje w bazie danych", "Nastąpiła próba usunięcia rekordu, który nie istnieje w bazie danych");
            return false;
        }

        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("id", id));

        return DatabaseManager.deleteRecords(this.getClass(), params);
    }
}
