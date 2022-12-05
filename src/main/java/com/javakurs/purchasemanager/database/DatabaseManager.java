package com.javakurs.purchasemanager.database;

import com.javakurs.purchasemanager.authorization.Auth;
import com.javakurs.purchasemanager.helper.DateHelper;
import com.javakurs.purchasemanager.helper.MsgHelper;
import com.javakurs.purchasemanager.helper.PropertiesHelper;
import com.javakurs.purchasemanager.model.BaseEntity;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager obsługujący bazę danych w aplikacji.
 */
public class DatabaseManager {
    /** Obiekt połączenia do bazy danych */
    private static Connection connection;
    /** Obiekt zapytania do bazy danych */
    private static Statement statement;

    /** Ścieżka do bazy danych */
    private static final String dburl = PropertiesHelper.getProperty("database.url");
    /** Nazwa sterownika bazy danych */
    private static final String driver = PropertiesHelper.getProperty("database.driver");
    /** Nazwa użytkownika bazy danych */
    private static final String user = PropertiesHelper.getProperty("database.user");
    /** Hasło użytkownika bazy danych */
    private static final String password = PropertiesHelper.getProperty("database.password");

    /**
     * Ładuje obiekt danej klasy na podstawie identyfikatora.
     *
     * @param clazz klasa, której obiekt ma zostać pobrany
     * @param recordId identyfikator rekordu do pobrania
     * @return obiekt pobrany z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> T getById(Class<? extends BaseEntity> clazz, Integer recordId) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("id", recordId));

        return getOne(clazz, params);
    }

    /**
     * Ładuje wszystkie obiekty danej klasy na podstawie identyfikatora.
     *
     * @param clazz klasa, której wszystkie obiekty mają zostać pobrane
     * @return lista wszystkich obiektów pobranych z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> getAll(Class<? extends BaseEntity> clazz) {
        return getList(clazz, null);
    }

    /**
     * Ładuje wszystkie obiekty danej klasy na podstawie identyfikatora użytkownika, który wprowadził dane.
     *
     * @param clazz klasa, której wszystkie obiekty mają zostać pobrane
     * @param insertUserId identyfikator użytkownika, który wprowadził dane
     * @return lista wszystkich obiektów pobranych z bazy danych lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> getAllByInsertUserId(Class<? extends BaseEntity> clazz, Integer insertUserId) {
        List<Pair<String, Object>> params = new ArrayList<>();
        params.add(new Pair<>("insertUserId", insertUserId));

        return getList(clazz, params);
    }

    /**
     * Znajduje jeden obiekt danej klasy spełniający określone warunki.
     *
     * @param clazz  nazwa klasy reprezentującej encję (nazwa klasy = nazwa tabeli, pola klasy = kolumny tabeli)
     * @param params parametry tworzące warunek filtrujący dane (pary: kolumna, wartość)
     * @param <T>    typ klasy reprezentującej encję
     * @return obiekt utworzony na podstawie danych pobranych z tabeli lub {@code null}, jeśli nie znaleziono rekordu
     */
    public static <T extends BaseEntity> T getOne(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params) {
        return getOne(clazz, params, new Query());
    }

