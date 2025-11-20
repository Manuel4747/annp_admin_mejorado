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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "detalles_plan_estrategicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesPlanEstrategicas.findAll", query = "SELECT d FROM DetallesPlanEstrategicas d"),
    @NamedQuery(name = "DetallesPlanEstrategicas.findOrdered", query = "SELECT d FROM DetallesPlanEstrategicas d WHERE d.departamento =:departamento or d.responsable =:responsable ORDER BY d.id DESC"),
    @NamedQuery(name = "DetallesPlanEstrategicas.findByPlanEstrategicica", query = "SELECT d FROM DetallesPlanEstrategicas d WHERE d.planEstrategica = :planEstrategica"),
    @NamedQuery(name = "DetallesPlanEstrategicas.findByDocumentoPlanificacion", query = "SELECT d FROM DetallesPlanEstrategicas d WHERE d.tipoDocumentos = :tipoDocumentos ORDER BY d.fechaHoraAlta DESC"),
    @NamedQuery(name = "DetallesPlanEstrategicas.findByFecha", query = "SELECT d FROM DetallesPlanEstrategicas d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta"),
    @NamedQuery(name = "DetallesPlanEstrategicas.findById", query = "SELECT d FROM DetallesPlanEstrategicas d WHERE d.id = :id")
})
public class DetallesPlanEstrategicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "item")
    private int item;
    @Basic(optional = false)
    @Column(name = "variable2")
    private double variable2;
    @Basic(optional = true)
    @Column(name = "valor_variable")
    private double valorVariable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resultado")
    private double resultado;
    @Basic(optional = true)
    @Column(name = "valor")
    private double valor;
    @Basic(optional = true)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Lob
    @Size(max = 65535)
    @Column(name = "ultima_observacion")
    private String ultimaObservacion;
    @Transient
    private String ultimaObservacionAux;
    @Transient
    private String ultimaObservacionAntecedenteAux;
    @JoinColumn(name = "usuario_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimaObservacion;
    @Column(name = "fecha_ultima_observacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaObservacion;
    @JoinColumn(name = "plan_estrategica", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PlanEstrategicas planEstrategica;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "actividad", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Actividades actividad;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosInforme estado;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsable;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "observacion_documento_planificacion", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosPlanificacion observacionDocumentoPlanificacion;
    @JoinColumn(name = "programacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Programacion programacion;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private TiposDocumentosJudiciales tipoDocumentos;
    
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "accionOperativa")
    private Collection<Archivos> archivos;

    public DetallesPlanEstrategicas() {
    }

    public DetallesPlanEstrategicas(Integer id) {
        this.id = id;
    }

    public DetallesPlanEstrategicas(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado,TiposDocumentosJudiciales tipoDocumentos) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.tipoDocumentos = tipoDocumentos;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
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

    public PlanEstrategicas getPlanEstrategica() {
        return planEstrategica;
    }

    public void setPlanEstrategica(PlanEstrategicas planEstrategica) {
        this.planEstrategica = planEstrategica;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public EstadosInforme getEstado() {
        return estado;
    }

    public void setEstado(EstadosInforme estado) {
        this.estado = estado;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public String getUltimaObservacionAux() {
        return ultimaObservacionAux;
    }

    public void setUltimaObservacionAux(String ultimaObservacionAux) {
        this.ultimaObservacionAux = ultimaObservacionAux;
    }

    public String getUltimaObservacionAntecedenteAux() {
        return ultimaObservacionAntecedenteAux;
    }

    public void setUltimaObservacionAntecedenteAux(String ultimaObservacionAntecedenteAux) {
        this.ultimaObservacionAntecedenteAux = ultimaObservacionAntecedenteAux;
    }

    public Usuarios getUsuarioUltimaObservacion() {
        return usuarioUltimaObservacion;
    }

    public void setUsuarioUltimaObservacion(Usuarios usuarioUltimaObservacion) {
        this.usuarioUltimaObservacion = usuarioUltimaObservacion;
    }

    public Date getFechaUltimaObservacion() {
        return fechaUltimaObservacion;
    }

    public void setFechaUltimaObservacion(Date fechaUltimaObservacion) {
        this.fechaUltimaObservacion = fechaUltimaObservacion;
    }

    public ObservacionesDocumentosPlanificacion getObservacionDocumentoPlanificacion() {
        return observacionDocumentoPlanificacion;
    }

    public void setObservacionDocumentoPlanificacion(ObservacionesDocumentosPlanificacion observacionDocumentoPlanificacion) {
        this.observacionDocumentoPlanificacion = observacionDocumentoPlanificacion;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public TiposDocumentosJudiciales getTipoDocumentos() {
        return tipoDocumentos;
    }

    public void setTipoDocumentos(TiposDocumentosJudiciales tipoDocumentos) {
        this.tipoDocumentos = tipoDocumentos;
    } 
     public Collection<Archivos> getArchivos() {
        return archivos;
    }

    public void setArchivos(Collection<Archivos> archivos) {
        this.archivos = archivos;
    }

    @XmlTransient
    public boolean verifObs() {
        if (ultimaObservacion == null && ultimaObservacionAux != null) {
            return true;
        } else if (ultimaObservacion != null && ultimaObservacionAux == null) {
            return false;
        } else if (ultimaObservacion == null && ultimaObservacionAux == null) {
            return false;
        }

        return !ultimaObservacion.equals(ultimaObservacionAux);
    }

    @XmlTransient
    public void transferirObs() {
        ultimaObservacion = ultimaObservacionAux;
    }

    @XmlTransient
    public String getUltimaObservacionString() {
        if (ultimaObservacion != null) {
            return ultimaObservacion.replace("\n", "<br />");
        } else {
            return "";
        }
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
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
        if (!(object instanceof DetallesPlanEstrategicas)) {
            return false;
        }
        DetallesPlanEstrategicas other = (DetallesPlanEstrategicas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DetallesPlanEstrategicas[ id=" + id + " ]";
    }

}
