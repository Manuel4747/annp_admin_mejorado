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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "fn_pagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnPagos.findAll", query = "SELECT f FROM FnPagos f")
    , @NamedQuery(name = "FnPagos.findById", query = "SELECT f FROM FnPagos f WHERE f.id = :id")
    , @NamedQuery(name = "FnPagos.findByFormaDePago", query = "SELECT f FROM FnPagos f WHERE f.formaDePago = :formaDePago")
    , @NamedQuery(name = "FnPagos.findByCuentaProveedor", query = "SELECT f FROM FnPagos f WHERE f.cuentaProveedor = :cuentaProveedor")
    , @NamedQuery(name = "FnPagos.findByImporte", query = "SELECT f FROM FnPagos f WHERE f.importe = :importe")
    , @NamedQuery(name = "FnPagos.findByCuentaSalida", query = "SELECT f FROM FnPagos f WHERE f.cuentaSalida = :cuentaSalida")
    , @NamedQuery(name = "FnPagos.findByProveedor", query = "SELECT f FROM FnPagos f WHERE f.proveedor = :proveedor")
    , @NamedQuery(name = "FnPagos.findByFecha", query = "SELECT f FROM FnPagos f WHERE f.fecha = :fecha")})
public class FnPagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "forma_de_pago")
    private Integer formaDePago;
    @Size(max = 50)
    @Column(name = "cuenta_proveedor")
    private String cuentaProveedor;
    @Column(name = "importe")
    private Long importe;
    @Size(max = 50)
    @Column(name = "cuenta_salida")
    private String cuentaSalida;
    @Column(name = "proveedor")
    private Integer proveedor;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @OneToMany(mappedBy = "pago")
    private Collection<FnComprobantesDePago> fnComprobantesDePagoCollection;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne
    private FnFacturas factura;
    @JoinColumn(name = "cheque", referencedColumnName = "id")
    @ManyToOne
    private FnCheques cheque;
    @OneToMany(mappedBy = "pago")
    private Collection<FnRetenciones> fnRetencionesCollection;

    public FnPagos() {
    }

    public FnPagos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(Integer formaDePago) {
        this.formaDePago = formaDePago;
    }

    public String getCuentaProveedor() {
        return cuentaProveedor;
    }

    public void setCuentaProveedor(String cuentaProveedor) {
        this.cuentaProveedor = cuentaProveedor;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public String getCuentaSalida() {
        return cuentaSalida;
    }

    public void setCuentaSalida(String cuentaSalida) {
        this.cuentaSalida = cuentaSalida;
    }

    public Integer getProveedor() {
        return proveedor;
    }

    public void setProveedor(Integer proveedor) {
        this.proveedor = proveedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlTransient
    public Collection<FnComprobantesDePago> getFnComprobantesDePagoCollection() {
        return fnComprobantesDePagoCollection;
    }

    public void setFnComprobantesDePagoCollection(Collection<FnComprobantesDePago> fnComprobantesDePagoCollection) {
        this.fnComprobantesDePagoCollection = fnComprobantesDePagoCollection;
    }

    public FnFacturas getFactura() {
        return factura;
    }

    public void setFactura(FnFacturas factura) {
        this.factura = factura;
    }

    public FnCheques getCheque() {
        return cheque;
    }

    public void setCheque(FnCheques cheque) {
        this.cheque = cheque;
    }

    @XmlTransient
    public Collection<FnRetenciones> getFnRetencionesCollection() {
        return fnRetencionesCollection;
    }

    public void setFnRetencionesCollection(Collection<FnRetenciones> fnRetencionesCollection) {
        this.fnRetencionesCollection = fnRetencionesCollection;
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
        if (!(object instanceof FnPagos)) {
            return false;
        }
        FnPagos other = (FnPagos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnPagos[ id=" + id + " ]";
    }
    
}
