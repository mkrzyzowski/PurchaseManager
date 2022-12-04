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
     * Ładuje obiekt na podstawie identyfikatora.
     *
     * @param clazz klasa, której obiekt ma zostać pobrany
     * @param recordId identyfikator rekordu do pobrania
     * @return obiekt pobrany z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> T loadById(Class<? extends BaseEntity> clazz, Integer recordId) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("id", recordId));

        return DatabaseManager.findOne(clazz, params);
    }

    /**
     * Ładuje wszystkie obiekty danej klasy na podstawie identyfikatora.
     *
     * @param clazz klasa, której wszystkie obiekty mają zostać pobrane
     * @return lista wszystkich obiektów pobranych z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> loadAll(Class<? extends BaseEntity> clazz) {
        return DatabaseManager.findList(clazz, null);
    }

    /**
     * Ładuje wszystkie obiekty danej klasy na podstawie identyfikatora użytkownika, który wprowadził dane.
     *
     * @param clazz klasa, której wszystkie obiekty mają zostać pobrane
     * @param insertUserId identyfikator użytkownika, który wprowadził dane
     * @return lista wszystkich obiektów pobranych z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> loadAllByInsertUserId(Class<? extends BaseEntity> clazz, Integer insertUserId) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("insertUserId", insertUserId));

        return DatabaseManager.findList(clazz, params);
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
        return DatabaseManager.modifyRecord(this);
    }

    /**
     * Usuwa wybrany obiekt z bazy danych.
     *
     * @return {@code true} - operacja zakończona powodzeniem, {@code false} - wystąpił błąd usuwania obiektu
     */
    public boolean delete() {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("id", id));

        return DatabaseManager.deleteRecords(this.getClass(), params);
    }
}
