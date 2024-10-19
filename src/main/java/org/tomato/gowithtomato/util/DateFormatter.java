package org.tomato.gowithtomato.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public  String format(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }
    public  String formatTripDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM");
        return dateTime.format(formatter);
    }

    public String formatTripInfo(LocalDateTime dateTime, String fromCity, String toCity) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = dateTime.format(timeFormatter);
        return time + " | " + fromCity + " → " + toCity;
    }
}
