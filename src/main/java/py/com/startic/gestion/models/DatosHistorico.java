/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.persistence.Lob;
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

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "datos_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosHistorico.findAll", query = "SELECT p FROM DatosHistorico p"),
    @NamedQuery(name = "DatosHistorico.findOrdered", query = "SELECT p FROM DatosHistorico p ORDER BY p.fechaHoraAlta DESC"),
    @NamedQuery(name = "DatosHistorico.findById", query = "SELECT p FROM DatosHistorico p WHERE p.id = :id"),
    @NamedQuery(name = "DatosHistorico.findByPeriodo", query = "SELECT p FROM DatosHistorico p WHERE p.programacion.año = :año"),
    @NamedQuery(name = "DatosHistorico.findByBuscarAccionesPorPeriodo", query = "SELECT p FROM DatosHistorico p WHERE p.accion = :accion AND p.periodo = :periodo ORDER BY p.fechaHoraAlta ASC"),
    @NamedQuery(name = "DatosHistorico.findByBuscarAccionesPorFecha", query = "SELECT p FROM DatosHistorico p WHERE p.fechaEjecucion >= :fechaDesde AND p.fechaEjecucion <= :fechaHasta AND p.accion = :accion  ORDER BY p.fechaHoraAlta ASC"),
    @NamedQuery(name = "DatosHistorico.findByFecha", query = "SELECT p FROM DatosHistorico p WHERE p.fechaHoraAlta >= :fechaDesde AND p.fechaHoraAlta <= :fechaHasta")})

public class DatosHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cantidad_programada")
    private double cantidadProgramada;
    @Column(name = "resultado")
    private double resultado;
    @Column(name = "cantidad_avance")
    private double cantidadAvance;
    @Column(name = "total_avance")
    private double totalAvance;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion_actividad")
    private String descripcionActividad;
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @NotNull
    @Column(name = "fecha_ejecucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEjecucion;
    @OneToMany(mappedBy = "usuarioAlta")
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "accion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Acciones accion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Empresas empresa;
    @JoinColumn(name = "programacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Programacion programacion;
    @JoinColumn(name = "periodo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Periodo periodo;
    @JoinColumn(name = "plan_estrategica", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private PlanEstrategicas planEstrategicas;
    @JoinColumn(name = "detalles_plan", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DetallesPlanEstrategicas detallesPlan;
   
    public DatosHistorico() {
    }

    public DatosHistorico(Integer id) {
        this.id = id;
    }

    public DatosHistorico(Integer id,double cantidadAvance, double resultado) {
        this.id = id;
        this.cantidadAvance = cantidadAvance;
        this.resultado = resultado;
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
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

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public double getCantidadProgramada() {
        return cantidadProgramada;
    }

    public void setCantidadProgramada(double cantidadProgramada) {
        this.cantidadProgramada = cantidadProgramada;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getCantidadAvance() {
        return cantidadAvance;
    }

    public void setCantidadAvance(double cantidadAvance) {
        this.cantidadAvance = cantidadAvance;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public PlanEstrategicas getPlanEstrategicas() {
        return planEstrategicas;
    }

    public void setPlanEstrategicas(PlanEstrategicas planEstrategicas) {
        this.planEstrategicas = planEstrategicas;
    }

    public double getTotalAvance() {
        return totalAvance;
    }

    public void setTotalAvance(double totalAvance) {
        this.totalAvance = totalAvance;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public DetallesPlanEstrategicas getDetallesPlan() {
        return detallesPlan;
    }

    public void setDetallesPlan(DetallesPlanEstrategicas detallesPlan) {
        this.detallesPlan = detallesPlan;
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
        if (!(object instanceof DatosHistorico)) {
            return false;
        }
        DatosHistorico other = (DatosHistorico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DatosHistorico[ id=" + id + " ]";
    }

}
