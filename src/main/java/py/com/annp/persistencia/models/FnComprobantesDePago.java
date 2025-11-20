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
@Table(name = "fn_comprobantes_de_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnComprobantesDePago.findAll", query = "SELECT f FROM FnComprobantesDePago f")
    , @NamedQuery(name = "FnComprobantesDePago.findById", query = "SELECT f FROM FnComprobantesDePago f WHERE f.id = :id")
    , @NamedQuery(name = "FnComprobantesDePago.findByTipo", query = "SELECT f FROM FnComprobantesDePago f WHERE f.tipo = :tipo")
    , @NamedQuery(name = "FnComprobantesDePago.findByFecha", query = "SELECT f FROM FnComprobantesDePago f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnComprobantesDePago.findByImporte", query = "SELECT f FROM FnComprobantesDePago f WHERE f.importe = :importe")})
public class FnComprobantesDePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "fecha")
    private Integer fecha;
    @Column(name = "importe")
    private Long importe;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne
    private FnPagos pago;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores proveedor;

    public FnComprobantesDePago() {
    }

    public FnComprobantesDePago(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public FnPagos getPago() {
        return pago;
    }

    public void setPago(FnPagos pago) {
        this.pago = pago;
    }

    public FnProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(FnProveedores proveedor) {
        this.proveedor = proveedor;
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
        if (!(object instanceof FnComprobantesDePago)) {
            return false;
        }
        FnComprobantesDePago other = (FnComprobantesDePago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnComprobantesDePago[ id=" + id + " ]";
    }
    
}
