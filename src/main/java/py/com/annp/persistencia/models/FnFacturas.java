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
@Table(name = "fn_facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnFacturas.findAll", query = "SELECT f FROM FnFacturas f")
    , @NamedQuery(name = "FnFacturas.findById", query = "SELECT f FROM FnFacturas f WHERE f.id = :id")
    , @NamedQuery(name = "FnFacturas.findByFecha", query = "SELECT f FROM FnFacturas f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnFacturas.findByCondicion", query = "SELECT f FROM FnFacturas f WHERE f.condicion = :condicion")
    , @NamedQuery(name = "FnFacturas.findByTimbrado", query = "SELECT f FROM FnFacturas f WHERE f.timbrado = :timbrado")
    , @NamedQuery(name = "FnFacturas.findByImporteTotal", query = "SELECT f FROM FnFacturas f WHERE f.importeTotal = :importeTotal")
    , @NamedQuery(name = "FnFacturas.findByIva10", query = "SELECT f FROM FnFacturas f WHERE f.iva10 = :iva10")
    , @NamedQuery(name = "FnFacturas.findByIva5", query = "SELECT f FROM FnFacturas f WHERE f.iva5 = :iva5")
    , @NamedQuery(name = "FnFacturas.findByExcentas", query = "SELECT f FROM FnFacturas f WHERE f.excentas = :excentas")
    , @NamedQuery(name = "FnFacturas.findByDescripcion", query = "SELECT f FROM FnFacturas f WHERE f.descripcion = :descripcion")})
public class FnFacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 2)
    @Column(name = "condicion")
    private String condicion;
    @Size(max = 10)
    @Column(name = "timbrado")
    private String timbrado;
    @Column(name = "importe_total")
    private Long importeTotal;
    @Column(name = "iva_10")
    private Long iva10;
    @Column(name = "iva_5")
    private Long iva5;
    @Column(name = "excentas")
    private Long excentas;
    @Size(max = 5000)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "orden_compra", referencedColumnName = "id")
    @ManyToOne
    private FnOrdenesDeCompra ordenCompra;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores proveedor;
    @OneToMany(mappedBy = "factura")
    private Collection<FnPagos> fnPagosCollection;
    @OneToMany(mappedBy = "factura")
    private Collection<FnDetalleFactura> fnDetalleFacturaCollection;

    public FnFacturas() {
    }

    public FnFacturas(Integer id) {
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

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public Long getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Long importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Long getIva10() {
        return iva10;
    }

    public void setIva10(Long iva10) {
        this.iva10 = iva10;
    }

    public Long getIva5() {
        return iva5;
    }

    public void setIva5(Long iva5) {
        this.iva5 = iva5;
    }

    public Long getExcentas() {
        return excentas;
    }

    public void setExcentas(Long excentas) {
        this.excentas = excentas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public FnOrdenesDeCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(FnOrdenesDeCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public FnProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(FnProveedores proveedor) {
        this.proveedor = proveedor;
    }

    @XmlTransient
    public Collection<FnPagos> getFnPagosCollection() {
        return fnPagosCollection;
    }

    public void setFnPagosCollection(Collection<FnPagos> fnPagosCollection) {
        this.fnPagosCollection = fnPagosCollection;
    }

    @XmlTransient
    public Collection<FnDetalleFactura> getFnDetalleFacturaCollection() {
        return fnDetalleFacturaCollection;
    }

    public void setFnDetalleFacturaCollection(Collection<FnDetalleFactura> fnDetalleFacturaCollection) {
        this.fnDetalleFacturaCollection = fnDetalleFacturaCollection;
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
        if (!(object instanceof FnFacturas)) {
            return false;
        }
        FnFacturas other = (FnFacturas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnFacturas[ id=" + id + " ]";
    }
    
}
