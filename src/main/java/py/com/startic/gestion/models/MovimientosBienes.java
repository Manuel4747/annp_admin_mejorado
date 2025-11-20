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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "movimientos_bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientosBienes.findAll", query = "SELECT m FROM MovimientosBienes m")
    , @NamedQuery(name = "MovimientosBienes.findByBien", query = "SELECT m FROM MovimientosBienes m WHERE m.bien = :bien ORDER BY m.fechaMovimiento DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "MovimientosBienes.findById", query = "SELECT m FROM MovimientosBienes m WHERE m.id = :id")
    , @NamedQuery(name = "MovimientosBienes.findByFechaHoraAlta", query = "SELECT m FROM MovimientosBienes m WHERE m.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "MovimientosBienes.findByFechaHoraUltimoEstado", query = "SELECT m FROM MovimientosBienes m WHERE m.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class MovimientosBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_movimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;
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
    @JoinColumn(name = "bien", referencedColumnName = "id")
    @ManyToOne
    private Bienes bien;
    @JoinColumn(name = "departamento_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoOrigen;
    @JoinColumn(name = "departamento_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoDestino;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "motivo_movimiento_bien", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private MotivosMovimientosBienes motivoMovimientoBien;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "responsable_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableOrigen;
    @JoinColumn(name = "responsable_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableDestino;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public MovimientosBienes() {
    }

    public MovimientosBienes(Integer id) {
        this.id = id;
    }

    public MovimientosBienes(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
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

    public Bienes getBien() {
        return bien;
    }

    public void setBien(Bienes bien) {
        this.bien = bien;
    }

    public Departamentos getDepartamentoOrigen() {
        return departamentoOrigen;
    }

    public void setDepartamentoOrigen(Departamentos departamentoOrigen) {
        this.departamentoOrigen = departamentoOrigen;
    }

    public Departamentos getDepartamentoDestino() {
        return departamentoDestino;
    }

    public void setDepartamentoDestino(Departamentos departamentoDestino) {
        this.departamentoDestino = departamentoDestino;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public MotivosMovimientosBienes getMotivoMovimientoBien() {
        return motivoMovimientoBien;
    }

    public void setMotivoMovimientoBien(MotivosMovimientosBienes motivoMovimientoBien) {
        this.motivoMovimientoBien = motivoMovimientoBien;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getResponsableOrigen() {
        return responsableOrigen;
    }

    public void setResponsableOrigen(Usuarios responsableOrigen) {
        this.responsableOrigen = responsableOrigen;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosBienes)) {
            return false;
        }
        MovimientosBienes other = (MovimientosBienes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models7.MovimientosBienes[ id=" + id + " ]";
    }
    
}
