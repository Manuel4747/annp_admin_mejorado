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
@Table(name = "movimientos_componentes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientosComponentes.findAll", query = "SELECT m FROM MovimientosComponentes m")
    , @NamedQuery(name = "MovimientosComponentes.findById", query = "SELECT m FROM MovimientosComponentes m WHERE m.id = :id")
    , @NamedQuery(name = "MovimientosComponentes.findByComponente", query = "SELECT m FROM MovimientosComponentes m WHERE m.componente = :componente ORDER BY m.fechaMovimiento DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "MovimientosComponentes.findByFechaHoraAlta", query = "SELECT m FROM MovimientosComponentes m WHERE m.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "MovimientosComponentes.findByFechaHoraUltimoEstado", query = "SELECT m FROM MovimientosComponentes m WHERE m.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "MovimientosComponentes.findByFechaMovimiento", query = "SELECT m FROM MovimientosComponentes m WHERE m.fechaMovimiento = :fechaMovimiento")})
public class MovimientosComponentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @Column(name = "fecha_movimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaMovimiento;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "motivo_movimiento_componente", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private MotivosMovimientosComponentes motivoMovimientoComponente;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "responsable_origen", referencedColumnName = "id")
    @ManyToOne
    private Usuarios responsableOrigen;
    @JoinColumn(name = "responsable_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableDestino;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ComponentesBienes componente;
    @JoinColumn(name = "departamento_origen", referencedColumnName = "id")
    @ManyToOne
    private Departamentos departamentoOrigen;
    @JoinColumn(name = "departamento_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoDestino;

    public MovimientosComponentes() {
    }

    public MovimientosComponentes(Integer id) {
        this.id = id;
    }

    public MovimientosComponentes(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Date fechaMovimiento) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.fechaMovimiento = fechaMovimiento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public MotivosMovimientosComponentes getMotivoMovimientoComponente() {
        return motivoMovimientoComponente;
    }

    public void setMotivoMovimientoComponente(MotivosMovimientosComponentes motivoMovimientoComponente) {
        this.motivoMovimientoComponente = motivoMovimientoComponente;
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

    public ComponentesBienes getComponente() {
        return componente;
    }

    public void setComponente(ComponentesBienes componente) {
        this.componente = componente;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosComponentes)) {
            return false;
        }
        MovimientosComponentes other = (MovimientosComponentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models10.MovimientosComponentes[ id=" + id + " ]";
    }
    
}
