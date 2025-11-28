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
@Table(name = "fn_organismos_finaciadores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnOrganismosFinaciadores.findAll", query = "SELECT f FROM FnOrganismosFinaciadores f")
    , @NamedQuery(name = "FnOrganismosFinaciadores.findById", query = "SELECT f FROM FnOrganismosFinaciadores f WHERE f.id = :id")
    , @NamedQuery(name = "FnOrganismosFinaciadores.findByNombre", query = "SELECT f FROM FnOrganismosFinaciadores f WHERE f.nombre = :nombre")})
public class FnOrganismosFinaciadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 1500)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "organismoFinanciador")
    private Collection<FnPresupuesto> fnPresupuestoCollection;

    public FnOrganismosFinaciadores() {
    }

    public FnOrganismosFinaciadores(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<FnPresupuesto> getFnPresupuestoCollection() {
        return fnPresupuestoCollection;
    }

    public void setFnPresupuestoCollection(Collection<FnPresupuesto> fnPresupuestoCollection) {
        this.fnPresupuestoCollection = fnPresupuestoCollection;
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
        if (!(object instanceof FnOrganismosFinaciadores)) {
            return false;
        }
        FnOrganismosFinaciadores other = (FnOrganismosFinaciadores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnOrganismosFinaciadores[ id=" + id + " ]";
    }
    
}
