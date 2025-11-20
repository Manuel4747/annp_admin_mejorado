/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.controllers.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author DELL
 */
public class ControlFecha {

    private LocalDate fechaInicio;
    private List<LocalDate> dates;

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public ControlFecha(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Stream<LocalDate> datesUntil(LocalDate fechaFin) {
        if (dates == null) {
            dates = new ArrayList<>();
        }
        System.out.println("iniciando bucle...");
        LocalDate f = fechaInicio;
        while (f.isBefore(fechaFin)) {
            dates.add(f);
            System.out.println("Fecha:" + f.format(DateTimeFormatter.ISO_DATE));
            f = f.plus(1, ChronoUnit.DAYS);
        }
        System.out.println("Fin de fechas...");
        return dates.stream();
    }
    
    public static LocalDate dateToLocalDate (Date oldDate) {
        // 1. Create a java.util.Date object

        // 2. Convert Date to Instant
        java.time.Instant instant = oldDate.toInstant();

        // 3. Get the system's default ZoneId
        ZoneId defaultZoneId = ZoneId.systemDefault();

        // 4. Convert Instant to ZonedDateTime and then to LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();

        System.out.println("Original java.util.Date: " + oldDate);
        System.out.println("Converted java.time.LocalDate: " + localDate);
        return localDate;
    }
    
    public static Date localDateToDAte(LocalDate localDate) {

        // 2. Convert LocalDate to a ZonedDateTime at the start of the day in a specific time zone
        //    You need to choose a ZoneId. ZoneId.systemDefault() uses the system's default time zone.
        java.time.ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());

        // 3. Convert ZonedDateTime to an Instant
        java.time.Instant instant = zonedDateTime.toInstant();

        // 4. Convert Instant to a java.util.Date
        Date date = Date.from(instant);

        System.out.println("LocalDate: " + localDate);
        System.out.println("java.util.Date: " + date);
        return date;
    }

}
