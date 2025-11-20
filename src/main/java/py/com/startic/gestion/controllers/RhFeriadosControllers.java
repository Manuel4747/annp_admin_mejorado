/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import py.com.startic.gestion.models.RhFeriados;
import py.com.startic.gestion.controllers.util.ControlFecha;

/**
 *
 * @author DELL
 */
@Named(value = "rhFeriadosControllers")
@ViewScoped
public class RhFeriadosControllers extends AbstractController<RhFeriados> {

    public RhFeriadosControllers() {
        super(RhFeriados.class);
    }

    public List<LocalDate> getFeriadosEntre(LocalDate inicio, LocalDate fin) {
        List<LocalDate> localDates = new ArrayList<>();
        try {
            if (ejbFacade != null) {
                List<RhFeriados> feriadosRows = ejbFacade.getEntityManager()
                        .createNamedQuery("RhFeriados.findBetweenFecha", RhFeriados.class)
                        .setParameter("fechaDesde", ControlFecha.localDateToDAte(inicio))
                        .setParameter("fechaHasta", ControlFecha.localDateToDAte(fin))
                        .getResultList();
                for (RhFeriados ff : feriadosRows) {
                    localDates.add(ControlFecha.dateToLocalDate(ff.getFecha()));
                }
            } else {
                System.out.println("EJB FACADE ES NULL");
            }

        } catch (Exception ex) {
            System.out.println("Exception al consultar feriados: " + ex.getMessage());
            ex.printStackTrace();
        }
        return localDates;
    }

    public long diffFechas(Date fechaIniDate, Date fechaFinDate) {
        LocalDate fechaInicio = ControlFecha.dateToLocalDate(fechaIniDate);
        LocalDate fechaFin = ControlFecha.dateToLocalDate(fechaFinDate);
        // Lista de festivos (ejemplo)
        Set<LocalDate> feriados = new HashSet<>();

        List<LocalDate> ferList = getFeriados(fechaInicio, fechaFin);
        if (ferList != null && !ferList.isEmpty()) {
            for (LocalDate fl:ferList){
                feriados.add(fl);
            }
        };

        long diasHabiles = contarDiasHabiles(fechaInicio, fechaFin, feriados);
        System.out.println("Días hábiles entre " + fechaInicio + " y " + fechaFin + ": " + diasHabiles);
        return diasHabiles;
    }

    private List<LocalDate> getFeriados(LocalDate fechaInicio, LocalDate fechaFin) {

        List<LocalDate> feridados = getFeriadosEntre(fechaInicio, fechaFin);
        return feridados;
    }

    public static long contarDiasHabiles(LocalDate fechaInicio, LocalDate fechaFin, Set<LocalDate> festivos) {
        // Asegurarse de que la fecha de fin no incluya el último día para el bucle
        // LocalDate endExclusive = fechaFin.plus(1, ChronoUnit.DAYS); // Si quieres incluir el último día

        // 1. Obtener todas las fechas entre inicio (incluida) y fin (excluida)
        ControlFecha fechaRef = new ControlFecha(fechaInicio.plus(1, ChronoUnit.DAYS));
        return fechaRef.datesUntil(fechaFin.plus(1, ChronoUnit.DAYS)) // fechaFin.plus(1, ChronoUnit.DAYS) para que sea inclusivo
                .filter(fecha -> fecha.getDayOfWeek() != DayOfWeek.SATURDAY
                && // Filtra los sábados
                fecha.getDayOfWeek() != DayOfWeek.SUNDAY) // y los domingos
                .filter(fecha -> !festivos.contains(fecha)) // y los festivos
                .count();                                                     // Cuenta el total de días restantes
    }
}
