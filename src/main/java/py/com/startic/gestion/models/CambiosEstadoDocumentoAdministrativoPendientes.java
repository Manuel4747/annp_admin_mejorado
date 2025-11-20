/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@Table(name = "cambios_estado_documento_administrativo_pendientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findAll", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativo", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.documentoAdministrativo = :documentoAdministrativo ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativoDepartamento", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.documentoAdministrativo = :documentoAdministrativo AND m.departamentoOrigen = :departamentoOrigen AND m.fechaHoraRespuesta is null ORDER BY m.fechaHoraAlta, m.fechaHoraUltimoEstado")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByDepartamentoOrigen", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.departamentoOrigen = :departamentoOrigen and m.respuesta = 'IN' ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativoDepartamentoOrigen", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.documentoAdministrativo = :documentoAdministrativo AND m.departamentoOrigen = :departamentoOrigen and m.respuesta = 'IN' ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findById", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.id = :id")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByFechaHoraAlta", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "CambiosEstadoDocumentoAdministrativoPendientes.findByFechaHoraUltimoEstado", query = "SELECT m FROM CambiosEstadoDocumentoAdministrativoPendientes m WHERE m.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class CambiosEstadoDocumentoAdministrativoPendientes implements Serializable {

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
    @Basic(optional = true)
    @Column(name = "respuesta")
    private String respuesta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRespuesta;
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne
    private DocumentosAdministrativos documentoAdministrativo;
    @JoinColumn(name = "departamento_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoOrigen;
    @JoinColumn(name = "departamento_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoDestino;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_respuesta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioRespuesta;
    @JoinColumn(name = "responsable_origen", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableOrigen;
    @JoinColumn(name = "responsable_destino", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableDestino;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "estado_original", referencedColumnName = "codigo")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosDocumentoAdministrativo estadoOriginal;
    @JoinColumn(name = "estado_final", referencedColumnName = "codigo")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosDocumentoAdministrativo estadoFinal;
    @JoinColumn(name = "cambio_estado_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private TransferenciasDocumentoAdministrativo cambioEstadoDocumento;

    public CambiosEstadoDocumentoAdministrativoPendientes() {
    }

    public CambiosEstadoDocumentoAdministrativoPendientes(Integer id) {
        this.id = id;
    }

    public CambiosEstadoDocumentoAdministrativoPendientes(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public EstadosDocumentoAdministrativo getEstadoOriginal() {
        return estadoOriginal;
    }

    public void setEstadoOriginal(EstadosDocumentoAdministrativo estadoOriginal) {
        this.estadoOriginal = estadoOriginal;
    }

    public EstadosDocumentoAdministrativo getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(EstadosDocumentoAdministrativo estadoFinal) {
        this.estadoFinal = estadoFinal;
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

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Date getFechaHoraRespuesta() {
        return fechaHoraRespuesta;
    }

    public void setFechaHoraRespuesta(Date fechaHoraRespuesta) {
        this.fechaHoraRespuesta = fechaHoraRespuesta;
    }

    public Usuarios getUsuarioRespuesta() {
        return usuarioRespuesta;
    }

    public void setUsuarioRespuesta(Usuarios usuarioRespuesta) {
        this.usuarioRespuesta = usuarioRespuesta;
    }

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
    }

    public TransferenciasDocumentoAdministrativo getCambioEstadoDocumento() {
        return cambioEstadoDocumento;
    }

    public void setCambioEstadoDocumento(TransferenciasDocumentoAdministrativo cambioEstadoDocumento) {
        this.cambioEstadoDocumento = cambioEstadoDocumento;
    }
    
    @XmlTransient
    public String getRespuestaString(){
        if(respuesta != null){
            if("NO".equals(respuesta)){
                return "RECHAZADO";
            }else if("SI".equals(respuesta)){
                return "APROBADO";
            }
        }
        
        return "PENDIENTE";
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
        if (!(object instanceof CambiosEstadoDocumentoAdministrativoPendientes)) {
            return false;
        }
        CambiosEstadoDocumentoAdministrativoPendientes other = (CambiosEstadoDocumentoAdministrativoPendientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.CambiosEstadoDocumentoAdministrativoPendientes[ id=" + id + " ]";
    }
    
}
