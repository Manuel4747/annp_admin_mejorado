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
@Table(name = "fn_proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnProveedores.findAll", query = "SELECT f FROM FnProveedores f")
    , @NamedQuery(name = "FnProveedores.findById", query = "SELECT f FROM FnProveedores f WHERE f.id = :id")
    , @NamedQuery(name = "FnProveedores.findByRuc", query = "SELECT f FROM FnProveedores f WHERE f.ruc = :ruc")
    , @NamedQuery(name = "FnProveedores.findByRazonSocial", query = "SELECT f FROM FnProveedores f WHERE f.razonSocial = :razonSocial")
    , @NamedQuery(name = "FnProveedores.findByDireccion", query = "SELECT f FROM FnProveedores f WHERE f.direccion = :direccion")
    , @NamedQuery(name = "FnProveedores.findByTelefono", query = "SELECT f FROM FnProveedores f WHERE f.telefono = :telefono")})
public class FnProveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 20)
    @Column(name = "ruc")
    private String ruc;
    @Size(max = 1000)
    @Column(name = "razon_social")
    private String razonSocial;
    @Size(max = 1000)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(mappedBy = "proveedor")
    private Collection<FnFacturas> fnFacturasCollection;
    @OneToMany(mappedBy = "proveedor")
    private Collection<FnComprobantesDePago> fnComprobantesDePagoCollection;
    @OneToMany(mappedBy = "proveedor")
    private Collection<FnContratos> fnContratosCollection;
    @OneToMany(mappedBy = "adjudicado")
    private Collection<FnLicitaciones> fnLicitacionesCollection;
    @OneToMany(mappedBy = "oferente")
    private Collection<FnDetalleLicitacion> fnDetalleLicitacionCollection;
    @OneToMany(mappedBy = "proveedor")
    private Collection<FnOrdenesDeCompra> fnOrdenesDeCompraCollection;

    public FnProveedores() {
    }

    public FnProveedores(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlTransient
    public Collection<FnFacturas> getFnFacturasCollection() {
        return fnFacturasCollection;
    }

    public void setFnFacturasCollection(Collection<FnFacturas> fnFacturasCollection) {
        this.fnFacturasCollection = fnFacturasCollection;
    }

    @XmlTransient
    public Collection<FnComprobantesDePago> getFnComprobantesDePagoCollection() {
        return fnComprobantesDePagoCollection;
    }

    public void setFnComprobantesDePagoCollection(Collection<FnComprobantesDePago> fnComprobantesDePagoCollection) {
        this.fnComprobantesDePagoCollection = fnComprobantesDePagoCollection;
    }

    @XmlTransient
    public Collection<FnContratos> getFnContratosCollection() {
        return fnContratosCollection;
    }

    public void setFnContratosCollection(Collection<FnContratos> fnContratosCollection) {
        this.fnContratosCollection = fnContratosCollection;
    }

    @XmlTransient
    public Collection<FnLicitaciones> getFnLicitacionesCollection() {
        return fnLicitacionesCollection;
    }

    public void setFnLicitacionesCollection(Collection<FnLicitaciones> fnLicitacionesCollection) {
        this.fnLicitacionesCollection = fnLicitacionesCollection;
    }

    @XmlTransient
    public Collection<FnDetalleLicitacion> getFnDetalleLicitacionCollection() {
        return fnDetalleLicitacionCollection;
    }

    public void setFnDetalleLicitacionCollection(Collection<FnDetalleLicitacion> fnDetalleLicitacionCollection) {
        this.fnDetalleLicitacionCollection = fnDetalleLicitacionCollection;
    }

    @XmlTransient
    public Collection<FnOrdenesDeCompra> getFnOrdenesDeCompraCollection() {
        return fnOrdenesDeCompraCollection;
    }

    public void setFnOrdenesDeCompraCollection(Collection<FnOrdenesDeCompra> fnOrdenesDeCompraCollection) {
        this.fnOrdenesDeCompraCollection = fnOrdenesDeCompraCollection;
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
        if (!(object instanceof FnProveedores)) {
            return false;
        }
        FnProveedores other = (FnProveedores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnProveedores[ id=" + id + " ]";
    }
    
}
