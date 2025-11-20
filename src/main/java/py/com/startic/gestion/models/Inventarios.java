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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "inventarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventarios.findAll", query = "SELECT i FROM Inventarios i")
    , @NamedQuery(name = "Inventarios.findOrdered", query = "SELECT i FROM Inventarios i ORDER BY i.fechaHoraAlta DESC")
    , @NamedQuery(name = "Inventarios.findById", query = "SELECT i FROM Inventarios i WHERE i.id = :id")
    , @NamedQuery(name = "Inventarios.findEnProceso", query = "SELECT i FROM Inventarios i WHERE i.estado.codigo = 'PR'")
    , @NamedQuery(name = "Inventarios.findVigente", query = "SELECT i FROM Inventarios i WHERE i.estado.codigo = 'TE'")
    , @NamedQuery(name = "Inventarios.findByFecha", query = "SELECT i FROM Inventarios i WHERE i.fecha = :fecha")
    , @NamedQuery(name = "Inventarios.findByFechaHoraAlta", query = "SELECT i FROM Inventarios i WHERE i.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Inventarios.findByFechaHoraUltimoEstado", query = "SELECT i FROM Inventarios i WHERE i.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class Inventarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
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
    @OneToMany(mappedBy = "inventario")
    private Collection<DetallesInventario> detallesInventarioCollection;
    @OneToMany(mappedBy = "inventario")
    private Collection<EntradasArticulo> entradasArticuloCollection;
    @OneToMany(mappedBy = "inventario")
    private Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection;
    @OneToMany(mappedBy = "inventario")
    private Collection<SalidasArticulo> salidasArticuloCollection;
    @OneToMany(mappedBy = "inventario")
    private Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosInventario estado;

    public Inventarios() {
    }

    public Inventarios(Integer id) {
        this.id = id;
    }

    public Inventarios(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
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

    @XmlTransient
    public Collection<DetallesInventario> getDetallesInventarioCollection() {
        return detallesInventarioCollection;
    }

    public void setDetallesInventarioCollection(Collection<DetallesInventario> detallesInventarioCollection) {
        this.detallesInventarioCollection = detallesInventarioCollection;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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

    public EstadosInventario getEstado() {
        return estado;
    }

    public void setEstado(EstadosInventario estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<DetallesEntradaArticulo> getDetallesEntradaArticuloCollection() {
        return detallesEntradaArticuloCollection;
    }

    public void setDetallesEntradaArticuloCollection(Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection) {
        this.detallesEntradaArticuloCollection = detallesEntradaArticuloCollection;
    }

    @XmlTransient
    public Collection<EntradasArticulo> getEntradasArticuloCollection() {
        return entradasArticuloCollection;
    }

    public void setEntradasArticuloCollection(Collection<EntradasArticulo> entradasArticuloCollection) {
        this.entradasArticuloCollection = entradasArticuloCollection;
    }

    @XmlTransient
    public Collection<SalidasArticulo> getSalidasArticuloCollection() {
        return salidasArticuloCollection;
    }

    public void setSalidasArticuloCollection(Collection<SalidasArticulo> salidasArticuloCollection) {
        this.salidasArticuloCollection = salidasArticuloCollection;
    }

    @XmlTransient
    public Collection<DetallesSalidaArticulo> getDetallesSalidaArticuloCollection() {
        return detallesSalidaArticuloCollection;
    }

    public void setDetallesSalidaArticuloCollection(Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection) {
        this.detallesSalidaArticuloCollection = detallesSalidaArticuloCollection;
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
        if (!(object instanceof Inventarios)) {
            return false;
        }
        Inventarios other = (Inventarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.Inventarios[ id=" + id + " ]";
    }
    
}
