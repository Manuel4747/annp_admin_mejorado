/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.datasource;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author eduardo
 */
@Entity
public class RepAntecedentesDocumentosJudicialesRes implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "tipo_resolucion")
    private String tipoResolucion;
    @Column(name = "tipo_resolucion_alt")
    private String tipoResolucionAlt;
    @Column(name = "nro_resolucion")
    private String nroResolucion;
    @Column(name = "resuelve")
    private String resuelve;
    @Column(name = "fecha")
    private String fecha;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(String tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public String getResuelve() {
        return resuelve;
    }

    public void setResuelve(String resuelve) {
        this.resuelve = resuelve;
    }

    public String getTipoResolucionAlt() {
        return tipoResolucionAlt;
    }

    public void setTipoResolucionAlt(String tipoResolucionAlt) {
        this.tipoResolucionAlt = tipoResolucionAlt;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
