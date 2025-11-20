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
@Table(name = "rh_det_plantillas_horario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhDetPlantillasHorario.findAll", query = "SELECT r FROM RhDetPlantillasHorario r")
    , @NamedQuery(name = "RhDetPlantillasHorario.findById", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.id = :id")
    , @NamedQuery(name = "RhDetPlantillasHorario.findByDiaSemana", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.diaSemana = :diaSemana")
    , @NamedQuery(name = "RhDetPlantillasHorario.findByHoraEntrada", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.horaEntrada = :horaEntrada")
    , @NamedQuery(name = "RhDetPlantillasHorario.findByHoraSalida", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.horaSalida = :horaSalida")
    , @NamedQuery(name = "RhDetPlantillasHorario.findByFechaHoraAlta", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "RhDetPlantillasHorario.findByFechaHoraUltimoEstado", query = "SELECT r FROM RhDetPlantillasHorario r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class RhDetPlantillasHorario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dia_semana")
    private int diaSemana;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIME)
    private Date horaEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_salida")
    @Temporal(TemporalType.TIME)
    private Date horaSalida;
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
    @JoinColumn(name = "plantilla_horario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RhPlantillasHorario plantillaHorario;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;

    public RhDetPlantillasHorario() {
    }

    public RhDetPlantillasHorario(Integer id) {
        this.id = id;
    }

    public RhDetPlantillasHorario(Integer id, int diaSemana, Date horaEntrada, Date horaSalida, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
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

    public RhPlantillasHorario getPlantillaHorario() {
        return plantillaHorario;
    }

    public void setPlantillaHorario(RhPlantillasHorario plantillaHorario) {
        this.plantillaHorario = plantillaHorario;
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

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
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
        if (!(object instanceof RhDetPlantillasHorario)) {
            return false;
        }
        RhDetPlantillasHorario other = (RhDetPlantillasHorario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models21.RhDetPlantillasHorario[ id=" + id + " ]";
    }
    
}
