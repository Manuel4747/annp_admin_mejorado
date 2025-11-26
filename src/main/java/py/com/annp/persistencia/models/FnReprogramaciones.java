/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_reprogramaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnReprogramaciones.findAll", query = "SELECT f FROM FnReprogramaciones f"),
    @NamedQuery(name = "FnReprogramaciones.findById", query = "SELECT f FROM FnReprogramaciones f WHERE f.id = :id"),
    @NamedQuery(name = "FnReprogramaciones.findByFechaInicio", query = "SELECT f FROM FnReprogramaciones f WHERE f.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "FnReprogramaciones.findByDescripcion", query = "SELECT f FROM FnReprogramaciones f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FnReprogramaciones.findByPeriodo", query = "SELECT f FROM FnReprogramaciones f WHERE f.periodo = :periodo"),
    @NamedQuery(name = "FnReprogramaciones.findByFechaAprobacion", query = "SELECT f FROM FnReprogramaciones f WHERE f.fechaAprobacion = :fechaAprobacion"),
    @NamedQuery(name = "FnReprogramaciones.findByNroDocAprobacion", query = "SELECT f FROM FnReprogramaciones f WHERE f.nroDocAprobacion = :nroDocAprobacion"),
    @NamedQuery(name = "FnReprogramaciones.findByImporte", query = "SELECT f FROM FnReprogramaciones f WHERE f.importe = :importe")})
public class FnReprogramaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacion;
    @Size(max = 100)
    @Column(name = "nro_doc_aprobacion")
    private String nroDocAprobacion;
    @Column(name = "importe")
    private Long importe;
    @JoinColumn(name = "tipos_reprogramaciones", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnTiposReprogramaciones tiposReprogramaciones;

    public FnReprogramaciones() {
    }

    public FnReprogramaciones(Integer id) {
        this.id = id;
    }

    public FnReprogramaciones(Integer id, Date fechaInicio, String descripcion, int periodo) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.descripcion = descripcion;
        this.periodo = periodo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getNroDocAprobacion() {
        return nroDocAprobacion;
    }

    public void setNroDocAprobacion(String nroDocAprobacion) {
        this.nroDocAprobacion = nroDocAprobacion;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public FnTiposReprogramaciones getTiposReprogramaciones() {
        return tiposReprogramaciones;
    }

    public void setTiposReprogramaciones(FnTiposReprogramaciones tiposReprogramaciones) {
        this.tiposReprogramaciones = tiposReprogramaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FnReprogramaciones)) {
            return false;
        }
        FnReprogramaciones other = (FnReprogramaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnReprogramaciones[ id=" + id + " ]";
    }

}
