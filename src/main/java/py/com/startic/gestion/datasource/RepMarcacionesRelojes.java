/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.datasource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
public class RepMarcacionesRelojes {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Column(name = "minimo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date minimo;
    @Column(name = "maximo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date maximo;

    @Transient
    private Long horas;
    @Transient
    private Long minutos;
    private String horaString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public Date getMinimo() {
        return minimo;
    }

    public void setMinimo(Date minimo) {
        this.minimo = minimo;
    }

    public Date getMaximo() {
        return maximo;
    }

    public void setMaximo(Date maximo) {
        this.maximo = maximo;
    }

    public Long getHoras() {
        calcularHorasMinutos();
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public Long getMinutos() {
        calcularHorasMinutos();
        return minutos;
    }

    public void setMinutos(Long minutos) {
        this.minutos = minutos;
    }

    public String getHoraString() {
        calcularHorasMinutos();
        String hh = padLeftZeros(String.valueOf(horas), 2);
        String mm = padLeftZeros(String.valueOf(minutos), 2);
        horaString =  hh + ":" + mm;
        return horaString;
    }

    private LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void calcularHorasMinutos() {
        if (horas == null || horas.equals(0)) {
            if (minimo == null || maximo == null) {
                horas = Long.valueOf(0);
                minutos = Long.valueOf(0);
            } else {
                LocalDateTime dateTime1 = convertToLocalDateViaInstant(minimo);
                LocalDateTime dateTime2 = convertToLocalDateViaInstant(maximo);

                // Calculate the duration between the two dates
                Duration duration = Duration.between(dateTime1, dateTime2);

                // Get the total difference in hours and minutes
                long totalMinutes = duration.toMinutes();
                horas = totalMinutes / 60;
                minutos = totalMinutes % 60;
            }
            System.out.println("Time difference: " + horas + " hours and " + minutos + " minutes.");
        }
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

}
