package py.com.startic.gestion.controllers.util;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.inject.Inject;
import py.com.startic.gestion.controllers.RhFeriadosControllers;

public class DiasHabiles_ {
    @Inject
    RhFeriadosControllers rhFeriadosControllers;

    public static void main(String[] args) {
        LocalDate fechaInicio = LocalDate.of(2025, 9, 30);
        LocalDate fechaFin = LocalDate.of(2025, 10, 1); // 30 de septiembre de 2024

        // Lista de festivos (ejemplo)
        Set<LocalDate> feriados = new HashSet<>();
        DiasHabiles_ dd = new DiasHabiles_();
        List<LocalDate> ferList = dd.getFeriados(fechaInicio, fechaFin);
        if (ferList!=null && !ferList.isEmpty()) feriados = (Set<LocalDate>) ferList;
        
        long diasHabiles = contarDiasHabiles(fechaInicio, fechaFin, feriados);
        System.out.println("Días hábiles entre " + fechaInicio + " y " + fechaFin + ": " + diasHabiles);
    }
    public static long diffFechas(Date fechaIniDate, Date fechaFinDate) {
        LocalDate fechaInicio = ControlFecha.dateToLocalDate(fechaIniDate);
        LocalDate fechaFin = ControlFecha.dateToLocalDate(fechaFinDate);
        // Lista de festivos (ejemplo)
        Set<LocalDate> feriados = new HashSet<>();
        DiasHabiles_ dd = new DiasHabiles_();
        List<LocalDate> ferList = dd.getFeriados(fechaInicio, fechaFin);
        if (ferList!=null && !ferList.isEmpty()) feriados = (Set<LocalDate>) ferList;
        
        long diasHabiles = contarDiasHabiles(fechaInicio, fechaFin, feriados);
        System.out.println("Días hábiles entre " + fechaInicio + " y " + fechaFin + ": " + diasHabiles);
        return diasHabiles;
    }
    private List<LocalDate> getFeriados(LocalDate fechaInicio, LocalDate fechaFin) {
        rhFeriadosControllers = new RhFeriadosControllers();
        List<LocalDate> feridados = rhFeriadosControllers.getFeriadosEntre(fechaInicio, fechaFin);
        return feridados;
    }
    public static long contarDiasHabiles(LocalDate fechaInicio, LocalDate fechaFin, Set<LocalDate> festivos) {
        // Asegurarse de que la fecha de fin no incluya el último día para el bucle
        // LocalDate endExclusive = fechaFin.plus(1, ChronoUnit.DAYS); // Si quieres incluir el último día

        // 1. Obtener todas las fechas entre inicio (incluida) y fin (excluida)
        ControlFecha fechaRef = new ControlFecha(fechaInicio.plus(1, ChronoUnit.DAYS));
        return fechaRef.datesUntil(fechaFin.plus(1, ChronoUnit.DAYS)) // fechaFin.plus(1, ChronoUnit.DAYS) para que sea inclusivo
                .filter(fecha -> fecha.getDayOfWeek() != DayOfWeek.SATURDAY && // Filtra los sábados
                                 fecha.getDayOfWeek() != DayOfWeek.SUNDAY)    // y los domingos
                .filter(fecha -> !festivos.contains(fecha))                   // y los festivos
                .count();                                                     // Cuenta el total de días restantes
    }
    
}