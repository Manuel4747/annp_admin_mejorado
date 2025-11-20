/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "ag_agendamientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgAgendamientos.findAll", query = "SELECT r FROM AgAgendamientos r")
    , @NamedQuery(name = "AgAgendamientos.findById", query = "SELECT r FROM AgAgendamientos r WHERE r.id = :id")
    , @NamedQuery(name = "AgAgendamientos.findByDescripcion", query = "SELECT r FROM AgAgendamientos r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "AgAgendamientos.findByFecha", query = "SELECT d FROM AgAgendamientos d WHERE d.fecha >= :fechaDesde AND d.fecha <= :fechaHasta ORDER BY d.fecha DESC, d.fechaHoraAlta DESC")
    , @NamedQuery(name = "AgAgendamientos.findByFechaANDDepartamentoDestino", query = "SELECT d FROM AgAgendamientos d WHERE d.fecha >= :fechaDesde AND d.fecha <= :fechaHasta AND d.departamentoDestino = :departamentoDestino ORDER BY d.fecha DESC, d.fechaHoraAlta DESC")
    , @NamedQuery(name = "AgAgendamientos.findByFechaHoraAlta", query = "SELECT r FROM AgAgendamientos r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "AgAgendamientos.findByFechaHoraUltimoEstado", query = "SELECT r FROM AgAgendamientos r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class AgAgendamientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "usuario_destino", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioDestino;
    @JoinColumn(name = "departamento_destino", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoDestino;
    @Basic(optional = false)
    @NotNull
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
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public AgAgendamientos() {
    }

    public AgAgendamientos(Integer id) {
        this.id = id;
    }

    public AgAgendamientos(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
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

    public Departamentos getDepartamentoDestino() {
        return departamentoDestino;
    }

    public void setDepartamentoDestino(Departamentos departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Usuarios getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuarios usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        if (!(object instanceof AgAgendamientos)) {
            return false;
        }
        AgAgendamientos other = (AgAgendamientos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Agendamientos[ id=" + id + " ]";
    }
    
}
