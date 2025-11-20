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
@Table(name = "documentos_resoluciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosResoluciones.findAll", query = "SELECT d FROM DocumentosResoluciones d ORDER BY d.fechaHoraAlta desc")
    , @NamedQuery(name = "DocumentosResoluciones.findByCanalEntradaDocumentoJudicial", query = "SELECT d FROM DocumentosResoluciones d WHERE (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaResolucion DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findByAtencion", query = "SELECT d FROM DocumentosResoluciones d WHERE d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.responsable = :responsable AND d.estado.codigo NOT IN ('2','4','6','8','10','12','14','16','18') ORDER BY d.fechaResolucion DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedDpto", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaResolucion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findByDescripcionMesaEntradaOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosResoluciones d WHERE d.descripcion like :descripcion AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findByNroMesaEntradaOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosResoluciones d WHERE d.entradaDocumento.nroIdentificacion like :nroIdentificacion AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")
     ,@NamedQuery(name = "DocumentosResoluciones.findOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")

    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaDpto", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrdered", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaResolucion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedAsignado", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedAsignado2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedAsignadoAll", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedAsignadoAll2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaResolucion >= :fechaDesde AND d.fechaResolucion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAlta", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAll", query = "SELECT d FROM DocumentosResoluciones d WHERE d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignado", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignado2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignado3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignadoAll", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignadoAll2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedFechaAltaAsignadoAll3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedDescripcionMesaEntradaAsignado2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.descripcion like :descripcion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedDescripcionMesaEntradaAsignado3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.descripcion like :descripcion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedDescripcionMesaEntradaAsignadoAll2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.descripcion like :descripcion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedDescripcionMesaEntradaAsignadoAll3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.descripcion like :descripcion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedNroMesaEntradaAsignado2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.entradaDocumento.nroIdentificacion like :nroIdentificacion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedNroMesaEntradaAsignado3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.entradaDocumento.nroIdentificacion like :nroIdentificacion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedNroMesaEntradaAsignadoAll2", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.entradaDocumento.nroIdentificacion like :nroIdentificacion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findOrderedNroMesaEntradaAsignadoAll3", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta > :fechaInicio AND d.entradaDocumento.nroIdentificacion like :nroIdentificacion AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosResoluciones.findById", query = "SELECT d FROM DocumentosResoluciones d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentosResoluciones.findByEntradaDocumento", query = "SELECT d FROM DocumentosResoluciones d WHERE d.entradaDocumento = :entradaDocumento")
    , @NamedQuery(name = "DocumentosResoluciones.findByEntradaDocumentoMaxDoc", query = "SELECT c FROM DocumentosResoluciones c WHERE c.id in (SELECT MAX(d.id) FROM DocumentosResoluciones d WHERE d.entradaDocumento = :entradaDocumento)")
    , @NamedQuery(name = "DocumentosResoluciones.findByDescripcion", query = "SELECT d FROM DocumentosResoluciones d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "DocumentosResoluciones.findByNroMesaEntradaJudicial", query = "SELECT d FROM DocumentosResoluciones d WHERE d.tipoDocumentoJudicial.codigo = 'JU' AND d.entradaDocumento.nroIdentificacion = :nroIdentificacion")
    , @NamedQuery(name = "DocumentosResoluciones.findByNroMesaEntradaAdministrativa", query = "SELECT d FROM DocumentosResoluciones d WHERE d.tipoDocumentoJudicial.codigo = 'AD' AND d.entradaDocumento.nroIdentificacion = :nroIdentificacion")
    , @NamedQuery(name = "DocumentosResoluciones.findByCaratula", query = "SELECT d FROM DocumentosResoluciones d WHERE d.tituloResolucion = :tituloResolucion")
    , @NamedQuery(name = "DocumentosResoluciones.findByCaratulaFecha", query = "SELECT d FROM DocumentosResoluciones d WHERE d.tituloResolucion like :tituloResolucion and d.fechaResolucion between :fechaDesde and :fechaHasta")
    , @NamedQuery(name = "DocumentosResoluciones.findByFechaHoraAlta", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DocumentosResoluciones.findByFechaHoraUltimoEstado", query = "SELECT d FROM DocumentosResoluciones d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DocumentosResoluciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entrada_documento", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private EntradasDocumentosResolucion entradaDocumento;
    @JoinColumn(name = "estado_procesal_documento_judicial", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosProcesalesDocumentosJudiciales estadoProcesalDocumentoJudicial;
    @JoinColumn(name = "observacion_documento_judicial", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosJudiciales observacionDocumentoJudicial;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = true)
    @NotNull
    @Size(max = 50)
    @Column(name = "nro_resolucion")
    private String nroResolucion;
    @Lob
    @Size(max = 65535)
    @Column(name = "titulo_resolucion")
    private String tituloResolucion;
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
    @Transient
    private String ultimaObservacionAntecedenteAux;
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
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Basic(optional = true)
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.DATE)
    private Date fechaResolucion;
     @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
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
    @JoinColumn(name = "tipo_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    @JoinColumn(name = "canal_entrada_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial;
    @JoinColumn(name = "subcategoria_documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial;
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
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosDocumento estado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosProceso estadoProceso;
    @JoinColumn(name = "estado_proceso_anterior", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosProceso estadoProcesoAnterior;
    @JoinColumn(name = "estado_anterior", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosDocumento estadoAnterior;
    @JoinColumn(name = "departamento_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoAnterior;
    @JoinColumn(name = "responsable_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableAnterior;
    
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "documentosResolucion")
    private Collection<Archivos> archivos;
    
    @Transient
    private boolean seleccionado;

    public DocumentosResoluciones() {
    }

    public DocumentosResoluciones(Integer id) {
        this.id = id;
    }

    public DocumentosResoluciones(Integer id, EntradasDocumentosResolucion entradaDocumento, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.entradaDocumento = entradaDocumento;
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

    public SubcategoriasDocumentosJudiciales getSubcategoriaDocumentoJudicial() {
        return subcategoriaDocumentoJudicial;
    }

    public void setSubcategoriaDocumentoJudicial(SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial) {
        this.subcategoriaDocumentoJudicial = subcategoriaDocumentoJudicial;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public CanalesEntradaDocumentoJudicial getCanalEntradaDocumentoJudicial() {
        return canalEntradaDocumentoJudicial;
    }

    public void setCanalEntradaDocumentoJudicial(CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial) {
        this.canalEntradaDocumentoJudicial = canalEntradaDocumentoJudicial;
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

    public String getUltimaObservacionAntecedenteAux() {
        return ultimaObservacion;
    }

    public void setUltimaObservacionAntecedenteAux(String ultimaObservacionAntecedenteAux) {
        this.ultimaObservacionAntecedenteAux = ultimaObservacionAntecedenteAux;
    }


    public String getEstadoProcesalAux() {
        return estadoProcesal;
    }

    public void setEstadoProcesalAux(String estadoProcesalAux) {
        this.estadoProcesalAux = estadoProcesalAux;
    }
    
    public EstadosProcesalesDocumentosJudiciales getEstadoProcesalDocumentoJudicial() {
        return estadoProcesalDocumentoJudicial;
    }

    public void setEstadoProcesalDocumentoJudicial(EstadosProcesalesDocumentosJudiciales estadoProcesalDocumentoJudicial) {
        this.estadoProcesalDocumentoJudicial = estadoProcesalDocumentoJudicial;
    }

    public Date getFechaHoraEstadoProcesal() {
        return fechaHoraEstadoProcesal;
    }

    public void setFechaHoraEstadoProcesal(Date fechaHoraEstadoProcesal) {
        this.fechaHoraEstadoProcesal = fechaHoraEstadoProcesal;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Collection<Archivos> getArchivos() {
        return archivos;
    }

    public void setArchivos(Collection<Archivos> archivos) {
        this.archivos = archivos;
    }
    
    @XmlTransient
    public boolean verifObs(){
        if(ultimaObservacion == null &&  ultimaObservacionAux != null){
            return true;
        }else if(ultimaObservacion != null &&  ultimaObservacionAux == null){
            return false;
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
        //if((estadoProcesal == null &&  estadoProcesalAux != null) || (estadoProcesal != null &&  estadoProcesalAux == null)){
        if(estadoProcesal == null &&  estadoProcesalAux != null){
            return true;
        }else if(estadoProcesal != null &&  estadoProcesalAux == null){
            return false;
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
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
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

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

   

    public EntradasDocumentosResolucion getEntradaDocumento() {
        return entradaDocumento;
    }

    public void setEntradaDocumento(EntradasDocumentosResolucion entradaDocumento) {
        this.entradaDocumento = entradaDocumento;
    }

    public ObservacionesDocumentosJudiciales getObservacionDocumentoJudicial() {
        return observacionDocumentoJudicial;
    }

    public void setObservacionDocumentoJudicial(ObservacionesDocumentosJudiciales observacionDocumentoJudicial) {
        this.observacionDocumentoJudicial = observacionDocumentoJudicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }
    

   

    @XmlTransient
    public String setDescripcionString() {
        if(descripcion != null){
            return descripcion.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public String getTituloResolucion() {
        return tituloResolucion;
    }

    public void setTituloResolucion(String tituloResolucion) {
        this.tituloResolucion = tituloResolucion;
    }


    @XmlTransient
    public String getTituloResolucionString() {
        if( tituloResolucion != null ){
            return tituloResolucion.replace("\n", "<br />");
        }else{
            return "";
        }
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

    public EstadosDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumento estado) {
        this.estado = estado;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public EstadosProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadosProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public EstadosProceso getEstadoProcesoAnterior() {
        return estadoProcesoAnterior;
    }

    public void setEstadoProcesoAnterior(EstadosProceso estadoProcesoAnterior) {
        this.estadoProcesoAnterior = estadoProcesoAnterior;
    }

    public EstadosDocumento getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadosDocumento estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public Departamentos getDepartamentoAnterior() {
        return departamentoAnterior;
    }

    public void setDepartamentoAnterior(Departamentos departamentoAnterior) {
        this.departamentoAnterior = departamentoAnterior;
    }

    public Usuarios getResponsableAnterior() {
        return responsableAnterior;
    }

    public void setResponsableAnterior(Usuarios responsableAnterior) {
        this.responsableAnterior = responsableAnterior;
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
        if (!(object instanceof DocumentosResoluciones)) {
            return false;
        }
        DocumentosResoluciones other = (DocumentosResoluciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.DocumentosResoluciones[ id=" + id + " ]";
    }
    
}
