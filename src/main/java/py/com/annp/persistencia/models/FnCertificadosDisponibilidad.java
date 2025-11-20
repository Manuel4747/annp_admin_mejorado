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
import jakarta.persistence.Id;
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
@Table(name = "fn_certificados_disponibilidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnCertificadosDisponibilidad.findAll", query = "SELECT f FROM FnCertificadosDisponibilidad f")
    , @NamedQuery(name = "FnCertificadosDisponibilidad.findById", query = "SELECT f FROM FnCertificadosDisponibilidad f WHERE f.id = :id")
    , @NamedQuery(name = "FnCertificadosDisponibilidad.findByFecha", query = "SELECT f FROM FnCertificadosDisponibilidad f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnCertificadosDisponibilidad.findByDescripcion", query = "SELECT f FROM FnCertificadosDisponibilidad f WHERE f.descripcion = :descripcion")})
public class FnCertificadosDisponibilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 5000)
    @Column(name = "descripcion")
    private String descripcion;

    public FnCertificadosDisponibilidad() {
    }

    public FnCertificadosDisponibilidad(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof FnCertificadosDisponibilidad)) {
            return false;
        }
        FnCertificadosDisponibilidad other = (FnCertificadosDisponibilidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnCertificadosDisponibilidad[ id=" + id + " ]";
    }
    
}
