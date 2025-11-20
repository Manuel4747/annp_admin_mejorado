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
@Table(name = "fn_contratos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnContratos.findAll", query = "SELECT f FROM FnContratos f")
    , @NamedQuery(name = "FnContratos.findById", query = "SELECT f FROM FnContratos f WHERE f.id = :id")
    , @NamedQuery(name = "FnContratos.findByFecha", query = "SELECT f FROM FnContratos f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FnContratos.findByDescripcion", query = "SELECT f FROM FnContratos f WHERE f.descripcion = :descripcion")
    , @NamedQuery(name = "FnContratos.findByImporte", query = "SELECT f FROM FnContratos f WHERE f.importe = :importe")})
public class FnContratos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 5000)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "importe")
    private Long importe;
    @JoinColumn(name = "licitacion", referencedColumnName = "id")
    @ManyToOne
    private FnLicitaciones licitacion;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne
    private FnProveedores proveedor;
    @OneToMany(mappedBy = "contrato")
    private Collection<FnOrdenesDeCompra> fnOrdenesDeCompraCollection;

    public FnContratos() {
    }

    public FnContratos(Integer id) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public FnLicitaciones getLicitacion() {
        return licitacion;
    }

    public void setLicitacion(FnLicitaciones licitacion) {
        this.licitacion = licitacion;
    }

    public FnProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(FnProveedores proveedor) {
        this.proveedor = proveedor;
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
        if (!(object instanceof FnContratos)) {
            return false;
        }
        FnContratos other = (FnContratos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnContratos[ id=" + id + " ]";
    }
    
}
