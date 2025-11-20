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
@Table(name = "fn_detalle_factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDetalleFactura.findAll", query = "SELECT f FROM FnDetalleFactura f")
    , @NamedQuery(name = "FnDetalleFactura.findById", query = "SELECT f FROM FnDetalleFactura f WHERE f.id = :id")
    , @NamedQuery(name = "FnDetalleFactura.findByCantidad", query = "SELECT f FROM FnDetalleFactura f WHERE f.cantidad = :cantidad")
    , @NamedQuery(name = "FnDetalleFactura.findByCostoUnitario", query = "SELECT f FROM FnDetalleFactura f WHERE f.costoUnitario = :costoUnitario")
    , @NamedQuery(name = "FnDetalleFactura.findByTotal", query = "SELECT f FROM FnDetalleFactura f WHERE f.total = :total")
    , @NamedQuery(name = "FnDetalleFactura.findByIva10", query = "SELECT f FROM FnDetalleFactura f WHERE f.iva10 = :iva10")
    , @NamedQuery(name = "FnDetalleFactura.findByIva5", query = "SELECT f FROM FnDetalleFactura f WHERE f.iva5 = :iva5")
    , @NamedQuery(name = "FnDetalleFactura.findByExcenta", query = "SELECT f FROM FnDetalleFactura f WHERE f.excenta = :excenta")
    , @NamedQuery(name = "FnDetalleFactura.findByTotalIva", query = "SELECT f FROM FnDetalleFactura f WHERE f.totalIva = :totalIva")})
public class FnDetalleFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "cantidad")
    private Long cantidad;
    @Column(name = "costo_unitario")
    private Long costoUnitario;
    @Column(name = "total")
    private Long total;
    @Column(name = "iva_10")
    private Long iva10;
    @Column(name = "iva_5")
    private Long iva5;
    @Column(name = "excenta")
    private Long excenta;
    @Column(name = "total_iva")
    private Long totalIva;
    @JoinColumn(name = "articulo", referencedColumnName = "id")
    @ManyToOne
    private FnArticulos articulo;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne
    private FnFacturas factura;

    public FnDetalleFactura() {
    }

    public FnDetalleFactura(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(Long costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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

    public Long getExcenta() {
        return excenta;
    }

    public void setExcenta(Long excenta) {
        this.excenta = excenta;
    }

    public Long getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(Long totalIva) {
        this.totalIva = totalIva;
    }

    public FnArticulos getArticulo() {
        return articulo;
    }

    public void setArticulo(FnArticulos articulo) {
        this.articulo = articulo;
    }

    public FnFacturas getFactura() {
        return factura;
    }

    public void setFactura(FnFacturas factura) {
        this.factura = factura;
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
        if (!(object instanceof FnDetalleFactura)) {
            return false;
        }
        FnDetalleFactura other = (FnDetalleFactura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDetalleFactura[ id=" + id + " ]";
    }
    
}
