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
 * @author eduardo
 */
@Entity
@Table(name = "documentos_judiciales_preliminares")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosJudicialesPreliminares.findAll", query = "SELECT d FROM DocumentosJudicialesPreliminares d ORDER BY d.fechaHoraAlta desc")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedDpto", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaPresentacion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedFechaAltaDpto", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrdered", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta ORDER BY d.fechaPresentacion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedAsignado", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedAsignadoAll", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedFechaAlta", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedFechaAltaAll", query = "SELECT d FROM DocumentosJudicialesPreliminares d ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedFechaAltaAsignado", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findOrderedFechaAltaAsignadoAll", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findById", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByEntradaDocumentoMaxDoc", query = "SELECT c FROM DocumentosJudicialesPreliminares c WHERE c.id in (SELECT MAX(d.id) FROM DocumentosJudiciales d)")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByDescripcionMesaEntrada", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.descripcionMesaEntrada = :descripcionMesaEntrada")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByNroExpediente", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.nroExpediente = :nroExpediente")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByCaratula", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.caratula = :caratula")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByFolios", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.folios = :folios")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByMotivoProceso", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.motivoProceso = :motivoProceso")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByFechaHoraAlta", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudicialesPreliminares.findByFechaHoraUltimoEstado", query = "SELECT d FROM DocumentosJudicialesPreliminares d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DocumentosJudicialesPreliminares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "estado_procesal_documento_judicial_preliminar", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosProcesalesDocumentosJudicialesPreliminares estadoProcesalDocumentoJudicialPreliminar;
    @JoinColumn(name = "observacion_documento_judicial_preliminar", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosJudicialesPreliminares observacionDocumentoJudicialPreliminar;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion_mesa_entrada")
    private String descripcionMesaEntrada;
    @Basic(optional = true)
    @Size(max = 40)
    @Column(name = "nro_expediente")
    private String nroExpediente;
    @Lob
    @Size(max = 65535)
    @Column(name = "causa")
    private String causa;
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula")
    private String caratula;
    @Lob
    @Size(max = 65535)
    @Column(name = "ultima_observacion")
    private String ultimaObservacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "estado_procesal")
    private String estadoProcesal;
    @Transient
    private String ultimaObservacionAux;
    @Basic(optional = true)
    @Column(name = "fecha_ultima_observacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaObservacion;
    @Transient
    private String estadoProcesalAux;
    @Basic(optional = true)
    @Column(name = "fecha_hora_estado_procesal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEstadoProcesal;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "folios")
    private String folios;
    @Lob
    @Size(max = 65535)
    @Column(name = "motivo_proceso")
    private String motivoProceso;
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Basic(optional = true)
    @Column(name = "documento_escaneado_preliminar")
    private Integer documentoEscaneadoPreliminar;
    @Basic(optional = true)
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPresentacion;
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
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimaObservacion;
    @JoinColumn(name = "usuario_estado_procesal", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioEstadoProcesal;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "estado_proceso_preliminar", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosProcesoPreliminar estadoProcesoPreliminar;

    public DocumentosJudicialesPreliminares() {
    }

    public DocumentosJudicialesPreliminares(Integer id) {
        this.id = id;
    }

    public DocumentosJudicialesPreliminares(Integer id, String descripcionMesaEntrada, String nroExpediente, String folios, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcionMesaEntrada = descripcionMesaEntrada;
        this.nroExpediente = nroExpediente;
        this.folios = folios;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EstadosProcesalesDocumentosJudicialesPreliminares getEstadoProcesalDocumentoJudicialPreliminar() {
        return estadoProcesalDocumentoJudicialPreliminar;
    }

    public void setEstadoProcesalDocumentoJudicialPreliminar(EstadosProcesalesDocumentosJudicialesPreliminares estadoProcesalDocumentoJudicialPreliminar) {
        this.estadoProcesalDocumentoJudicialPreliminar = estadoProcesalDocumentoJudicialPreliminar;
    }

    public ObservacionesDocumentosJudicialesPreliminares getObservacionDocumentoJudicialPreliminar() {
        return observacionDocumentoJudicialPreliminar;
    }

    public void setObservacionDocumentoJudicialPreliminar(ObservacionesDocumentosJudicialesPreliminares observacionDocumentoJudicialPreliminar) {
        this.observacionDocumentoJudicialPreliminar = observacionDocumentoJudicialPreliminar;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    @XmlTransient
    public String getEstadoProcesalString() {
        if( estadoProcesal != null){
            return estadoProcesal.replace("\n", "<br />");
        }else{
            return "";
        }
    }
    
    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public String getUltimaObservacionAux() {
        return ultimaObservacion;
    }

    public void setUltimaObservacionAux(String ultimaObservacionAux) {
        this.ultimaObservacionAux = ultimaObservacionAux;
    }


    public String getEstadoProcesalAux() {
        return estadoProcesal;
    }

    public void setEstadoProcesalAux(String estadoProcesalAux) {
        this.estadoProcesalAux = estadoProcesalAux;
    }

    public Date getFechaHoraEstadoProcesal() {
        return fechaHoraEstadoProcesal;
    }

    public void setFechaHoraEstadoProcesal(Date fechaHoraEstadoProcesal) {
        this.fechaHoraEstadoProcesal = fechaHoraEstadoProcesal;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }
    
    @XmlTransient
    public boolean verifObs(){
        if((ultimaObservacion == null &&  ultimaObservacionAux != null) || (ultimaObservacion != null &&  ultimaObservacionAux == null)){
            return true;
        }else if(ultimaObservacion == null &&  ultimaObservacionAux == null){
            return false;
        }
        
        return !ultimaObservacion.equals(ultimaObservacionAux);
    }
    
    @XmlTransient
    public void transferirObs(){
        ultimaObservacion = ultimaObservacionAux;
    }

    @XmlTransient
    public boolean verifEstadoProcesal(){
        if((estadoProcesal == null &&  estadoProcesalAux != null) || (estadoProcesal != null &&  estadoProcesalAux == null)){
            return true;
        }else if(estadoProcesal == null &&  estadoProcesalAux == null){
            return false;
        }
        
        return !estadoProcesal.equals(estadoProcesalAux);
    }
    
    @XmlTransient
    public void transferirEstadoProcesal(){
        estadoProcesal = estadoProcesalAux;
    }

    public Usuarios getUsuarioEstadoProcesal() {
        return usuarioEstadoProcesal;
    }

    public void setUsuarioEstadoProcesal(Usuarios usuarioEstadoProcesal) {
        this.usuarioEstadoProcesal = usuarioEstadoProcesal;
    }
    
    public Usuarios getUsuarioUltimaObservacion() {
        return usuarioUltimaObservacion;
    }

    public void setUsuarioUltimaObservacion(Usuarios usuarioUltimaObservacion) {
        this.usuarioUltimaObservacion = usuarioUltimaObservacion;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    @XmlTransient
    public String getUltimaObservacionString() {
        if( ultimaObservacion != null){
            return ultimaObservacion.replace("\n", "<br />");
        }else{
            return "";
        }
    }
    
    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Date getFechaUltimaObservacion() {
        return fechaUltimaObservacion;
    }

    public void setFechaUltimaObservacion(Date fechaUltimaObservacion) {
        this.fechaUltimaObservacion = fechaUltimaObservacion;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    @XmlTransient
    public String setDescripcionMesaEntradaString() {
        if(descripcionMesaEntrada != null){
            return descripcionMesaEntrada.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getCaratula() {
        return caratula;
    }

    @XmlTransient
    public String getCaratulaString() {
        if( caratula != null ){
            return caratula.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public String getMotivoProceso() {
        return motivoProceso;
    }


    @XmlTransient
    public String getMotivoProcesoString() {
        if(motivoProceso != null){
            return motivoProceso.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public void setMotivoProceso(String motivoProceso) {
        this.motivoProceso = motivoProceso;
    }

    public Integer getDocumentoEscaneadoPreliminar() {
        return documentoEscaneadoPreliminar;
    }

    public void setDocumentoPreliminar(Integer documentoEscaneadoPreliminar) {
        this.documentoEscaneadoPreliminar = documentoEscaneadoPreliminar;
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

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public EstadosProcesoPreliminar getEstadoProcesoPreliminar() {
        return estadoProcesoPreliminar;
    }

    public void setEstadoProcesoPreliminar(EstadosProcesoPreliminar estadoProcesoPreliminar) {
        this.estadoProcesoPreliminar = estadoProcesoPreliminar;
    }

    @XmlTransient
    public String getTieneDocumento() {
        if (documentoEscaneadoPreliminar == null) {
            return "NO";
        } else {
            return "SI";
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
        if (!(object instanceof DocumentosJudicialesPreliminares)) {
            return false;
        }
        DocumentosJudicialesPreliminares other = (DocumentosJudicialesPreliminares) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.DocumentosJudicialesPreliminares[ id=" + id + " ]";
    }
    
}
