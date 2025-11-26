/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDistribucion.findAll", query = "SELECT f FROM FnDistribucion f")
    , @NamedQuery(name = "FnDistribucion.findById", query = "SELECT f FROM FnDistribucion f WHERE f.id = :id")
    , @NamedQuery(name = "FnDistribucion.findByDescripcion", query = "SELECT f FROM FnDistribucion f WHERE f.descripcion = :descripcion")})
public class FnDistribucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribucion")
    private Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribucion")
    private Collection<FnSubDistribucion> fnSubDistribucionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribucion")
    private Collection<FnDetallePresupuesto> fnDetallePresupuestoCollection;

    public FnDistribucion() {
    }

    public FnDistribucion(Integer id) {
        this.id = id;
    }

    public FnDistribucion(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<FnDetallePlanFinanciero> getFnDetallePlanFinancieroCollection() {
        return fnDetallePlanFinancieroCollection;
    }

    public void setFnDetallePlanFinancieroCollection(Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection) {
        this.fnDetallePlanFinancieroCollection = fnDetallePlanFinancieroCollection;
    }

    @XmlTransient
    public Collection<FnSubDistribucion> getFnSubDistribucionCollection() {
        return fnSubDistribucionCollection;
    }

    public void setFnSubDistribucionCollection(Collection<FnSubDistribucion> fnSubDistribucionCollection) {
        this.fnSubDistribucionCollection = fnSubDistribucionCollection;
    }

    @XmlTransient
    public Collection<FnDetallePresupuesto> getFnDetallePresupuestoCollection() {
        return fnDetallePresupuestoCollection;
    }

    public void setFnDetallePresupuestoCollection(Collection<FnDetallePresupuesto> fnDetallePresupuestoCollection) {
        this.fnDetallePresupuestoCollection = fnDetallePresupuestoCollection;
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
        if (!(object instanceof FnDistribucion)) {
            return false;
        }
        FnDistribucion other = (FnDistribucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDistribucion[ id=" + id + " ]";
    }
    
}
