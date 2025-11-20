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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_ordenes_de_compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnOrdenesDeCompra.findAll", query = "SELECT f FROM FnOrdenesDeCompra f")
    , @NamedQuery(name = "FnOrdenesDeCompra.findById", query = "SELECT f FROM FnOrdenesDeCompra f WHERE f.id = :id")
    , @NamedQuery(name = "FnOrdenesDeCompra.findByFecha", query = "SELECT f FROM FnOrdenesDeCompra f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnOrdenesDeCompra.findByImporte", query = "SELECT f FROM FnOrdenesDeCompra f WHERE f.importe = :importe")})
public class FnOrdenesDeCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "importe")
    private Long importe;
    @OneToMany(mappedBy = "ordenCompra")
    private Collection<FnFacturas> fnFacturasCollection;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores proveedor;
    @JoinColumn(name = "contrato", referencedColumnName = "id")
    @ManyToOne
    private FnContratos contrato;

    public FnOrdenesDeCompra() {
    }

    public FnOrdenesDeCompra(Integer id) {
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

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    @XmlTransient
    public Collection<FnFacturas> getFnFacturasCollection() {
        return fnFacturasCollection;
    }

    public void setFnFacturasCollection(Collection<FnFacturas> fnFacturasCollection) {
        this.fnFacturasCollection = fnFacturasCollection;
    }

    public FnProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(FnProveedores proveedor) {
        this.proveedor = proveedor;
    }

    public FnContratos getContrato() {
        return contrato;
    }

    public void setContrato(FnContratos contrato) {
        this.contrato = contrato;
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
        if (!(object instanceof FnOrdenesDeCompra)) {
            return false;
        }
        FnOrdenesDeCompra other = (FnOrdenesDeCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnOrdenesDeCompra[ id=" + id + " ]";
    }
    
}
