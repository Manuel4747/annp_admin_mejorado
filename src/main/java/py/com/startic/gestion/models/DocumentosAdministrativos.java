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
import py.com.startic.gestion.controllers.Constantes;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "documentos_administrativos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosAdministrativos.findAll", query = "SELECT d FROM DocumentosAdministrativos d ORDER BY d.fechaHoraAlta desc")
    , @NamedQuery(name = "DocumentosAdministrativos.findByCanalEntradaDocumentoAdministrativo", query = "SELECT d FROM DocumentosAdministrativos d WHERE (d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo OR d.departamento = :departamento) ORDER BY d.fechaPresentacion DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findByCausa", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.causa = :causa ORDER BY d.fechaPresentacion DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findByAtencion", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.responsable = :responsable AND d.estado.codigo NOT IN ('2','4','6','8','10','12','14','16','18') ORDER BY d.fechaPresentacion DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedDpto", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND (d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo OR d.departamento = :departamento) ORDER BY d.fechaPresentacion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo AND (d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaDpto", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrdered", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo ORDER BY d.fechaPresentacion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedAsignado", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedAsignado2", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumento AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedAsignadoAll", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedAsignadoAll2", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAlta", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAll", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignado", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignado2", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumentoAdministrativo AND d.departamento = :departamento and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignado3", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumentoAdministrativo AND d.departamento = :departamento and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignadoAll", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignadoAll2", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumentoAdministrativo and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findOrderedFechaAltaAsignadoAll3", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoAdministrativo = :canalEntradaDocumentoAdministrativo AND d.tipoDocumentoAdministrativo IN :tiposDocumentoAdministrativo and d.estado.tipo = :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosAdministrativos.findById", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentosAdministrativos.findByDocumentoAdministrativo", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.documentoAdministrativo = :documentoAdministrativo")
    , @NamedQuery(name = "DocumentosAdministrativos.findByEntradaDocumento", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.entradaDocumentoAdministrativo = :entradaDocumento")
    , @NamedQuery(name = "DocumentosAdministrativos.findByEntradaDocumentoMaxDoc", query = "SELECT c FROM DocumentosAdministrativos c WHERE c.id in (SELECT MAX(d.id) FROM DocumentosAdministrativos d WHERE d.entradaDocumentoAdministrativo = :entradaDocumento)")
    , @NamedQuery(name = "DocumentosAdministrativos.findByDescripcionMesaEntrada", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.descripcionMesaEntrada = :descripcionMesaEntrada")
    , @NamedQuery(name = "DocumentosAdministrativos.findByNroMesaEntradaAdministrativa", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.tipoDocumentoAdministrativo.codigo = 'MM' AND d.entradaDocumentoAdministrativo.nroMesaEntrada = :nroMesaEntrada")
    , @NamedQuery(name = "DocumentosAdministrativos.findByCaratula", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.caratula = :caratula")
    , @NamedQuery(name = "DocumentosAdministrativos.findByMotivoProceso", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.motivoProceso = :motivoProceso")
    , @NamedQuery(name = "DocumentosAdministrativos.findByFechaHoraAlta", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DocumentosAdministrativos.findByFechaPresentacion", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaPresentacion = :fechaPresentacion")
    , @NamedQuery(name = "DocumentosAdministrativos.findByFechaHoraUltimoEstado", query = "SELECT d FROM DocumentosAdministrativos d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DocumentosAdministrativos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entrada_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private EntradasDocumentosAdministrativos entradaDocumentoAdministrativo;
    @JoinColumn(name = "estado_procesal_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosProcesalesDocumentosAdministrativos estadoProcesalDocumentoAdministrativo;
    @JoinColumn(name = "observacion_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosAdministrativos observacionDocumentoAdministrativo;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion_mesa_entrada")
    private String descripcionMesaEntrada;
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
    @Lob
    @Size(max = 65535)
    @Column(name = "motivo_proceso")
    private String motivoProceso;
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Basic(optional = true)
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
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
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraElaboracion;
    @JoinColumn(name = "tipo_documento_administrativo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosAdministrativos tipoDocumentoAdministrativo;
    @JoinColumn(name = "canal_entrada_documento_administrativo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private CanalesEntradaDocumentoAdministrativo canalEntradaDocumentoAdministrativo;
    @JoinColumn(name = "subcategoria_documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo;
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
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @JoinColumn(name = "usuario_elaboracion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioElaboracion;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosDocumentoAdministrativo estado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @Basic(optional = true)
    @Column(name = "plazo")
    private Integer plazo;
    @JoinColumn(name = "estado_anterior", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosDocumentoAdministrativo estadoAnterior;
    @JoinColumn(name = "departamento_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoAnterior;
    @JoinColumn(name = "responsable_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableAnterior;
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosAdministrativos documentoAdministrativo;
    @Column(name = "visto")
    private boolean visto;
    @JoinColumn(name = "usuario_visto", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioVisto;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisto;
    @Basic(optional = true)
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Size(max = 20)
    @Column(name = "nro_final")
    private String nroFinal;
    @Size(max = 20)
    @Column(name = "nomenclatura_final")
    private String nomenclaturaFinal;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_final")
    private String textoFinal;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_archivo")
    private String codigoArchivo;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "hash")
    private String hash;
    /*
    @JoinColumn(name = "tipo_prioridad", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposPrioridad tipoPrioridad;
*/

    public DocumentosAdministrativos() {
    }

    public DocumentosAdministrativos(Integer id) {
        this.id = id;
    }

    public DocumentosAdministrativos(Integer id, EntradasDocumentosAdministrativos entradaDocumentoAdministrativo, String descripcionMesaEntrada, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.entradaDocumentoAdministrativo = entradaDocumentoAdministrativo;
        this.descripcionMesaEntrada = descripcionMesaEntrada;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public String getNroFinalString() {
        if( tipoDocumentoAdministrativo != null){
            return Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_MM.equals(tipoDocumentoAdministrativo.getCodigo())?nroFinal:"";
        }else{
            return "";
        }
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

    public String getUltimaObservacionAntecedenteAux() {
        return ultimaObservacion;
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

    public EntradasDocumentosAdministrativos getEntradaDocumentoAdministrativo() {
        return entradaDocumentoAdministrativo;
    }

    public void setEntradaDocumentoAdministrativo(EntradasDocumentosAdministrativos entradaDocumentoAdministrativo) {
        this.entradaDocumentoAdministrativo = entradaDocumentoAdministrativo;
    }

    public TiposDocumentosAdministrativos getTipoDocumentoAdministrativo() {
        return tipoDocumentoAdministrativo;
    }

    public void setTipoDocumentoAdministrativo(TiposDocumentosAdministrativos tipoDocumentoAdministrativo) {
        this.tipoDocumentoAdministrativo = tipoDocumentoAdministrativo;
    }

    public CanalesEntradaDocumentoAdministrativo getCanalEntradaDocumentoAdministrativo() {
        return canalEntradaDocumentoAdministrativo;
    }

    public void setCanalEntradaDocumentoAdministrativo(CanalesEntradaDocumentoAdministrativo canalEntradaDocumentoAdministrativo) {
        this.canalEntradaDocumentoAdministrativo = canalEntradaDocumentoAdministrativo;
    }

    public SubcategoriasDocumentosAdministrativos getSubcategoriaDocumentoAdministrativo() {
        return subcategoriaDocumentoAdministrativo;
    }

    public void setSubcategoriaDocumentoAdministrativo(SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo) {
        this.subcategoriaDocumentoAdministrativo = subcategoriaDocumentoAdministrativo;
    }

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraElaboracion() {
        return fechaHoraElaboracion;
    }

    public void setFechaHoraElaboracion(Date fechaHoraElaboracion) {
        this.fechaHoraElaboracion = fechaHoraElaboracion;
    }

    public Usuarios getUsuarioElaboracion() {
        return usuarioElaboracion;
    }

    public void setUsuarioElaboracion(Usuarios usuarioElaboracion) {
        this.usuarioElaboracion = usuarioElaboracion;
    }

    public String getNomenclaturaFinal() {
        return nomenclaturaFinal;
    }

    public void setNomenclaturaFinal(String nomenclaturaFinal) {
        this.nomenclaturaFinal = nomenclaturaFinal;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public Usuarios getUsuarioVisto() {
        return usuarioVisto;
    }

    public void setUsuarioVisto(Usuarios usuarioVisto) {
        this.usuarioVisto = usuarioVisto;
    }

    public Date getFechaHoraVisto() {
        return fechaHoraVisto;
    }

    public void setFechaHoraVisto(Date fechaHoraVisto) {
        this.fechaHoraVisto = fechaHoraVisto;
    }

    public String getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(String codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public EstadosProcesalesDocumentosAdministrativos getEstadoProcesalDocumentoAdministrativo() {
        return estadoProcesalDocumentoAdministrativo;
    }

    public void setEstadoProcesalDocumentoAdministrativo(EstadosProcesalesDocumentosAdministrativos estadoProcesalDocumentoAdministrativo) {
        this.estadoProcesalDocumentoAdministrativo = estadoProcesalDocumentoAdministrativo;
    }

    public ObservacionesDocumentosAdministrativos getObservacionDocumentoAdministrativo() {
        return observacionDocumentoAdministrativo;
    }

    public void setObservacionDocumentoAdministrativo(ObservacionesDocumentosAdministrativos observacionDocumentoAdministrativo) {
        this.observacionDocumentoAdministrativo = observacionDocumentoAdministrativo;
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

    public EstadosDocumentoAdministrativo getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumentoAdministrativo estado) {
        this.estado = estado;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public EstadosDocumentoAdministrativo getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadosDocumentoAdministrativo estadoAnterior) {
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

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNroFinal() {
        return nroFinal;
    }

    public void setNroFinal(String nroFinal) {
        this.nroFinal = nroFinal;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }
/*
    public TiposPrioridad getTipoPrioridad() {
        return tipoPrioridad;
    }

    public void setTipoPrioridad(TiposPrioridad tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }
    */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosAdministrativos)) {
            return false;
        }
        DocumentosAdministrativos other = (DocumentosAdministrativos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.DocumentosAdministrativos[ id=" + id + " ]";
    }
    
}
