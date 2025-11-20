/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
 * @author DELL
 */
@Entity
@Table(name = "formularios_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormulariosPermisos.findAll", query = "SELECT f FROM FormulariosPermisos f"),
    @NamedQuery(name = "FormulariosPermisos.findById", query = "SELECT f FROM FormulariosPermisos f WHERE f.id = :id"),
    @NamedQuery(name = "FormulariosPermisos.findOrdered", query = "SELECT f FROM FormulariosPermisos f WHERE f.funcionario = :funcionario or f.responsableDestino = :responsableDestino ORDER BY f.id DESC"),
    @NamedQuery(name = "FormulariosPermisos.findByFuncionario", query = "SELECT f FROM FormulariosPermisos f WHERE f.funcionario = :funcionario"),
    @NamedQuery(name = "FormulariosPermisos.findByResponsableDestino", query = "SELECT f FROM FormulariosPermisos f WHERE f.responsableDestino = :responsableDestino"),
    @NamedQuery(name = "FormulariosPermisos.findByEstado", query = "SELECT f FROM FormulariosPermisos f WHERE f.estado = :estado"),
    @NamedQuery(name = "FormulariosPermisos.findByCodigoEstado", query = "SELECT f FROM FormulariosPermisos f WHERE f.estado.codigo = :estado"),
    @NamedQuery(name = "FormulariosPermisos.findByActividad", query = "SELECT f FROM FormulariosPermisos f WHERE f.observacion = :observacion")})
public class FormulariosPermisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "fecha_Hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @Column(name = "cantidad_dia")
    private Integer cantidadDia;
    @Column(name = "acumulacion")
    private Integer acumulacion;
    @Size(max = 50)
    @Column(name = "dias")
    private String dias;
    @Size(max = 50)
    @Column(name = "horas")
    private String horas;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @Size(max = 200)
    @Column(name = "direccion_general")
    private String direccionGeneral;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = true)
    @Size(max = 10)
    @Column(name = "nro_documento")
    private String nroDocumento;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "jefatura")
    private String jefatura;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "adjunto")
    private String adjunto;
    @Column(name = "hora_desde")
    @Temporal(TemporalType.TIME)
    private Date horaDesde;
    @Column(name = "hora_hasta")
    @Temporal(TemporalType.TIME)
    private Date horaHasta;
    @Size(max = 4)
    @Column(name = "año")
    private String año;
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
    @JoinColumn(name = "funcionario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios funcionario;
    @JoinColumn(name = "dependencia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos dependencia;
    @JoinColumn(name = "tipo_formulario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposFormulario tiposFormulario;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosSolicitudPermiso estado;
    @JoinColumn(name = "responsable_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableDestino;
    @JoinColumn(name = "tipo_permiso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposPermisos tiposPermisos;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
     @JoinColumn(name = "observaciones_solicitudes_permisos", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesSolicitudesPermisos observacionSolicitudesPermisos;
     
    @Column(name = "acumulado")
    private boolean acumulado;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Integer getCantidadDia() {
        return cantidadDia;
    }

    public void setCantidadDia(Integer cantidadDia) {
        this.cantidadDia = cantidadDia;
    }

    public Integer getAcumulacion() {
        return acumulacion;
    }

    public void setAcumulacion(Integer acumulacion) {
        this.acumulacion = acumulacion;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDireccionGeneral() {
        return direccionGeneral;
    }

    public void setDireccionGeneral(String direccionGeneral) {
        this.direccionGeneral = direccionGeneral;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getJefatura() {
        return jefatura;
    }

    public void setJefatura(String jefatura) {
        this.jefatura = jefatura;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public Date getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(Date horaDesde) {
        this.horaDesde = horaDesde;
    }

    public Date getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(Date horaHasta) {
        this.horaHasta = horaHasta;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public Departamentos getDependencia() {
        return dependencia;
    }

    public void setDependencia(Departamentos dependencia) {
        this.dependencia = dependencia;
    }

    public TiposFormulario getTiposFormulario() {
        return tiposFormulario;
    }

    public void setTiposFormulario(TiposFormulario tiposFormulario) {
        this.tiposFormulario = tiposFormulario;
    }

    public EstadosSolicitudPermiso getEstado() {
        return estado;
    }

    public void setEstado(EstadosSolicitudPermiso estado) {
        this.estado = estado;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public TiposPermisos getTiposPermisos() {
        return tiposPermisos;
    }

    public void setTiposPermisos(TiposPermisos tiposPermisos) {
        this.tiposPermisos = tiposPermisos;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }
     public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
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

    public ObservacionesSolicitudesPermisos getObservacionSolicitudesPermisos() {
        return observacionSolicitudesPermisos;
    }

    public void setObservacionSolicitudesPermisos(ObservacionesSolicitudesPermisos observacionSolicitudesPermisos) {
        this.observacionSolicitudesPermisos = observacionSolicitudesPermisos;
    }

    public boolean isAcumulado() {
        return acumulado;
    }

    public void setAcumulado(boolean acumulado) {
        this.acumulado = acumulado;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormulariosPermisos)) {
            return false;
        }
        FormulariosPermisos other = (FormulariosPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.FormulariosPermisos[ id=" + id + " ]";
    }

}
