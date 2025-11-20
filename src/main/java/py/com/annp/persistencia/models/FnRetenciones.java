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
@Table(name = "fn_retenciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnRetenciones.findAll", query = "SELECT f FROM FnRetenciones f")
    , @NamedQuery(name = "FnRetenciones.findById", query = "SELECT f FROM FnRetenciones f WHERE f.id = :id")
    , @NamedQuery(name = "FnRetenciones.findByPorcentaje", query = "SELECT f FROM FnRetenciones f WHERE f.porcentaje = :porcentaje")
    , @NamedQuery(name = "FnRetenciones.findByMonto", query = "SELECT f FROM FnRetenciones f WHERE f.monto = :monto")})
public class FnRetenciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "porcentaje")
    private Long porcentaje;
    @Column(name = "monto")
    private Long monto;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne
    private FnPagos pago;

    public FnRetenciones() {
    }

    public FnRetenciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Long porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }

    public FnPagos getPago() {
        return pago;
    }

    public void setPago(FnPagos pago) {
        this.pago = pago;
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
        if (!(object instanceof FnRetenciones)) {
            return false;
        }
        FnRetenciones other = (FnRetenciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnRetenciones[ id=" + id + " ]";
    }
    
}
