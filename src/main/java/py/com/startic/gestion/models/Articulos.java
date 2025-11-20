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
@Table(name = "articulos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulos.findAll", query = "SELECT a FROM Articulos a")
    , @NamedQuery(name = "Articulos.findOrdered", query = "SELECT a FROM Articulos a ORDER BY a.descripcion")
    , @NamedQuery(name = "Articulos.findByCodigo", query = "SELECT a FROM Articulos a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "Articulos.findByDescripcion", query = "SELECT a FROM Articulos a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "Articulos.findByStock", query = "SELECT a FROM Articulos a WHERE a.stock = :stock")
    , @NamedQuery(name = "Articulos.findByStockCritico", query = "SELECT a FROM Articulos a WHERE a.stockCritico >= a.stock")
    , @NamedQuery(name = "Articulos.findByMarca", query = "SELECT a FROM Articulos a WHERE a.marca = :marca")
    , @NamedQuery(name = "Articulos.findByModelo", query = "SELECT a FROM Articulos a WHERE a.modelo = :modelo")
    , @NamedQuery(name = "Articulos.findByFechaHoraAlta", query = "SELECT a FROM Articulos a WHERE a.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Articulos.findByFechaHoraUltimoEstado", query = "SELECT a FROM Articulos a WHERE a.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Articulos.findByEmpresa", query = "SELECT a FROM Articulos a WHERE a.empresa = :empresa")})
public class Articulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private int stock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock_critico")
    private int stockCritico;
    @Size(max = 200)
    @Column(name = "marca")
    private String marca;
    @Size(max = 200)
    @Column(name = "modelo")
    private String modelo;
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
    @JoinColumn(name = "unidad", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Unidades unidad;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Rubros rubro;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesInventario> detallesInventarioCollection;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public Articulos() {
    }

    public Articulos(String codigo) {
        this.codigo = codigo;
    }

    public Articulos(String codigo, String descripcion, int stock, int stockCritico, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Empresas empresa) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.stockCritico = stockCritico;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.empresa = empresa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Rubros getRubro() {
        return rubro;
    }

    public void setRubro(Rubros rubro) {
        this.rubro = rubro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(int stockCritico) {
        this.stockCritico = stockCritico;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Collection<DetallesInventario> getDetallesInventarioCollection() {
        return detallesInventarioCollection;
    }

    @XmlTransient
    public void setDetallesInventarioCollection(Collection<DetallesInventario> detallesInventarioCollection) {
        this.detallesInventarioCollection = detallesInventarioCollection;
    }

    @XmlTransient
    public Collection<DetallesSalidaArticulo> getDetallesSalidaArticuloCollection() {
        return detallesSalidaArticuloCollection;
    }

    public void setDetallesSalidaArticuloCollection(Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection) {
        this.detallesSalidaArticuloCollection = detallesSalidaArticuloCollection;
    }
    
    @XmlTransient
    public Collection<DetallesEntradaArticulo> getDetallesEntradaArticuloCollection() {
        return detallesEntradaArticuloCollection;
    }

    public void setDetallesEntradaArticuloCollection(Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection) {
        this.detallesEntradaArticuloCollection = detallesEntradaArticuloCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulos)) {
            return false;
        }
        Articulos other = (Articulos) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    @XmlTransient
    public String getDescripcionLarga() {
        return descripcion + " (" + stock + " " + unidad.getSimbolo() + ")";
    }
    
}
