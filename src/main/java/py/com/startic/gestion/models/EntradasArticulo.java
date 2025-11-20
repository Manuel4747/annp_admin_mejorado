/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
 * @author grecia
 */
@Entity
@Table(name = "entradas_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradasArticulo.findAll", query = "SELECT e FROM EntradasArticulo e ORDER BY e.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findOrdered", query = "SELECT s FROM EntradasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findFechaEntradaOrdered", query = "SELECT s FROM EntradasArticulo s WHERE s.fechaEntrada >= :fechaDesde AND s.fechaEntrada <= :fechaHasta ORDER BY s.fechaEntrada DESC, s.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findById", query = "SELECT e FROM EntradasArticulo e WHERE e.id = :id")
    , @NamedQuery(name = "EntradasArticulo.findByConcepto", query = "SELECT e FROM EntradasArticulo e WHERE e.concepto = :concepto")
    , @NamedQuery(name = "EntradasArticulo.findByNroFactura", query = "SELECT e FROM EntradasArticulo e WHERE e.nroFactura = :nroFactura")
    , @NamedQuery(name = "EntradasArticulo.findByNroLlamado", query = "SELECT e FROM EntradasArticulo e WHERE e.nroLlamado = :nroLlamado")
    , @NamedQuery(name = "EntradasArticulo.findByFechaHoraAlta", query = "SELECT e FROM EntradasArticulo e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EntradasArticulo.findByFechaHoraUltimoEstado", query = "SELECT e FROM EntradasArticulo e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EntradasArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "concepto")
    private String concepto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_factura")
    private String nroFactura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_llamado")
    private String nroLlamado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrada;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "entradaArticulo")
    private Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedores proveedor;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public EntradasArticulo() {
    }

    public EntradasArticulo(Integer id) {
        this.id = id;
    }

    public EntradasArticulo(Integer id, String concepto, String nroFactura, String nroLlamado, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.concepto = concepto;
        this.nroFactura = nroFactura;
        this.nroLlamado = nroLlamado;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

    public void setNroLlamado(String nroLlamado) {
        this.nroLlamado = nroLlamado;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @XmlTransient
    public Collection<DetallesEntradaArticulo> getDetallesEntradaArticuloCollection() {
        return detallesEntradaArticuloCollection;
    }

    public void setDetallesEntradaArticuloCollection(Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection) {
        this.detallesEntradaArticuloCollection = detallesEntradaArticuloCollection;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof EntradasArticulo)) {
            return false;
        }
        EntradasArticulo other = (EntradasArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return concepto;
    }
    
}
