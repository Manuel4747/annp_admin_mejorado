/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.datasource;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "chart_antecedentes_por_dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChartAntecedentesPorDia.findByRangoFechaAlta", query = "SELECT r FROM ChartAntecedentesPorDia r WHERE r.fechaAlta >= :fechaDesde AND r.fechaAlta <= :fechaHasta ORDER BY r.fechaAlta")})
public class ChartAntecedentesPorDia implements Serializable {
    @Id
    @Temporal(jakarta.persistence.TemporalType.DATE)
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    @Column(name = "cantidad")
    private Integer cantidad;
    
    public ChartAntecedentesPorDia(){
        
    }
    
    public ChartAntecedentesPorDia(Date fechaAlta, Integer cantidad){
        this.fechaAlta = fechaAlta;
        this.cantidad = cantidad;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
