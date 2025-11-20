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
@Table(name = "fn_objetos_de_gasto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnObjetosDeGasto.findAll", query = "SELECT f FROM FnObjetosDeGasto f")
    , @NamedQuery(name = "FnObjetosDeGasto.findById", query = "SELECT f FROM FnObjetosDeGasto f WHERE f.id = :id")
    , @NamedQuery(name = "FnObjetosDeGasto.findByNumero", query = "SELECT f FROM FnObjetosDeGasto f WHERE f.numero = :numero")
    , @NamedQuery(name = "FnObjetosDeGasto.findByDenominacion", query = "SELECT f FROM FnObjetosDeGasto f WHERE f.denominacion = :denominacion")
    , @NamedQuery(name = "FnObjetosDeGasto.findByConcepto", query = "SELECT f FROM FnObjetosDeGasto f WHERE f.concepto = :concepto")
    , @NamedQuery(name = "FnObjetosDeGasto.findByAbreviacion", query = "SELECT f FROM FnObjetosDeGasto f WHERE f.abreviacion = :abreviacion")})
public class FnObjetosDeGasto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "denominacion")
    private String denominacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "concepto")
    private String concepto;
    @Size(max = 50)
    @Column(name = "abreviacion")
    private String abreviacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objetosDeGasto")
    private Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objetoDeGasto")
    private Collection<FnDetallePresupuesto> fnDetallePresupuestoCollection;

    public FnObjetosDeGasto() {
    }

    public FnObjetosDeGasto(Integer id) {
        this.id = id;
    }

    public FnObjetosDeGasto(Integer id, int numero, String denominacion, String concepto) {
        this.id = id;
        this.numero = numero;
        this.denominacion = denominacion;
        this.concepto = concepto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    @XmlTransient
    public Collection<FnDetallePlanFinanciero> getFnDetallePlanFinancieroCollection() {
        return fnDetallePlanFinancieroCollection;
    }

    public void setFnDetallePlanFinancieroCollection(Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection) {
        this.fnDetallePlanFinancieroCollection = fnDetallePlanFinancieroCollection;
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
        if (!(object instanceof FnObjetosDeGasto)) {
            return false;
        }
        FnObjetosDeGasto other = (FnObjetosDeGasto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnObjetosDeGasto[ id=" + id + " ]";
    }
    
}
