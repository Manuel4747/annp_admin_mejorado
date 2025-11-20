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
 * @author eduardo
 */
@Entity
@Table(name = "especificaciones_subcuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspecificacionesSubcuenta.findAll", query = "SELECT e FROM EspecificacionesSubcuenta e")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findById", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.id = :id")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findByOrdered", query = "SELECT e FROM EspecificacionesSubcuenta e ORDER BY e.subcuenta.cuenta.codigo, e.subcuenta.codigo, e.codigo")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findBySubcuenta", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.subcuenta = :subcuenta ORDER BY e.subcuenta.cuenta.codigo, e.subcuenta.codigo, e.codigo")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findByDescripcion", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findByCodigo", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.codigo = :codigo")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findByFechaHoraAlta", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EspecificacionesSubcuenta.findByFechaHoraUltimoEstado", query = "SELECT e FROM EspecificacionesSubcuenta e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EspecificacionesSubcuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "codigo")
    private String codigo;
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
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "subcuenta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subcuentas subcuenta;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "especificacionSubcuenta")
    private Collection<Bienes> bienesCollection;

    public EspecificacionesSubcuenta() {
    }

    public EspecificacionesSubcuenta(Integer id) {
        this.id = id;
    }

    public EspecificacionesSubcuenta(Integer id, String descripcion, String codigo, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Subcuentas getSubcuenta() {
        return subcuenta;
    }

    public void setSubcuenta(Subcuentas subcuenta) {
        this.subcuenta = subcuenta;
    }


    @XmlTransient
    public Collection<Bienes> getBienesCollection() {
        return bienesCollection;
    }

    public void setBienesCollection(Collection<Bienes> bienesCollection) {
        this.bienesCollection = bienesCollection;
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
        if (!(object instanceof EspecificacionesSubcuenta)) {
            return false;
        }
        EspecificacionesSubcuenta other = (EspecificacionesSubcuenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return subcuenta.getCuenta().getCodigo() + " - " + subcuenta.getCuenta().getDescripcion() + " / " + subcuenta.getCodigo() + " - " + subcuenta.getDescripcion() + " / " + codigo + " - " + descripcion;
    }
    
}
