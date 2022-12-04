package com.javakurs.purchasemanager.helper;

import com.javakurs.purchasemanager.MainApplication;

import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa ułatwiająca korzystanie z parametrów aplikacji.
 */
public class PropertiesHelper {
    /** Właściwości aplikacji z pliku application.properties */
    private static final Properties properties;

    static {
        properties = new Properties();

        try {
            InputStream inputStream = MainApplication.class.getResourceAsStream("application.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            MsgHelper.showError("Błąd ładowania pliku właściwości.", e.getMessage());
        }
    }

    /**
     * Zwraca wartość właściwości aplikacji o przekazanej nazwie.
     *
     * @param name nazwa właściwości
     * @return wartość właściwości aplikacji
     */
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    /**
     * Zwraca wartość właściwości aplikacji o przekazanej nazwie.
     *
     * @param name         nazwa właściwości
     * @param defaultValue domyślna wartość, zwracana jeśli nie zdefiniowano właściwości w pliku application.properties
     * @return wartość właściwości aplikacji
     */
    public static String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }
}
