package com.javakurs.purchasemanager.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa zawierająca przydatne metody do obsługi dat.
 */
public class DateHelper {
    /**
     * Zwraca łańcuch znaków reprezentujący datę.
     *
     * @param localDateTime data do sformatowania
     * @return sformatowana data
     */
    public static String getAsString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return localDateTime.format(formatter);
    }

    /**
     * Zwraca datę odczytaną na podstawie łańcucha znaków.
     *
     * @param formattedDate data w postaci łańcucha znaków.
     * @return odczytana data
     */
    public static LocalDateTime getFromString(String formattedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(formattedDate, formatter);
    }
}
