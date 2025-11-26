/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_detalle_licitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDetalleLicitacion.findAll", query = "SELECT f FROM FnDetalleLicitacion f")
    , @NamedQuery(name = "FnDetalleLicitacion.findById", query = "SELECT f FROM FnDetalleLicitacion f WHERE f.id = :id")})
public class FnDetalleLicitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "licitacion", referencedColumnName = "id")
    @ManyToOne
    private FnLicitaciones licitacion;
    @JoinColumn(name = "oferente", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores oferente;

    public FnDetalleLicitacion() {
    }

    public FnDetalleLicitacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FnLicitaciones getLicitacion() {
        return licitacion;
    }

    public void setLicitacion(FnLicitaciones licitacion) {
        this.licitacion = licitacion;
    }

    public FnProveedores getOferente() {
        return oferente;
    }

    public void setOferente(FnProveedores oferente) {
        this.oferente = oferente;
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
        if (!(object instanceof FnDetalleLicitacion)) {
            return false;
        }
        FnDetalleLicitacion other = (FnDetalleLicitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDetalleLicitacion[ id=" + id + " ]";
    }
    
}
