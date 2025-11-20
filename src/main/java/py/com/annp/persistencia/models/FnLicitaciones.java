/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_licitaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnLicitaciones.findAll", query = "SELECT f FROM FnLicitaciones f")
    , @NamedQuery(name = "FnLicitaciones.findById", query = "SELECT f FROM FnLicitaciones f WHERE f.id = :id")
    , @NamedQuery(name = "FnLicitaciones.findByFecha", query = "SELECT f FROM FnLicitaciones f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnLicitaciones.findByDescripcion", query = "SELECT f FROM FnLicitaciones f WHERE f.descripcion = :descripcion")
    , @NamedQuery(name = "FnLicitaciones.findByEstado", query = "SELECT f FROM FnLicitaciones f WHERE f.estado = :estado")})
public class FnLicitaciones implements Serializable {

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
    @Column(name = "estado")
    private Integer estado;
    @OneToMany(mappedBy = "licitacion")
    private Collection<FnContratos> fnContratosCollection;
    @JoinColumn(name = "adjudicado", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores adjudicado;
    @OneToMany(mappedBy = "licitacion")
    private Collection<FnDetalleLicitacion> fnDetalleLicitacionCollection;

    public FnLicitaciones() {
    }

    public FnLicitaciones(Integer id) {
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<FnContratos> getFnContratosCollection() {
        return fnContratosCollection;
    }

    public void setFnContratosCollection(Collection<FnContratos> fnContratosCollection) {
        this.fnContratosCollection = fnContratosCollection;
    }

    public FnProveedores getAdjudicado() {
        return adjudicado;
    }

    public void setAdjudicado(FnProveedores adjudicado) {
        this.adjudicado = adjudicado;
    }

    @XmlTransient
    public Collection<FnDetalleLicitacion> getFnDetalleLicitacionCollection() {
        return fnDetalleLicitacionCollection;
    }

    public void setFnDetalleLicitacionCollection(Collection<FnDetalleLicitacion> fnDetalleLicitacionCollection) {
        this.fnDetalleLicitacionCollection = fnDetalleLicitacionCollection;
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
        if (!(object instanceof FnLicitaciones)) {
            return false;
        }
        FnLicitaciones other = (FnLicitaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnLicitaciones[ id=" + id + " ]";
    }
    
}