    /**
     * Znajduje jeden obiekt danej klasy spełniający określone warunki.
     *
     * @param clazz  nazwa klasy reprezentującej encję (nazwa klasy = nazwa tabeli, pola klasy = kolumny tabeli)
     * @param params parametry tworzące warunek filtrujący dane (pary: kolumna, wartość)
     * @param query  obiekt zawierający elementy zapytania zdefiniowane przez użytkownika
     * @param <T>    typ klasy reprezentującej encję
     * @return obiekt utworzony na podstawie danych pobranych z tabeli lub {@code null}, jeśli nie znaleziono rekordu
     */
    public static <T extends BaseEntity> T getOne(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params, Query query) {
        try {
            openConnection();

            String queryStatement = prepareSelectStatement(clazz, params, query) + " LIMIT 1";
            ResultSet resultSet = statement.executeQuery(queryStatement);
            if (!resultSet.next())
                return null;

            T result = createResult(resultSet, clazz);
            closeConnection();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd pobierania rekordu z tabeli " + clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Znajduje listę obiektów danej klasy spełniających określone warunki.
     *
     * @param clazz  nazwa klasy reprezentującej encję (nazwa klasy = nazwa tabeli, pola klasy = kolumny tabeli)
     * @param params parametry tworzące warunek filtrujący dane (pary: kolumna, wartość)
     * @param <T>    typ klasy reprezentującej encję
     * @return lista obiektów utworzonych na podstawie danych pobranych z tabeli lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> getList(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params) {
        return getList(clazz, params, new Query());
    }

    /**
     * Znajduje listę rekordów spełniających określone warunki.
     *
     * @param clazz  nazwa klasy reprezentującej encję (nazwa klasy = nazwa tabeli, pola klasy = kolumny tabeli)
     * @param params parametry tworzące warunek filtrujący dane (pary: kolumna, wartość)
     * @param query  obiekt zawierający elementy zapytania zdefiniowane przez użytkownika
     * @param <T>    typ klasy reprezentującej encję
     * @return lista obiektów utworzonych na podstawie danych pobranych z tabeli lub {@code null}, gdy wystąpił błąd pobierania danych
     */
    public static <T extends BaseEntity> List<T> getList(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params, Query query) {
        try {
            openConnection();

            String queryToExecute = prepareSelectStatement(clazz, params, query);
            ResultSet resultSet = statement.executeQuery(queryToExecute);
            List<T> result = new ArrayList<>();
            while (resultSet.next())
                result.add(createResult(resultSet, clazz));

            closeConnection();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd pobierania rekordów z tabeli " + clazz.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Tworzy rekord w bazie danych.
     *
     * @param entity obiekt do zapisania
     * @return identyfikator utworzonego rekordu lub {@code null} w przypadku błędu
     */
    public static <T extends BaseEntity> Integer insertRecord(T entity) {
        try {
            openConnection();

            String insertStmt = prepareInsertStatement(entity);
            int result = statement.executeUpdate(insertStmt);
            if (result == 0)
                return null;

            Query query = new Query();
            query.setSqlOrderBy("id DESC");
            T insertedEntity = getOne(entity.getClass(), null, query);
            if (insertedEntity == null)
                return null;

            Integer id = insertedEntity.getId();
            entity.setId(id);
            entity.setInsertDate(insertedEntity.getInsertDate());
            entity.setInsertUserId(insertedEntity.getInsertUserId());

            closeConnection();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd tworzenia rekordu w tabeli " + entity.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Modyfikuje rekord w bazie danych.
     *
     * @param entity modyfikowany obiekt
     * @return {@code true} - operacja zakończona powodzeniem
     */
    public static <T extends BaseEntity> boolean modifyRecord(T entity) {
        try {
            openConnection();

            String modifyStmt = prepareModifyStatement(entity);
            boolean result = statement.executeUpdate(modifyStmt) == 1;
            if (!result)
                return false;

            List<Pair<String, Object>> params = new ArrayList<>();
            params.add(new Pair<>("id", entity.getId()));
            T modifiedEntity = getOne(entity.getClass(), params);
            entity.setModifyDate(modifiedEntity.getModifyDate());
            entity.setModifyUserId(modifiedEntity.getModifyUserId());

            closeConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd tworzenia rekordu w tabeli " + entity.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    /**
     * Usuwa rekord/-y z tabeli.
     *
     * @param clazz  nazwa klasy reprezentującej encję (nazwa klasy = nazwa tabeli, pola klasy = kolumny tabeli)
     * @param params parametry tworzące warunek filtrujący dane (pary: kolumna, wartość)
     * @return {@code true} - operacja zakończona powodzeniem
     */
    public static boolean deleteRecords(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params) {
        try {
            openConnection();

            String deleteStmt = prepareDeleteStatement(clazz, params);
            int result = statement.executeUpdate(deleteStmt);

            closeConnection();
            return result != 0;
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd usuwania rekordów z tabeli " + clazz.getSimpleName(), e.getMessage());
            return false;
        }
    }

    /**
     * Tworzy obiekt encji na podstawie danych pobranych z bazy danych.
     *
     * @param record rekord pobrany z bazy danych
     * @param clazz  klasa reprezentująca encję
     * @param <T>    typ reprezentujący encję
     * @return obiekt reprezentujący rekord pobrany z bazy danych
     * @throws Exception błąd tworzenia obiektu
     */
    private static <T extends BaseEntity> T createResult(ResultSet record, Class<? extends BaseEntity> clazz) throws Exception {
        T result = (T) clazz.getDeclaredConstructor().newInstance();
        for (Field field : getAllFields(clazz)) {
            String fieldName = field.getName();
            Optional<Field> declaredFieldOptional = getAllFields(clazz).stream().filter(f -> f.getName().equals(fieldName)).findFirst();
            if (declaredFieldOptional.isEmpty())
                continue;

            Field declaredField = declaredFieldOptional.get();
            declaredField.setAccessible(true);
            Class<?> type = declaredField.getType();
            if (Arrays.asList(Integer.class, Double.class, Boolean.class, String.class).contains(type))
                declaredField.set(result, record.getObject(fieldName));
            else if (type.equals(LocalDateTime.class))
                declaredField.set(result, record.getTimestamp(fieldName) == null ? null : record.getTimestamp(fieldName).toLocalDateTime());

            declaredField.setAccessible(false);
        }

        return result;
    }

    /**
     * Przygotowuje treść zapytania do wykonania.
     *
     * @param clazz  klasa reprezentująca encję
     * @param params parametry zawężające wyniki do pobrania
     * @param query  obiekt klasy {@code Query} zawierający elementy zapytania zdefiniowane przez użytkownika
     * @return zapytanie SQL do wykonania
     */
    private static String prepareSelectStatement(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params, Query query) {
        String fromStmt = query.getSqlFrom() == null ? clazz.getSimpleName() : query.getSqlFrom();
        String fieldStmt = query.getSqlFields() == null ? getFieldsAsString(clazz) : query.getSqlFields();
        String conditionStmt = query.getSqlWhere() == null ? getSqlWhere(params) : query.getSqlWhere();
        String orderByStmt = query.getSqlOrderBy() == null ? "Id" : query.getSqlOrderBy();

        return String.format("SELECT %s FROM %s WHERE %s ORDER BY %s", fieldStmt, fromStmt, conditionStmt, orderByStmt);
    }

    /**
     * Przygotowuje treść komendy tworzącej rekord.
     *
     * @param entity dane rekordu do zapisania
     * @return komenda tworząca rekord
     */
    private static <T extends BaseEntity> String prepareInsertStatement(T entity) throws IllegalAccessException {
        Class<? extends BaseEntity> clazz = entity.getClass();
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(entity);
            if (value == null) {
                field.setAccessible(false);
                continue;
            }

            columns.add(field.getName());

            if (field.getType().equals(LocalDateTime.class))
                values.add(String.format("'%s'", DateHelper.getAsString((LocalDateTime) value)));
            else if (field.getType().equals(String.class))
                values.add(String.format("'%s'", value));
            else
                values.add(value.toString());
            field.setAccessible(false);
        }

        addStandardInsertInfoToStatement(columns, values);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", clazz.getSimpleName(), columns, values);
    }

    /**
     * Dodaje standardowe informacje o tworzonym rekordzie.
     *
     * @param columns lista kolumn tworzenego rekordu
     * @param values  lista wartości tworzonego rekordu
     */
    private static void addStandardInsertInfoToStatement(StringJoiner columns, StringJoiner values) {
        columns.add("insertDate");
        values.add(String.format("'%s'", DateHelper.getAsString(LocalDateTime.now())));
        columns.add("insertUserId");
        values.add(Auth.getLoggedInUser().getId().toString());
    }

    /**
     * Przygotowuje treść komendy modyfikującej rekord.
     *
     * @param entity dane rekordu do zapisania
     * @return komenda modyfikująca rekord
     */
    private static <T extends BaseEntity> String prepareModifyStatement(T entity) throws IllegalAccessException {
        Class<? extends BaseEntity> clazz = entity.getClass();
        StringJoiner values = new StringJoiner(", ");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(entity);

            if (value == null)
                values.add(String.format("%s = NULL", fieldName));
            else if (field.getType().equals(LocalDateTime.class))
                values.add(String.format("%s = '%s'", fieldName, DateHelper.getAsString((LocalDateTime) value)));
            else if (field.getType().equals(String.class))
                values.add(String.format("%s = '%s'", fieldName, value));
            else
                values.add(String.format("%s = %s", fieldName, value));
            field.setAccessible(false);
        }

        addStandardModifyInfoToStatement(values);
        return String.format("UPDATE %s SET %s WHERE id = %d", clazz.getSimpleName(), values, entity.getId());
    }

    /**
     * Dodaje standardowe informacje o modyfikowanym rekordzie.
     *
     * @param values lista wartości modyfikowanego rekordu (kolumna = wartość, kolumna2 = wartość2, ...)
     */
    private static void addStandardModifyInfoToStatement(StringJoiner values) {
        values.add(String.format("modifyDate = '%s'", DateHelper.getAsString(LocalDateTime.now())));
        values.add(String.format("modifyUserId = %d", Auth.getLoggedInUser().getId()));
    }

    /**
     * Przygotowuje treść komendy usuwającej dane.
     *
     * @param clazz  klasa reprezentująca encję
     * @param params parametry zawężające rekordy do usunięcia
     * @return komenda
     */
    private static String prepareDeleteStatement(Class<? extends BaseEntity> clazz, List<Pair<String, Object>> params) {
        return String.format("DELETE FROM %s WHERE %s", clazz.getSimpleName(), getSqlWhere(params));
    }

    /**
     * Otwiera połączenie do bazy danych.
     */
    private static void openConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName(driver).newInstance();
        connection = DriverManager.getConnection(dburl, user, password);
        statement = connection.createStatement();
    }

    /**
     * Zamyka połączenie do bazy danych.
     */
    private static void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

    /**
     * Przygotowuje warunek na podstawie przekazanych parametrów.
     *
     * @param params parametry
     * @return łańcuch znaków reprezentujący warunek zapytania SQL
     */
    private static String getSqlWhere(List<Pair<String, Object>> params) {
        if (params == null) return "1 = 1";

        StringJoiner sqlWhere = new StringJoiner(" AND ");
        for (Pair<String, Object> param : params) {
            Object value = param.getValue();

            if (value == null)
                sqlWhere.add(String.format("%s IS NULL", param.getKey()));
            else if (value instanceof Integer || value instanceof Double || value instanceof Boolean)
                sqlWhere.add(String.format("%s = %s", param.getKey(), value));
            else if (value instanceof String)
                sqlWhere.add(String.format("%s = '%s'", param.getKey(), value));
            else if (value instanceof LocalDateTime)
                sqlWhere.add(String.format("%s = '%s'", param.getKey(), DateHelper.getAsString((LocalDateTime) value)));
        }
        return sqlWhere.length() > 0 ? sqlWhere.toString() : "1 = 1";
    }

    /**
     * Pobiera nazwy pól, które reprezentują kolumny w tabeli.
     *
     * @param clazz klasa
     * @return łańcuch znaków reprezentujący nazwy wszystkich kolumn do pobrania
     */
    private static String getFieldsAsString(Class<? extends BaseEntity> clazz) {
        List<Field> fields = getAllFields(clazz);
        return fields.stream().map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    /**
     * Zwraca wszystkie pola z klasy oraz nadklas.
     *
     * @param clazz klasa, której pola mają zostać znalezione
     * @return lista pól
     */
    private static List<Field> getAllFields(Class<? extends BaseEntity> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass())
            fields.addAll(Arrays.asList(c.getDeclaredFields()));

        return fields;
    }
}
