package py.com.startic.gestion.controllers;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.RootRenderer;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
//import jakarta.imageio.ImageIO;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.poi.util.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;/*
import org.xhtmlrenderer.extend.FontResolver;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;*/
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.CantidadItem;
import py.com.startic.gestion.datasource.Metadatos;
import py.com.startic.gestion.models.AliasesKeystore;
import py.com.startic.gestion.models.ArchivosAdministrativo;
import py.com.startic.gestion.models.TransferenciasDocumentoAdministrativo;
import py.com.startic.gestion.models.CambiosEstadoDocumentoAdministrativoPendientes;
import py.com.startic.gestion.models.CanalesEntradaDocumentoAdministrativo;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosAdministrativosAutoguardados;
import py.com.startic.gestion.models.EntradasDocumentosAdministrativos;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosTransferenciaDocumentoAdministrativo;
import py.com.startic.gestion.models.EstadosDocumentoAdministrativo;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosAdministrativos;
import py.com.startic.gestion.models.ExcepcionesDocumentoAdministrativo;
import py.com.startic.gestion.models.ExpFeriados;
import py.com.startic.gestion.models.FirmasArchivoAdministrativo;
import py.com.startic.gestion.models.FlujosDocumentoAdministrativo;
import py.com.startic.gestion.models.FormatosArchivoAdministrativo;
import py.com.startic.gestion.models.ObservacionesDocumentosAdministrativos;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.SubcategoriasDocumentosAdministrativos;
import py.com.startic.gestion.models.TiposArchivoAdministrativo;
import py.com.startic.gestion.models.TiposArchivoAdministrativoPorDepartamentos;
import py.com.startic.gestion.models.TiposDocumentosAdministrativos;
import py.com.startic.gestion.models.TiposEnvio;
import py.com.startic.gestion.models.TiposPrioridad;
import py.com.startic.gestion.models.Usuarios;
import py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativos;
import py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativosAutoguardados;
import py.com.startic.gestion.models.VistosPorDocumentosAdministrativos;
/*import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;
*/
@Named(value = "documentosAdministrativosController")
@ViewScoped
public class DocumentosAdministrativosController extends AbstractController<DocumentosAdministrativos> {

    @Inject
    UserTransaction ut;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private DocumentosAdministrativosAutoguardadosController documentoAdministrativoAutoguardadoController;
    @Inject
    private UsuariosPorDocumentosAdministrativosAutoguardadosController usuarioPorDocumentoAdministrativoAutoguardadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private TiposDocumentosAdministrativosController tiposDocumentosAdministrativosController;
    @Inject
    private ObservacionesDocumentosAdministrativosController obsController;
    @Inject
    private EstadosProcesalesDocumentosAdministrativosController estadosProcesalesDocumentosAdministrativosController;
    @Inject
    private EntradasDocumentosAdministrativosController entradasDocumentosAdministrativosController;
    @Inject
    private DocumentosEscaneadosController documentosEscaneadosController;
    @Inject
    private TransferenciasDocumentoAdministrativoController transferenciasDocumentoAdministrativoController;
    @Inject
    private CambiosEstadoDocumentoAdministrativoPendientesController cambiosEstadoDocumentoPendientesController;
    @Inject
    private ArchivosAdministrativoController archivosController;
    @Inject
    private EstadosDocumentoAdministrativoController estadosDocumentoController;
    @Inject
    private UsuariosPorDocumentosAdministrativosController usuariosPorDocumentosAdministrativosController;
    @Inject
    private ObservacionesDocumentosAdministrativosController observacionesDocumentosAdministrativosController;
    @Inject
    private TiposArchivoAdministrativoPorDepartamentosController tiposArchivoAdministrativoPorDepartamentosController;
    @Inject
    private AliasesKeystoreController aliasesKeystoreController;
    @Inject
    private FirmasArchivoAdministrativoController firmasController;
    @Inject
    private VistosPorDocumentosAdministrativosController vistoPorDocumentoAdministrativoController;
    private final FiltroURL filtroURL = new FiltroURL();
    private EntradasDocumentosAdministrativos entradaDocumentoAdministrativo;
    private String nuevaCausa;
    private String nombreJuez;
    private String nombreEstado;
    private String ultimaObservacion;
    private CanalesEntradaDocumentoAdministrativo canal;
    //private TiposDocumentosAdministrativos tipoDoc;
    //private TiposDocumentosAdministrativos tipoDoc2;
    private List<TiposDocumentosAdministrativos> tiposDoc;
    private String tiposDocString;
    private Usuarios usuario;
    private Usuarios usuarioBkp;
    // private Departamentos departamento;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaInicio;
    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private FlujosDocumentoAdministrativo flujoDoc;
    private String sgteEstado;
    private Collection<Usuarios> listaUsuariosTransf;
    private Collection<Usuarios> listaUsuariosTransfArchivados;
    private boolean hayDocumentosAtencion;
    private Usuarios responsableDestino;
    private Integer responsableDestinoId;
    private SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
    private List<CambiosEstadoDocumentoAdministrativoPendientes> listaTransfer;
    private CambiosEstadoDocumentoAdministrativoPendientes selectedTransfer;
    private Integer plazo;
    //private List<TransferenciasDocumentoAdministrativo> listaSalida;
    private List<DocumentosAdministrativos> listaSalida;
    private List<DocumentosAdministrativos> listaBorradores;
    private List<DocumentosAdministrativos> listaArchivados;
    private List<DocumentosAdministrativos> listaCC;
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<ArchivosAdministrativo> listaArchivos;
    private List<ArchivosAdministrativo> listaArchivosSalida;
    private DocumentosAdministrativos selectedSalida;
    private DocumentosAdministrativos selectedCC;
    private List<ArchivosAdministrativo> listaArchivosCC;
    private List<ArchivosAdministrativo> listaArchivosBorradores;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatariosBorradores;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatarios;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatariosArchivados;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatariosSalida;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatariosCC;
    // DocumentosAdministrativos selectedBorradores;
    private List<ArchivosAdministrativo> listaArchivosArchivados;
    private DocumentosAdministrativos selectedArchivados;
    private String descripcionArchivo;
    private ParametrosSistema par;
    private ArchivosAdministrativo docImprimir;
    private String content;
    private String nombre;
    private String url;
    private String endpoint;
    private HttpSession session;
    private SimpleDateFormat formatAno = new SimpleDateFormat("YY");
    private String descripcionMesaEntrada;
    private SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo;
    private List<SubcategoriasDocumentosAdministrativos> listaSubcategoriaDocumentoAdministrativo;
    private TiposDocumentosAdministrativos tipoDocumentoAdministrativo;
    private String accion;
    private List<ObservacionesDocumentosAdministrativos> listaObservaciones;
    private List<ObservacionesDocumentosAdministrativos> listaObservacionesSalida;
    private List<ObservacionesDocumentosAdministrativos> listaObservacionesArchivados;
    private List<ObservacionesDocumentosAdministrativos> listaObservacionesBorradores;
    private List<ObservacionesDocumentosAdministrativos> listaObservacionesCC;
    private List<ObservacionesDocumentosAdministrativos> listaObservacionesHistorico;
    private String titulo;
    private ArchivosAdministrativo archivo;
    private List<FormatosArchivoAdministrativo> listaFormatos;
    private List<TiposArchivoAdministrativo> listaTiposArchivo;
    private EstadosTransferenciaDocumentoAdministrativo estadoArchivo;
    private EstadosTransferenciaDocumentoAdministrativo estadoTransDocAdmEnRevision;
    private EstadosTransferenciaDocumentoAdministrativo estadoTransDocAdmEnProyecto;
    private EstadosTransferenciaDocumentoAdministrativo estadoTransDocAdmFinalizada;
    private EstadosDocumentoAdministrativo estadoDocAdmEnProyecto;
    private EstadosDocumentoAdministrativo estadoDocAdmActivo;
    private EstadosDocumentoAdministrativo estadoDocAdmArchivado;
    private List<EstadosTransferenciaDocumentoAdministrativo> listaEstadosArchivo;
    // private String secuencia;
    // private Date fechaFinal;
    // private String textoFinal;
    private final String inicio = "#";
    private final String fin = "#";
    private String mensajeConfirmacion;
    private Usuarios usuarioEnviar;
    private List<Usuarios> listaUsuariosEnviar;
    private List<Usuarios> listaPosiblesUsuariosEnviar;
    private Usuarios usuarioRemitente;
    private List<Usuarios> listaUsuariosRemitente;
    private List<Usuarios> listaPosiblesUsuariosRemitente;
    private Usuarios usuarioCC;
    private List<Usuarios> listaUsuariosCC;
    private List<Usuarios> listaPosiblesUsuariosCC;
    private TiposEnvio tipoEnvioArchivoAdmRemitente;
    private TiposEnvio tipoEnvioArchivoAdmDestinatario;
    private TiposEnvio tipoEnvioArchivoAdmConCopia;
    private String responderA;
    private List<DocumentosAdministrativos> listaHistorico;
    private List<UsuariosPorDocumentosAdministrativos> listaDestinatariosHistorico;
    private List<ArchivosAdministrativo> listaArchivosHistorico;
    private DocumentosAdministrativos selectedHistorico;
    private String contrasenaAlias;
    private String nuevoEstado;
    private UsuariosPorDocumentosAdministrativos usuariosPorDocumentosAdministrativosActual;
    private Collection<Usuarios> listaPosiblesUsuariosDevolver;
    private Usuarios usuarioDevolver;
    private String sessionId;
    private String firmaId;
    private List<FirmasArchivoAdministrativo> listaFirmasAdministrativas;
    private String leyendaResponderReenviar;
    private DocumentosAdministrativos docResponder;
    private String nomenclaturaMemo;
    private List<TiposArchivoAdministrativoPorDepartamentos> listaTiposArchivoAdministrativoPorDepartamentos;
    private TiposArchivoAdministrativoPorDepartamentos tiposArchivoAdministrativoPorDepartamentosSelected;
    private String tieneToken;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String nombreBandeja;
    private Usuarios usuarioBandeja;
    private boolean esResponder;
    private Date fechaIniVistoCC;
    private Integer activeTab;
    // private TiposPrioridad tipoPrioridad;

    private boolean busquedaPorFechaAlta;
    private boolean editando;
    private boolean autoGuardado;

    private UploadedFile file;

    private UploadedFiles files;
    private DocumentosAdministrativos selectedVerificar;

    private List<UploadedFile> listaFiles;

    public DocumentosAdministrativos getSelectedVerificar() {
        return selectedVerificar;
    }

    public void setSelectedVerificar(DocumentosAdministrativos selectedVerificar) {
        this.selectedVerificar = selectedVerificar;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    /*
    public TiposPrioridad getTipoPrioridad() {
        return tipoPrioridad;
    }

    public void setTipoPrioridad(TiposPrioridad tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }
     */
    public String getNombreBandeja() {
        return nombreBandeja;
    }

    public void setNombreBandeja(String nombreBandeja) {
        this.nombreBandeja = nombreBandeja;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getTieneToken() {
        return tieneToken;
    }

    public void setTieneToken(String tieneToken) {
        this.tieneToken = tieneToken;
    }

    public TiposArchivoAdministrativoPorDepartamentos getTiposArchivoAdministrativoPorDepartamentosSelected() {
        return tiposArchivoAdministrativoPorDepartamentosSelected;
    }

    public void setTiposArchivoAdministrativoPorDepartamentosSelected(TiposArchivoAdministrativoPorDepartamentos tiposArchivoAdministrativoPorDepartamentosSelected) {
        this.tiposArchivoAdministrativoPorDepartamentosSelected = tiposArchivoAdministrativoPorDepartamentosSelected;
    }

    public List<TiposArchivoAdministrativoPorDepartamentos> getListaTiposArchivoAdministrativoPorDepartamentos() {
        return listaTiposArchivoAdministrativoPorDepartamentos;
    }

    public void setListaTiposArchivoAdministrativoPorDepartamentos(List<TiposArchivoAdministrativoPorDepartamentos> listaTiposArchivoAdministrativoPorDepartamentos) {
        this.listaTiposArchivoAdministrativoPorDepartamentos = listaTiposArchivoAdministrativoPorDepartamentos;
    }

    public String getNomenclaturaMemo() {
        return nomenclaturaMemo;
    }

    public void setNomenclaturaMemo(String nomenclaturaMemo) {
        this.nomenclaturaMemo = nomenclaturaMemo;
    }

    public String getLeyendaResponderReenviar() {
        return leyendaResponderReenviar;
    }

    public void setLeyendaResponderReenviar(String leyendaResponderReenviar) {
        this.leyendaResponderReenviar = leyendaResponderReenviar;
    }

    public List<UploadedFile> getListaFiles() {
        return listaFiles;
    }

    public void setListaFiles(List<UploadedFile> listaFiles) {
        this.listaFiles = listaFiles;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservacionesCC() {
        return listaObservacionesCC;
    }

    public void setListaObservacionesCC(List<ObservacionesDocumentosAdministrativos> listaObservacionesCC) {
        this.listaObservacionesCC = listaObservacionesCC;
    }

    public Usuarios getUsuarioDevolver() {
        return usuarioDevolver;
    }

    public void setUsuarioDevolver(Usuarios usuarioDevolver) {
        this.usuarioDevolver = usuarioDevolver;
    }

    public Collection<Usuarios> getListaPosiblesUsuariosDevolver() {
        return listaPosiblesUsuariosDevolver;
    }

    public void setListaPosiblesUsuariosDevolver(Collection<Usuarios> listaPosiblesUsuariosDevolver) {
        this.listaPosiblesUsuariosDevolver = listaPosiblesUsuariosDevolver;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getContrasenaAlias() {
        return contrasenaAlias;
    }

    public void setContrasenaAlias(String contrasenaAlias) {
        this.contrasenaAlias = contrasenaAlias;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservacionesHistorico() {
        return listaObservacionesHistorico;
    }

    public void setListaObservacionesHistorico(List<ObservacionesDocumentosAdministrativos> listaObservacionesHistorico) {
        this.listaObservacionesHistorico = listaObservacionesHistorico;
    }

    public DocumentosAdministrativos getSelectedHistorico() {
        return selectedHistorico;
    }

    public void setSelectedHistorico(DocumentosAdministrativos selectedHistorico) {
        this.selectedHistorico = selectedHistorico;
    }

    public List<DocumentosAdministrativos> getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List<DocumentosAdministrativos> listaHistorico) {
        this.listaHistorico = listaHistorico;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatariosHistorico() {
        return listaDestinatariosHistorico;
    }

    public void setListaDestinatariosHistorico(List<UsuariosPorDocumentosAdministrativos> listaDestinatariosHistorico) {
        this.listaDestinatariosHistorico = listaDestinatariosHistorico;
    }

    public List<ArchivosAdministrativo> getListaArchivosHistorico() {
        return listaArchivosHistorico;
    }

    public void setListaArchivosHistorico(List<ArchivosAdministrativo> listaArchivosHistorico) {
        this.listaArchivosHistorico = listaArchivosHistorico;
    }

    public String getResponderA() {
        return responderA;
    }

    public void setResponderA(String responderA) {
        this.responderA = responderA;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatariosArchivados() {
        return listaDestinatariosArchivados;
    }

    public void setListaDestinatariosArchivados(List<UsuariosPorDocumentosAdministrativos> listaDestinatariosArchivados) {
        this.listaDestinatariosArchivados = listaDestinatariosArchivados;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatariosSalida() {
        return listaDestinatariosSalida;
    }

    public void setListaDestinatariosSalida(List<UsuariosPorDocumentosAdministrativos> listaDestinatariosSalida) {
        this.listaDestinatariosSalida = listaDestinatariosSalida;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatariosCC() {
        return listaDestinatariosCC;
    }

    public void setListaDestinatariosCC(List<UsuariosPorDocumentosAdministrativos> listaDestinatariosCC) {
        this.listaDestinatariosCC = listaDestinatariosCC;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatarios() {
        return listaDestinatarios;
    }

    public void setListaDestinatarios(List<UsuariosPorDocumentosAdministrativos> listaDestinatarios) {
        this.listaDestinatarios = listaDestinatarios;
    }

    public List<UsuariosPorDocumentosAdministrativos> getListaDestinatariosBorradores() {
        return listaDestinatariosBorradores;
    }

    public void setListaDestinatariosBorradores(List<UsuariosPorDocumentosAdministrativos> listaDestinatariosBorradores) {
        this.listaDestinatariosBorradores = listaDestinatariosBorradores;
    }

    public List<ArchivosAdministrativo> getListaArchivosCC() {
        return listaArchivosCC;
    }

    public void setListaArchivosCC(List<ArchivosAdministrativo> listaArchivosCC) {
        this.listaArchivosCC = listaArchivosCC;
    }

    public DocumentosAdministrativos getSelectedCC() {
        return selectedCC;
    }

    public void setSelectedCC(DocumentosAdministrativos selectedCC) {
        this.selectedCC = selectedCC;
    }

    public List<DocumentosAdministrativos> getListaCC() {
        return listaCC;
    }

    public void setListaCC(List<DocumentosAdministrativos> listaCC) {
        this.listaCC = listaCC;
    }

    public List<Usuarios> getListaUsuariosEnviar() {
        return listaUsuariosEnviar;
    }

    public void setListaUsuariosEnviar(List<Usuarios> listaUsuariosEnviar) {
        this.listaUsuariosEnviar = listaUsuariosEnviar;
    }

    public List<Usuarios> getListaUsuariosRemitente() {
        return listaUsuariosRemitente;
    }

    public void setListaUsuariosRemitente(List<Usuarios> listaUsuariosRemitente) {
        this.listaUsuariosRemitente = listaUsuariosRemitente;
    }

    public TiposEnvio getTipoEnvioArchivoAdmRemitente() {
        return tipoEnvioArchivoAdmRemitente;
    }

    public void setTipoEnvioArchivoAdmRemitente(TiposEnvio tipoEnvioArchivoAdmRemitente) {
        this.tipoEnvioArchivoAdmRemitente = tipoEnvioArchivoAdmRemitente;
    }

    public TiposEnvio getTipoEnvioArchivoAdmDestinatario() {
        return tipoEnvioArchivoAdmDestinatario;
    }

    public void setTipoEnvioArchivoAdmDestinatario(TiposEnvio tipoEnvioArchivoAdmDestinatario) {
        this.tipoEnvioArchivoAdmDestinatario = tipoEnvioArchivoAdmDestinatario;
    }

    public TiposEnvio getTipoEnvioArchivoAdmConCopia() {
        return tipoEnvioArchivoAdmConCopia;
    }

    public void setTipoEnvioArchivoAdmConCopia(TiposEnvio tipoEnvioArchivoAdmConCopia) {
        this.tipoEnvioArchivoAdmConCopia = tipoEnvioArchivoAdmConCopia;
    }

    public UploadedFiles getFiles() {
        return files;
    }

    public void setFiles(UploadedFiles files) {
        this.files = files;
    }

    public Usuarios getUsuarioRemitente() {
        return usuarioRemitente;
    }

    public void setUsuarioRemitente(Usuarios usuarioRemitente) {
        this.usuarioRemitente = usuarioRemitente;
    }

    public List<Usuarios> getListaPosiblesUsuariosRemitente() {
        return listaPosiblesUsuariosRemitente;
    }

    public void setListaPosiblesUsuariosRemitente(List<Usuarios> listaPosiblesUsuariosRemitente) {
        this.listaPosiblesUsuariosRemitente = listaPosiblesUsuariosRemitente;
    }

    public Usuarios getUsuarioCC() {
        return usuarioCC;
    }

    public void setUsuarioCC(Usuarios usuarioCC) {
        this.usuarioCC = usuarioCC;
    }

    public List<Usuarios> getListaUsuariosCC() {
        return listaUsuariosCC;
    }

    public void setListaUsuariosCC(List<Usuarios> listaUsuariosCC) {
        this.listaUsuariosCC = listaUsuariosCC;
    }

    public List<Usuarios> getListaPosiblesUsuariosCC() {
        return listaPosiblesUsuariosCC;
    }

    public void setListaPosiblesUsuariosCC(List<Usuarios> listaPosiblesUsuariosCC) {
        this.listaPosiblesUsuariosCC = listaPosiblesUsuariosCC;
    }

    public List<Usuarios> getListaPosiblesUsuariosEnviar() {
        return listaPosiblesUsuariosEnviar;
    }

    public void setListaPosiblesUsuariosEnviar(List<Usuarios> listaPosiblesUsuariosEnviar) {
        this.listaPosiblesUsuariosEnviar = listaPosiblesUsuariosEnviar;
    }

    public Usuarios getUsuarioEnviar() {
        return usuarioEnviar;
    }

    public void setUsuarioEnviar(Usuarios usuarioEnviar) {
        this.usuarioEnviar = usuarioEnviar;
    }

    public String getMensajeConfirmacion() {
        return mensajeConfirmacion;
    }

    public void setMensajeConfirmacion(String mensajeConfirmacion) {
        this.mensajeConfirmacion = mensajeConfirmacion;
    }

    public SubcategoriasDocumentosAdministrativos getSubcategoriaDocumentoAdministrativo() {
        return subcategoriaDocumentoAdministrativo;
    }

    public void setSubcategoriaDocumentoAdministrativo(SubcategoriasDocumentosAdministrativos subcategoriaDocumentoAdministrativo) {
        this.subcategoriaDocumentoAdministrativo = subcategoriaDocumentoAdministrativo;
    }

    public List<DocumentosAdministrativos> getListaBorradores() {
        return listaBorradores;
    }

    public void setListaBorradores(List<DocumentosAdministrativos> listaBorradores) {
        this.listaBorradores = listaBorradores;
    }

    public List<ArchivosAdministrativo> getListaArchivosBorradores() {
        return listaArchivosBorradores;
    }

    public void setListaArchivosBorradores(List<ArchivosAdministrativo> listaArchivosBorradores) {
        this.listaArchivosBorradores = listaArchivosBorradores;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservacionesBorradores() {
        return listaObservacionesBorradores;
    }

    public void setListaObservacionesBorradores(List<ObservacionesDocumentosAdministrativos> listaObservacionesBorradores) {
        this.listaObservacionesBorradores = listaObservacionesBorradores;
    }

    public List<EstadosTransferenciaDocumentoAdministrativo> getListaEstadosArchivo() {
        return listaEstadosArchivo;
    }

    public void setListaEstadosArchivo(List<EstadosTransferenciaDocumentoAdministrativo> listaEstadosArchivo) {
        this.listaEstadosArchivo = listaEstadosArchivo;
    }

    public EstadosTransferenciaDocumentoAdministrativo getEstadoArchivo() {
        return estadoArchivo;
    }

    public void setEstadoArchivo(EstadosTransferenciaDocumentoAdministrativo estadoArchivo) {
        this.estadoArchivo = estadoArchivo;
    }

    public List<TiposArchivoAdministrativo> getListaTiposArchivo() {
        return listaTiposArchivo;
    }

    public void setListaTiposArchivo(List<TiposArchivoAdministrativo> listaTiposArchivo) {
        this.listaTiposArchivo = listaTiposArchivo;
    }

    public List<FormatosArchivoAdministrativo> getListaFormatos() {
        return listaFormatos;
    }

    public void setListaFormatos(List<FormatosArchivoAdministrativo> listaFormatos) {
        this.listaFormatos = listaFormatos;
    }

    public ArchivosAdministrativo getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivosAdministrativo archivo) {
        this.archivo = archivo;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservacionesSalida() {
        return listaObservacionesSalida;
    }

    public void setListaObservacionesSalida(List<ObservacionesDocumentosAdministrativos> listaObservacionesSalida) {
        this.listaObservacionesSalida = listaObservacionesSalida;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservacionesArchivados() {
        return listaObservacionesArchivados;
    }

    public void setListaObservacionesArchivados(List<ObservacionesDocumentosAdministrativos> listaObservacionesArchivados) {
        this.listaObservacionesArchivados = listaObservacionesArchivados;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<ObservacionesDocumentosAdministrativos> getListaObservaciones() {
        return listaObservaciones;
    }

    public void setListaObservaciones(List<ObservacionesDocumentosAdministrativos> listaObservaciones) {
        this.listaObservaciones = listaObservaciones;
    }

    public List<SubcategoriasDocumentosAdministrativos> getListaSubcategoriaDocumentoAdministrativo() {
        return listaSubcategoriaDocumentoAdministrativo;
    }

    public void setListaSubcategoriaDocumentoAdministrativo(List<SubcategoriasDocumentosAdministrativos> listaSubcategoriaDocumentoAdministrativo) {
        this.listaSubcategoriaDocumentoAdministrativo = listaSubcategoriaDocumentoAdministrativo;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    public List<DocumentosAdministrativos> getListaArchivados() {
        return listaArchivados;
    }

    public void setListaArchivados(List<DocumentosAdministrativos> listaArchivados) {
        this.listaArchivados = listaArchivados;
    }

    public List<ArchivosAdministrativo> getListaArchivosArchivados() {
        return listaArchivosArchivados;
    }

    public void setListaArchivosArchivados(List<ArchivosAdministrativo> listaArchivosArchivados) {
        this.listaArchivosArchivados = listaArchivosArchivados;
    }

    public DocumentosAdministrativos getSelectedArchivados() {
        return selectedArchivados;
    }

    public void setSelectedArchivados(DocumentosAdministrativos selectedArchivados) {
        this.selectedArchivados = selectedArchivados;
    }

    public DocumentosAdministrativos getSelectedSalida() {
        return selectedSalida;
    }

    public void setSelectedSalida(DocumentosAdministrativos selectedSalida) {
        this.selectedSalida = selectedSalida;
    }

    public List<ArchivosAdministrativo> getListaArchivosSalida() {
        return listaArchivosSalida;
    }

    public void setListaArchivosSalida(List<ArchivosAdministrativo> listaArchivosSalida) {
        this.listaArchivosSalida = listaArchivosSalida;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public Integer getResponsableDestinoId() {
        return responsableDestinoId;
    }

    public void setResponsableDestinoId(Integer responsableDestinoId) {
        this.responsableDestinoId = responsableDestinoId;
    }

    public boolean isHayDocumentosAtencion() {
        return hayDocumentosAtencion;
    }

    public void setHayDocumentosAtencion(boolean hayDocumentosAtencion) {
        this.hayDocumentosAtencion = hayDocumentosAtencion;
    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        return listaUsuariosTransf;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    public Collection<Usuarios> getListaUsuariosTransfArchivados() {
        return listaUsuariosTransfArchivados;
    }

    public void setListaUsuariosTransfArchivados(Collection<Usuarios> listaUsuariosTransfArchivados) {
        this.listaUsuariosTransfArchivados = listaUsuariosTransfArchivados;
    }

    public String getSgteEstado() {
        return sgteEstado;
    }

    public void setSgteEstado(String sgteEstado) {
        this.sgteEstado = sgteEstado;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Date getFechaAltaDesde() {
        return fechaAltaDesde;
    }

    public void setFechaAltaDesde(Date fechaAltaDesde) {
        this.fechaAltaDesde = fechaAltaDesde;
    }

    public Date getFechaAltaHasta() {
        return fechaAltaHasta;
    }

    public void setFechaAltaHasta(Date fechaAltaHasta) {
        this.fechaAltaHasta = fechaAltaHasta;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getNombreJuez() {
        return nombreJuez;
    }

    public void setNombreJuez(String nombreJuez) {
        this.nombreJuez = nombreJuez;
    }

    public String getNuevaCausa() {
        return nuevaCausa;
    }

    public void setNuevaCausa(String nuevaCausa) {
        this.nuevaCausa = nuevaCausa;
    }

    public EntradasDocumentosAdministrativos getEntradaDocumentoAdministrativo() {
        return entradaDocumentoAdministrativo;
    }

    public void setEntradaDocumentoAdministrativo(EntradasDocumentosAdministrativos entradaDocumentoAdministrativo) {
        this.entradaDocumentoAdministrativo = entradaDocumentoAdministrativo;
    }

    public List<CambiosEstadoDocumentoAdministrativoPendientes> getListaTransfer() {
        return listaTransfer;
    }

    public void setListaTransfer(List<CambiosEstadoDocumentoAdministrativoPendientes> listaTransfer) {
        this.listaTransfer = listaTransfer;
    }

    public CambiosEstadoDocumentoAdministrativoPendientes getSelectedTransfer() {
        return selectedTransfer;
    }

    public void setSelectedTransfer(CambiosEstadoDocumentoAdministrativoPendientes selectedTransfer) {
        this.selectedTransfer = selectedTransfer;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public List<DocumentosAdministrativos> getListaSalida() {
        return listaSalida;
    }

    public void setListaSalida(List<DocumentosAdministrativos> listaSalida) {
        this.listaSalida = listaSalida;
    }

    public List<ArchivosAdministrativo> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(List<ArchivosAdministrativo> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public String getFirmaId() {
        return firmaId;
    }

    public void setFirmaId(String firmaId) {
        this.firmaId = firmaId;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public DocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete DocumentosAdministrativos Entity
        super(DocumentosAdministrativos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();

        canal = new CanalesEntradaDocumentoAdministrativo();
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_ADMINISTRATIVO_MM);
        TiposDocumentosAdministrativos tipoDoc = new TiposDocumentosAdministrativos();
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_MM);
        TiposDocumentosAdministrativos tipoDoc2 = new TiposDocumentosAdministrativos();
        tipoDoc2.setCodigo(Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_PV);

        editando = false;

        autoGuardado = false;

        activeTab = 0;

        tiposDoc = new ArrayList<>();
        tiposDoc.add(tipoDoc);
        tiposDoc.add(tipoDoc2);

        tiposDocString = " ('" + Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_MM + "', '" + Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_PV + "') ";
        titulo = "Memos";

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        Usuarios us = (Usuarios) session.getAttribute("Backup");

        if (us != null) {
            usuario = us;
            usuarioBkp = (Usuarios) session.getAttribute("Usuarios");
        } else {
            usuario = (Usuarios) session.getAttribute("Usuarios");
            usuarioBkp = null;
        }

        usuarioBandeja = obtenerSuperiorInmediato(usuario.getDepartamento());

        nombreBandeja = "Bandejas de " + ((usuarioBandeja == null) ? "" : usuarioBandeja.getNombresApellidos()) + " - " + ((usuarioBandeja == null) ? "" : usuarioBandeja.getDepartamento().getNombre());

        //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaInicio = myCal.getTime();
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaAltaHasta);
        cal2.add(Calendar.DATE, 1);
        fechaAltaHasta = cal2.getTime();

        Calendar myCal3 = Calendar.getInstance();
        myCal3.set(Calendar.YEAR, 2023);
        myCal3.set(Calendar.MONTH, 0);
        myCal3.set(Calendar.DAY_OF_MONTH, 1);
        fechaIniVistoCC = myCal3.getTime();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        try {
            tipoDocumentoAdministrativo = this.ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosAdministrativos.findByCodigo", TiposDocumentosAdministrativos.class).setParameter("codigo", Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_MM).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error de configuracion. No se puede iniciar pantalla");
            return;
        }

        listaTiposArchivo = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativo.findAll", TiposArchivoAdministrativo.class).getResultList();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");

        List<TiposEnvio> tipos = ejbFacade.getEntityManager().createNamedQuery("TiposEnvio.findByCodigo", TiposEnvio.class).setParameter("codigo", Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).getResultList();

        if (!tipos.isEmpty()) {
            tipoEnvioArchivoAdmDestinatario = tipos.get(0);
        }

        tipos = ejbFacade.getEntityManager().createNamedQuery("TiposEnvio.findByCodigo", TiposEnvio.class).setParameter("codigo", Constantes.TIPO_ENVIO_ARCHIVO_ADM_REMITENTE).getResultList();

        if (!tipos.isEmpty()) {
            tipoEnvioArchivoAdmRemitente = tipos.get(0);
        }

        tipos = ejbFacade.getEntityManager().createNamedQuery("TiposEnvio.findByCodigo", TiposEnvio.class).setParameter("codigo", Constantes.TIPO_ENVIO_ARCHIVO_ADM_CON_COPIA).getResultList();

        if (!tipos.isEmpty()) {
            tipoEnvioArchivoAdmConCopia = tipos.get(0);
        }

        estadoTransDocAdmEnRevision = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getSingleResult();
        estadoTransDocAdmEnProyecto = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getSingleResult();
        estadoTransDocAdmFinalizada = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO).getSingleResult();

        estadoDocAdmArchivado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ARCHIVADO).getSingleResult();
        estadoDocAdmEnProyecto = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_EN_PROYECTO).getSingleResult();
        estadoDocAdmActivo = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ACTIVO).getSingleResult();

        if (Constantes.ACCION_BANDEJA_SALIDA.equals(accion)) {
            activeTab = 2;
        }
        
        obtenerDatos(accion);

        List<DocumentosAdministrativosAutoguardados> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.findByUsuarioAlta", DocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usuario).getResultList();

        if (!lista.isEmpty()) {
            autoGuardado = true;
            if (lista.get(0).getDocumentoAdministrativoOriginal() == null) {
                // prepareCreate(lista.get(0).getDocumentoAdministrativoPadre(), 1);
                prepareCreate();

                if (lista.get(0).getDocumentoAdministrativoPadre() != null) {
                    int id = lista.get(0).isResponder()?1:2;
                    leyendaResponderReenviar = (id == 1) ? "RESPONDER A:" : "REENVIAR:";
                    cambiarSubcategorias(esResponder ? 1 : 2);
                    responderA = lista.get(0).getResponderA();
                    getSelected().setDocumentoAdministrativo(lista.get(0).getDocumentoAdministrativoPadre());
                }

                // getSelected().setDocumentoAdministrativo(lista.get(0).getDocumentoAdministrativoPadre());
                PrimeFaces.current().ajax().update("DocumentosAdministrativosCreateForm");

                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosAdministrativosCreateDialog').show();");
            } else {
                listaPosiblesUsuariosCC = obtenerEncargados();

                listaPosiblesUsuariosRemitente = obtenerPosiblesRemitentes(usuario);

                /*
                        List<RolesPorUsuarios> list = ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", RolesPorUsuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).setParameter("usuario", usuario.getId()).getResultList();

                        if(!list.isEmpty()){
                            listaPosiblesUsuariosRemitente.add(usuario);
                        }
                 */
                listaUsuariosEnviar = new ArrayList<>();
                listaUsuariosCC = new ArrayList<>();
                listaUsuariosRemitente = new ArrayList<>();

                usuarioEnviar = null;

                usuarioRemitente = null;
                if (!listaPosiblesUsuariosRemitente.isEmpty()) {
                    usuarioRemitente = listaPosiblesUsuariosRemitente.get(0);
                }

                usuarioCC = null;

                if (usuarioRemitente != null) {
                    listaUsuariosRemitente.add(usuarioRemitente);
                }

                listaPosiblesUsuariosEnviar = obtenerPosiblesDestinatarios(usuarioRemitente.getDepartamento());

                listaFormatos = null;

                cambiarSubcategorias(1);

                setSelected(lista.get(0).getDocumentoAdministrativoOriginal());
                prepareEdit();

                // archivo = new ArchivosAdministrativo();
                prepareAutoguardado(lista.get(0), true);

                accion = Constantes.ACCION_EN_PROYECTO;
                PrimeFaces.current().ajax().update("DocumentosAdministrativosEditForm");

                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosAdministrativosEditDialog').show();");
            }
        }


    }

    private Usuarios obtenerSuperiorInmediato(Departamentos dpto) {
        return obtenerSuperiorInmediato(dpto, true);
    }

    private Usuarios obtenerSuperiorInmediato(Departamentos dpto, boolean mostrarError) {

        if (dpto != null) {
            List<Usuarios> listaUsu2 = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", dpto).setParameter("rol", Constantes.ROL_ENCARGADO_DESPACHO).getResultList();

            if (listaUsu2.size() == 1) {
                return listaUsu2.get(0);
            } else if (listaUsu2.size() > 1) {
                if (mostrarError) {
                    JsfUtil.addErrorMessage("Hay mas de un encargado de despacho de la dependencia " + dpto.getNombre());
                }
                return null;
            }

            List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", dpto).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

            if (listaUsu.isEmpty()) {
                if (mostrarError) {
                    JsfUtil.addErrorMessage("No se encuentra encargado de la dependencia " + dpto.getNombre());
                }
                return null;
            } else if (listaUsu.size() > 1) {
                if (mostrarError) {
                    JsfUtil.addErrorMessage("Hay mas de un encargado de la dependencia " + dpto.getNombre());
                }
                return null;
            }

            return listaUsu.get(0);
        }
        return null;
    }

    public String validarExterno(DocumentosAdministrativos doc) {
        if (doc != null) {
            String link = null;
            try {
                /*String encoded = Base64.getEncoder().encodeToString(Utils.encryptMsg(String.valueOf(doc.getId()), Utils.generateKey()));
                encoded = URLEncoder.encode(encoded, StandardCharsets.UTF_8.toString());
*/
//                link = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_MEMO + "?hash=" + encoded;
                // link = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/administrativo/faces/pages/validacion/validacion.xhtml?hash=" + encoded;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return link == null ? "#" : link;
        }

        JsfUtil.addErrorMessage("No se pudo encontrar memorandum a validar");
        return "#";
    }

    public void prepareVerificar(DocumentosAdministrativos doc) {
        selectedVerificar = doc;
    }

    public String validarInterno(DocumentosAdministrativos doc) {
        if (doc != null) {
            String link = null;
            try {
//                String encoded = Base64.getEncoder().encodeToString(Utils.encryptMsg(String.valueOf(doc.getId()), Utils.generateKey()));
     //           encoded = URLEncoder.encode(encoded, StandardCharsets.UTF_8.toString());
        //        link = url + "/" + endpoint + "/" + Constantes.URL_VALIDACION_MEMO + "?hash=" + encoded;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return link == null ? "#" : link;
        }

        JsfUtil.addErrorMessage("No se pudo encontrar memorandum a validar");
        return "#";
    }

    public void borrarAutoGuardado(Usuarios usu) {

        try {
            ut.begin();
            ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.deleteByUsuarioAlta", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usu).executeUpdate();
            ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.deleteByUsuarioAlta", DocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usu).executeUpdate();
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void borrarAutoGuardado(DocumentosAdministrativos doc) {

        try {
            ut.begin();
            ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoOriginal", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoOriginal", doc).executeUpdate();
            ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoOriginal", DocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoOriginal", doc).executeUpdate();
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private void borrarUsuarioPorDocumentoAdministrativoAutoguardado(DocumentosAdministrativosAutoguardados documentoAdministrativoAutoguardado, TiposEnvio tipoEnvio, Usuarios usu) {

        try {
            ut.begin();
            ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.deleteByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardado).setParameter("tipoEnvio", tipoEnvio).setParameter("usuarioAlta", usu).executeUpdate();
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void autoGuardar() {

        if (editando) {

            List<DocumentosAdministrativosAutoguardados> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.findByUsuarioAlta", DocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usuario).getResultList();

            if (lista != null) {
                if (lista.size() > 1) {
                    borrarAutoGuardado(usuario);
                    lista = new ArrayList<>();
                }

                if (lista.isEmpty()) {

                    DocumentosAdministrativosAutoguardados doc = new DocumentosAdministrativosAutoguardados(subcategoriaDocumentoAdministrativo, responderA, archivo == null ? null : archivo.getFormato(), null, descripcionMesaEntrada, archivo == null ? null : archivo.getTexto(), ejbFacade.getSystemDate(), usuario, (getSelected() == null ? null : (getSelected().getId() == null ? null : getSelected())), docResponder, esResponder);

                    documentoAdministrativoAutoguardadoController.setSelected(doc);

                    documentoAdministrativoAutoguardadoController.saveNew(null);

                } else {
                    DocumentosAdministrativosAutoguardados doc = lista.get(0);

                    doc.setSubcategoriaDocumentoAdministrativo(subcategoriaDocumentoAdministrativo);
                    doc.setResponderA(responderA);
                    doc.setFormato(archivo == null ? null : archivo.getFormato());
                    doc.setTipoPrioridad(null);
                    doc.setAsunto(descripcionMesaEntrada);
                    doc.setTexto(archivo == null ? null : archivo.getTexto());
                    doc.setFechaHoraAlta(ejbFacade.getSystemDate());
                    doc.setUsuarioAlta(usuario);
                    doc.setDocumentoAdministrativoPadre(docResponder);
                    doc.setResponder(esResponder);
                    doc.setDocumentoAdministrativoOriginal((getSelected() == null ? null : (getSelected().getId() == null ? null : getSelected())));

                    documentoAdministrativoAutoguardadoController.setSelected(doc);
                    documentoAdministrativoAutoguardadoController.save(null);
                }

            }

            UsuariosPorDocumentosAdministrativosAutoguardados auto = null;

            boolean encontro;
            boolean entro;

            if (listaUsuariosCC != null) {

                List<UsuariosPorDocumentosAdministrativosAutoguardados> listaUsu = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardadoController.getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).setParameter("usuarioAlta", usuario).getResultList();

                entro = false;
                for (Usuarios usu : listaUsuariosCC) {
                    entro = true;
                    encontro = false;
                    for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            /*
                            auto = usuActual;
                            auto.setTipoEnvio(tipoEnvioArchivoAdmConCopia);
                            auto.setUsuario(usu);
                            auto.setUsuarioAlta(usuario);
                            auto.setDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected());
                            usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                            usuarioPorDocumentoAdministrativoAutoguardadoController.save(null);
                             */
                            break;
                        }

                        /*
                        if (listCC.size() > 1) {
                            borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmConCopia, usuario);
                            listCC = new ArrayList<>();
                        }

                        if (listCC.isEmpty()) {
                        } else {
                        }
                         */
                    }

                    if (!encontro) {
                        auto = new UsuariosPorDocumentosAdministrativosAutoguardados(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmConCopia, usu, usuario);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.saveNew(null);
                    }
                }

                List<UsuariosPorDocumentosAdministrativosAutoguardados> usuBorrar = new ArrayList<>();
                for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                    encontro = false;
                    for (Usuarios usu : listaUsuariosCC) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            break;
                        }
                    }

                    if (!encontro) {
                        usuBorrar.add(usuActual);
                    }
                }

                for (UsuariosPorDocumentosAdministrativosAutoguardados usu : usuBorrar) {
                    listaUsu.remove(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.delete(null);
                }
            } else {
                borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmConCopia, usuario);
            }

            if (listaUsuariosRemitente != null) {

                List<UsuariosPorDocumentosAdministrativosAutoguardados> listaUsu = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardadoController.getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).setParameter("usuarioAlta", usuario).getResultList();

                for (Usuarios usu : listaUsuariosRemitente) {
                    encontro = false;
                    for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            /*
                            auto = usuActual;
                            auto.setTipoEnvio(tipoEnvioArchivoAdmRemitente);
                            auto.setUsuario(usu);
                            auto.setUsuarioAlta(usuario);
                            auto.setDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected());
                            usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                            usuarioPorDocumentoAdministrativoAutoguardadoController.save(null);
                             */
                            break;
                        }

                        /*
                        if (listCC.size() > 1) {
                            borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmConCopia, usuario);
                            listCC = new ArrayList<>();
                        }

                        if (listCC.isEmpty()) {
                        } else {
                        }
                         */
                    }

                    if (!encontro) {
                        auto = new UsuariosPorDocumentosAdministrativosAutoguardados(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmRemitente, usu, usuario);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.saveNew(null);
                    }
                }

                List<UsuariosPorDocumentosAdministrativosAutoguardados> usuBorrar = new ArrayList<>();
                for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                    encontro = false;
                    for (Usuarios usu : listaUsuariosRemitente) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            break;
                        }
                    }

                    if (!encontro) {
                        usuBorrar.add(usuActual);
                    }
                }

                for (UsuariosPorDocumentosAdministrativosAutoguardados usu : usuBorrar) {
                    listaUsu.remove(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.delete(null);
                }
            } else {
                borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmRemitente, usuario);
            }

            if (listaUsuariosEnviar != null) {

                List<UsuariosPorDocumentosAdministrativosAutoguardados> listaUsu = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardadoController.getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).setParameter("usuarioAlta", usuario).getResultList();

                for (Usuarios usu : listaUsuariosEnviar) {
                    encontro = false;
                    for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            /*
                            auto = usuActual;
                            auto.setTipoEnvio(tipoEnvioArchivoAdmDestinatario);
                            auto.setUsuario(usu);
                            auto.setUsuarioAlta(usuario);
                            auto.setDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected());
                            usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                            usuarioPorDocumentoAdministrativoAutoguardadoController.save(null);
                             */
                            break;
                        }

                        /*
                        if (listCC.size() > 1) {
                            borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmConCopia, usuario);
                            listCC = new ArrayList<>();
                        }

                        if (listCC.isEmpty()) {
                        } else {
                        }
                         */
                    }

                    if (!encontro) {
                        auto = new UsuariosPorDocumentosAdministrativosAutoguardados(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmDestinatario, usu, usuario);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.saveNew(null);
                    }
                }

                List<UsuariosPorDocumentosAdministrativosAutoguardados> usuBorrar = new ArrayList<>();
                for (UsuariosPorDocumentosAdministrativosAutoguardados usuActual : listaUsu) {
                    encontro = false;
                    for (Usuarios usu : listaUsuariosEnviar) {
                        if (usuActual.getUsuario().equals(usu)) {
                            encontro = true;
                            break;
                        }
                    }

                    if (!encontro) {
                        usuBorrar.add(usuActual);
                    }
                }

                for (UsuariosPorDocumentosAdministrativosAutoguardados usu : usuBorrar) {
                    listaUsu.remove(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(usu);
                    usuarioPorDocumentoAdministrativoAutoguardadoController.delete(null);
                }
            } else {
                borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmDestinatario, usuario);
            }

            /*
            if (listaUsuariosRemitente != null) {
                for (Usuarios usu : listaUsuariosRemitente) {
                    List<UsuariosPorDocumentosAdministrativosAutoguardados> listRem = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardadoController.getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).setParameter("usuarioAlta", usuario).getResultList();

                    if (listRem.size() > 1) {
                        borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmRemitente, usuario);
                        listRem = new ArrayList<>();
                    }

                    if (listRem.isEmpty()) {
                        auto = new UsuariosPorDocumentosAdministrativosAutoguardados(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmRemitente, usu, usuario);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.saveNew(null);
                    } else {
                        auto = listRem.get(0);
                        auto.setTipoEnvio(tipoEnvioArchivoAdmRemitente);
                        auto.setUsuario(usu);
                        auto.setUsuarioAlta(usuario);
                        auto.setDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected());
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.save(null);
                    }
                }
            }

            if (listaUsuariosEnviar != null) {
                for (Usuarios usu : listaUsuariosEnviar) {
                    List<UsuariosPorDocumentosAdministrativosAutoguardados> listEnviar = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardadoUsuarioAltaTipoEnvio", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", documentoAdministrativoAutoguardadoController.getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).setParameter("usuarioAlta", usuario).getResultList();

                    if (listEnviar.size() > 1) {
                        borrarUsuarioPorDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmDestinatario, usuario);
                        listEnviar = new ArrayList<>();
                    }

                    if (listEnviar.isEmpty()) {
                        auto = new UsuariosPorDocumentosAdministrativosAutoguardados(documentoAdministrativoAutoguardadoController.getSelected(), tipoEnvioArchivoAdmDestinatario, usu, usuario);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.saveNew(null);
                    } else {
                        auto = listEnviar.get(0);
                        auto.setTipoEnvio(tipoEnvioArchivoAdmDestinatario);
                        auto.setUsuario(usu);
                        auto.setUsuarioAlta(usuario);
                        auto.setDocumentoAdministrativoAutoguardado(documentoAdministrativoAutoguardadoController.getSelected());
                        usuarioPorDocumentoAdministrativoAutoguardadoController.setSelected(auto);
                        usuarioPorDocumentoAdministrativoAutoguardadoController.save(null);
                    }
                }
            }
             */
        }

    }

    public void verDoc(String archivo) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            try {
                fileByte = Files.readAllBytes(new File(par.getRutaArchivosAdministrativo() + "/" + archivo).toPath());
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        }
    }

    public void verDocCompleto(List<ArchivosAdministrativo> lista) {
        if (lista != null) {

            if (!lista.isEmpty()) {

                Date fecha = ejbFacade.getSystemDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);
                String nombreArchivo = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";
                try {
                    try {
                        PdfDocument pdf = new PdfDocument(new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo));
                        PdfMerger merger = new PdfMerger(pdf);

                        //for (ExpActuaciones act : listaActuaciones) {
                        ArchivosAdministrativo act = null;
                        for (int i = 0; i < lista.size(); i++) {
                            act = lista.get(i);
                            if (act.getRuta() != null) {
                                PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(par.getRutaArchivosAdministrativo() + "/" + act.getRuta()));
                                merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
                                firstSourcePdf.close();
                            }
                        }

                        PageSize ps = new PageSize(pdf.getFirstPage().getPageSize());
                        pdf.addNewPage(pdf.getNumberOfPages() + 1, ps);

                        PdfPage page = pdf.getPage(pdf.getNumberOfPages());

                        float fontSize = 16.0f;
                        float allowedWidth = 500;

                        Paragraph paragraph = new Paragraph("Se deja constancia de que el presente histórico de documentos no cuenta con un sistema de verificación de la autenticidad y validez propio. A dicho efectos, se tendrá en cuenta el código QR consignado en cada documento original, que deberá ser descargado por separado.").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);

                        Canvas canvas = new Canvas(new PdfCanvas(page), pdf, page.getMediaBox());
                        // RootRenderer canvasRenderer = canvas.getRenderer();
                        // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                        /*
                        while (paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                            paragraph.setFontSize(--fontSize);
                        }
                         */

                        // float xCoord = page.getPageSize().getRight() / 2;
                        float xCoord = 50;
                        float yCoord = 800;

                        paragraph.setWidth(allowedWidth);
                        canvas.showTextAligned(paragraph, xCoord, yCoord, TextAlignment.JUSTIFIED);

                        canvas.close();
                        /*
                        Paragraph paragraph2 = new Paragraph(" con un sistema de verificación de la autenticidad y valides propio.").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(fontSize);

                        Canvas canvas2 = new Canvas(new PdfCanvas(page), pdf, page.getMediaBox());
                        RootRenderer canvasRenderer2 = canvas2.getRenderer();
                        // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                        while (paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                            paragraph2.setFontSize(--fontSize);
                        }

                        float xCoord2 = page.getPageSize().getRight() / 2;
                        float yCoord2 = 780;

                        paragraph2.setWidth(allowedWidth);
                        canvas2.showTextAligned(paragraph2, xCoord2, yCoord2, TextAlignment.CENTER);
                        
                        canvas2.close();

                        Paragraph paragraph3 = new Paragraph("A dichos efectos se tendrá en cuenta el código QR de cada documento individualmente.").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(fontSize);

                        Canvas canvas3 = new Canvas(new PdfCanvas(page), pdf, page.getMediaBox());
                        RootRenderer canvasRenderer3 = canvas3.getRenderer();
                        // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                        while (paragraph3.createRendererSubTree().setParent(canvasRenderer3).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                            paragraph3.setFontSize(--fontSize);
                        }

                        float xCoord3 = page.getPageSize().getRight() / 2;
                        float yCoord3 = 760;

                        paragraph3.setWidth(allowedWidth);
                        canvas3.showTextAligned(paragraph3, xCoord3, yCoord3, TextAlignment.CENTER);
                        
                        canvas3.close();
                         */
                        pdf.close();

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        return;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return;
                    }

                    HttpServletResponse httpServletResponse = null;
                    try {
                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.setContentType("application/pdf");
                        // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                        httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                        byte[] fileByte = null;

                        try {
                            fileByte = Files.readAllBytes(new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo).toPath());
                        } catch (IOException ex) {
                            JsfUtil.addErrorMessage("No tiene documento adjunto");
                        }

                        servletOutputStream.write(fileByte);
                        FacesContext.getCurrentInstance().responseComplete();

                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
                        e.printStackTrace();

                        if (httpServletResponse != null) {
                            if (httpServletResponse.getHeader("Content-disposition") == null) {
                                httpServletResponse.addHeader("Content-disposition", "inline");
                            } else {
                                httpServletResponse.setHeader("Content-disposition", "inline");
                            }

                        }
                        JsfUtil.addErrorMessage("No se pudo generar el reporte.");
                    }
                } finally {
                    if (nombreArchivo != null) {
                        if (!"".equals(nombreArchivo)) {
                            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo);
                            f.delete();
                        }
                    }
                }
            }
        }
    }

    public void obtenerDatos(String acc) {
        // Si viene el parametro "tipo" es que es la pantalla de archivos segun estado
        if (Constantes.ACCION_EN_PROYECTO.equals(acc)) {
            titulo = "EN ELABORACION";
            List<EstadosTransferenciaDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getResultList();
            if (!estados.isEmpty()) {
                listaBorradores = buscarPorFechaAltaBorradores(estados.get(0), usuario, null);
            }
        } else if (Constantes.ACCION_EN_REVISION.equals(acc)) {
            titulo = "EN REVISION";
            List<EstadosTransferenciaDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
            if (!estados.isEmpty()) {
                listaBorradores = buscarPorFechaAltaBorradores(estados.get(0), usuario, null);
            }
        } else if (Constantes.ACCION_PARA_REVISION.equals(acc)) {
            titulo = "PARA REVISION";
            List<EstadosTransferenciaDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
            if (!estados.isEmpty()) {
                listaBorradores = buscarPorFechaAltaBorradores(estados.get(0), null, usuario);
            }
        } else {

            // Si no viene el parametro "tipo" es que es la pantalla de Bandeja de Entrada
            buscarBandejaEntrada();
            buscarBandejaSalida();
            buscarBandejaArchivado();
            buscarBandejaCC();
            // obtenerListaSalida();
            // buscarPorFechaAltaArchivados();
        }
    }

    public boolean deshabilitarBorrarUsuario(Usuarios usu) {
        return usu != null ? usu.isIncluidoAutomaticamente() : true;
    }

    public boolean renderedToken() {
        return usuario != null ? usuario.isTieneToken() : false;
    }

    public boolean renderedConfiguracionNros() {
        return true;
    }

    public boolean renderedUsuarioDevolver() {
        return !deshabilitarUsuarioDevolver();
    }

    public boolean deshabilitarUsuarioDevolver() {
        return !"PR".equals(nuevoEstado);
    }

    public boolean deshabilitarPrevisualizar() {
        if (archivo != null) {
            if (archivo.getFormato() != null) {
                if (archivo.getTipoArchivo() != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean deshabilitarMasDatos() {
        return deshabilitarPrevisualizar();
    }

    public boolean deshabilitarUsuarioEnviar() {
        return deshabilitarPrevisualizar() || esResponder;
    }

    public void handleFileUpload(FileUploadEvent event) {

        if (listaFiles == null) {
            listaFiles = new ArrayList<>();
        }
        /*
        if(listaFiles.size() > 2){
            JsfUtil.addErrorMessage("Solo se puede agregar hasta 3 documentos");
            return;
        }
         */
        listaFiles.add(event.getFile());

    }

    public void borrarFile(UploadedFile item) {
        if (listaFiles != null) {
            listaFiles.remove(item);
        }
    }

    public void borrarArchAdm(ArchivosAdministrativo arch) {

        List<DocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", arch.getDocumentoAdministrativo().getId()).getResultList();

        if (!lista.isEmpty()) {
            if (Constantes.ESTADO_DOC_ADM_EN_PROYECTO.equals(lista.get(0).getEstado().getCodigo())) {
                Date fecha = ejbFacade.getSystemDate();
                arch.setFechaHoraBorrado(fecha);
                arch.setUsuarioBorrado(usuario);

                archivosController.setSelected(arch);

                archivosController.save(null);

                archivosController.delete(null);

                obtenerArchivosBorradores();
            } else {
                JsfUtil.addErrorMessage("El documento ya no es un proyecto, no se puede borrar");
            }
        } else {
            JsfUtil.addErrorMessage("No se encontro documento.");
        }
    }

    public void borrarDocAdm() {
        if (getSelected() != null) {
            List<DocumentosAdministrativos> l = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", getSelected().getId()).getResultList();

            if (!l.isEmpty()) {
                if (Constantes.ESTADO_DOC_ADM_EN_PROYECTO.equals(l.get(0).getEstado().getCodigo())) {

                    Date fecha = ejbFacade.getSystemDate();

                    List<ArchivosAdministrativo> listaArch = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).getResultList();

                    for (ArchivosAdministrativo arch : listaArch) {

                        arch.setFechaHoraBorrado(fecha);
                        arch.setUsuarioBorrado(usuario);

                        archivosController.setSelected(arch);

                        archivosController.save(null);

                        archivosController.delete(null);
                    }

                    List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).getResultList();

                    for (UsuariosPorDocumentosAdministrativos usu : lista) {

                        usu.setFechaHoraBorrado(fecha);
                        usu.setUsuarioBorrado(usuario);

                        usuariosPorDocumentosAdministrativosController.setSelected(usu);

                        usuariosPorDocumentosAdministrativosController.save(null);

                        usuariosPorDocumentosAdministrativosController.delete(null);
                    }

                    List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativo", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).getResultList();

                    for (TransferenciasDocumentoAdministrativo transf : listaTransf) {

                        transf.setFechaHoraBorrado(fecha);
                        transf.setUsuarioBorrado(usuario);

                        transferenciasDocumentoAdministrativoController.setSelected(transf);

                        transferenciasDocumentoAdministrativoController.save(null);

                        transferenciasDocumentoAdministrativoController.delete(null);
                    }

                    List<ObservacionesDocumentosAdministrativos> listaObs = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).getResultList();

                    for (ObservacionesDocumentosAdministrativos ob : listaObs) {

                        ob.setFechaHoraBorrado(fecha);
                        ob.setUsuarioBorrado(usuario);

                        observacionesDocumentosAdministrativosController.setSelected(ob);

                        observacionesDocumentosAdministrativosController.save(null);

                        observacionesDocumentosAdministrativosController.delete(null);
                    }

                    getSelected().setUsuarioBorrado(usuario);
                    getSelected().setFechaHoraBorrado(fecha);

                    super.delete(null);
                    obtenerDatos(accion);
                    resetParentsBorradores();
                } else {
                    JsfUtil.addErrorMessage("El documento ya no es un proyecto, no se puede borrar");
                }
            } else {
                JsfUtil.addErrorMessage("No se encontro documento.");
            }
        }
    }

    public boolean deshabilitarHistorico(DocumentosAdministrativos doc) {
        return false;
    }

    /*
    private Usuarios obtenerSuperiorJerarquico(Usuarios usu) {

        Departamentos dpto = usu.getDepartamento();
        List<Usuarios> usuarios = null;
        while (dpto != null) {
            usuarios = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperior", Usuarios.class).setParameter("usuario", usu.getId()).setParameter("rol", Constantes.ROL_ENCARGADO).setParameter("departamento", dpto).getResultList();
            if (!usuarios.isEmpty()) {
                break;
            }

            dpto = dpto.getDepartamentoPadre();
        }

        if (usuarios != null) {
            if (!usuarios.isEmpty()) {
                if (usuarios.size() == 1) {
                    return usuarios.get(0);
                }
            }
        }

        return null;
    }
     */

 /*
    private Usuarios obtenerRemitente(DocumentosAdministrativos doc){
        
        List<UsuariosPorDocumentosAdministrativos> usuarios = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", doc).setParameter("rol", Constantes.ROL_ENCARGADO).setParameter("departamento", dpto).getResultList();
        if(!usuarios.isEmpty()){
            break;
        }
        
        return null;
    }
     */
    public void prepareCambiarEstadoArchivoAdm(String nuevoEstado) {

        this.nuevoEstado = nuevoEstado;

        if (getSelected() != null) {
            ArchivosAdministrativo item = obtenerPrimerArchivo(getSelected());
            archivo = item;
            // Usuarios usu = obtenerSuperiorJerarquico(usuario);

            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());

            prepareObs();

            if ("RS".equals(nuevoEstado)) {
                List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

                if (!lista.isEmpty()) {
                    if (lista.get(0).getUsuario() != null) {
                        mensajeConfirmacion = "Enviar a revisión por " + lista.get(0).getUsuario().getNombresApellidos() + "?";
                    } else {
                        mensajeConfirmacion = "Enviar a revisión?";
                    }
                }
            } else {
                // List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();

                usuarioDevolver = getSelected().getUsuarioElaboracion();

                listaPosiblesUsuariosDevolver = new ArrayList<>();
                // listaPosiblesUsuariosDevolver.add(usuarioDevolver);

                // List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", item.getDocumentoAdministrativo()).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

                if (!lista.isEmpty()) {
                    List<Usuarios> listaDevolver = obtenerPosiblesDevolver(lista.get(0).getUsuario());
                    listaPosiblesUsuariosDevolver.addAll(listaDevolver);
                }

                boolean encontro = false;
                for (Usuarios usu : listaPosiblesUsuariosDevolver) {
                    if (usu.equals(usuarioDevolver)) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    listaPosiblesUsuariosDevolver.add(usuarioDevolver);
                }

                mensajeConfirmacion = "Devolver a elaboración?";

                /*
            if (getSelected() != null) {
                if (getSelected().getUsuarioAlta() != null) {
                    mensajeConfirmacion = "Devolver a elaboración para " + getSelected().getUsuarioElaboracion().getNombresApellidos() + "?";
                } else {
                    mensajeConfirmacion = "Devolver a elaboración?";
                }
            }
                 */
            }
        }
    }

    /*
    public void cambiarEstadoArchivoAdm(ArchivosAdministrativo item, String nuevoEstado) {
        List<EstadosTransferenciaDocumentoAdministrativo> est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", nuevoEstado).getResultList();

        if (!est.isEmpty()) {
            List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", item.getDocumentoAdministrativo()).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

            if (!lista.isEmpty()) {
                item.setEstado(est.get(0));
                // item.setUsuarioRevision(lista.get(0).getUsuario());
                archivosController.setSelected(item);
                archivosController.save(null);
                setSelected(null);
                obtenerDatos(accion);
                resetParentsBorradores();
                PrimeFaces.current().ajax().update("menuForm");
            } else {
                JsfUtil.addErrorMessage("No se pudo encontrar superior jerarquico");
            }
        }
    }
     */
    public void cambiarEstadoArchivoAdm(String nuevoEstado) {
        if (getSelected() != null) {
            List<EstadosTransferenciaDocumentoAdministrativo> est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", nuevoEstado).getResultList();

            if (!est.isEmpty()) {
                TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                if (transf != null) {
                    transf.setEstado(est.get(0));
                    /*
                    if ("RS".equals(nuevoEstado)) {
                        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                        if (!lista.isEmpty()) {
                            transf.setUsuarioRevision(lista.get(0).getUsuario());
                        }
                    } else {
                        // List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();
                        if (getSelected().getUsuarioAlta() != null) {
                            transf.setUsuarioRevision(getSelected().getUsuarioAlta());
                        }
                    }
                     */
                    // item.setUsuarioRevision(lista.get(0).getUsuario());

                    Date fecha = ejbFacade.getSystemDate();

                    if ("PR".equals(nuevoEstado)) {
                        if (!getSelected().verifObs()) {
                            JsfUtil.addErrorMessage("Debe agregar una observación");
                            return;
                        }
                        if (getSelected().getUsuarioElaboracion() != null) {
                            if (!getSelected().getUsuarioElaboracion().equals(usuarioDevolver)) {
                                getSelected().setUsuarioElaboracion(usuarioDevolver);
                                getSelected().setFechaHoraElaboracion(fecha);
                                super.save(null);

                                transf.setUsuarioElaboracion(usuarioDevolver);
                                transf.setFechaHoraElaboracion(fecha);
                            }
                        }
                    }

                    transferenciasDocumentoAdministrativoController.setSelected(transf);
                    transferenciasDocumentoAdministrativoController.save(null);

                    if (getSelected().verifObs()) {

                        getSelected().transferirObs();

                        ObservacionesDocumentosAdministrativos obs = new ObservacionesDocumentosAdministrativos();

                        List<EstadosDocumentoAdministrativo> estAdm = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_EN_PROYECTO).getResultList();

                        if (!estAdm.isEmpty()) {
                            obs.setUsuarioAlta(usuario);
                            obs.setUsuarioUltimoEstado(usuario);
                            obs.setFechaHoraAlta(fecha);
                            obs.setFechaHoraUltimoEstado(fecha);
                            obs.setObservacion(getSelected().getUltimaObservacion());
                            obs.setDocumentoAdministrativo(getSelected());
                            obs.setDepartamento(usuario.getDepartamento());
                            obs.setEstadoDocumentoAdministrativo(estAdm.get(0));

                            obsController.setSelected(obs);
                            obsController.saveNew(null);

                            getSelected().setFechaUltimaObservacion(fecha);
                            getSelected().setObservacionDocumentoAdministrativo(obs);
                            getSelected().setUsuarioUltimaObservacion(usuario);

                            super.save(null);
                        }
                    }

                    borrarAutoGuardado(getSelected());
                    setSelected(null);
                    obtenerDatos(accion);
                    resetParentsBorradores();
                    PrimeFaces.current().ajax().update("menuForm");
                }
            }
        }
    }

    public void modificarNroSecuencia(TiposArchivoAdministrativo tipo, Departamentos dpto, boolean incrementar) {
        List<TiposArchivoAdministrativoPorDepartamentos> lista = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativoPorDepartamentos.findByTipoArchivoAdministrativoANDDepartamento", TiposArchivoAdministrativoPorDepartamentos.class).setParameter("tipoArchivoAdministrativo", tipo).setParameter("departamento", dpto).getResultList();
        if (!lista.isEmpty()) {

            String nroSecuencia = lista.get(0).getSecuencia();

            String[] array = nroSecuencia.split("/");

            if (array.length > 0) {
                DateFormat format = new SimpleDateFormat("yyyy");

                Date fecha = ejbFacade.getSystemDate();

                String anoActual = format.format(fecha);

                if (anoActual.equals(array[1])) {
                    Integer nro = Integer.valueOf(array[0]);
                    if (incrementar) {
                        nro++;
                    } else {
                        nro--;
                    }
                    nroSecuencia = nro + "/" + array[1];
                } else {
                    nroSecuencia = "1/" + anoActual;
                }

                lista.get(0).setSecuencia(nroSecuencia);
                lista.get(0).setUsuarioUltimoEstado(usuario);
                lista.get(0).setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
                tiposArchivoAdministrativoPorDepartamentosController.setSelected(lista.get(0));

                tiposArchivoAdministrativoPorDepartamentosController.save(null);
            }

        }
    }

    private String obtenerNroSecuencia(TiposArchivoAdministrativo tipo, Departamentos dpto) {
        String nroSecuencia = "";
        List<TiposArchivoAdministrativoPorDepartamentos> doc = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativoPorDepartamentos.findByTipoArchivoAdministrativoANDDepartamento", TiposArchivoAdministrativoPorDepartamentos.class).setParameter("tipoArchivoAdministrativo", tipo).setParameter("departamento", dpto).getResultList();

        if (!doc.isEmpty()) {
            if (doc.get(0).getSecuencia() == null) {
                DateFormat format = new SimpleDateFormat("yyyy");
                Date fecha = ejbFacade.getSystemDate();
                String anoActual = format.format(fecha);

                nroSecuencia = "1/" + anoActual;
                doc.get(0).setSecuencia(nroSecuencia);
                tiposArchivoAdministrativoPorDepartamentosController.setSelected(doc.get(0));
                tiposArchivoAdministrativoPorDepartamentosController.save(null);
            } else if ("".equals(doc.get(0).getSecuencia())) {
                DateFormat format = new SimpleDateFormat("yyyy");
                Date fecha = ejbFacade.getSystemDate();
                String anoActual = format.format(fecha);

                nroSecuencia = "1/" + anoActual;
                doc.get(0).setSecuencia(nroSecuencia);
                tiposArchivoAdministrativoPorDepartamentosController.setSelected(doc.get(0));
                tiposArchivoAdministrativoPorDepartamentosController.save(null);
            } else {

                DateFormat format = new SimpleDateFormat("yyyy");
                Date fecha = ejbFacade.getSystemDate();
                String anoActual = format.format(fecha);
                nroSecuencia = doc.get(0).getSecuencia();

                String array[] = nroSecuencia.split("/");

                if (array.length > 1) {
                    if (!array[1].equals(anoActual)) {
                        nroSecuencia = "1/" + anoActual;
                        doc.get(0).setSecuencia(nroSecuencia);
                        tiposArchivoAdministrativoPorDepartamentosController.setSelected(doc.get(0));
                        tiposArchivoAdministrativoPorDepartamentosController.save(null);
                    }
                }
            }
        } else {

            DateFormat format = new SimpleDateFormat("yyyy");
            Date fecha = ejbFacade.getSystemDate();
            String anoActual = format.format(fecha);

            nroSecuencia = "1/" + anoActual;

            TiposArchivoAdministrativoPorDepartamentos t = new TiposArchivoAdministrativoPorDepartamentos(tipo, dpto, nroSecuencia);

            tiposArchivoAdministrativoPorDepartamentosController.setSelected(t);
            tiposArchivoAdministrativoPorDepartamentosController.saveNew(null);
        }

        return nroSecuencia;
    }

    private boolean esEncargadoDespacho(Usuarios usu) {
        List<RolesPorUsuarios> lista = ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class).setParameter("usuario", usu.getId()).getResultList();
        if (!lista.isEmpty()) {
            for (RolesPorUsuarios us : lista) {
                if (Constantes.ROL_ENCARGADO_DESPACHO == us.getRolesPorUsuariosPK().getRol()) {
                    return true;
                }
            }
        }

        return false;
    }

    private String generarPDF(String nombreArch, String asunto, TiposArchivoAdministrativo tipo, String textoActual, String ruta, Departamentos dpto, List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente, List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario, List<UsuariosPorDocumentosAdministrativos> listaUsuCC, String secuencia, Date fechaFinal, boolean agregarSello, DocumentosAdministrativos docRes, String codigoArchivo, DocumentosAdministrativos docOri) {

        // String nombreArch = "";
        try {
            //if (arch.getFormato() != null) {

            List<Metadatos> listaMetadatos = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            //Date fecha = generarFechaPresentacion(ejbFacade.getSystemDateOnly());

            /*
Calendar myCal = Calendar.getInstance();
myCal.set(Calendar.YEAR, 2022);
myCal.set(Calendar.MONTH, 4);
myCal.set(Calendar.DAY_OF_MONTH, 12);
myCal.set(Calendar.HOUR_OF_DAY, 15);
myCal.set(Calendar.MINUTE, 38);
myCal.set(Calendar.SECOND, 0);
Date fechaInicio = myCal.getTime();
            Date fecha = generarFechaPresentacion(fechaInicio);
             */
            //Date fechaActuacion = fecha;
            //fechaFinal = fechaActuacion;
            Metadatos meta = new Metadatos("fecha", format.format(fechaFinal));
            listaMetadatos.add(meta);

            Metadatos meta2 = null;
            if (secuencia != null) {
                String[] array = secuencia.split("/");
                if (array.length > 1) {
                    if (array[0].length() == 1) {
                        secuencia = "0" + secuencia;
                    }
                }

                meta2 = new Metadatos("nro", (dpto == null ? "" : (dpto.getNomenclaturaMemo() == null ? "" : dpto.getNomenclaturaMemo())) + " " + secuencia);
            } else {
                meta2 = new Metadatos("nro", "");
            }

            String textoFinal = (textoActual == null) ? "" : textoActual;

            listaMetadatos.add(meta2);

            // List<UsuariosPorDocumentosAdministrativos> listaUsuariosActual = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
            String remitentes = "";
            if (listaUsuRemitente != null) {
                if (!listaUsuRemitente.isEmpty()) {
                    for (UsuariosPorDocumentosAdministrativos usu : listaUsuRemitente) {
                        if (!"".equals(remitentes)) {
                            remitentes += ", \n";
                        }

                        if (esEncargadoDespacho(usu.getUsuario())) {
                            remitentes += usu.getUsuario().getNombresApellidos() + ", Encargado de Despacho - " + Utils.capitalizeString(usu.getUsuario().getDepartamento().getNombre());
                        } else {
                            remitentes += usu.getUsuario().getNombresApellidos() + ", " + Utils.capitalizeString(usu.getUsuario().getDepartamento().getNombre());
                        }
                    }
                }
            }
            Metadatos meta4 = new Metadatos("remitente", remitentes);
            listaMetadatos.add(meta4);

            // List<UsuariosPorDocumentosAdministrativos> listaUsuariosActual2 = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();
            String destinatarios = "";
            if (listaUsuDestinatario != null) {
                if (!listaUsuDestinatario.isEmpty()) {
                    for (UsuariosPorDocumentosAdministrativos usu : listaUsuDestinatario) {
                        if (!"".equals(destinatarios)) {
                            destinatarios += ", \n";
                        }

                        if (esEncargadoDespacho(usu.getUsuario())) {
                            destinatarios += usu.getUsuario().getNombresApellidos() + ", Encargado de Despacho - " + Utils.capitalizeString(usu.getUsuario().getDepartamento().getNombre());
                        } else {
                            destinatarios += usu.getUsuario().getNombresApellidos() + ", " + Utils.capitalizeString(usu.getUsuario().getDepartamento().getNombre());
                        }
                    }
                }
            }
            Metadatos meta5 = new Metadatos("destinatario", destinatarios);
            listaMetadatos.add(meta5);

            String concopia = "";
            if (listaUsuCC != null) {
                if (!listaUsuCC.isEmpty()) {
                    for (UsuariosPorDocumentosAdministrativos usu : listaUsuCC) {
                        if (!"".equals(concopia)) {
                            concopia += ", \n";
                        }

                        concopia += usu.getUsuario().getNombresApellidos() + ", " + Utils.capitalizeString(usu.getUsuario().getDepartamento().getNombre());
                    }
                }
            }
            Metadatos meta6 = new Metadatos("concopia", concopia);
            listaMetadatos.add(meta6);

            Metadatos meta3 = new Metadatos("texto", (textoActual == null) ? "" : textoActual);
            listaMetadatos.add(meta3);
            if (getSelected() != null) {
                /*
                Metadatos meta4 = new Metadatos("causa", getSelected().getCausa().replace("-", "/"));
                listaMetadatos.add(meta4);
                Metadatos meta5 = new Metadatos("caratula", getSelected().getCaratula());
                listaMetadatos.add(meta5);
                 */
                Metadatos meta7 = new Metadatos("url", url);
                listaMetadatos.add(meta6);
            } else {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                url = request.getRequestURL().toString();
                String uri = request.getRequestURI();
                int pos = url.lastIndexOf(uri);
                url = url.substring(0, pos);

                Metadatos meta7 = new Metadatos("url", url);
                listaMetadatos.add(meta6);
            }

            Metadatos meta8 = new Metadatos("asunto", asunto);
            listaMetadatos.add(meta8);

            String stringFinal = ((textoActual == null) ? "" : textoActual);
            // @page { margin: 130px 50px 50px}

            // String referencia = (docRes == null ? "" : (docRes.getNroFinal() == null ? "" : "<p style='text-align:justify'>Ref.: En respuesta a MEMORANDUM " + (docRes.getDepartamento() == null ? "" : (docRes.getDepartamento().getNomenclaturaMemo() == null ? "" : docRes.getDepartamento().getNomenclaturaMemo())) + " " + (docRes.getNroFinal()==null?"":docRes.getNroFinal()) + "</p>"));
            DocumentosAdministrativos docAct = docRes;
            DocumentosAdministrativos docFinal = null;

            while (docAct != null) {
                if (Constantes.TIPO_DOCUMENTO_ADMINISTRATIVO_MM.equals(docAct.getTipoDocumentoAdministrativo().getCodigo())) {
                    docFinal = docAct;
                }
                /*
                try{
                    docAct = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", docAct.getDocumentoAdministrativo()).getSingleResult();
                }catch(Exception e){
                    docAct = null;
                }
                 */

                docAct = docAct.getDocumentoAdministrativo();
            }

            String referencia = (docFinal == null ? "" : (docFinal.getNroFinal() == null ? "" : "<p style='text-align:justify'>Ref.: MEMORANDUM " + (docFinal.getNomenclaturaFinal() == null ? "" : docFinal.getNomenclaturaFinal()) + " " + (docFinal.getNroFinal() == null ? "" : docFinal.getNroFinal()) + "</p>"));

            if (!"".equals(referencia)) {
                referencia = "<p>&nbsp;</p>" + referencia;
            }

            if (referencia == null) {
                referencia = "";
            }

            String stringTipoDoc = "";
            if (tipo.getId().equals(Constantes.TIPO_ARCHIVO_ADM_MEMO)) {
                stringTipoDoc = "<div align='center'>MEMORANDUM " + (dpto.getNomenclaturaMemo() == null ? "" : dpto.getNomenclaturaMemo()) + " " + secuencia + "</div><p><strong>A </strong>:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<strong>#destinatario#</strong></p><p><strong>De </strong>:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<strong>#remitente#</strong></p><p><strong>Cc:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;#concopia#</strong></p><p><strong>Asunto:&nbsp; &nbsp; &nbsp; &nbsp; #asunto#</strong></p><p><strong>Fecha </strong>:&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<strong>#fecha#</strong></p><hr />";
            }

            // "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /><title></title><style>@media print {html, body {width: 183.6mm;height: 280.5mm;margin-top: 15mm;margin-left: 10mm;margin-right: 70mm;margin-bottom: 18mm;}}</style></head>
            // stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";
            // stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + "<div align='center'>MEMORANDUM " + dpto.getNomenclaturaMemo() + " " + secuencia + "</div>" + referencia + stringFinal + "</body></html>";
            //stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + "<div align='center'>MEMORANDUM " + dpto.getNomenclaturaMemo() + " " + secuencia + "</div>" + "<div align='right'>Asunción " + format.format(fechaFinal) + "</div>" + referencia + stringFinal + "</body></html>";
            //stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + "<p style='text-align:center'>MEMORANDUM " + dpto.getNomenclaturaMemo() + " " + secuencia + "</p>" + referencia + stringFinal + "</body></html>";
            
            
            stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + referencia + stringTipoDoc + stringFinal + "</body></html>";
            try ( OutputStream outputStream = new FileOutputStream(ruta + File.separator + nombreArch + "_temp")) {
//                ITextRenderer renderer = new ITextRenderer();
          //      SharedContext sharedContext = renderer.getSharedContext();

        //        sharedContext.setPrint(true);
          //      sharedContext.setInteractive(false);

         //       FontResolver resolver = renderer.getFontResolver();

             //   renderer.getFontResolver().addFont(par.getRutaArchivosAdministrativo() + "/garamond/GaramondRegular.ttf", true);

                String string = sustituirTags(stringFinal, listaMetadatos);

           //     renderer.setDocumentFromString(string);
           //     renderer.layout();
            //    renderer.createPDF(outputStream);

            }

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(ruta + File.separator + nombreArch + "_temp"), new PdfWriter(ruta + File.separator + nombreArch));
            // Document doc = new Document(pdfDoc);

            pdfDoc.setTagged();
            // Document doc = new Document(pdfDoc, PageSize.A4);
            Document doc = new Document(pdfDoc);

            // doc.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.1f));
            // doc.setMargins(200, 200, 200, 200);
            // java.awt.Image imagen = new java.awt.Image
            File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
          //  Image image = ImageIO.read(pathToFile);

            // Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            // Font catFont = new Font("Arial", Font.PLAIN, 18);
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {

                // if ((i % 2) > 0) {
                PdfPage pagina = pdfDoc.getPage(i);
                Rectangle pageSize = pagina.getPageSize();

                List<TabStop> tabStops = new ArrayList<>();
                float width = (pageSize.getWidth() - 50) - doc.getLeftMargin() - doc.getRightMargin();
                tabStops.add(new TabStop(width / 2, TabAlignment.CENTER));
                tabStops.add(new TabStop(width, TabAlignment.LEFT));

                // Cabecera
                float x = (pageSize.getWidth() / 2) - 75;
                float y = pageSize.getTop() - 80;
                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                Rectangle rect = new Rectangle(x, y, 150, 60);
              //  under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                under.setStrokeColor(magentaColor).moveTo(88, 820).lineTo(550, 820).closePathStroke();

                under.setStrokeColor(magentaColor).moveTo(88, 105).lineTo(550, 105).closePathStroke();

                Rectangle rect2 = new Rectangle(88, pageSize.getTop() - 140, 480, 60);
                under.rectangle(rect2);
                // under.stroke();
                Canvas canvas = new Canvas(under, pdfDoc, rect2);
                PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);
                Text titleCab = new Text("Misión: Órgano constitucional que juzga el desempeño de los magistrados judiciales, agentes fiscales y defensores públicos por la supuesta comisión de delitos").setFont(bold).setFontSize(6).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Paragraph pCab = new Paragraph().addTabStops(tabStops).add(new Tab()).add(titleCab).setMultipliedLeading(0.1f);
                Text titleCab2 = new Text(" o mal desempeño en el ejercicio de sus funciones,observando el debido proceso y velando por la correcta administración de justicia, en tutela de los derechos de los ciudadanos").setFont(bold).setFontSize(6).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Paragraph pCab2 = new Paragraph().addTabStops(tabStops).add(new Tab()).add(titleCab2).add(new Tab()).add(new Tab()).setMultipliedLeading(0.1f);

                canvas.add(pCab);
                canvas.add(pCab2);
                canvas.close();

                // Pie
                Rectangle rectPie = new Rectangle(88, 10, 480, 90);
                under.rectangle(rectPie);
                // under.stroke();
                Canvas canvasPie = new Canvas(under, pdfDoc, rectPie);
                PdfFont fontPie = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                PdfFont boldPie = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);

                Text titlePie = new Text("Visión: Ser una institución confiable y reconocida por la aplicación de procesos transparentes, objetivos e imparciales en el cumplimiento de su rol").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Text titlePie2 = new Text("constitucional, para el fortalecimiento del estado de derecho, en beneficio de la sociedad").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Text titlePie3 = new Text("14 de Mayo esq. Oliva - Ed. El Ciervo	                                                                                                                                     Tel: (595 21) 442662").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Text titlePie4 = new Text("www.jem.gov.py                                                                                                                                                                         Asunción - Paraguay").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                Paragraph pPie = new Paragraph().add(titlePie).setMultipliedLeading(0.1f);
                Paragraph pPie2 = new Paragraph().addTabStops(tabStops).add(new Tab()).add(titlePie2).add(new Tab()).add(new Tab()).setMultipliedLeading(1.0f);
                Paragraph pPie3 = new Paragraph().add(titlePie3).setMultipliedLeading(0.1f);
                Paragraph pPie4 = new Paragraph().add(titlePie4).setMultipliedLeading(0.1f);

                canvasPie.add(pPie);
                canvasPie.add(pPie2);
                canvasPie.add(pPie3);
                canvasPie.add(pPie4);
                canvasPie.close();

                under.saveState();
                // under2.saveState();
                // }
                Paragraph preface = new Paragraph();
                preface.add(new Paragraph(" "));

                // 
            }

            doc.close();

            if (agregarSello) {
                try ( InputStream in = new BufferedInputStream(new FileInputStream(ruta + File.separator + nombreArch));  OutputStream out = new BufferedOutputStream(new FileOutputStream(ruta + File.separator + nombreArch + "_temp"))) {

                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Path path = Paths.get(ruta + File.separator + nombreArch);
                long tamano = Files.size(path);
                agregarSelloDeCargoQR(ruta + File.separator + nombreArch, remitentes, remitentes, "***", tamano / (1024 * 1024), fechaFinal, docOri, fechaFinal, codigoArchivo);
                // File archivo = new File(ruta + File.separator + nombreArch + "_temp");
                // archivo.delete();
            }

            File archivo = new File(ruta + File.separator + nombreArch + "_temp");
            archivo.delete();

            //}
            // }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

        return nombreArch;
    }

    private String obtenerAlias(Usuarios usu) {
        List<AliasesKeystore> alias = ejbFacade.getEntityManager().createNamedQuery("AliasesKeystore.findByUsuarioANDEstado", AliasesKeystore.class).setParameter("usuario", usu).setParameter("estado", new Estados("AC")).getResultList();
        if (alias.isEmpty()) {
            return null;
        }

        return alias.get(0).getAlias();
    }

    public boolean verifFirma(ArchivosAdministrativo doc, TransferenciasDocumentoAdministrativo transf) {
        if (doc != null) {
            if (doc.getRuta() == null) {
                if (getSelected() != null) {
                    // TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    // List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstado", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_IN)).getResultList();
                    if (transf != null) {
                        if (!transf.isFirmado()) {
                            File file = new File(par.getRutaArchivosAdministrativo() + File.separator + par.getKeystore());

                            try {
                                KeyStore keyStore = loadKeyStore(file, par.getContrasenaKeystore(), "JKS");

                                String alias = obtenerAlias(usuario);

                                if (alias != null) {

                                    PrivateKey pk = (PrivateKey) keyStore.getKey(alias, contrasenaAlias.toCharArray());
                                    return true;
                                } else {
                                    JsfUtil.addErrorMessage("No se encuentro configuracion para firma");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JsfUtil.addErrorMessage("Documento ya está firmado");
                        }
                    } else {
                        JsfUtil.addErrorMessage("No se encuentra transferencia");
                    }
                } else {
                    JsfUtil.addErrorMessage("Documento no seleccionado");
                }
            } else {
                JsfUtil.addErrorMessage("Documento ya fue generado");
            }
        }
        return false;
    }

    private void volverAtrasFirma(ArchivosAdministrativo act) {
        if (act.getFormato() != null) {
            act.setRuta(null);
            archivosController.setSelected(act);
            archivosController.save(null);
        }
    }

    public String firmar(TransferenciasDocumentoAdministrativo transf, ArchivosAdministrativo doc, String nombreArchActual, String secuencia, Date fechaFinal) {
        if (doc != null) {
            if (doc.getRuta() == null) {
                if (getSelected() != null) {
                    // TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    // List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstado", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_IN)).getResultList();
                    if (transf != null) {
                        if (!transf.isFirmado()) {
                            File file = new File(par.getRutaArchivosAdministrativo() + File.separator + "keystore");

                            try {
                                KeyStore keyStore = loadKeyStore(file, par.getContrasenaKeystore(), "JKS");

                                String alias = obtenerAlias(usuario);

                                if (alias != null) {

                                    PrivateKey pk = (PrivateKey) keyStore.getKey(alias, contrasenaAlias.toCharArray());
                                    Certificate[] chain = keyStore.getCertificateChain(alias);
                                    BouncyCastleProvider provider = new BouncyCastleProvider();
                                    Security.addProvider(provider);

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);
                                    Date fechaHora = ejbFacade.getSystemDate();
                                    String nombreArch = simpleDateFormat.format(fechaHora) + "_" + doc.getId() + ".pdf";

                                    sign(nombreArchActual, par.getRutaArchivosAdministrativo() + File.separator + nombreArch, chain, pk, DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS, "firma", "asuncion");

                                    File f = new File(nombreArchActual);
                                    f.delete();

                                    return nombreArch;

                                } else {
                                    JsfUtil.addErrorMessage("No se encuentro configuracion para firma");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JsfUtil.addErrorMessage("Documento ya está firmado");
                        }
                    } else {
                        JsfUtil.addErrorMessage("No se encuentra transferencia");
                    }
                } else {
                    JsfUtil.addErrorMessage("Documento no seleccionado");
                }
            } else {
                JsfUtil.addErrorMessage("Documento ya fue generado");
            }
        }
        return null;
    }

    public void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, PdfSigner.CryptoStandard subfilter, String reason, String location)
            throws GeneralSecurityException, IOException {
        // Creating the reader and the signer
        PdfReader reader = new PdfReader(src);
        PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), true);

        int contador = 0;
        int contadorY = 0;

        String nombre = ((X509Certificate) chain[0]).getSubjectDN().getName();

        String[] partesNombre = nombre.split("[,]");

        for (String parte : partesNombre) {
            // System.out.println("Parte: " + parte );
            String[] valores = parte.split("[=]");
            // System.out.println("Valores: " + valores[0] + ", " + valores[1] );
            if ("CN".equals(valores[0].trim())) {
                nombre = valores[1].trim();
                break;
            }
        }

        Rectangle rect = new Rectangle(70 + (150 * contador), 70 + (120 * contadorY), 150, 120);
        PdfSignatureAppearance sap = signer.getSignatureAppearance();
        // sap.setLayer2Text("Firmado por");
        sap.setReason("");
        sap.setReasonCaption("");
        sap.setLocationCaption("");
        sap.setLocation("");

        BufferedImage image;
      //  try {
//            image = ImageIO.read(new File(par.getRutaArchivosAdministrativo() + File.separator + "logoJEMTransparenciaAdm.png"));
            // get the Graphics object
//            Graphics g = image.getGraphics();
            // set font

            int tamano = 75;
            Font font = new Font("Arial", Font.PLAIN, tamano);
           // FontMetrics metrics = g.getFontMetrics(font);
            String nombre1 = "";
            String nombre2 = "";
           // if (image.getWidth() - metrics.stringWidth(nombre) < 0) {
                String array[] = nombre.split(" ");
                int i = 0;
                for (; i < array.length / 2; i++) {
                    nombre1 += " " + array[i];
                }

                for (int j = i; j < array.length; j++) {
                    nombre2 += " " + array[j];
                }

    //        } else {
                nombre1 = nombre;
        //    }

            Color maroon = new Color(0, 0, 0);

            // Map attributes = g.getFont().getAttributes();
            // attributes.put(TextAttribute.SIZE, 20f);
            // g.setFont(font.deriveFont(attributes));
//            g.setFont(font);
     //       g.setColor(maroon);
            // display the text at the coordinates(x=50, y=150)

            String label = "Firmado digitalmente por:";
/*
            int positionX = (image.getWidth() - metrics.stringWidth(label)) / 2;
            int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(label, positionX, positionY);

            positionX = (image.getWidth() - metrics.stringWidth(nombre1)) / 2;
            positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(nombre1, positionX, positionY + tamano);

            if (!"".equals(nombre2)) {
                positionX = (image.getWidth() - metrics.stringWidth(nombre2)) / 2;
                positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                g.drawString(nombre2, positionX, positionY + tamano * 2);
            }*/
            /*
                if (!"".equals(cargo)) {
                        positionX = (image.getWidth() - metrics.stringWidth(cargo)) / 2;
                        positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                        g.drawString(cargo, positionX, positionY + tamano * 2);
                }
             */

//            g.dispose();
            // write the image
          //  ImageIO.write(image, "png", new File(par.getRutaArchivosAdministrativo() + File.separator + "logoJEMTransparenciaAdm1.png"));
       /* } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/

        sap.setRenderingMode(RenderingMode.GRAPHIC);
        sap.setSignatureGraphic(ImageDataFactory.create(par.getRutaArchivosAdministrativo() + File.separator + "logoJEMTransparenciaAdm1.png"));
        sap.setCertificate(chain[0]);
        sap.setPageRect(rect);
        sap.setPageNumber(signer.getDocument().getNumberOfPages() - 1);
        /*
        // Creating the appearance
        PdfSignatureAppearance appearance = signer.getSignatureAppearance()
                .setReason(reason)
                .setLocation(location)
                .setReuseAppearance(false);
        Rectangle rect = new Rectangle(36, 648, 200, 100);
        appearance
                .setPageRect(rect)
                .setPageNumber(1);
         */
        signer.setFieldName("sig");
        // Creating the signature
        IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        IExternalDigest digest = new BouncyCastleDigest();
        signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);

    }

    public static KeyStore loadKeyStore(final File keystoreFile,
            final String password, final String keyStoreType)
            throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException {
        if (null == keystoreFile) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }

        final URI keystoreUri = keystoreFile.toURI();
        final URL keystoreUrl = keystoreUri.toURL();
        final KeyStore keystore = KeyStore.getInstance(keyStoreType);
        InputStream is = null;
        try {
            is = keystoreUrl.openStream();
            keystore.load(is, null == password ? null : password.toCharArray());
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return keystore;
    }

    private void generarFirmas() {

    }

    public void prueba() {

        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        Date fechaF = generarFechaPresentacion(ejbFacade.getSystemDate());

        return;
    }

    public void firmarPorToken() {
        if (getSelected() != null) {
            borrarAutoGuardado(usuario);
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());

            ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());

            Date fecha = ejbFacade.getSystemDate();

            if (transf != null) {

                List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();
                List<UsuariosPorDocumentosAdministrativos> listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                listaFirmasAdministrativas = new ArrayList<>();
                int contador = 0;
                for (UsuariosPorDocumentosAdministrativos usuDest : listaUsuDestinatario) {
                    List<UsuariosPorDocumentosAdministrativos> listaUsuDest = new ArrayList<>();
                    listaUsuDest.add(usuDest);

                    contador++;
                    Date fechaF = generarFechaPresentacion(ejbFacade.getSystemDate());
                    String secuencia = obtenerNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento());
                    String codigoArchivo = generarCodigoArchivo();
                    generarPDF("M" + sessionId + "_" + contador + ".pdf_tmp", getSelected().getDescripcionMesaEntrada(), arch.getTipoArchivo(), arch.getTexto(), par.getRutaArchivosAdministrativo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), listaUsuRemitente, listaUsuDest, listaUsuCC, secuencia, fechaF, true, arch.getDocumentoAdministrativo().getDocumentoAdministrativo(), codigoArchivo, arch.getDocumentoAdministrativo());
                    /*
                    try (
                        InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArch));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArch + "_tmp"))) {

                        byte[] buffer = new byte[1024];
                        int lengthRead;
                        while ((lengthRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, lengthRead);
                            out.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     */

                    FirmasArchivoAdministrativo firma = new FirmasArchivoAdministrativo();

                    firma.setArchivoAdministrativo(arch);
                    firma.setFechaHora(fecha);
                    firma.setEstado("PE");
                    firma.setDocumentoAdministrativo(arch.getDocumentoAdministrativo());
                    firma.setRuta("M" + sessionId + "_" + contador + ".pdf_tmp");
                    firma.setSesion("M" + sessionId + "_" + contador);
                    firma.setUsuario(usuario);
                    firma.setNroFinal(secuencia);
                    firma.setNomenclaturaFinal(listaUsuRemitente.get(0).getUsuario().getDepartamento().getNomenclaturaMemo());
                    firma.setFechaFinal(fechaF);
                    firma.setTextoFinal(arch.getTexto());
                    firma.setCodigoArchivo(codigoArchivo);
                    firma.setDestinatario(usuDest.getUsuario());

                    firmasController.setSelected(firma);
                    firmasController.saveNew(null);

                    listaFirmasAdministrativas.add(firma);

                    modificarNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), true);
                }

                boolean resp = false;
                for (FirmasArchivoAdministrativo firma : listaFirmasAdministrativas) {
                    resp = firmarUno(firma);
                    if (!resp) {
                        break;
                    }
                }

                // Si falla la firma volver atras nros de memo
                if (resp) {
                    actualizarRevisado(true);
                } else {
                    for (FirmasArchivoAdministrativo firma : listaFirmasAdministrativas) {
                        modificarNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), false);
                    }
                }
            }
        }
    }

    public boolean firmarUno(FirmasArchivoAdministrativo firma) {

        boolean resp = false;

        //if (docImprimir != null) {
        Date fecha = ejbFacade.getSystemDate();

        /*
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + "_" + docImprimir.getId() + ".pdf";
            File copied = new File(par.getRutaArchivos() + "/" + nombreArchivo);
            File original = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);

            try {
                FileUtils.copyFile(original, copied);
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error en proceso.");
                return resp;
            }

            docImprimir.setRuta(nombreArchivo);

            archivosController.setSelected(docImprimir);
            archivosController.save(null);
         */
        int cont = 60;
        FirmasArchivoAdministrativo firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("FirmasArchivoAdministrativo.findById", FirmasArchivoAdministrativo.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            // System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);
            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {
                    resp = true;
                } else {
                    // volverAtrasFirma(docImprimir);
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                // volverAtrasFirma(docImprimir);
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            // volverAtrasFirma(docImprimir);
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }
        //}
        return resp;

    }

    public void actualizarRevisado(boolean firmadoPorToken) {
        try {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch.getFormato() != null) {
                    TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    if (!firmadoPorToken) {
                        if (!verifFirma(arch, transf)) {
                            return;
                        }
                    }

                    usuariosPorDocumentosAdministrativosActual = null;

                    Date fechaF = generarFechaPresentacion(ejbFacade.getSystemDate());

                    transf = actualizarPrimerDoc(firmadoPorToken, fechaF);

                    if (transf != null) {
                        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();

                        List<UsuariosPorDocumentosAdministrativos> listaUtilizada = new ArrayList<>();

                        for (int i = 0; i < lista.size(); i++) {
                            UsuariosPorDocumentosAdministrativos usu = lista.get(i);
                            if (Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo())) {
                                if (!usu.equals(usuariosPorDocumentosAdministrativosActual)) {
                                    actualizarPorDestinatario(usu, transf, firmadoPorToken, fechaF);
                                    listaUtilizada.add(usu);
                                }
                            }
                        }

                        for (UsuariosPorDocumentosAdministrativos usu : listaUtilizada) {
                            usuariosPorDocumentosAdministrativosController.setSelected(usu);
                            usuariosPorDocumentosAdministrativosController.delete(null);
                        }

                        obtenerDatos(accion);

                        setSelected(null);
                        resetParentsBorradores();
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

    }

    public void actualizarPorDestinatario(UsuariosPorDocumentosAdministrativos usuDest, TransferenciasDocumentoAdministrativo transfPar, boolean firmadoPorToken, Date fechaF) {

        Date fecha = ejbFacade.getSystemDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

        EntradasDocumentosAdministrativos entradaDoc = new EntradasDocumentosAdministrativos();

        entradaDoc.setFechaHoraUltimoEstado(fecha);
        entradaDoc.setUsuarioUltimoEstado(usuario);
        entradaDoc.setFechaHoraAlta(fecha);
        entradaDoc.setUsuarioAlta(usuario);
        entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

        DocumentosAdministrativos doc = new DocumentosAdministrativos();

        doc.setCanalEntradaDocumentoAdministrativo(new CanalesEntradaDocumentoAdministrativo(Constantes.CANAL_ENTRADA_DOCUMENTO_ADMINISTRATIVO_MM));
        doc.setCaratula(getSelected().getCaratula());
        doc.setCausa(getSelected().getCausa());
        doc.setDepartamento(usuDest.getUsuario().getDepartamento());
        doc.setDepartamentoAnterior(getSelected().getDepartamentoAnterior());
        doc.setDescripcionMesaEntrada(getSelected().getDescripcionMesaEntrada());
        doc.setDocumentoAdministrativo(getSelected().getDocumentoAdministrativo());
        doc.setEntradaDocumentoAdministrativo(entradaDoc);
        doc.setEstado(getSelected().getEstado());
        doc.setEstadoAnterior(getSelected().getEstadoAnterior());
        doc.setEstadoProcesal(getSelected().getEstadoProcesal());
        doc.setFechaHoraAlta(fecha);
        doc.setFechaHoraBorrado(null);
        doc.setFechaHoraEstadoProcesal(null);
        doc.setFechaHoraUltimoEstado(fecha);
        // doc.setFechaPresentacion(getSelected().getFechaPresentacion());
        doc.setFechaUltimaObservacion(null);
        doc.setMostrarWeb(getSelected().getMostrarWeb());
        doc.setMotivoProceso(getSelected().getMotivoProceso());
        doc.setObservacionDocumentoAdministrativo(null);
        doc.setPlazo(getSelected().getPlazo());
        doc.setResponsable(usuDest.getUsuario());
        doc.setResponsableAnterior(getSelected().getResponsableAnterior());
        doc.setSubcategoriaDocumentoAdministrativo(getSelected().getSubcategoriaDocumentoAdministrativo());
        doc.setTipoDocumentoAdministrativo(getSelected().getTipoDocumentoAdministrativo());
        doc.setUltimaObservacion(null);
        doc.setUsuarioAlta(usuario);
        doc.setUsuarioBorrado(null);
        doc.setUsuarioEstadoProcesal(null);
        doc.setUsuarioUltimaObservacion(null);
        doc.setUsuarioUltimoEstado(usuario);
        doc.setFechaPresentacion(fechaF);

        String codigoArchivo = generarCodigoArchivo();

        doc.setCodigoArchivo(codigoArchivo);

        DocumentosAdministrativos docAnt = getSelected();

        setSelected(doc);

        this.saveNew(null);

        setSelected(docAnt);

        TransferenciasDocumentoAdministrativo transf = new TransferenciasDocumentoAdministrativo();

        transf.setDepartamentoDestino(transfPar.getDepartamentoDestino());
        transf.setDepartamentoOrigen(transfPar.getDepartamentoOrigen());
        transf.setDocumentoAdministrativo(doc);
        transf.setEstado(transfPar.getEstado());
        transf.setFechaHoraAlta(transfPar.getFechaHoraAlta());
        transf.setFechaHoraBorrado(null);
        //transf.setFechaHoraFirmado(transfPar.getFechaHoraFirmado());
        transf.setFechaHoraFirmado(null);
        transf.setFechaHoraRevisado(transfPar.getFechaHoraRevisado());
        transf.setFechaHoraTransferencia(transfPar.getFechaHoraTransferencia());
        transf.setFechaHoraUltimoEstado(transfPar.getFechaHoraUltimoEstado());
        //transf.setFirmado(transfPar.isFirmado());
        transf.setFirmado(false);
        transf.setResponsableDestino(usuDest.getUsuario());
        transf.setResponsableOrigen(transfPar.getResponsableOrigen());
        transf.setRevisado(transfPar.isRevisado());
        transf.setUsuarioAlta(transfPar.getUsuarioAlta());
        transf.setUsuarioBorrado(null);
        transf.setUsuarioFirma(transfPar.getUsuarioFirma());
        // transf.setUsuarioFirmado(transfPar.getUsuarioFirmado());
        transf.setUsuarioFirmado(null);
        transf.setUsuarioRevisado(transfPar.getUsuarioRevisado());
        transf.setUsuarioRevision(transfPar.getUsuarioRevision());
        transf.setUsuarioUltimoEstado(transfPar.getUsuarioUltimoEstado());
        transferenciasDocumentoAdministrativoController.setSelected(transf);
        transferenciasDocumentoAdministrativoController.saveNew(null);

        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transfPar).getResultList();

        for (UsuariosPorDocumentosAdministrativos usu : lista) {
            if (!Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo()) || usu.equals(usuDest)) {
                UsuariosPorDocumentosAdministrativos usua = new UsuariosPorDocumentosAdministrativos();
                usua.setDocumentoAdministrativo(doc);
                usua.setFechaHoraAlta(usu.getFechaHoraAlta());
                usua.setFechaHoraBorrado(null);
                usua.setFechaHoraUltimoEstado(usu.getFechaHoraUltimoEstado());
                usua.setTipoEnvio(usu.getTipoEnvio());
                usua.setTransferenciaDocumentoAdministrativo(transf);
                usua.setUsuario(usu.getUsuario());
                usua.setDepartamento(usu.getDepartamento());
                usua.setUsuarioAlta(usu.getUsuarioAlta());
                usua.setUsuarioBorrado(null);
                usua.setUsuarioUltimoEstado(usu.getUsuarioUltimoEstado());

                usuariosPorDocumentosAdministrativosController.setSelected(usua);
                usuariosPorDocumentosAdministrativosController.saveNew(null);
            }

        }

        List<ArchivosAdministrativo> lista2 = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", transfPar.getDocumentoAdministrativo()).getResultList();
        boolean primerArchivo = true;
        String textoF = null;
        String secuencia = null;
        String nomenclatura = null;
        for (ArchivosAdministrativo archivo : lista2) {
            ArchivosAdministrativo archi = new ArchivosAdministrativo();
            archi.setDescripcion(archivo.getDescripcion());
            archi.setDocumentoAdministrativo(doc);
            archi.setFechaHoraAlta(fecha);
            archi.setFechaHoraBorrado(null);
            archi.setFechaHoraUltimoEstado(archivo.getFechaHoraUltimoEstado());
            archi.setFormato(archivo.getFormato());
            // archi.setRuta(url);
            archi.setTextoFinal(archivo.getTextoFinal());
            archi.setTipoArchivo(archivo.getTipoArchivo());
            archi.setUsuarioAlta(archivo.getUsuarioAlta());
            archi.setUsuarioBorrado(null);
            archi.setUsuarioUltimoEstado(archivo.getUsuarioUltimoEstado());
            archi.setTexto(archivo.getTexto());
            archi.setFechaFinal(archivo.getFechaFinal());

            archivosController.setSelected(archi);
            archivosController.saveNew(null);

            if (primerArchivo) {
                String nombreArch = null;

                List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

                secuencia = obtenerNroSecuencia(archi.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento());
                // Date fechaF = generarFechaPresentacion(ejbFacade.getSystemDateOnly());
                if (!firmadoPorToken) {
                    List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = new ArrayList<>();
                    listaUsuDestinatario.add(usuDest);
                    List<UsuariosPorDocumentosAdministrativos> listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                    textoF = archi.getTexto();
                    String nombreArchivo = generarPDF(simpleDateFormat.format(fecha) + "_" + archi.getDocumentoAdministrativo().getId() + "_" + archi.getId() + ".pdf", doc.getDescripcionMesaEntrada(), archi.getTipoArchivo(), textoF, par.getRutaArchivosAdministrativo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), listaUsuRemitente, listaUsuDestinatario, listaUsuCC, secuencia, fechaF, true, archi.getDocumentoAdministrativo().getDocumentoAdministrativo(), codigoArchivo, archi.getDocumentoAdministrativo());

                    try ( InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo + "_tmp"))) {

                        byte[] buffer = new byte[1024];
                        int lengthRead;
                        while ((lengthRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, lengthRead);
                            out.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    nombreArch = firmar(transf, archi, par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo + "_tmp", secuencia, fechaF);
                } else {
                    for (FirmasArchivoAdministrativo firma : listaFirmasAdministrativas) {
                        if (usuDest.getUsuario().equals(firma.getDestinatario())) {
                            String nombreArchTmp = firma.getRuta();
                            textoF = firma.getTextoFinal();
                            secuencia = firma.getNroFinal();
                            nomenclatura = firma.getNomenclaturaFinal();
                            nombreArch = simpleDateFormat.format(fecha) + "_" + archi.getDocumentoAdministrativo().getId() + "_" + archi.getId() + ".pdf";
                            File file = new File(par.getRutaArchivosAdministrativo() + File.separator + nombreArchTmp);
                            File file2 = new File(par.getRutaArchivosAdministrativo() + File.separator + nombreArch);
                            file.renameTo(file2);
                            break;
                        }
                    }
                }

                if (nombreArch != null) {

                    transf.setUsuarioFirmado(usuario);
                    transf.setFechaHoraFirmado(fecha);
                    transf.setFirmado(true);
                    transferenciasDocumentoAdministrativoController.setSelected(transf);
                    transferenciasDocumentoAdministrativoController.save(null);

                    archi.setRuta(nombreArch);
                    archi.setTextoFinal(textoF);
                    archi.setNroFinal(secuencia);
                    archi.setNomenclaturaFinal(nomenclatura);
                    archi.setFechaFinal(fechaF);

                    archivosController.setSelected(archi);

                    archivosController.save(null);

                    if (!firmadoPorToken) {
                        modificarNroSecuencia(archi.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), true);
                    }
                }
            } else {
                String nombreArchivo = simpleDateFormat.format(fecha);
                nombreArchivo += "_" + archi.getDocumentoAdministrativo().getId() + "_" + archi.getId() + ".pdf";

                try ( InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + archivo.getRuta()));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo))) {

                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                archi.setNroFinal(archivo.getNroFinal());
                archi.setFechaFinal(archivo.getFechaFinal());
                archi.setTextoFinal(archivo.getTextoFinal());
                archi.setNomenclaturaFinal(archivo.getNomenclaturaFinal());
                archi.setRuta(nombreArchivo);
                archivosController.setSelected(archi);
                archivosController.save(null);
            }

            primerArchivo = false;
        }

        doc.setNroFinal(secuencia);
        doc.setNomenclaturaFinal(nomenclatura);
        doc.setFechaFinal(fechaF);
        doc.setTextoFinal(textoF);

        docAnt = getSelected();
        setSelected(doc);

        super.save(null);

        setSelected(docAnt);

    }

    public TransferenciasDocumentoAdministrativo actualizarPrimerDoc(boolean firmadoPorToken, Date fechaF) {
        TransferenciasDocumentoAdministrativo transf = null;
        try {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch.getFormato() != null) {
                    List<EstadosTransferenciaDocumentoAdministrativo> est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO).getResultList();

                    if (!est.isEmpty()) {

                        Date fecha = ejbFacade.getSystemDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                        transf = obtenerTransferenciaActual(getSelected());

                        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();

                        Usuarios usuDest = null;
                        for (UsuariosPorDocumentosAdministrativos usu : lista) {
                            if (Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo())) {
                                usuariosPorDocumentosAdministrativosActual = usu;
                                usuDest = usu.getUsuario();
                                break;
                            }

                        }
                        // List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstado", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_IN)).getResultList();
                        if (transf != null) {

                            List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

                            String nombreArch = null;
                            String secuencia = obtenerNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento());
                            String textoF = null;
                            String nomenclatura = listaUsuRemitente.get(0).getUsuario().getDepartamento().getNomenclaturaMemo();

                            String codigoArchivo = "";
                            if (!firmadoPorToken) {
                                codigoArchivo = generarCodigoArchivo();
                                List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = new ArrayList<>();
                                listaUsuDestinatario.add(usuariosPorDocumentosAdministrativosActual);
                                List<UsuariosPorDocumentosAdministrativos> listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                                textoF = arch.getTexto();
                                nombre = generarPDF(simpleDateFormat.format(fecha) + "_" + arch.getDocumentoAdministrativo().getId() + "_" + arch.getId() + ".pdf", getSelected().getDescripcionMesaEntrada(), arch.getTipoArchivo(), textoF, par.getRutaArchivosAdministrativo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), listaUsuRemitente, listaUsuDestinatario, listaUsuCC, secuencia, fechaF, true, arch.getDocumentoAdministrativo().getDocumentoAdministrativo(), codigoArchivo, arch.getDocumentoAdministrativo());

                                try (
                                         InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + nombre));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombre + "_tmp"))) {

                                    byte[] buffer = new byte[1024];
                                    int lengthRead;
                                    while ((lengthRead = in.read(buffer)) > 0) {
                                        out.write(buffer, 0, lengthRead);
                                        out.flush();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                nombreArch = firmar(transf, arch, par.getRutaArchivosAdministrativo() + File.separator + nombre + "_tmp", secuencia, fechaF);
                            } else {
                                for (FirmasArchivoAdministrativo firma : listaFirmasAdministrativas) {
                                    if (arch.equals(firma.getArchivoAdministrativo())) {
                                        String nombreArchTmp = firma.getRuta();
                                        textoF = firma.getTextoFinal();
                                        secuencia = firma.getNroFinal();
                                        nomenclatura = firma.getNomenclaturaFinal();
                                        codigoArchivo = firma.getCodigoArchivo();

                                        nombreArch = simpleDateFormat.format(fecha) + "_" + arch.getDocumentoAdministrativo().getId() + "_" + arch.getId() + ".pdf";
                                        File file = new File(par.getRutaArchivosAdministrativo() + File.separator + nombreArchTmp);
                                        File file2 = new File(par.getRutaArchivosAdministrativo() + File.separator + nombreArch);
                                        file.renameTo(file2);
                                        break;
                                    }
                                }
                            }

                            if (nombreArch == null) {
                                return null;
                            }

                            /*
                                    transf.setUsuarioFirmado(usuario);
                                    transf.setFechaHoraFirmado(fecha);
                                    transf.setFirmado(true);
                                    transferenciasDocumentoAdministrativoController.setSelected(transf);
                                    transferenciasDocumentoAdministrativoController.save(null);

                                    doc.setRuta(nombreArch);
                                    doc.setTextoFinal(doc.getTexto());
                                    doc.setNroFinal(secuencia);
                                    doc.setFechaFinal(fechaFinal);

                                    archivosController.setSelected(doc);

                                    archivosController.save(null);
                             */
                            arch.setRuta(nombreArch);
                            arch.setTextoFinal(textoF);
                            arch.setNroFinal(secuencia);
                            arch.setNomenclaturaFinal(nomenclatura);
                            arch.setFechaFinal(fechaF);
                            arch.setCodigoArchivo(codigoArchivo);

                            archivosController.setSelected(arch);
                            archivosController.save(null);

                            docImprimir = arch;
                            // TransferenciasDocumentoAdministrativo transf = listaTransf.get(0);
                            transf.setEstado(est.get(0));
                            transf.setUsuarioRevisado(usuario);
                            transf.setFechaHoraRevisado(fecha);
                            transf.setFechaHoraTransferencia(fecha);
                            transf.setRevisado(true);

                            transf.setUsuarioFirmado(usuario);
                            transf.setFechaHoraFirmado(fecha);
                            transf.setFirmado(true);

                            transferenciasDocumentoAdministrativoController.setSelected(transf);
                            transferenciasDocumentoAdministrativoController.save(null);

                            getSelected().setFechaHoraUltimoEstado(fecha);
                            getSelected().setUsuarioUltimoEstado(usuario);
                            getSelected().setResponsableAnterior(getSelected().getResponsable());
                            getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                            getSelected().setResponsable(usuDest);
                            getSelected().setDepartamento(usuDest.getDepartamento());
                            getSelected().setFechaPresentacion(fechaF);
                            getSelected().setTextoFinal(textoF);
                            getSelected().setNroFinal(secuencia);
                            getSelected().setNomenclaturaFinal(nomenclatura);
                            getSelected().setCodigoArchivo(codigoArchivo);

                            getSelected().setEstado(new EstadosDocumentoAdministrativo(Constantes.ESTADO_USUARIO_AC));

                            this.save(null);

                            if (!firmadoPorToken) {
                                modificarNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), true);
                            }

                            if (getSelected().getDocumentoAdministrativo() != null) {
                                List<EstadosDocumentoAdministrativo> listaEstado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();

                                if (!listaEstado.isEmpty()) {
                                    DocumentosAdministrativos docAnt = getSelected();
                                    DocumentosAdministrativos docResp = getSelected().getDocumentoAdministrativo();
                                    docResp.setEstado(listaEstado.get(0));
                                    setSelected(docResp);
                                    super.save(null);

                                    setSelected(docAnt);
                                }

                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return transf;
    }

    /*
    public void actualizarRevisado() {
        try {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch.getFormato() != null) {
                    TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    if (!verifFirma(arch, transf)) {
                        return;
                    }

                    usuariosPorDocumentosAdministrativosActual = null;
                    transf = actualizarPrimerDoc();


                    List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();

                    List<UsuariosPorDocumentosAdministrativos> listaUtilizada = new ArrayList<>();

                    for (int i = 0; i < lista.size(); i++) {
                        UsuariosPorDocumentosAdministrativos usu = lista.get(i);
                        if (Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo())) {
                            if (!usu.equals(usuariosPorDocumentosAdministrativosActual)) {
                                actualizarPorDestinatario(usu, transf);
                                listaUtilizada.add(usu);
                            }
                        }
                    }

                    for (UsuariosPorDocumentosAdministrativos usu : listaUtilizada) {
                        usuariosPorDocumentosAdministrativosController.setSelected(usu);
                        usuariosPorDocumentosAdministrativosController.delete(null);
                    }

                    obtenerDatos(accion);

                    setSelected(null);
                    resetParentsBorradores();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

    }

    public void actualizarPorDestinatario(UsuariosPorDocumentosAdministrativos usuDest, TransferenciasDocumentoAdministrativo transfPar) {

        Date fecha = ejbFacade.getSystemDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

        EntradasDocumentosAdministrativos entradaDoc = new EntradasDocumentosAdministrativos();

        entradaDoc.setFechaHoraUltimoEstado(fecha);
        entradaDoc.setUsuarioUltimoEstado(usuario);
        entradaDoc.setFechaHoraAlta(fecha);
        entradaDoc.setUsuarioAlta(usuario);
        entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

        DocumentosAdministrativos doc = new DocumentosAdministrativos();

        doc.setCanalEntradaDocumentoAdministrativo(new CanalesEntradaDocumentoAdministrativo(Constantes.CANAL_ENTRADA_DOCUMENTO_ADMINISTRATIVO_MM));
        doc.setCaratula(getSelected().getCaratula());
        doc.setCausa(getSelected().getCausa());
        doc.setDepartamento(usuDest.getUsuario().getDepartamento());
        doc.setDepartamentoAnterior(getSelected().getDepartamentoAnterior());
        doc.setDescripcionMesaEntrada(getSelected().getDescripcionMesaEntrada());
        doc.setDocumentoAdministrativo(null);
        doc.setEntradaDocumentoAdministrativo(entradaDoc);
        doc.setEstado(getSelected().getEstado());
        doc.setEstadoAnterior(getSelected().getEstadoAnterior());
        doc.setEstadoProcesal(getSelected().getEstadoProcesal());
        doc.setFechaHoraAlta(fecha);
        doc.setFechaHoraBorrado(null);
        doc.setFechaHoraEstadoProcesal(null);
        doc.setFechaHoraUltimoEstado(fecha);
        doc.setFechaPresentacion(getSelected().getFechaPresentacion());
        doc.setFechaUltimaObservacion(null);
        doc.setMostrarWeb(getSelected().getMostrarWeb());
        doc.setMotivoProceso(getSelected().getMotivoProceso());
        doc.setObservacionDocumentoAdministrativo(null);
        doc.setPlazo(getSelected().getPlazo());
        doc.setResponsable(usuDest.getUsuario());
        doc.setResponsableAnterior(getSelected().getResponsableAnterior());
        doc.setSubcategoriaDocumentoAdministrativo(getSelected().getSubcategoriaDocumentoAdministrativo());
        doc.setTipoDocumentoAdministrativo(getSelected().getTipoDocumentoAdministrativo());
        doc.setUltimaObservacion(null);
        doc.setUsuarioAlta(usuario);
        doc.setUsuarioBorrado(null);
        doc.setUsuarioEstadoProcesal(null);
        doc.setUsuarioUltimaObservacion(null);
        doc.setUsuarioUltimoEstado(usuario);

        DocumentosAdministrativos docAnt = getSelected();

        setSelected(doc);

        this.saveNew(null);

        setSelected(docAnt);

        TransferenciasDocumentoAdministrativo transf = new TransferenciasDocumentoAdministrativo();

        transf.setDepartamentoDestino(transfPar.getDepartamentoDestino());
        transf.setDepartamentoOrigen(transfPar.getDepartamentoOrigen());
        transf.setDocumentoAdministrativo(doc);
        transf.setEstado(transfPar.getEstado());
        transf.setFechaHoraAlta(transfPar.getFechaHoraAlta());
        transf.setFechaHoraBorrado(null);
        //transf.setFechaHoraFirmado(transfPar.getFechaHoraFirmado());
        transf.setFechaHoraFirmado(null);
        transf.setFechaHoraRevisado(transfPar.getFechaHoraRevisado());
        transf.setFechaHoraTransferencia(transfPar.getFechaHoraTransferencia());
        transf.setFechaHoraUltimoEstado(transfPar.getFechaHoraUltimoEstado());
        //transf.setFirmado(transfPar.isFirmado());
        transf.setFirmado(false);
        transf.setResponsableDestino(usuDest.getUsuario());
        transf.setResponsableOrigen(transfPar.getResponsableOrigen());
        transf.setRevisado(transfPar.isRevisado());
        transf.setUsuarioAlta(transfPar.getUsuarioAlta());
        transf.setUsuarioBorrado(null);
        transf.setUsuarioFirma(transfPar.getUsuarioFirma());
        // transf.setUsuarioFirmado(transfPar.getUsuarioFirmado());
        transf.setUsuarioFirmado(null);
        transf.setUsuarioRevisado(transfPar.getUsuarioRevisado());
        transf.setUsuarioRevision(transfPar.getUsuarioRevision());
        transf.setUsuarioUltimoEstado(transfPar.getUsuarioUltimoEstado());
        transferenciasDocumentoAdministrativoController.setSelected(transf);
        transferenciasDocumentoAdministrativoController.saveNew(null);

        

        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transfPar).getResultList();

        for (UsuariosPorDocumentosAdministrativos usu : lista) {
            if (!Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo()) || usu.equals(usuDest)) {
                UsuariosPorDocumentosAdministrativos usua = new UsuariosPorDocumentosAdministrativos();
                usua.setDocumentoAdministrativo(doc);
                usua.setFechaHoraAlta(usu.getFechaHoraAlta());
                usua.setFechaHoraBorrado(null);
                usua.setFechaHoraUltimoEstado(usu.getFechaHoraUltimoEstado());
                usua.setTipoEnvio(usu.getTipoEnvio());
                usua.setTransferenciaDocumentoAdministrativo(transf);
                usua.setUsuario(usu.getUsuario());
                usua.setUsuarioAlta(usu.getUsuarioAlta());
                usua.setUsuarioBorrado(null);
                usua.setUsuarioUltimoEstado(usu.getUsuarioUltimoEstado());

                usuariosPorDocumentosAdministrativosController.setSelected(usua);
                usuariosPorDocumentosAdministrativosController.saveNew(null);
            }

        }

        List<ArchivosAdministrativo> lista2 = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", transfPar.getDocumentoAdministrativo()).getResultList();
        boolean primerArchivo = true;
        for (ArchivosAdministrativo archivo : lista2) {
            ArchivosAdministrativo archi = new ArchivosAdministrativo();
            archi.setDescripcion(archivo.getDescripcion());
            archi.setDocumentoAdministrativo(doc);
            archi.setFechaHoraAlta(fecha);
            archi.setFechaHoraBorrado(null);
            archi.setFechaHoraUltimoEstado(archivo.getFechaHoraUltimoEstado());
            archi.setFormato(archivo.getFormato());
            // archi.setRuta(url);
            archi.setTextoFinal(archivo.getTextoFinal());
            archi.setTipoArchivo(archivo.getTipoArchivo());
            archi.setUsuarioAlta(archivo.getUsuarioAlta());
            archi.setUsuarioBorrado(null);
            archi.setUsuarioUltimoEstado(archivo.getUsuarioUltimoEstado());
            archi.setTexto(archivo.getTexto());
            archi.setFechaFinal(archivo.getFechaFinal());

            archivosController.setSelected(archi);
            archivosController.saveNew(null);

            if(primerArchivo){
                List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = new ArrayList<>();
                listaUsuDestinatario.add(usuDest);
                List<UsuariosPorDocumentosAdministrativos> listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                String nombreArchivo = generarPDF(simpleDateFormat.format(fecha) + "_" + archi.getId() + ".pdf", archi.getTipoArchivo(), archi.getTexto(), par.getRutaArchivosAdministrativo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), listaUsuRemitente, listaUsuDestinatario, listaUsuCC);

                try (
                    InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo + "_tmp"))) {

                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            
                firmar(transf, archi, par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo + "_tmp", secuencia, archi.getFechaFinal());
                incrementarNroSecuencia(archi.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento()); 
            }else{
                String nombreArchivo = simpleDateFormat.format(fecha);
                nombreArchivo += "_" + archi.getId() + ".pdf";

                try (
                    InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + archivo.getRuta()));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo))) {

                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                archi.setNroFinal(archivo.getNroFinal());
                archi.setRuta(nombreArchivo);
                archivosController.setSelected(archi);
                archivosController.save(null);
            }
            
            primerArchivo = false;
        }

    }

    public TransferenciasDocumentoAdministrativo actualizarPrimerDoc() {
        TransferenciasDocumentoAdministrativo transf = null;
        try {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch.getFormato() != null) {
                    List<EstadosTransferenciaDocumentoAdministrativo> est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO).getResultList();

                    if (!est.isEmpty()) {

                        
                        Date fecha = ejbFacade.getSystemDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                        transf = obtenerTransferenciaActual(getSelected());
                        
                        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();

                        Usuarios usuDest = null;
                        for (UsuariosPorDocumentosAdministrativos usu : lista) {
                            if (Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo())) {
                                usuariosPorDocumentosAdministrativosActual = usu;
                                usuDest = usu.getUsuario();
                                break;
                            }

                        }
                        // List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstado", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_IN)).getResultList();
                        if (transf != null) {

                            List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                            List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = new ArrayList<>();
                            listaUsuDestinatario.add(usuariosPorDocumentosAdministrativosActual);
                            List<UsuariosPorDocumentosAdministrativos> listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                            nombre = generarPDF(simpleDateFormat.format(fecha) + "_" + arch.getId() + ".pdf", arch.getTipoArchivo(), arch.getTexto(), par.getRutaArchivosAdministrativo(), listaUsuRemitente.get(0).getUsuario().getDepartamento(), listaUsuRemitente, listaUsuDestinatario, listaUsuCC);

                            try (
                                InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivosAdministrativo() + File.separator + nombre));  OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombre + "_tmp"))) {

                                byte[] buffer = new byte[1024];
                                int lengthRead;
                                while ((lengthRead = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, lengthRead);
                                    out.flush();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            firmar(transf, arch, par.getRutaArchivosAdministrativo() + File.separator + nombre + "_tmp", secuencia, fechaFinal);

                            docImprimir = arch;
                            // TransferenciasDocumentoAdministrativo transf = listaTransf.get(0);
                            transf.setEstado(est.get(0));
                            transf.setUsuarioRevisado(usuario);
                            transf.setFechaHoraRevisado(fecha);
                            transf.setFechaHoraTransferencia(fecha);
                            transf.setRevisado(true);
                            transferenciasDocumentoAdministrativoController.setSelected(transf);
                            transferenciasDocumentoAdministrativoController.save(null);
                        


                            getSelected().setFechaHoraUltimoEstado(fecha);
                            getSelected().setUsuarioUltimoEstado(usuario);
                            getSelected().setResponsableAnterior(getSelected().getResponsable());
                            getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                            getSelected().setResponsable(usuDest);
                            getSelected().setDepartamento(usuDest.getDepartamento());

                            getSelected().setEstado(new EstadosDocumentoAdministrativo(Constantes.ESTADO_USUARIO_AC));

                            this.save(null);
                            incrementarNroSecuencia(arch.getTipoArchivo(), listaUsuRemitente.get(0).getUsuario().getDepartamento()); 
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return transf;
    }
     */
 /*

    public void actualizarRevisado() {
        try {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch.getFormato() != null) {
                    List<EstadosTransferenciaDocumentoAdministrativo> est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO).getResultList();

                    if (!est.isEmpty()) {

                        Date fecha = ejbFacade.getSystemDate();

                        TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                        // List<TransferenciasDocumentoAdministrativo> listaTransf = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstado", TransferenciasDocumentoAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_IN)).getResultList();
                        if (transf != null) {
                            // TransferenciasDocumentoAdministrativo transf = listaTransf.get(0);
                            transf.setEstado(est.get(0));
                            transf.setUsuarioRevisado(usuario);
                            transf.setFechaHoraRevisado(fecha);
                            transf.setFechaHoraTransferencia(fecha);
                            transf.setRevisado(true);
                            transferenciasDocumentoAdministrativoController.setSelected(transf);
                            transferenciasDocumentoAdministrativoController.save(null);
                        }
                        
                        // TransferenciasDocumentoAdministrativo transferenciasDocumentoAdministrativo = new TransferenciasDocumentoAdministrativo();
                        // transferenciasDocumentoAdministrativo.setDocumentoAdministrativo(getSelected());

                        // transferenciasDocumentoAdministrativo.setResponsableOrigen(null);
                        // transferenciasDocumentoAdministrativo.setDepartamentoOrigen(null);

                        // transferenciasDocumentoAdministrativo.setResponsableDestino(getSelected().getResponsable());
                        // transferenciasDocumentoAdministrativo.setDepartamentoDestino(getSelected().getDepartamento());

                        // transferenciasDocumentoAdministrativo.setFechaHoraAlta(fecha);
                        // transferenciasDocumentoAdministrativo.setFechaHoraUltimoEstado(fecha);
                        // transferenciasDocumentoAdministrativo.setUsuarioAlta(usuario);
                        // transferenciasDocumentoAdministrativo.setUsuarioUltimoEstado(usuario);

                        // transferenciasDocumentoAdministrativoController.setSelected(transferenciasDocumentoAdministrativo);
                        // transferenciasDocumentoAdministrativoController.save(null);
                         
                        //// List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTransferirNULL", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).getResultList();

                        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();

                        Usuarios usuDest = null;
                        for (UsuariosPorDocumentosAdministrativos usu : lista) {
                            if (Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO.equals(usu.getTipoEnvio().getCodigo())) {
                                usuDest = usu.getUsuario();
                                break;
                            }
                            
                            // usu.setTransferenciaDocumentoAdministrativo(transferenciasDocumentoAdministrativoController.getSelected());
                            // usuariosPorDocumentosAdministrativosController.setSelected(usu);
                            // usuariosPorDocumentosAdministrativosController.save(null);
                             
                        }

                        getSelected().setFechaHoraUltimoEstado(fecha);
                        getSelected().setUsuarioUltimoEstado(usuario);
                        getSelected().setResponsableAnterior(getSelected().getResponsableAnterior());
                        getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                        getSelected().setResponsable(usuDest);
                        getSelected().setDepartamento(usuDest.getDepartamento());

                        getSelected().setEstado(new EstadosDocumentoAdministrativo(Constantes.ESTADO_USUARIO_AC));

                        this.save(null);
                        incrementarNroSecuencia(arch.getTipoArchivo(), usuario.getDepartamento());

                        
                        //String nombreArch = generarPDF(par.getRutaArchivosAdministrativo() + "/", transf);
                        ////docImprimir.setUsuarioRevisado(usuario);
                        ////docImprimir.setFechaHoraRevisado(ejbFacade.getSystemDate());
                        //docImprimir.setRuta(nombreArch);
                        //docImprimir.setTextoFinal(docImprimir.getTexto());
                        //docImprimir.setNroFinal(secuencia);
                        //docImprimir.setFechaFinal(fechaFinal);
                        ////docImprimir.setRevisado(true);
                        //// docImprimir.setEstado(est.get(0));
                        //archivosController.setSelected(docImprimir);
                        //archivosController.save(null);
                         
                        obtenerDatos(accion);

                        
                        // List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();

                        // if(!lista.isEmpty()){
                        //    getSelected().setResponsableAnterior(getSelected().getResponsable());
                        //    getSelected().setResponsable(lista.get(0).getUsuario());
                        //    getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                        //    getSelected().setDepartamento(lista.get(0).getUsuario().getDepartamento());

                        //    super.save(null);
                        //}  
                         
                        setSelected(null);
                        resetParentsBorradores();
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
    }
        
     */
    public void modifUsuarioEnviar() {
        listaUsuariosEnviar = new ArrayList<>();
        listaUsuariosEnviar.add(usuarioEnviar);
    }

    public void modifUsuarioRemitente() {
        listaUsuariosRemitente = new ArrayList<>();
        listaUsuariosRemitente.add(usuarioRemitente);
        listaUsuariosEnviar = new ArrayList<>();
        listaUsuariosCC = new ArrayList<>();
        usuarioEnviar = null;
        usuarioCC = null;
        listaPosiblesUsuariosCC = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        listaPosiblesUsuariosEnviar = obtenerPosiblesDestinatarios(usuarioRemitente.getDepartamento());
        /*
        for (Usuarios usu : listaUsuariosEnviar) {
            if (agregarSuperiorInmediatoCC(usuarioRemitente, usu)) {
                break;
            }
        }
         */
    }

    private boolean agregarSuperiorInmediatoCC(Usuarios usuRem, Usuarios usu) {

        Integer nivel = obtenerNivelDepartamento(usuRem.getDepartamento());

        if (nivel != 2) { // Nivel de Direccion General
            boolean esSubalterno = destinatarioEsSubalterno(usuRem.getDepartamento(), usu.getDepartamento());
            if (!esSubalterno) {
                // List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuRem.getDepartamento()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

                Usuarios superior = obtenerSuperiorInmediato(usuRem.getDepartamento());

                if (superior != null) {

                    if (superior.equals(usuRem) && usuRem.getDepartamento() != null) {
                        // listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuRem.getDepartamento().getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                        superior = obtenerSuperiorInmediato(usuRem.getDepartamento().getDepartamentoPadre());
                    }

                    if (superior != null) {
                        if (!superior.equals(usu)) {

                            Usuarios usuEncontrado = null;
                            boolean encontro2 = false;
                            for (Usuarios usu2 : listaUsuariosCC) {
                                if (usu2.equals(superior)) {
                                    encontro2 = true;
                                    usuEncontrado = usu2;
                                }
                            }

                            if (encontro2 && usuEncontrado != null) {
                                listaUsuariosCC.remove(usuEncontrado);
                                usuEncontrado.setIncluidoAutomaticamente(true);
                                listaUsuariosCC.add(usuEncontrado);
                            }

                            boolean encontro3 = false;
                            for (Usuarios usu3 : listaUsuariosEnviar) {
                                if (usu3.equals(superior)) {
                                    encontro3 = true;
                                }
                            }

                            if (!encontro2 && !encontro3) {
                                superior.setIncluidoAutomaticamente(true);
                                listaUsuariosCC.add(superior);
                            }
                        }
                    }
                }
            }
            return !esSubalterno;
        }
        return false;
    }

    /*
    private boolean agregarSuperiorInmediatoCC(Usuarios usuRem, Usuarios usu) {

        Integer nivel = obtenerNivelDepartamento(usuRem.getDepartamento());

        if (nivel != 2) { // Nivel de Direccion General
            boolean esSubalterno = destinatarioEsSubalterno(usuRem.getDepartamento(), usu.getDepartamento());
            if (!esSubalterno) {
                // documentosAdministrativosController.usuarioCC, documentosAdministrativosController.listaUsuariosCC
                List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuRem.getDepartamento()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

                if (!listaUsu.isEmpty()) {

                    if (listaUsu.get(0).equals(usuRem) && usuRem.getDepartamento() != null) {
                        listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuRem.getDepartamento().getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                    }

                    if (!listaUsu.isEmpty()) {
                        if (!listaUsu.get(0).equals(usu)) {

                            Usuarios usuEncontrado = null;
                            boolean encontro2 = false;
                            for (Usuarios usu2 : listaUsuariosCC) {
                                if (usu2.equals(listaUsu.get(0))) {
                                    encontro2 = true;
                                    usuEncontrado = usu2;
                                }
                            }

                            if (encontro2 && usuEncontrado != null) {
                                listaUsuariosCC.remove(usuEncontrado);
                                usuEncontrado.setIncluidoAutomaticamente(true);
                                listaUsuariosCC.add(usuEncontrado);
                            }

                            boolean encontro3 = false;
                            for (Usuarios usu3 : listaUsuariosEnviar) {
                                if (usu3.equals(listaUsu.get(0))) {
                                    encontro3 = true;
                                }
                            }

                            if (!encontro2 && !encontro3) {
                                listaUsu.get(0).setIncluidoAutomaticamente(true);
                                listaUsuariosCC.add(listaUsu.get(0));
                            }
                        }
                    }
                }
            }
            return !esSubalterno;
        }
        return false;
    }
     */

 /*
    public boolean destinatarioFueraDepartamentoPadre(Usuarios usuActual, Usuarios usuAgregar) {

        if (usuActual != null) {
            if (usuActual.getDepartamento() != null) {
                List<Usuarios> lista = obtenerUsuarios(usuActual.getDepartamento().getDepartamentoPadre() == null ? usuActual.getDepartamento() : usuActual.getDepartamento().getDepartamentoPadre());

                for (Usuarios usu : lista) {
                    if (usu.equals(usuAgregar)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
     */
    public boolean destinatarioEsSubalterno(Departamentos dptoActual, Departamentos dptoVerif) {
        Departamentos dptoTest = dptoVerif;
        while (dptoTest != null) {
            if (dptoTest.equals(dptoActual)) {
                return true;
            }
            dptoTest = dptoTest.getDepartamentoPadre();
        }

        return false;
    }

    public void agregarUsuario(Usuarios usu, List<Usuarios> lista) {

        if (usu != null) {

            if (lista == null) {
                lista = new ArrayList<>();
            }

            boolean encontro = false;
            for (Usuarios us : lista) {
                if (us.equals(usu)) {
                    encontro = true;
                }
            }

            if (!encontro) {
                lista.add(usu);
                if (listaUsuariosEnviar.equals(lista)) {
                    agregarSuperiorInmediatoCC(usuarioRemitente, usu);
                }
            }
        }
    }

    /*
    public void borrarUsuario(Usuarios usuActual, List<Usuarios> lista) {

        if (lista != null) {
            if (usuActual != null) {

                if (!usuActual.isIncluidoAutomaticamente()) {
                    lista.remove(usuActual);
                    if (listaUsuariosEnviar.equals(lista)) {

                        boolean encontro = false;
                        for (Usuarios usu : lista) {
                            if (!destinatarioEsSubalterno(usuarioRemitente.getDepartamento(), usu.getDepartamento())) {
                                encontro = true;
                                break;
                            }
                        }

                        if (!encontro) {
                            List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuario.getDepartamento().getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

                            if (!listaUsu.isEmpty()) {
                                //if (listaUsu.get(0).equals(usuActual)) {
                                listaUsuariosCC.remove(listaUsu.get(0));
                                //}
                            }
                        }
                    }
                } else {
                    JsfUtil.addErrorMessage("No se puede borrar una persona agregada automáticamente");
                }
            }
        }
    }
     */
    public void borrarUsuario(Usuarios usuActual, List<Usuarios> lista) {

        if (lista != null) {
            if (usuActual != null) {

                if (!usuActual.isIncluidoAutomaticamente()) {
                    usuarioEnviar = null;
                    lista.remove(usuActual);
                    if (listaUsuariosEnviar.equals(lista)) {

                        boolean encontro = false;
                        for (Usuarios usu : lista) {
                            if (!destinatarioEsSubalterno(usuarioRemitente.getDepartamento(), usu.getDepartamento())) {
                                encontro = true;
                                break;
                            }
                        }

                        if (!encontro) {
                            // List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usuario.getDepartamento().getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                            Usuarios superior = obtenerSuperiorInmediato(usuario.getDepartamento().getDepartamentoPadre());

                            if (superior != null) {
                                //if (listaUsu.get(0).equals(usuActual)) {
                                listaUsuariosCC.remove(superior);
                                //}
                            }
                        }
                    }
                } else {
                    JsfUtil.addErrorMessage("No se puede borrar una persona agregada automáticamente");
                }
            }
        }
    }

    public void saveUsuarios(TiposEnvio tipoEnvio, List<Usuarios> lista, TransferenciasDocumentoAdministrativo transf) {
        List<UsuariosPorDocumentosAdministrativos> listaUsuariosActual = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvio).getResultList();
        saveUsuarios(tipoEnvio, getSelected(), lista, listaUsuariosActual, transf);
    }

    public void saveUsuarios(TiposEnvio tipoEnvio, DocumentosAdministrativos doc, List<Usuarios> listaUsuariosEnv, List<UsuariosPorDocumentosAdministrativos> listaUsuariosActual, TransferenciasDocumentoAdministrativo transf) {

        try {
            Date fecha = ejbFacade.getSystemDate();
            Usuarios usu2 = null;
            boolean encontro = false;

            for (Usuarios usu : listaUsuariosEnv) {
                encontro = false;
                for (int i = 0; i < listaUsuariosActual.size(); i++) {
                    usu2 = listaUsuariosActual.get(i).getUsuario();
                    if (usu2.equals(usu)) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    UsuariosPorDocumentosAdministrativos usuDoc = new UsuariosPorDocumentosAdministrativos();
                    usuDoc.setUsuario(usu);
                    usuDoc.setDepartamento(usu.getDepartamento());
                    usuDoc.setDocumentoAdministrativo(doc);
                    usuDoc.setTransferenciaDocumentoAdministrativo(transf);
                    usuDoc.setUsuarioAlta(usuario);
                    usuDoc.setFechaHoraAlta(fecha);
                    usuDoc.setUsuarioUltimoEstado(usuario);
                    usuDoc.setFechaHoraUltimoEstado(fecha);
                    usuDoc.setIncluidoAutomaticamente(usu.isIncluidoAutomaticamente());

                    usuDoc.setTipoEnvio(tipoEnvio);

                    usuariosPorDocumentosAdministrativosController.setSelected(usuDoc);
                    usuariosPorDocumentosAdministrativosController.saveNew(null);
                }
            }

            for (int i = 0; i < listaUsuariosActual.size(); i++) {
                usu2 = listaUsuariosActual.get(i).getUsuario();
                encontro = false;
                for (Usuarios per : listaUsuariosEnv) {
                    if (usu2.equals(per)) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    usuariosPorDocumentosAdministrativosController.setSelected(listaUsuariosActual.get(i));
                    usuariosPorDocumentosAdministrativosController.delete(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean renderedParaRevision() {
        if (Constantes.ACCION_EN_PROYECTO.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean renderedParaElaboracion() {
        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean renderedResponderA() {
        if (getSelected() != null) {
            return getSelected().getDocumentoAdministrativo() != null;
        }

        return false;
    }
    
    public boolean renderedAutoGuardado(){
        if (getSelected() != null) {
            return autoGuardado;
        }

        return false;
    }

    /*
    public boolean deshabilitarParaRevision(ArchivosAdministrativo arch) {
        if (Constantes.ACCION_EN_PROYECTO.equals(accion)) {
            if(arch!=null){
                // return !Constantes.ESTADO_ARCHIVO_ADM_PROYECTO.equals(arch.getEstado().getCodigo()) || !usuario.equals(arch.getUsuarioRevision());
                
                List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", arch.getDocumentoAdministrativo()).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                
                if(!lista.isEmpty()){
                    return !(Constantes.ESTADO_ARCHIVO_ADM_PROYECTO.equals(arch.getEstado().getCodigo()) && !usuario.equals(arch.getUsuarioRevision()));
                }
                
            }
        }
        return true;
    }
     */
    public boolean deshabilitarParaRevision() {
        if(usuarioBandeja == null){
            return true;
        }
        if (Constantes.ACCION_EN_PROYECTO.equals(accion)) {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch != null) {
                    TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    if (transf != null) {
                        return !(Constantes.ESTADO_ARCHIVO_ADM_PROYECTO.equals(transf.getEstado().getCodigo()) && !usuario.equals(transf.getUsuarioRevision()));
                    }
                }

            }
        }
        return true;
    }

    public boolean deshabilitarParaElaboracion() {
        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            if (getSelected() != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch != null) {
                    TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                    if (transf != null) {
                        if (transf.isFirmado()) {
                            return true;
                        }
                        return !(Constantes.ESTADO_ARCHIVO_ADM_REVISION.equals(transf.getEstado().getCodigo()) && usuario.equals(transf.getUsuarioRevision()) && !getSelected().getUsuarioAlta().equals(getSelected().getResponsable()));
                    }
                }

            }
        }
        return true;
    }

    public boolean renderedRevisado() {

        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            return true;
        }

        return false;
    }

    public boolean renderedFirmar() {

        if (Constantes.ACCION_PARA_REVISION.equals(accion) || Constantes.ACCION_EN_PROYECTO.equals(accion)) {
            return true;
        }

        return false;
    }

    public boolean renderedEnviar() {

        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            return true;
        }

        return false;
    }

    /*
    public boolean deshabilitarRevisado(ArchivosAdministrativo arch) {
        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            if (arch != null) {
                // if (filtroURL.verifPermiso(Constantes.PERMISO_ARCHIVO_ADM_REVISADO)) {
                    return !(!arch.isRevisado() && usuario.equals(arch.getUsuarioRevision()) && Constantes.ESTADO_ARCHIVO_ADM_REVISION.equals(arch.getEstado().getCodigo()));
                // }
            }
        }
        return true;
    }
     */
    public boolean deshabilitarRevisado() {
        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            if (transf != null) {
                // if (filtroURL.verifPermiso(Constantes.PERMISO_ARCHIVO_ADM_REVISADO)) {
                return !(!transf.isRevisado() && !transf.isFirmado() && usuario.equals(transf.getUsuarioRevision()) && Constantes.ESTADO_ARCHIVO_ADM_REVISION.equals(transf.getEstado().getCodigo()));
                // }
            }
        }
        return true;
    }

    public boolean deshabilitarFirmar() {
        if (Constantes.ACCION_PARA_REVISION.equals(accion) || Constantes.ACCION_EN_PROYECTO.equals(accion)) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            if (transf != null) {
                // if (filtroURL.verifPermiso(Constantes.PERMISO_ARCHIVO_ADM_REVISADO)) {
                return !(!transf.isRevisado() && !transf.isFirmado() && usuario.equals(transf.getUsuarioRevision()) && (Constantes.ESTADO_ARCHIVO_ADM_REVISION.equals(transf.getEstado().getCodigo()) || Constantes.ESTADO_ARCHIVO_ADM_PROYECTO.equals(transf.getEstado().getCodigo())));
                // }
            }
        }
        return true;
    }

    public boolean deshabilitarEnviar() {
        if (Constantes.ACCION_PARA_REVISION.equals(accion)) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            if (transf != null) {
                // if (filtroURL.verifPermiso(Constantes.PERMISO_ARCHIVO_ADM_REVISADO)) {
                return !(transf.isFirmado() && usuario.equals(transf.getUsuarioRevision()) && Constantes.ESTADO_ARCHIVO_ADM_REVISION.equals(transf.getEstado().getCodigo()));
                // }
            }
        }
        return true;
    }

    /*
    public boolean deshabilitarRevisado() {
        return deshabilitarRevisado(docImprimir);
    }
     */
 /*
    public boolean deshabilitarEditar(ArchivosAdministrativo arch) {
        if (arch != null) {
            if(Constantes.ACCION_EN_PROYECTO.equals(accion) && (Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(arch.getEstado().getCodigo()) || arch.getRuta() != null || arch.isRevisado())){
                return false;
            }else if(Constantes.ACCION_EN_REVISION.equals(accion) && usuario.equals(arch.getUsuarioRevision()) && !(Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(arch.getEstado().getCodigo()) || arch.getRuta() != null || arch.isRevisado())){
                return false;
            }else if(!(Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(arch.getEstado().getCodigo()) || arch.getRuta() != null || arch.isRevisado())){
                return false;
            }
        }
        return true;
    }
     */
    public boolean deshabilitarBorrar() {
        if (getSelected() != null) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            if (transf != null) {
                return transf.isFirmado();
            }
        }
        return true;
    }

    public boolean deshabilitarEditar() {
        if (getSelected() != null) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            if (transf != null) {
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());
                if (arch != null) {
                    if (Constantes.ACCION_EN_PROYECTO.equals(accion) && !(Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(transf.getEstado().getCodigo()) || arch.getRuta() != null || transf.isFirmado())) {
                        return false;
                    } else if (Constantes.ACCION_PARA_REVISION.equals(accion) && usuario.equals(transf.getUsuarioRevision()) && !(Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(transf.getEstado().getCodigo()) || arch.getRuta() != null || transf.isFirmado())) {
                        return false;
                        /*
                    } else if (!(Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO.equals(transf.getEstado().getCodigo()) || arch.getRuta() != null || transf.isFirmado())) {
                        return false;
                         */
                    }
                }
            }
        }
        return true;
    }

    public void prepareCerrarDialogoPrevisualizar() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();
        }
    }

    public boolean deshabilitarCamposEdicionActuacion() {
        return false;
        /*
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                if (actuacion.getEstado() != null) {

                    if (Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {
                        if (actuacion.getPreopinante() != null) {
                            return !(esPersonaAsociada(actuacion.getPreopinante(), personaUsuario) && !actuacion.getPreopinante().equals(personaUsuario));
                        }
                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {
                        boolean resp = (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;;
                        return resp;
                        //return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante() == personaUsuario) : true;
                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(actuacion.getEstado().getCodigo())) {
                        if (actuacion.getPreopinante() != null) {
                            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", actuacion).setParameter("personaFirmante", actuacion.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                            if (lista.isEmpty()) {
                                //JsfUtil.addErrorMessage("No se encuentra persona firmante para el preopinante");
                                return true;
                            }

                            if (lista.get(0).isRevisado()) {
                                return !(actuacion.getPreopinante().equals(personaUsuario));
                            } else {
                                return !esPersonaAsociada(actuacion.getPreopinante(), personaOrigen);
                            }
                        }
                        return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;
                    }

                    List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                    if (!lista.isEmpty()) {
                        return lista.get(0).isDeshabilitar();
                    }
                }
            }
        }
        return true;
         */
    }

    public boolean deshabilitarEstadoArchivoCreate() {
        return true;
    }

    /*
    public boolean deshabilitarFormatos() {
        if (archivo != null) {
            return (archivo.getTipoArchivo() == null) ? false : deshabilitarCamposEdicionActuacion();
        }
        return true;
    }
     */
    public boolean deshabilitarFormatos() {
        return subcategoriaDocumentoAdministrativo == null;
    }

    public void actualizarTexto() {
        if (archivo != null) {
            if (archivo.getFormato() != null) {
                archivo.setTexto(archivo.getFormato().getTexto());
            }
        }
    }

    public void buscarFormatos() {
        prepareCerrarDialogoPrevisualizar();
        if (subcategoriaDocumentoAdministrativo != null) {
            if (subcategoriaDocumentoAdministrativo.getTipoArchivo() != null) {
                if (archivo != null) {
                    archivo.setTipoArchivo(subcategoriaDocumentoAdministrativo.getTipoArchivo());
                }
                listaFormatos = ejbFacade.getEntityManager().createNamedQuery("FormatosArchivoAdministrativo.findByTipoArchivoANDDepartamento", FormatosArchivoAdministrativo.class).setParameter("tipoArchivo", subcategoriaDocumentoAdministrativo.getTipoArchivo()).setParameter("departamento", usuario.getDepartamento()).getResultList();

                // listaFormatos = ejbFacade.getEntityManager().createNamedQuery("FormatosArchivoAdministrativo.findByTipoArchivo", FormatosArchivoAdministrativo.class).setParameter("tipoArchivo", subcategoriaDocumentoAdministrativo.getTipoArchivo()).getResultList();
                return;
            }
        }

        listaFormatos = null;
    }

    public String obtenerSgteNroMesaEntradaAdministrativa() {

        Date fecha = ejbFacade.getSystemDate();

        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery("select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_administrativos as d, entradas_documentos_administrativos as e where d.entrada_documento_administrativo = e.id AND d.tipo_documento_administrativo in ('" + tipoDocumentoAdministrativo.getCodigo() + "') AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return Utils.padLeft(String.valueOf(cod.getCodigo() + 1) + "-" + formatAno.format(fecha), "0", 6);
    }

    public void cambiarSubcategorias() {
        listaSubcategoriaDocumentoAdministrativo = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosAdministrativos.findByTipoDocumentoAdministrativoEstado", SubcategoriasDocumentosAdministrativos.class).setParameter("tipoDocumentoAdministrativo", tipoDocumentoAdministrativo).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    public void cambiarSubcategorias(Integer id) {
        if (id == 2) {
            listaSubcategoriaDocumentoAdministrativo = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosAdministrativos.findByEstado", SubcategoriasDocumentosAdministrativos.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        } else {
            listaSubcategoriaDocumentoAdministrativo = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosAdministrativos.findByIdANDEstado", SubcategoriasDocumentosAdministrativos.class).setParameter("id", id).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        }
    }

    public void prepareHistorico(DocumentosAdministrativos doc) {
        prepareHistorico(doc, false);
    }

    public void prepareHistorico(DocumentosAdministrativos doc, boolean incluirEnProyecto) {
        listaHistorico = new ArrayList<>();

        DocumentosAdministrativos d = doc;
        while (true) {
            DocumentosAdministrativos docu = null;

            try {
                docu = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findByDocumentoAdministrativo", DocumentosAdministrativos.class).setParameter("documentoAdministrativo", d).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // if (docu != null && !docu.getEstado().getCodigo().equals(Constantes.ESTADO_DOC_ADM_EN_PROYECTO)) {
            if (docu != null) {
                d = docu;
            } else {
                break;
            }
        }

        while (d != null) {
            DocumentosAdministrativos docu = null;

            try {
                docu = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", d.getId()).getSingleResult();
            } catch (Exception e) {
                break;
            }

            if (!docu.getEstado().getCodigo().equals(Constantes.ESTADO_DOC_ADM_EN_PROYECTO) || incluirEnProyecto) {
                listaHistorico.add(docu);
            }

            d = docu.getDocumentoAdministrativo();
        }

        listaArchivosHistorico = new ArrayList<>();
        listaDestinatariosHistorico = new ArrayList<>();

    }

    public DocumentosAdministrativos prepareCreate(DocumentosAdministrativos doc, int id) {

        DocumentosAdministrativos d = prepareCreate();

        esResponder = id == 1;

        docResponder = doc;
        cambiarSubcategorias(id);

        if (id == 1) {
            leyendaResponderReenviar = "RESPONDER A:";

            /*
                    List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", doc).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

                    usuarioEnviar = null;
                    if(!lista.isEmpty()){
                        listaPosiblesUsuariosEnviar = new ArrayList<>();
                        listaPosiblesUsuariosEnviar.add(lista.get(0).getUsuario());
                        listaUsuariosEnviar = new ArrayList<>();
                        listaUsuariosEnviar.add(lista.get(0).getUsuario());
                        usuarioEnviar = lista.get(0).getUsuario();
                    }
             */
            Usuarios superior = obtenerSuperiorInmediato(docResponder.getDepartamentoAnterior());

            usuarioEnviar = null;
            if (superior != null) {
                listaPosiblesUsuariosEnviar = new ArrayList<>();
                listaPosiblesUsuariosEnviar.add(superior);
                listaUsuariosEnviar = new ArrayList<>();
                listaUsuariosEnviar.add(superior);
                usuarioEnviar = superior;
            }

        } else {
            leyendaResponderReenviar = "REENVIAR:";
        }

        d.setDocumentoAdministrativo(doc);

        responderA = doc.getDescripcionMesaEntrada();

        /*
                PrimeFaces.current().ajax().update("DocumentosJudicialesHistorialForm");

                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosJudicialesHistorialDialog').show();");
         */
        return d;

    }

    /*
    private List<Usuarios> obtenerPosiblesRemitentes(Usuarios usu) {
        // return this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        List<Usuarios> listaUsu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usu.getDepartamento()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

        if (!listaUsu.isEmpty()) {
            if (usuario.equals(listaUsu.get(0)) && usu.getDepartamento() != null && usu.getDepartamento().getDepartamentoPadre() != null) {
                List<Usuarios> listaUsu2 = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", usu.getDepartamento().getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                listaUsu.addAll(listaUsu2);
            }
        }
        return listaUsu;
    }
     */
    private List<Usuarios> obtenerPosiblesRemitentes(Usuarios usu) {
        List<Usuarios> listaUsu = new ArrayList<>();

        Usuarios superior = obtenerSuperiorInmediato(usu.getDepartamento());

        if (superior != null) {
            listaUsu.add(superior);
            if (usu.equals(superior) && usu.getDepartamento() != null && usu.getDepartamento().getDepartamentoPadre() != null) {
                Usuarios superior2 = obtenerSuperiorInmediato(usu.getDepartamento().getDepartamentoPadre());
                if (superior2 != null) {
                    listaUsu.add(superior2);
                }
            }
        }

        return listaUsu;
    }

    private int obtenerNivelDepartamento(Departamentos dptoPar) {
        Departamentos dptoActual = dptoPar.getDepartamentoPadre();
        int nivel = 1;
        while (dptoActual != null) {
            nivel++;
            dptoActual = dptoActual.getDepartamentoPadre();
        }
        return nivel;
    }

    /*
    private List<Usuarios> obtenerUsuarios(Departamentos dpto) {
        // return = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        List<Usuarios> listaUsu = new ArrayList<>();
        List<Usuarios> listaUsuTemp = null;

        int nivel = obtenerNivelDepartamento(dpto);
        listaUsuTemp = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRol", Usuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

        if (nivel > 1) {
            for (Usuarios usua : listaUsuTemp) {
                if(!usua.getDepartamento().equals(dpto)){
                    int niv = obtenerNivelDepartamento(usua.getDepartamento());
                    if (niv <= nivel) {
                        listaUsu.add(usua);
                    }
                }
            }

        } else {
            listaUsu.addAll(listaUsuTemp);
        }

        return listaUsu;
    }
     */
    private List<Usuarios> obtenerMaximoNivel() {
        List<Usuarios> lista = new ArrayList<>();
        List<Departamentos> listaDptos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstadoANDEsMaximoNivel", Departamentos.class).setParameter("estado", new Estados("AC")).setParameter("esMaximoNivel", true).getResultList();
        for (Departamentos dpto : listaDptos) {
            Usuarios superior = obtenerSuperiorInmediato(dpto);
            if (superior != null) {
                lista.add(superior);
            }
        }

        return lista;
    }

    private List<Usuarios> obtenerEncargados() {
        List<Usuarios> lista = new ArrayList<>();
        List<Departamentos> listaDptos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados("AC")).getResultList();
        for (Departamentos dpto : listaDptos) {
            Usuarios superior = obtenerSuperiorInmediato(dpto, false);
            if (superior != null) {
                lista.add(superior);
            }
        }

        return lista;
    }

    private List<Usuarios> obtenerPosiblesDevolver(Usuarios usu) {
        List<Usuarios> listaUsu = new ArrayList<>();
        List<Usuarios> listaUsuTemp = null;

        listaUsuTemp = obtenerEncargados();

        for (Usuarios usua : listaUsuTemp) {
            if (destinatarioEsSubalterno(usu.getDepartamento(), usua.getDepartamento())) {
                if (!usua.equals(usu)) {
                    listaUsu.add(usua);
                }
            }
        }

        return listaUsu;
    }

    /*
    private List<Usuarios> obtenerPosiblesDevolver(Usuarios usu) {
        List<Usuarios> listaUsu = new ArrayList<>();
        List<Usuarios> listaUsuTemp = null;

        listaUsuTemp = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRol", Usuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

        for (Usuarios usua : listaUsuTemp) {
            if (destinatarioEsSubalterno(usu.getDepartamento(), usua.getDepartamento())) {
                if (!usua.equals(usu)) {
                    listaUsu.add(usua);
                }
            }
        }

        return listaUsu;
    }
     */

 /*
    private List<Usuarios> obtenerPosiblesDestinatarios(Departamentos dpto) {
        if ((dpto.getDepartamentoPadre() == null && dpto.isEsMaximoNivel()) || dpto.isEsMaximoNivel() || dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA) || dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_AUDITORIA)) {
            // return this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
            return this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRol", Usuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
        } else {
            List<Usuarios> listaUsu = new ArrayList<>();
            List<Usuarios> listaUsuTemp = null;

            int nivel = obtenerNivelDepartamento(dpto);
            listaUsuTemp = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRol", Usuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();

            List<Usuarios> listaSup = null;

            if (dpto.getDepartamentoPadre() != null) {
                listaSup = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findSuperiorInmediato", Usuarios.class).setParameter("departamento", dpto.getDepartamentoPadre()).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
            }

            if (nivel > 1) {

                if (listaSup != null) {
                    if (!listaSup.isEmpty()) {
                        if (listaSup.size() == 1) {
                            listaUsu.add(listaSup.get(0));
                        } else {
                            JsfUtil.addErrorMessage("La dependencia " + dpto.getDepartamentoPadre().getNombre() + " tiene mas de un encargado. Corrija esto antes de continuar");
                        }
                    } else {
                        JsfUtil.addErrorMessage("La dependencia " + dpto.getDepartamentoPadre().getNombre() + " no tienen ningun encargado. Corrija esto antes de continuar");
                    }
                }

                for (Usuarios usua : listaUsuTemp) {
                    if (!usua.getDepartamento().equals(dpto)) {
                        int niv = obtenerNivelDepartamento(usua.getDepartamento());
                        if (niv >= nivel) {
                            if (destinatarioEsSubalterno(dpto, usua.getDepartamento()) || (nivel == 3 && niv == 3) || (nivel == niv && destinatarioEsSubalterno(dpto.getDepartamentoPadre() == null ? dpto : dpto.getDepartamentoPadre(), usua.getDepartamento()))) {
                                listaUsu.add(usua);
                            }
                        }
                    }
                }
                
                if(dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA)){
                    List<Usuarios> listaUsu1 = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRolANDDepartamentoId", Usuarios.class).setParameter("departamento", Constantes.DEPARTAMENTO_SECRETARIA_JURIDICA).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                    if(!listaUsu1.isEmpty()){
                        listaUsu.add(listaUsu1.get(0));
                    }
                }
                
                if(dpto.getId().equals(Constantes.DEPARTAMENTO_SECRETARIA_JURIDICA)){
                    List<Usuarios> listaUsu1 = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRolANDDepartamentoId", Usuarios.class).setParameter("departamento", Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
                    if(!listaUsu1.isEmpty()){
                        listaUsu.add(listaUsu1.get(0));
                    }
                }

            } else {
                listaUsu.addAll(listaUsuTemp);
            }

            return listaUsu;
        }
    }
     */
    private List<Usuarios> obtenerPosiblesDestinatarios(Departamentos dpto) {
        if ((dpto.getDepartamentoPadre() == null && dpto.isEsMaximoNivel()) || dpto.isEsMaximoNivel() || dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA) || dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_AUDITORIA)) {
            // return this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
            return obtenerEncargados();
        } else {
            List<Usuarios> listaUsu = new ArrayList<>();
            List<Usuarios> listaUsuTemp = null;

            int nivel = obtenerNivelDepartamento(dpto);
            listaUsuTemp = obtenerEncargados();

            List<Usuarios> listaSup = new ArrayList<>();

            if (dpto.getDepartamentoPadre() != null) {

                if (dpto.getDepartamentoPadre().isEsMaximoNivel()) {
                    List<Usuarios> listaUsuMaximoNivel = obtenerMaximoNivel();
                    if(listaUsuMaximoNivel != null){
                        listaUsu.addAll(listaUsuMaximoNivel);
                    }
                } else {
                    Usuarios us = obtenerSuperiorInmediato(dpto.getDepartamentoPadre());
                    if(us != null){
                        listaSup.add(us);
                    }
                }
            }

            if (nivel > 1) {

                if (listaSup != null) {
                    if (!listaSup.isEmpty()) {
                        if (listaSup.size() == 1) {
                            listaUsu.add(listaSup.get(0));
                        } else {
                            JsfUtil.addErrorMessage("La dependencia " + dpto.getDepartamentoPadre().getNombre() + " tiene mas de un encargado. Corrija esto antes de continuar");
                        }
                    } else {
                        JsfUtil.addErrorMessage("La dependencia " + dpto.getDepartamentoPadre().getNombre() + " no tienen ningun encargado. Corrija esto antes de continuar");
                    }
                }

                for (Usuarios usua : listaUsuTemp) {
                    if (!usua.getDepartamento().equals(dpto)) {
                        int niv = obtenerNivelDepartamento(usua.getDepartamento());
                        if (niv >= nivel) {
                            if (destinatarioEsSubalterno(dpto, usua.getDepartamento()) || (nivel == 3 && niv == 3) || (nivel == niv && destinatarioEsSubalterno(dpto.getDepartamentoPadre() == null ? dpto : dpto.getDepartamentoPadre(), usua.getDepartamento()))) {
                                listaUsu.add(usua);
                            }
                        }
                    }
                }

                if (dpto.getId().equals(Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA)) {
                    Usuarios superior = obtenerSuperiorInmediato(new Departamentos(Constantes.DEPARTAMENTO_SECRETARIA_JURIDICA));

                    if (superior != null) {
                        listaUsu.add(superior);
                    }
                }

                if (dpto.getId().equals(Constantes.DEPARTAMENTO_SECRETARIA_JURIDICA)) {
                    Usuarios superior = obtenerSuperiorInmediato(new Departamentos(Constantes.DEPARTAMENTO_DIRECCION_EJECUTIVA));

                    if (superior != null) {
                        listaUsu.add(superior);
                    }
                }

            } else {
                listaUsu.addAll(listaUsuTemp);
            }

            List<Usuarios> excepciones = obtenerExcepcionesDocumentoAdministrativo(listaUsuTemp, dpto);

            List<Usuarios> excepFinal = new ArrayList<>();
            for (Usuarios ex : excepciones) {
                boolean encontro = false;
                for (Usuarios usu : listaUsu) {
                    if (ex.getDepartamento().equals(usu.getDepartamento())) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    excepFinal.add(ex);
                }
            }

            listaUsu.addAll(excepFinal);

            return listaUsu;
        }
    }

    private List<Usuarios> obtenerExcepcionesDocumentoAdministrativo(List<Usuarios> encargados, Departamentos dpto) {
        List<ExcepcionesDocumentoAdministrativo> lista = ejbFacade.getEntityManager().createNamedQuery("ExcepcionesDocumentoAdministrativo.findByDepartamentoOrigen", ExcepcionesDocumentoAdministrativo.class).setParameter("departamentoOrigen", dpto).getResultList();

        List<Usuarios> listaFinal = new ArrayList<>();

        for (ExcepcionesDocumentoAdministrativo dp : lista) {
            for (Usuarios usu : encargados) {
                if (usu.getDepartamento().equals(dp.getDepartamentoDestino())) {
                    listaFinal.add(usu);
                    break;
                }
            }
        }

        return listaFinal;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String key = event.getRowKey();

        if (key != null && !((Integer) oldValue).equals((Integer) newValue)) {
            /*
            detallesSelected = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findById", DetallesInventario.class).setParameter("id", Integer.valueOf(key)).getSingleResult();
            detallesSelected.setCantidad((Integer) newValue);
            detallesInventarioController.setSelected(detallesSelected);
            detallesInventarioController.save(null);
             */
        }
    }

    public void prepareEditConfiguracionMemo() {

        // List<Departamentos> dpto = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findById", Departamentos.class).setParameter("id", usuario.getDepartamento().getId()).getResultList();
        //if(!dpto.isEmpty()){
        // nomenclaturaMemo = dpto.get(0).getNomenclaturaMemo();
        // descripcionDpto = dpto.get(0).getNombre();
        cambioContrasena1 = "";
        cambioContrasena2 = "";
        nomenclaturaMemo = usuario.getDepartamento().getNomenclaturaMemo();

        tieneToken = usuario.isTieneToken() ? "1" : "2";

        List<TiposArchivoAdministrativo> listaTiposArchivoAdministrativo = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativo.findById", TiposArchivoAdministrativo.class).setParameter("id", 1).getResultList();

        List<TiposArchivoAdministrativoPorDepartamentos> lista = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativoPorDepartamentos.findByDepartamento", TiposArchivoAdministrativoPorDepartamentos.class).setParameter("departamento", usuario.getDepartamento()).getResultList();

        for (TiposArchivoAdministrativo t : listaTiposArchivoAdministrativo) {
            boolean encontro = false;
            if (t.isMostrarEnMenu()) {
                for (TiposArchivoAdministrativoPorDepartamentos tipo : lista) {
                    if (tipo.getTipoArchivoAdministrativo().equals(t)) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    TiposArchivoAdministrativoPorDepartamentos tip = new TiposArchivoAdministrativoPorDepartamentos();
                    tip.setDepartamento(usuario.getDepartamento());
                    tip.setSecuencia(null);
                    tip.setTipoArchivoAdministrativo(t);
                    tiposArchivoAdministrativoPorDepartamentosController.setSelected(tip);
                    tiposArchivoAdministrativoPorDepartamentosController.saveNew(null);
                }
            }
        }

        DateFormat format = new SimpleDateFormat("yyyy");

        Date fecha = ejbFacade.getSystemDate();

        String anoActual = format.format(fecha);

        // listaTiposArchivoAdministrativoPorDepartamentos = ejbFacade.getEntityManager().createNativeQuery("select a.departamento, t.descripcion, a.secuencia from tipos_archivo_administrativo as t left join tipos_archivo_administrativo_por_departamentos as a on (t.id = a.tipo_archivo_administrativo) where t.mostrar_en_menu = true and (a.departamento = 43 or a.departamento is null);", TiposArchivoAdministrativoPorDepartamentos.class).setParameter(1, dpto.get(0)).getResultList();
        listaTiposArchivoAdministrativoPorDepartamentos = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativoPorDepartamentos.findByDepartamento", TiposArchivoAdministrativoPorDepartamentos.class).setParameter("departamento", usuario.getDepartamento()).getResultList();

        for (TiposArchivoAdministrativoPorDepartamentos t : listaTiposArchivoAdministrativoPorDepartamentos) {
            String secuencia = t.getSecuencia();
            if (secuencia != null) {
                String[] array = secuencia.split("/");
                if (array.length > 1) {
                    t.setNro(Integer.valueOf(array[0]));
                    t.setAno(array[1]);
                } else {
                    t.setNro(null);
                    t.setAno(anoActual);
                }
            } else {
                t.setNro(null);
                t.setAno(anoActual);
            }
        }
        // }
    }

    public void guardarConfiguracionMemo() {
        if (cambioContrasena1 != null) {
            for (TiposArchivoAdministrativoPorDepartamentos t : listaTiposArchivoAdministrativoPorDepartamentos) {
                if (t.getNro() != null && t.getAno() != null) {
                    t.setSecuencia(String.valueOf(t.getNro()) + "/" + t.getAno());
                }

                tiposArchivoAdministrativoPorDepartamentosController.setSelected(t);
                tiposArchivoAdministrativoPorDepartamentosController.save(null);

                usuario.setTieneToken("1".equals(tieneToken));

                usuarioAltaController.setSelected(usuario);
                usuarioAltaController.save2();
                usuarioAltaController.setSelected(null);

                usuario.getDepartamento().setNomenclaturaMemo(nomenclaturaMemo);

                departamentoController.setSelected(usuario.getDepartamento());
                departamentoController.save(null);

                if (cambioContrasena1 != null && cambioContrasena2 != null) {
                    if (!"".equals(cambioContrasena1) && !"".equals(cambioContrasena2)) {
                        if (cambioContrasena1.equals(cambioContrasena2)) {
                            generarToken(par, usuario, cambioContrasena1);
                        } else {
                            JsfUtil.addErrorMessage("Contraseñas no coinciden");
                        }
                    }
                }
                cambioContrasena1 = null;
                cambioContrasena2 = null;
            }
        }
    }

    private KeyPair genKeyPair() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keypair = keyGen.genKeyPair();
            return keypair;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }
/*
    X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
            throws GeneralSecurityException, IOException {
        PrivateKey privkey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + days * 86400000l);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name owner = new X500Name(dn);

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        // info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
        info.set(X509CertInfo.SUBJECT, owner);
        // info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
        info.set(X509CertInfo.ISSUER, owner);
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);
        return cert;
    }
*/
    public void generarToken(ParametrosSistema param, Usuarios usu, String contrasena) {
        if (param != null) {
            if (param.getKeystore() != null && param.getContrasenaKeystore() != null) {
                List<AliasesKeystore> alias = ejbFacade.getEntityManager().createNamedQuery("AliasesKeystore.findByUsuarioANDEstado", AliasesKeystore.class).setParameter("usuario", usu).setParameter("estado", new Estados("AC")).getResultList();
                File file = new File(param.getRutaArchivosAdministrativo() + File.separator + param.getKeystore());

                Date fecha = ejbFacade.getSystemDate();
                try {
                    KeyStore keyStore = loadKeyStore(file, param.getContrasenaKeystore(), "JKS");

                    KeyPair keypair = genKeyPair();
                    PrivateKey secretKey = keypair.getPrivate();

//                    X509Certificate cert = generateCertificate("CN=" + usu.getNombresApellidos() + ", OU=TIC, O=JEM, L=Asuncion, ST=PY, C=PY", keypair, 3650, "SHA256withRSA");

                    Certificate lista[] = new Certificate[1];

             //       lista[0] = cert;
                    AliasesKeystore aliasOri = null;
                    if (alias.isEmpty()) {
                        AliasesKeystore al = new AliasesKeystore(null, String.valueOf(usu.getId()), usu, usu, usu, new Estados("AC"), fecha, fecha);
                        aliasesKeystoreController.setSelected(al);
                        aliasesKeystoreController.saveNew(null);

                        al.setAlias(String.valueOf(usu.getId()) + "_" + al.getId());
                        aliasesKeystoreController.setSelected(al);
                        aliasesKeystoreController.save(null);

                        aliasOri = al;
                    } else {
                        alias.get(0).setAlias(String.valueOf(usu.getId()) + "_" + alias.get(0).getId());
                        aliasesKeystoreController.setSelected(alias.get(0));
                        aliasesKeystoreController.save(null);

                        aliasOri = alias.get(0);
                    }

                    keyStore.setKeyEntry(String.valueOf(usu.getId()) + "_" + aliasOri.getId(), secretKey, contrasena.toCharArray(), lista);

                    keyStore.store(new FileOutputStream(param.getRutaArchivosAdministrativo() + File.separator + param.getKeystore()), param.getContrasenaKeystore().toCharArray());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public DocumentosAdministrativos prepareCreateNuevo() {
        borrarAutoGuardado(usuario);
        return prepareCreate();
    }

    public void prepareCerrarDialogoNuevo() {
        borrarAutoGuardado(usuario);
        PrimeFaces.current().executeScript("PF('pollAutoGuardadoNuevo').stop()");
    }

    public void prepareCerrarDialogoEdit() {
        borrarAutoGuardado(usuario);
        PrimeFaces.current().executeScript("PF('pollAutoGuardadoEdit').stop()");
    }

    public DocumentosAdministrativos prepareCreate() {

        docResponder = null;
        DocumentosAdministrativos doc = super.prepareCreate(null);

        entradaDocumentoAdministrativo = new EntradasDocumentosAdministrativos();

        esResponder = false;

        responderA = "";

        archivo = new ArchivosAdministrativo();
        // docImprimir = archivo;

        descripcionMesaEntrada = "";
        subcategoriaDocumentoAdministrativo = null;

        listaFiles = null;

        listaEstadosArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findAll", EstadosTransferenciaDocumentoAdministrativo.class).getResultList();

        // EstadosDocumentoAdministrativo estado = obtenerSgteEstado(Constantes.ESTADO_DOCUMENTO_INGRESADO);
        // listaPosiblesUsuariosEnviar = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirArchivoAdm", Usuarios.class).setParameter("tipoDocumentoAdministrativo", tiposDoc.get(0).getCodigo()).setParameter("estadoDocumentoActual", estado.getCodigo()).getResultList();
        // listaPosiblesUsuariosCC = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirArchivoAdm", Usuarios.class).setParameter("tipoDocumentoAdministrativo", tiposDoc.get(0).getCodigo()).setParameter("estadoDocumentoActual", estado.getCodigo()).getResultList();
        // listaPosiblesUsuariosRemitente = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirArchivoAdm", Usuarios.class).setParameter("tipoDocumentoAdministrativo", tiposDoc.get(0).getCodigo()).setParameter("estadoDocumentoActual", estado.getCodigo()).getResultList();
        // listaPosiblesUsuariosCC = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        //listaPosiblesUsuariosCC =  this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstadoANDEstadoDepartamentoANDRol", Usuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).getResultList();
        listaPosiblesUsuariosCC = obtenerEncargados();

        listaPosiblesUsuariosRemitente = obtenerPosiblesRemitentes(usuario);

        /*
            List<RolesPorUsuarios> list = ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", RolesPorUsuarios.class).setParameter("rol", Constantes.ROL_ENCARGADO).setParameter("usuario", usuario.getId()).getResultList();

            if(!list.isEmpty()){
                listaPosiblesUsuariosRemitente.add(usuario);
            }
         */
        listaUsuariosEnviar = new ArrayList<>();
        listaUsuariosCC = new ArrayList<>();
        listaUsuariosRemitente = new ArrayList<>();

        usuarioEnviar = null;

        usuarioRemitente = null;
        if (!listaPosiblesUsuariosRemitente.isEmpty()) {
            usuarioRemitente = listaPosiblesUsuariosRemitente.get(0);
        } else {
            JsfUtil.addErrorMessage("Lista de remitentes vacia");
            return null;
        }

        usuarioCC = null;

        if (usuarioRemitente != null) {
            listaUsuariosRemitente.add(usuarioRemitente);
        }

        listaPosiblesUsuariosEnviar = obtenerPosiblesDestinatarios(usuarioRemitente.getDepartamento());

        listaFormatos = null;

        cambiarSubcategorias(1);
        /*
            List<TiposPrioridad> lista = ejbFacade.getEntityManager().createNamedQuery("TiposPrioridad.findByCodigo", TiposPrioridad.class).setParameter("codigo", Constantes.TIPO_PRIORIDAD_NORMAL).getResultList();

            if (!lista.isEmpty()) {
                tipoPrioridad = lista.get(0);
            }
         */
        editando = true;

        List<DocumentosAdministrativosAutoguardados> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.findByUsuarioAlta", DocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usuario).getResultList();

        if (!lista.isEmpty()) {
            if (lista.get(0).getDocumentoAdministrativoOriginal() == null) {
                prepareAutoguardado(lista.get(0), false);
            }
        }

        return doc;
    }

    public void prepareEditUsuarios() {
        prepareEditUsuarios(getSelected());
    }

    public void prepareEditUsuarios(DocumentosAdministrativos doc) {

        TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(doc);

        List<UsuariosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();

        listaUsuariosEnviar = new ArrayList<>();

        for (UsuariosPorDocumentosAdministrativos us : lista) {
            listaUsuariosEnviar.add(us.getUsuario());
        }

        usuarioEnviar = null;

        if (!listaUsuariosEnviar.isEmpty()) {
            usuarioEnviar = listaUsuariosEnviar.get(0);
        }

        lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();

        listaUsuariosRemitente = new ArrayList<>();

        for (UsuariosPorDocumentosAdministrativos us : lista) {
            listaUsuariosRemitente.add(us.getUsuario());
        }

        usuarioRemitente = null;

        if (!listaUsuariosRemitente.isEmpty()) {
            usuarioRemitente = listaUsuariosRemitente.get(0);
        }

        lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

        listaUsuariosCC = new ArrayList<>();

        for (UsuariosPorDocumentosAdministrativos us : lista) {
            listaUsuariosCC.add(us.getUsuario());
        }

        usuarioCC = null;

        if(usuarioRemitente != null){
        // listaPosiblesUsuariosEnviar = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
            listaPosiblesUsuariosEnviar = obtenerPosiblesDestinatarios(usuarioRemitente.getDepartamento());
        }

        if (usuarioEnviar != null) {
            if( listaPosiblesUsuariosEnviar != null){
                boolean encontro = false;
                for (Usuarios us : listaPosiblesUsuariosEnviar) {
                    if (us.equals(usuarioEnviar)) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    listaPosiblesUsuariosEnviar.add(usuarioEnviar);
                }
            }
        }

        //listaPosiblesUsuariosRemitente = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        listaPosiblesUsuariosRemitente = obtenerPosiblesRemitentes(usuario);

        if (usuarioRemitente != null) {
            boolean encontro = false;
            for (Usuarios us : listaPosiblesUsuariosRemitente) {
                if (us.equals(usuarioRemitente)) {
                    encontro = true;
                    break;
                }
            }

            if (!encontro) {
                listaPosiblesUsuariosRemitente.add(usuarioRemitente);
            }
        }

        // listaPosiblesUsuariosCC = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();
        listaPosiblesUsuariosCC = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByUsuarioNOTNULL", Usuarios.class).getResultList();

    }

    public void prepareEditOtro() {
        borrarAutoGuardado(usuario);
        prepareEdit();
    }

    public void prepareEdit() {
        prepareEdit(getSelected());
    }

    public void prepareEdit(DocumentosAdministrativos doc) {
        ArchivosAdministrativo item = obtenerPrimerArchivo(doc);
        if (item != null && doc != null) {
            docImprimir = item;

            esResponder = false;

            archivo = item;
            subcategoriaDocumentoAdministrativo = item.getDocumentoAdministrativo().getSubcategoriaDocumentoAdministrativo();

            descripcionMesaEntrada = doc.getDescripcionMesaEntrada();

            if (subcategoriaDocumentoAdministrativo != null) {
                if (subcategoriaDocumentoAdministrativo.getTipoArchivo() != null) {
                    listaFormatos = ejbFacade.getEntityManager().createNamedQuery("FormatosArchivoAdministrativo.findByTipoArchivo", FormatosArchivoAdministrativo.class).setParameter("tipoArchivo", subcategoriaDocumentoAdministrativo.getTipoArchivo()).getResultList();
                }
            }

            listaEstadosArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findAll", EstadosTransferenciaDocumentoAdministrativo.class).getResultList();

            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(doc);

            if (transf != null) {
                // estadoArchivo = item.getEstado();
                estadoArchivo = transf.getEstado();
            }

            cambiarSubcategorias();
            editando = true;
            
            

            prepareEditUsuarios(doc);

            // tipoPrioridad = getSelected().getTipoPrioridad();
        }
    }

    public void prepareAutoguardado(DocumentosAdministrativosAutoguardados docGuardado, boolean esEdit) {

        boolean encontro = false;
        if (esEdit) {
            if (docGuardado.getDocumentoAdministrativoPadre() != null) {
                if (getItems2() != null) {
                    for (DocumentosAdministrativos dc : getItems2()) {
                        if (dc.equals(docGuardado.getDocumentoAdministrativoPadre())) {
                            encontro = true;
                        }
                    }
                }
                if (!encontro) {
                    borrarAutoGuardado(usuario);
                }
            } else {
                encontro = true;
            }
        } else {
            encontro = true;
        }

        if (encontro) {
            autoGuardado = true;
            responderA = docGuardado.getResponderA();
            descripcionMesaEntrada = docGuardado.getAsunto();

            if (archivo != null) {
                archivo.setTexto(docGuardado.getTexto());
                archivo.setFormato(docGuardado.getFormato());
            }

            subcategoriaDocumentoAdministrativo = docGuardado.getSubcategoriaDocumentoAdministrativo();
            docResponder = docGuardado.getDocumentoAdministrativoPadre();
            esResponder = docGuardado.isResponder();
            buscarFormatos();

            listaUsuariosEnviar = new ArrayList<>();
            listaUsuariosCC = new ArrayList<>();
            listaUsuariosRemitente = new ArrayList<>();
            List<UsuariosPorDocumentosAdministrativosAutoguardados> listaDet = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativosAutoguardados.findByDocumentoAdministrativoAutoguardado", UsuariosPorDocumentosAdministrativosAutoguardados.class).setParameter("documentoAdministrativoAutoguardado", docGuardado).getResultList();
            for (UsuariosPorDocumentosAdministrativosAutoguardados usu : listaDet) {
                if (tipoEnvioArchivoAdmConCopia.equals(usu.getTipoEnvio())) {
                    listaUsuariosCC.add(usu.getUsuario());
                    usuarioCC = usu.getUsuario();
                }
                if (tipoEnvioArchivoAdmDestinatario.equals(usu.getTipoEnvio())) {
                    listaUsuariosEnviar.add(usu.getUsuario());
                    usuarioEnviar = usu.getUsuario();
                }
                if (tipoEnvioArchivoAdmRemitente.equals(usu.getTipoEnvio())) {
                    listaUsuariosRemitente.add(usu.getUsuario());
                    usuarioRemitente = usu.getUsuario();
                }
            }
        }
    }

    public void prepareEstadoProcesal() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setEstadoProcesalAux(null);
            getSelected().setEstadoProcesal(null);
        }
    }

    public void prepareObs() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setUltimaObservacionAux(null);
            getSelected().setUltimaObservacion(null);
        }
    }

    public void prepareAlzarArchivo() {
        descripcionArchivo = "";
    }

    public void prepareListTransferir() {
        prepareListTransferir(getSelected());
    }

    public void prepareListTransferir(DocumentosAdministrativos doc) {
        listaTransfer = ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativoDepartamentoOrigen", CambiosEstadoDocumentoAdministrativoPendientes.class).setParameter("documentoAdministrativo", doc).setParameter("departamentoOrigen", usuario.getDepartamento()).getResultList();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);
        departamentoController.setSelected(null);

        if (getSelected() != null) {
            if (!getSelected().isVisto()) {
                getSelected().setVisto(true);
                getSelected().setUsuarioVisto(usuario);

                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraVisto(fecha);

                super.save(null);
            }
        }

        obtenerArchivos();
        // obtenerObservaciones();
        // obtenerTransferencias();
        obtenerDestinatarios();
    }

    public void resetParentsSalida() {
        obtenerArchivosSalida();
        // obtenerObservacionesSalida();
        // obtenerTransferenciasSalida();
        obtenerDestinatariosSalida();
    }

    public void resetParentsBorradores() {
        obtenerArchivosBorradores();
        obtenerDestinatariosBorradores();
        //obtenerObservacionesBorradores();
    }

    public void resetParentsArchivados() {
        obtenerArchivosArchivados();
        // obtenerObservacionesArchivados();
        // obtenerTransferenciasArchivados();
        obtenerDestinatariosArchivados();
    }

    public void resetParentsCC() {
        if (selectedCC != null) {
            List<VistosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("VistosPorDocumentosAdministrativos.findByUsuarioDocumentoAdministrativo", VistosPorDocumentosAdministrativos.class).setParameter("usuario", usuario).setParameter("documentoAdministrativo", selectedCC).getResultList();
            if (lista.isEmpty()) {
                VistosPorDocumentosAdministrativos visto = new VistosPorDocumentosAdministrativos(null, usuario, selectedCC, ejbFacade.getSystemDate(), usuario.getDepartamento());
                vistoPorDocumentoAdministrativoController.setSelected(visto);
                vistoPorDocumentoAdministrativoController.saveNew(null);
            }
        }

        obtenerArchivosCC();
        // obtenerObservacionesCC();
        // obtenerTransferenciasCC();
        obtenerDestinatariosCC();
    }

    public void resetParentsHistorico() {
        obtenerArchivosHistorico();
        // obtenerObservacionesHistorico();
        // obtenerTransferenciasHistorico();
        obtenerDestinatariosHistorico();
    }

    public void prepareVerDoc() {
        prepareVerDoc(getSelected());
    }

    public void prepareVerDocVisto(DocumentosAdministrativos doc) {
        prepareVerDoc(doc);

        if (doc != null) {
            if (!doc.isVisto()) {
                doc.setVisto(true);
                doc.setUsuarioVisto(usuario);

                Date fecha = ejbFacade.getSystemDate();

                doc.setFechaHoraVisto(fecha);

                DocumentosAdministrativos docAnt = getSelected();

                setSelected(doc);

                super.save(null);

                setSelected(docAnt);
            }
        }
    }

    public void prepareVerDocVistoCC(DocumentosAdministrativos doc) {
        prepareVerDoc(doc);

        if (doc != null) {
            List<VistosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("VistosPorDocumentosAdministrativos.findByUsuarioDocumentoAdministrativo", VistosPorDocumentosAdministrativos.class).setParameter("usuario", usuario).setParameter("documentoAdministrativo", doc).getResultList();
            if (lista.isEmpty()) {
                VistosPorDocumentosAdministrativos visto = new VistosPorDocumentosAdministrativos(null, usuario, doc, ejbFacade.getSystemDate(), usuario.getDepartamento());
                vistoPorDocumentoAdministrativoController.setSelected(visto);
                vistoPorDocumentoAdministrativoController.saveNew(null);
            }
        }
    }

    public void prepareVerDoc(DocumentosAdministrativos doc) {
        ArchivosAdministrativo item = obtenerPrimerArchivo(doc);

        prepareVerDoc(item);
    }

    public void prepareVerDoc(ArchivosAdministrativo doc) {
        docImprimir = doc;

        if (getSelected() != null) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();

            firmaId = "";
            for (int i = 1; i <= listaUsuDestinatario.size(); i++) {
                if (!"".equals(firmaId)) {
                    firmaId += ",";
                }
                firmaId += "M" + sessionId + "_" + i;
            }
        }

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void prepareFirmarDoc() {
        contrasenaAlias = "";
    }

    public void prepareVerFirmarDoc() {
        prepareVerDoc(getSelected());
    }

    public void prepareVerFirmarDoc(DocumentosAdministrativos doc) {
        ArchivosAdministrativo item = obtenerPrimerArchivo(doc);
        prepareVerDoc(item);
    }

    public void prepareVerFirmarDoc(ArchivosAdministrativo doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    /*
    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getRuta() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getRuta()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                    String partes[] = docImprimir.getRuta().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }
     */
    public Date generarFechaPresentacion(Date fecha) {
        // Aqui analizar dias habiles y feriados

        Date fechaFinal = fecha;

        boolean encontro = false;
        LocalDateTime localDateTime = null;
        while (true) {
            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                localDateTime = localDateTime.plusDays(2);
                Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curr);
                cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                cal.set(Calendar.MINUTE, par.getMinutoInicio());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                fechaFinal = cal.getTime();
                encontro = true;
            } else if (localDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                localDateTime = localDateTime.plusDays(1);
                Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curr);
                cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                cal.set(Calendar.MINUTE, par.getMinutoInicio());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                fechaFinal = cal.getTime();
                encontro = true;
            }
            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            List<ExpFeriados> lista = ejbFacade.getEntityManager().createNamedQuery("ExpFeriados.findByFecha", ExpFeriados.class).setParameter("fecha", fechaFinal).getResultList();
            if (lista.isEmpty()) {
                if (encontro) {
                    break;
                } else {
                    int hora = localDateTime.getHour();
                    int minuto = localDateTime.getMinute();
                    int segundo = localDateTime.getSecond();

                    if (hora < par.getHoraInicio() || (hora == par.getHoraInicio() && minuto < par.getMinutoInicio())) {
                        Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(curr);
                        cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                        cal.set(Calendar.MINUTE, par.getMinutoInicio());
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        fechaFinal = cal.getTime();
                    }

                    boolean continuar = false;

                    if (hora > par.getHoraFin() || (hora == par.getHoraFin() && minuto >= par.getMinutoFin())) {
                        encontro = true;
                        localDateTime = localDateTime.plusDays(1);
                        Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(curr);
                        cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                        cal.set(Calendar.MINUTE, par.getMinutoInicio());
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        fechaFinal = cal.getTime();
                        continuar = true;
                    }

                    if (continuar) {
                        continue;
                    } else {
                        break;
                    }
                }
            }

            encontro = true;

            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            localDateTime = localDateTime.plusDays(1);
            Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();

            cal.setTime(curr);

            cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
            cal.set(Calendar.MINUTE, par.getMinutoInicio());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            fechaFinal = cal.getTime();
        }

        return fechaFinal;
    }

    private String sustituirTags(String texto, List<Metadatos> listaMetadatos) {
        if (texto != null) {
            for (Metadatos metadato : listaMetadatos) {
                texto = texto.replace(inicio + metadato.getClave().trim() + fin, metadato.getValor().trim());
            }
        }
        return texto;
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;
                Date fecha = ejbFacade.getSystemDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                if (docImprimir.getRuta() != null) {

                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivosAdministrativo() + "/" + docImprimir.getRuta()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }

                    if (fileByte != null) {

                        String partes[] = docImprimir.getRuta().split("[.]");
                        String ext = "pdf";

                        if (partes.length > 1) {
                            ext = partes[partes.length - 1];
                        }

                        nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                        FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
                        outputStream.write(fileByte);

                        outputStream.close();

                        // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                    }
                } else if (docImprimir.getFormato() != null) {

                    TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(docImprimir.getDocumentoAdministrativo());
                    List<UsuariosPorDocumentosAdministrativos> listaUsuRemitente = null;
                    List<UsuariosPorDocumentosAdministrativos> listaUsuDestinatario = null;
                    List<UsuariosPorDocumentosAdministrativos> listaUsuCC = null;
                    Departamentos dpto = null;
                    if (transf != null) {

                        listaUsuRemitente = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmRemitente).getResultList();
                        listaUsuDestinatario = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmDestinatario).getResultList();
                        listaUsuCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativoANDTipoEnvio", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).setParameter("tipoEnvio", tipoEnvioArchivoAdmConCopia).getResultList();

                        dpto = transf.getResponsableOrigen().getDepartamento();
                    } else {

                        listaUsuRemitente = new ArrayList<>();
                        for (Usuarios usu : listaUsuariosRemitente) {
                            UsuariosPorDocumentosAdministrativos us = new UsuariosPorDocumentosAdministrativos();
                            us.setUsuario(usu);
                            listaUsuRemitente.add(us);
                        }

                        listaUsuDestinatario = new ArrayList<>();
                        for (Usuarios usu : listaUsuariosEnviar) {
                            UsuariosPorDocumentosAdministrativos us = new UsuariosPorDocumentosAdministrativos();
                            us.setUsuario(usu);
                            listaUsuDestinatario.add(us);
                        }

                        listaUsuCC = new ArrayList<>();
                        for (Usuarios usu : listaUsuariosCC) {
                            UsuariosPorDocumentosAdministrativos us = new UsuariosPorDocumentosAdministrativos();
                            us.setUsuario(usu);
                            listaUsuCC.add(us);
                        }

                        dpto = usuarioRemitente.getDepartamento();
                    }

                    /*
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);
                    Date fechaHora = ejbFacade.getSystemDate();
                    nombreArch = simpleDateFormat.format(fechaHora) + "_" + docImprimir.getId() + ".pdf";
                     */
                    Date fechaF = generarFechaPresentacion(ejbFacade.getSystemDate());
                    String secuencia = obtenerNroSecuencia(docImprimir.getTipoArchivo(), dpto);

                    DocumentosAdministrativos docRes = null;

                    if (docResponder == null) {
                        docRes = docImprimir.getDocumentoAdministrativo() == null ? null : docImprimir.getDocumentoAdministrativo().getDocumentoAdministrativo();
                    } else {
                        docRes = docResponder;
                    }

                    nombre = generarPDF(simpleDateFormat.format(fecha) + "_" + (docImprimir.getDocumentoAdministrativo() == null ? "0" : (docImprimir.getDocumentoAdministrativo().getId() == null ? "0" : docImprimir.getDocumentoAdministrativo().getId())) + "_" + (docImprimir.getId() == null ? "0" : docImprimir.getId()) + ".pdf", (docImprimir.getDocumentoAdministrativo() == null ? descripcionMesaEntrada : (docImprimir.getDocumentoAdministrativo().getDescripcionMesaEntrada() == null ? descripcionMesaEntrada : docImprimir.getDocumentoAdministrativo().getDescripcionMesaEntrada())), docImprimir.getTipoArchivo(), docImprimir.getTexto(), Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/", dpto, listaUsuRemitente, listaUsuDestinatario, listaUsuCC, secuencia, fechaF, false, docRes, null, null);

                    /*
                    List<Metadatos> listaMetadatos = new ArrayList<>();
                    SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                    Date fecha = generarFechaPresentacion(ejbFacade.getSystemDateOnly());
                    
        //Calendar myCal = Calendar.getInstance();
        //myCal.set(Calendar.YEAR, 2022);
        //myCal.set(Calendar.MONTH, 4);
        //myCal.set(Calendar.DAY_OF_MONTH, 12);
        //myCal.set(Calendar.HOUR_OF_DAY, 15);
        //myCal.set(Calendar.MINUTE, 38);
        //myCal.set(Calendar.SECOND, 0);
        //Date fechaInicio = myCal.getTime();
        //            Date fecha = generarFechaPresentacion(fechaInicio);
                     

                    Date fechaActuacion = fecha;

                    fechaFinal = fechaActuacion;

                    Metadatos meta = new Metadatos("fecha", format.format(fechaActuacion));
                    listaMetadatos.add(meta);
                    secuencia = null;

                    secuencia = "1/2022";

                    Metadatos meta2 = null;
                    if (secuencia != null) {
                        String[] array = secuencia.split("/");
                        if (array.length > 1) {
                            if (array[0].length() == 1) {
                                secuencia = "0" + secuencia;
                            }
                        }

                        meta2 = new Metadatos("nro", secuencia);
                    } else {
                        meta2 = new Metadatos("nro", "");
                    }

                    textoFinal = (docImprimir.getTexto() == null) ? "" : docImprimir.getTexto();

                    listaMetadatos.add(meta2);
                    Metadatos meta3 = new Metadatos("texto", (docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
                    listaMetadatos.add(meta3);
                    if (getSelected() != null) {
                        
                        //Metadatos meta4 = new Metadatos("causa", getSelected().getCausa().replace("-", "/"));
                        //listaMetadatos.add(meta4);
                        //Metadatos meta5 = new Metadatos("caratula", getSelected().getCaratula());
                        //listaMetadatos.add(meta5);
                         
                        Metadatos meta6 = new Metadatos("url", url);
                        listaMetadatos.add(meta6);
                    } else {
                        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                        url = request.getRequestURL().toString();
                        String uri = request.getRequestURI();
                        int pos = url.lastIndexOf(uri);
                        url = url.substring(0, pos);

                        Metadatos meta6 = new Metadatos("url", url);
                        listaMetadatos.add(meta6);
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);
                    Date fechaHora = ejbFacade.getSystemDate();
                    nombre = session.getId() + "_" + simpleDateFormat.format(fechaHora) + ".pdf";

                    String stringFinal = ((docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
                    // @page { margin: 130px 50px 50px}

                    // "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /><title></title><style>@media print {html, body {width: 183.6mm;height: 280.5mm;margin-top: 15mm;margin-left: 10mm;margin-right: 70mm;margin-bottom: 18mm;}}</style></head>
                    stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";
                    try ( OutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp")) {
                        ITextRenderer renderer = new ITextRenderer();
                        SharedContext sharedContext = renderer.getSharedContext();

                        sharedContext.setPrint(true);
                        sharedContext.setInteractive(false);

                        FontResolver resolver = renderer.getFontResolver();

                        renderer.getFontResolver().addFont(par.getRutaArchivos() + "/garamond/GaramondRegular.ttf", true);

                        renderer.setDocumentFromString(sustituirTags(stringFinal, listaMetadatos));
                        renderer.layout();
                        renderer.createPDF(outputStream);

                    }

                    PdfDocument pdfDoc = new PdfDocument(new PdfReader(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp"), new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre));
                    // Document doc = new Document(pdfDoc);

                    pdfDoc.setTagged();
                    // Document doc = new Document(pdfDoc, PageSize.A4);
                    Document doc = new Document(pdfDoc);

                    // doc.setMargins(200, 200, 200, 200);
                    // java.awt.Image imagen = new java.awt.Image
                    File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
                    Image image = ImageIO.read(pathToFile);

                    for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                        if ((i % 2) > 0) {
                            PdfPage pagina = pdfDoc.getPage(i);
                            Rectangle pageSize = pagina.getPageSize();
                            float x = (pageSize.getWidth() / 2) - 75;
                            float y = pageSize.getTop() - 80;
                            PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                            Rectangle rect = new Rectangle(x, y, 150, 60);
                            under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                            com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                            under.setStrokeColor(magentaColor).moveTo(88, 856).lineTo(550, 856).closePathStroke();

                            under.saveState();
                            // under2.saveState();
                        }
                    }

                    doc.close();

                    File archivo = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp");
                    archivo.delete();
                    //}
                     */
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;

    }

    /*
    private void generarPdf(ArchivosAdministrativo arch){
        if (arch.getFormato() != null) {

            List<Metadatos> listaMetadatos = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            Date fecha = generarFechaPresentacion(ejbFacade.getSystemDateOnly());

            Date fechaActuacion = fecha;

            fechaFinal = fechaActuacion;

            Metadatos meta = new Metadatos("fecha", format.format(fechaActuacion));
            listaMetadatos.add(meta);
            secuencia = null;

            secuencia = "1/2022";

            Metadatos meta2 = null;
            if (secuencia != null) {
                String[] array = secuencia.split("/");
                if (array.length > 1) {
                    if (array[0].length() == 1) {
                        secuencia = "0" + secuencia;
                    }
                }

                meta2 = new Metadatos("nro", secuencia);
            } else {
                meta2 = new Metadatos("nro", "");
            }

            textoFinal = (arch.getTexto() == null) ? "" : arch.getTexto();

            listaMetadatos.add(meta2);
            Metadatos meta3 = new Metadatos("texto", (docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
            listaMetadatos.add(meta3);
            if (getSelected() != null) {
                
                //Metadatos meta4 = new Metadatos("causa", getSelected().getCausa().replace("-", "/"));
                //listaMetadatos.add(meta4);
                //Metadatos meta5 = new Metadatos("caratula", getSelected().getCaratula());
                //listaMetadatos.add(meta5);
                 
                Metadatos meta6 = new Metadatos("url", url);
                listaMetadatos.add(meta6);
            } else {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                url = request.getRequestURL().toString();
                String uri = request.getRequestURI();
                int pos = url.lastIndexOf(uri);
                url = url.substring(0, pos);

                Metadatos meta6 = new Metadatos("url", url);
                listaMetadatos.add(meta6);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);
            Date fechaHora = ejbFacade.getSystemDate();
            nombre = session.getId() + "_" + simpleDateFormat.format(fechaHora) + ".pdf";

            String stringFinal = ((docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
            // @page { margin: 130px 50px 50px}

            // "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /><title></title><style>@media print {html, body {width: 183.6mm;height: 280.5mm;margin-top: 15mm;margin-left: 10mm;margin-right: 70mm;margin-bottom: 18mm;}}</style></head>
            stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";
            try ( OutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp")) {
                ITextRenderer renderer = new ITextRenderer();
                SharedContext sharedContext = renderer.getSharedContext();

                sharedContext.setPrint(true);
                sharedContext.setInteractive(false);

                FontResolver resolver = renderer.getFontResolver();

                renderer.getFontResolver().addFont(par.getRutaArchivos() + "/garamond/GaramondRegular.ttf", true);

                renderer.setDocumentFromString(sustituirTags(stringFinal, listaMetadatos));
                renderer.layout();
                renderer.createPDF(outputStream);

            }

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp"), new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre));
            // Document doc = new Document(pdfDoc);

            pdfDoc.setTagged();
            // Document doc = new Document(pdfDoc, PageSize.A4);
            Document doc = new Document(pdfDoc);

            // doc.setMargins(200, 200, 200, 200);
            // java.awt.Image imagen = new java.awt.Image
            File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
            Image image = ImageIO.read(pathToFile);

            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                if ((i % 2) > 0) {
                    PdfPage pagina = pdfDoc.getPage(i);
                    Rectangle pageSize = pagina.getPageSize();
                    float x = (pageSize.getWidth() / 2) - 75;
                    float y = pageSize.getTop() - 80;
                    PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                    Rectangle rect = new Rectangle(x, y, 150, 60);
                    under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                    com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                    under.setStrokeColor(magentaColor).moveTo(88, 856).lineTo(550, 856).closePathStroke();

                    under.saveState();
                    // under2.saveState();
                }
            }

            doc.close();

            File archivo = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre + "_temp");
            archivo.delete();
            //}
        }
    }
     */
    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;

        }
    }

    private void obtenerObservaciones() {
        if (getSelected() != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmActivo);
            estados.add(estadoDocAdmArchivado);
            listaObservaciones = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservaciones = new ArrayList<>();

        }
    }

    private void obtenerObservacionesHistorico() {
        if (selectedHistorico != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmActivo);
            estados.add(estadoDocAdmArchivado);
            listaObservacionesHistorico = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedHistorico).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservacionesHistorico = new ArrayList<>();

        }
    }

    private void obtenerDestinatariosSalida() {
        if (selectedSalida != null) {
            listaDestinatariosSalida = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedSalida).getResultList();
        } else {
            listaDestinatariosSalida = new ArrayList<>();
        }
    }

    /*
    private void obtenerTransferenciasSalida() {
        if (selectedSalida != null) {
            listaDestinatariosSalida = ejbFacade.getEntityManager().createNativeQuery("select u.* from transferencias_documento_administrativo as t, usuarios_por_documentos_administrativos as u, tipos_envio as e, documentos_administrativos as d where d.id = u.documento_administrativo and u.transferencia_documento_administrativo = t.id and u.tipo_envio = e.codigo and u.documento_administrativo = ?1 and t.estado = 'FI' and d.estado in ('AC','AR') order by t.fecha_hora_alta, e.descripcion", UsuariosPorDocumentosAdministrativos.class).setParameter(1, selectedSalida.getId()).getResultList();
        } else {
            listaDestinatariosSalida = new ArrayList<>();

        }
    }
     */
    private void obtenerDestinatariosArchivados() {
        if (selectedArchivados != null) {
            listaDestinatariosArchivados = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedArchivados).getResultList();
        } else {
            listaDestinatariosArchivados = new ArrayList<>();
        }
    }

    /*
    private void obtenerTransferenciasArchivados() {
        if (selectedArchivados != null) {
            listaDestinatariosArchivados = ejbFacade.getEntityManager().createNativeQuery("select u.* from transferencias_documento_administrativo as t, usuarios_por_documentos_administrativos as u, tipos_envio as e, documentos_administrativos as d where d.id = u.documento_administrativo and u.transferencia_documento_administrativo = t.id and u.tipo_envio = e.codigo and u.documento_administrativo = ?1 and t.estado = 'FI' and d.estado = 'AR' order by t.fecha_hora_alta, e.descripcion", UsuariosPorDocumentosAdministrativos.class).setParameter(1, selectedArchivados.getId()).getResultList();
        } else {
            listaDestinatariosArchivados = new ArrayList<>();

        }
    }
     */
    private void obtenerDestinatariosCC() {
        if (selectedCC != null) {
            listaDestinatariosCC = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedCC).getResultList();
        } else {
            listaDestinatariosCC = new ArrayList<>();
        }
    }

    /*
    private void obtenerTransferenciasCC() {
        if (selectedCC != null) {
            listaDestinatariosCC = ejbFacade.getEntityManager().createNativeQuery("select u.* from transferencias_documento_administrativo as t, usuarios_por_documentos_administrativos as u, tipos_envio as e, documentos_administrativos as d where d.id = u.documento_administrativo and u.transferencia_documento_administrativo = t.id and u.tipo_envio = e.codigo and u.documento_administrativo = ?1 and t.estado = 'FI' and d.estado in ('AC','AR') order by t.fecha_hora_alta, e.descripcion", UsuariosPorDocumentosAdministrativos.class).setParameter(1, selectedCC.getId()).getResultList();
        } else {
            listaDestinatariosCC = new ArrayList<>();

        }
    }
     */
    private void obtenerDestinatarios() {
        if (getSelected() != null) {
            listaDestinatarios = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).getResultList();
        } else {
            listaDestinatarios = new ArrayList<>();
        }
    }

    /*
    private void obtenerTransferencias() {
        if (getSelected() != null) {
            listaDestinatarios = ejbFacade.getEntityManager().createNativeQuery("select u.* from transferencias_documento_administrativo as t, usuarios_por_documentos_administrativos as u, tipos_envio as e, documentos_administrativos as d where d.id = u.documento_administrativo and u.transferencia_documento_administrativo = t.id and u.tipo_envio = e.codigo and u.documento_administrativo = ?1 and t.estado = 'FI' and d.estado in ('AC','AR')  order by t.fecha_hora_alta, e.descripcion", UsuariosPorDocumentosAdministrativos.class).setParameter(1, getSelected().getId()).getResultList();
        } else {
            listaDestinatarios = new ArrayList<>();

        }
    }
     */
    private void obtenerDestinatariosHistorico() {
        if (selectedHistorico != null) {
            listaDestinatariosHistorico = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedHistorico).getResultList();
        } else {
            listaDestinatariosHistorico = new ArrayList<>();
        }
    }

    /*
    private void obtenerTransferenciasHistorico() {
        if (selectedHistorico != null) {
            listaDestinatariosHistorico = ejbFacade.getEntityManager().createNativeQuery("select u.* from transferencias_documento_administrativo as t, usuarios_por_documentos_administrativos as u, tipos_envio as e, documentos_administrativos as d where d.id = u.documento_administrativo and u.transferencia_documento_administrativo = t.id and u.tipo_envio = e.codigo and u.documento_administrativo = ?1 and t.estado = 'FI' and d.estado in ('AC','AR')  order by t.fecha_hora_alta, e.descripcion", UsuariosPorDocumentosAdministrativos.class).setParameter(1, selectedHistorico.getId()).getResultList();
        } else {
            listaDestinatariosHistorico = new ArrayList<>();

        }
    }
     */
    private void obtenerObservacionesArchivados() {
        if (selectedArchivados != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmActivo);
            estados.add(estadoDocAdmArchivado);
            listaObservacionesArchivados = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedArchivados).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservacionesArchivados = new ArrayList<>();
        }
    }

    private void obtenerObservacionesCC() {
        if (selectedCC != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmActivo);
            estados.add(estadoDocAdmArchivado);
            listaObservacionesCC = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedCC).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservacionesCC = new ArrayList<>();
        }
    }

    private void obtenerObservacionesSalida() {
        if (selectedSalida != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmActivo);
            estados.add(estadoDocAdmArchivado);
            listaObservacionesSalida = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", selectedSalida).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservacionesSalida = new ArrayList<>();
        }
    }

    private void obtenerObservacionesBorradores() {
        if (getSelected() != null) {
            List<EstadosDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoDocAdmEnProyecto);
            listaObservacionesBorradores = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosAdministrativos.findByDocumentoAdministrativoANDEstadosDocumentoAdministrativo", ObservacionesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).setParameter("estadosDocumentoAdministrativo", estados).getResultList();
        } else {
            listaObservacionesBorradores = new ArrayList<>();
        }
    }

    private void obtenerArchivos() {
        if (getSelected() != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivos = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivos = new ArrayList<>();
        }
    }

    private void obtenerArchivosHistorico() {
        if (selectedHistorico != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivosHistorico = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", selectedHistorico).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivosHistorico = new ArrayList<>();
        }
    }

    private void obtenerArchivosArchivados() {
        if (selectedArchivados != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivosArchivados = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", selectedArchivados).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivosArchivados = new ArrayList<>();
        }
    }

    private void obtenerArchivosSalida() {
        if (selectedSalida != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivosSalida = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", selectedSalida).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivosSalida = new ArrayList<>();
        }
    }

    private void obtenerArchivosCC() {
        if (selectedCC != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivosCC = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", selectedCC).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivosCC = new ArrayList<>();
        }
    }

    private void obtenerArchivosBorradores() {
        if (getSelected() != null) {
            List<TiposArchivoAdministrativo> tiposArchivo = new ArrayList<>();
            tiposArchivo.add(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));
            listaArchivosBorradores = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).setParameter("tiposArchivo", tiposArchivo).getResultList();
        } else {
            listaArchivosBorradores = new ArrayList<>();
        }
    }

    private void obtenerDestinatariosBorradores() {
        if (getSelected() != null) {
            TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
            listaDestinatariosBorradores = ejbFacade.getEntityManager().createNamedQuery("UsuariosPorDocumentosAdministrativos.findByTransferenciaDocumentoAdministrativo", UsuariosPorDocumentosAdministrativos.class).setParameter("transferenciaDocumentoAdministrativo", transf).getResultList();
        } else {
            listaDestinatariosBorradores = new ArrayList<>();
        }
    }

    public boolean deshabilitarResponder() {
        return getSelected() == null;
    }

    public boolean desabilitarBotonAlzarArchivo() {
        return false;
    }

    public boolean deshabilitarEditPlazo() {
        return !filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_PLAZOS);
    }

    public String rowClass(DocumentosAdministrativos item) {
        //if (item.getTipoPrioridad() == null) {
        return ((item.getEstado().getTipo().equals("RE")) ? "" : (item.isVisto() ? "" : "green"));
        /*} else {
            return (item.getEstado().getTipo().equals("RE")) ? "" : (item.getTipoPrioridad().getCodigo().equals(Constantes.TIPO_PRIORIDAD_URGENTE) ? "red" : (item.isVisto() ? "" : "green"));
        }*/
    }
    // return (item.getResponsable().equals(usuario)) ? ((item.getEstado().getTipo().equals("RE")) ? "" : (item.isVisto() ? "" : "green")) : "";

    public String rowClassElaboracion(DocumentosAdministrativos item) {
//         return (item.getEstado().getTipo().equals("RE")) ? "" : (item.getTipoPrioridad().getCodigo().equals(Constantes.TIPO_PRIORIDAD_URGENTE)?"red":"");
        return "";
    }

    public String rowClassCC(DocumentosAdministrativos item) {
        // return (item.getResponsable().equals(usuario)) ? ((item.getEstado().getTipo().equals("RE")) ? "" : (item.isVisto() ? "" : "green")) : "";
        // return (item.getEstado().getTipo().equals("RE")) ? "" : (item.isVisto() ? "" : "green");

        if (!item.getEstado().getTipo().equals("RE")) {
            if (item.getFechaPresentacion() != null) {
                if (fechaIniVistoCC.before(item.getFechaPresentacion())) {
                    List<VistosPorDocumentosAdministrativos> lista = ejbFacade.getEntityManager().createNamedQuery("VistosPorDocumentosAdministrativos.findByDepartamentoDocumentoAdministrativo", VistosPorDocumentosAdministrativos.class).setParameter("departamento", usuario.getDepartamento()).setParameter("documentoAdministrativo", item).getResultList();
                    return lista.isEmpty() ? "green" : "";
                }
            }

        }

        return "";
    }

    public void alzarArchivoBorradores() {
        if (getSelected() != null) {
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }
            alzarArchivo(descripcionArchivo, bytes, getSelected());
            obtenerArchivosBorradores();
        }
    }

    public void alzarArchivo() {
        if (getSelected() != null) {
            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }
            alzarArchivo(descripcionArchivo, bytes, getSelected());
            obtenerArchivos();
        }
    }

    public void alzarArchivo(String desc, byte[] bytes, DocumentosAdministrativos doc) {

        if (doc != null) {
            /*
            if (arch == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (arch.getContents().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }
             */

            if (bytes == null) {
                return;
            } else if (bytes.length == 0) {
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            EstadosTransferenciaDocumentoAdministrativo est = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_FINALIZADO).getSingleResult();

            ArchivosAdministrativo archi = new ArchivosAdministrativo();

            archi.setDocumentoAdministrativo(doc);

            archi.setDescripcion(desc);

            archi.setRuta("");
            // archi.setEstado(est);

            archi.setFechaHoraAlta(fecha);

            archi.setFechaHoraUltimoEstado(fecha);

            archi.setUsuarioAlta(usuario);

            archi.setUsuarioUltimoEstado(usuario);
            //archi.setUsuarioRevisado(null);
            //archi.setFechaHoraRevisado(null);
            //archi.setUsuarioRevision(usuario);

            archi.setTipoArchivo(new TiposArchivoAdministrativo(Constantes.TIPO_ARCHIVO_ADM_ADJUNTO));

            archivosController.setSelected(archi);

            archivosController.saveNew(null);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + doc.getId() + "_" + archi.getId() + ".pdf";
            /*
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(arch.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }
             */
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(par.getRutaArchivosAdministrativo() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;

            }

            archi.setRuta(nombreArchivo);

            archivosController.save(null);

        }

    }

    /*
    public void obtenerListaSalida() {
        listaSalida = new ArrayList<>();
        String comando = "select a.* from cambios_estado_documento_administrativo as a, estados_documento_administrativo as b, documentos_administrativos as d where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and  a.estado_final = b.codigo and d.id = a.documento_administrativo and d.tipo_documento_administrativo in " + tiposDocString + " and a.departamento_origen = " + usuario.getDepartamento().getId() + " and a.id in (select max(c.id) from cambios_estado_documento_administrativo as c, estados_documento_administrativo as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_final = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and a.documento_administrativo = c.documento_administrativo) order by a.fecha_hora_alta desc";
        List<TransferenciasDocumentoAdministrativo> cambios = ejbFacade.getEntityManager().createNativeQuery(comando, TransferenciasDocumentoAdministrativo.class).getResultList();
        for (TransferenciasDocumentoAdministrativo cambio : cambios) {

            comando = "select a.* from cambios_estado_documento_administrativo as a, estados_documento_administrativo as e where a.estado_final = e.codigo and a.documento_administrativo = " + cambio.getDocumentoAdministrativo().getId() + " and a.fecha_hora_alta >= '" + format3.format(cambio.getFechaHoraAlta()) + "' ORDER BY a.fecha_hora_alta DESC";
            List<TransferenciasDocumentoAdministrativo> lista = this.ejbFacade.getEntityManager().createNativeQuery(comando, TransferenciasDocumentoAdministrativo.class).getResultList();
            for (TransferenciasDocumentoAdministrativo cam : lista) {
                if (!cam.getDepartamentoOrigen().equals(cam.getDepartamentoDestino())) {
                    listaSalida.add(cam);
                    break;
                }
            }

        }
    }
     */
 /*
    public void cambiarEstado() {
        if (getSelected() != null) {
            try {
                flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumentoAdministrativo.findByEstadoDocumentoActual", FlujosDocumentoAdministrativo.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoAdministrativo().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede cambiar estado");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            EstadosDocumentoAdministrativo estadoDocumentoFinal = null;
            try {
                estadoDocumentoFinal = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar estado final del documento. Documento no se puede cambiar estado");
                return;
            }

            CambiosEstadoDocumentoAdministrativo cambioEstadoDocumento = new CambiosEstadoDocumentoAdministrativo();
            cambioEstadoDocumento.setDocumentoAdministrativo(getSelected());

            cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());

            cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoFinal(estadoDocumentoFinal);

            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
            transferenciasDocumentoAdministrativoController.save(null);

            getSelected().setEstado(estadoDocumentoFinal);
            getSelected().setEstadoProcesal(estadoDocumentoFinal.getDescripcion());

            EstadosProcesalesDocumentosAdministrativos estadoProc = new EstadosProcesalesDocumentosAdministrativos();

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEstadoProcesal(estadoDocumentoFinal.getDescripcion());
            estadoProc.setDocumentoAdministrativo(getSelected());

            // estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
            // estadosProcesalesDocumentosAdministrativosController.saveNew2(null);
            getSelected().setEstadoProcesalDocumentoAdministrativo(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);

            verifDocumentosAtencion();
        }
    }
     */
 /*
    public String navigateObservacionesDocumentosAdministrativosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/verDocumentosAdministrativosMesa/index");
        }
        return "/pages/observacionesDocumentosAdministrativos/index";
    }

    public String navigateEstadosProcesalesDocumentosAdministrativosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosAdministrativos_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosAdministrativos.findByDocumentoAdministrativo", EstadosProcesalesDocumentosAdministrativos.class).setParameter("documentoAdministrativo", getSelected()).getResultList());
        }
        return "/pages/estadosProcesalesDocumentosAdministrativos/index";
    }

    public String navigateCambiosEstadoDocumento() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("documento_administrativo_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/documentosAdministrativos/index");
        }
        return "/pages/cambiosEstadoDocumentoAdministrativo/index";
    }
     */
    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public boolean mostrarBanderin(Date fecha) {
        if (fecha != null) {
            Date now = ejbFacade.getSystemDateOnly(0);

            long diff = now.getTime() - fecha.getTime();

            long dias = TimeUnit.MILLISECONDS.toDays(diff);

            if (dias < 8) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Collection<DocumentosAdministrativos> getItems() {
        return super.getItems2();
    }

    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;

            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedAsignadoAll2", DocumentosAdministrativos.class
                ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedAsignado2", DocumentosAdministrativos.class
                ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).setParameter("departamento", usuario.getDepartamento()).getResultList());
            }
        }
    }

    /*
    public void buscarPorFechaAlta() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;
            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedFechaAltaAsignadoAll2", DocumentosAdministrativos.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedFechaAltaAsignado2", DocumentosAdministrativos.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList());
            }

        }
    }
     */
    public List<DocumentosAdministrativos> buscarBandejaEntrada() {
        return buscarBandejaEntrada(false);
    }

    public List<DocumentosAdministrativos> buscarBandejaEntrada(boolean soloNoVistos) {
        List<DocumentosAdministrativos> lista = new ArrayList<>();
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            if (activeTab == 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaAltaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                busquedaPorFechaAlta = true;

                /*
                if (soloNoVistos) {
                    lista = ejbFacade.getEntityManager().createNativeQuery(      "select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.usuario = ?1      and u.tipo_envio = ?2 and not d.visto and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuarioBandeja.getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();
                } else {
                    lista = ejbFacade.getEntityManager().createNativeQuery(      "select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.usuario = ?1 and u.tipo_envio = ?2 and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuarioBandeja.getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();
                    super.setItems(lista);
                }
                 */
                if (soloNoVistos) {
                    lista = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.departamento = ?1 and u.tipo_envio = ?2 and not d.visto and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY d.fecha_presentacion DESC, r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();
                } else {
                    lista = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.departamento = ?1 and u.tipo_envio = ?2 and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY d.fecha_presentacion DESC, r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();
                    super.setItems(lista);
                }
            } else {
                super.setItems(null);
            }
        }

        return lista;
    }

    public void verPDFsHistorico() {
        List<ArchivosAdministrativo> lista = obtenerPDFs(listaHistorico);
        verDocCompleto(lista);
    }

    private List<ArchivosAdministrativo> obtenerPDFs(List<DocumentosAdministrativos> lista) {
        List<ArchivosAdministrativo> listaArch = new ArrayList<>();

        ArchivosAdministrativo primero;

        for (DocumentosAdministrativos uno : lista) {
            primero = obtenerPrimerArchivo(uno);
            listaArch.add(primero);
        }

        return listaArch;
    }

    public List<DocumentosAdministrativos> buscarBandejaCC() {
        return buscarBandejaCC(false);
    }

    public List<DocumentosAdministrativos> buscarBandejaCC(boolean soloNoVistos) {
        List<DocumentosAdministrativos> lista = new ArrayList<>();
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            if (activeTab == 2) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaAltaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                busquedaPorFechaAlta = true;

                if (soloNoVistos) {
                    lista = ejbFacade.getEntityManager().createNativeQuery("select l.* from documentos_administrativos as l where l.id in (select d.id from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.fecha_presentacion >= '" + format2.format(fechaIniVistoCC) + "' and d.estado <> ?3 and u.departamento = ?1 and u.tipo_envio = ?2) and (select count(*) from vistos_por_documentos_administrativos as v where v.departamento = ?4 and v.documento_administrativo = l.id) = 0 ORDER BY l.fecha_presentacion DESC, l.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_CON_COPIA).setParameter(3, Constantes.ESTADO_DOC_ADM_EN_PROYECTO).setParameter(4, usuario.getDepartamento().getId()).getResultList();
                } else {
                    lista = ejbFacade.getEntityManager().createNativeQuery("select l.* from documentos_administrativos as l where l.id in (select d.id from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado <> ?3 and u.departamento = ?1 and u.tipo_envio = ?2) ORDER BY l.fecha_presentacion DESC, l.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_CON_COPIA).setParameter(3, Constantes.ESTADO_DOC_ADM_EN_PROYECTO).getResultList();
                    listaCC = new ArrayList<>();
                    listaCC.addAll(lista);
                }
            } else {
                listaCC = null;
            }
        }
        return lista;
    }

    public Integer buscarCantidadBandejaCC() {
        /*
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;

            CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_administrativos as l where l.id in (select d.id from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.fecha_presentacion >= '" + format2.format(fechaIniVistoCC) + "' and d.estado <> ?3 and u.departamento = ?1 and u.tipo_envio = ?2) and (select count(*) from vistos_por_documentos_administrativos as v where v.departamento = ?4 and v.documento_administrativo = l.id) = 0 ORDER BY l.fecha_presentacion DESC, l.fecha_hora_alta DESC", CantidadItem.class).setParameter(1, usuarioBandeja.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_CON_COPIA).setParameter(3, Constantes.ESTADO_DOC_ADM_EN_PROYECTO).setParameter(4, usuarioBandeja.getDepartamento().getId()).getSingleResult();

            return cant.getCantidad();
        }
         */
        return 0;
    }

    public List<DocumentosAdministrativos> buscarBandejaArchivado() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            if (activeTab == 3) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaAltaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                busquedaPorFechaAlta = true;

                // listaArchivados = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.usuario = ?1 and u.tipo_envio = ?2 and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuarioBandeja.getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();
                listaArchivados = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado = ?3 and u.departamento = ?1 and u.tipo_envio = ?2 and u.transferencia_documento_administrativo in (select max(id) from transferencias_documento_administrativo as t where t.documento_administrativo = d.id) ORDER BY d.fecha_presentacion DESC, r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_DESTINATARIO).setParameter(3, Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();
            } else {
                listaArchivados = null;
            }
        }
        return listaArchivados;
    }

    public List<DocumentosAdministrativos> buscarBandejaSalida() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            if (activeTab == 1) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaAltaHasta);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();
                busquedaPorFechaAlta = true;

                // listaSalida = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado in (?3,?4) and u.usuario = ?1 and u.tipo_envio = ?2 ORDER BY r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuarioBandeja.getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_REMITENTE).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).setParameter(4, Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();
                listaSalida = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_administrativos as d, usuarios_por_documentos_administrativos as u, transferencias_documento_administrativo as r where r.estado = 'FI' AND r.id = u.transferencia_documento_administrativo and d.id = u.documento_administrativo and d.estado in (?3,?4) and u.departamento = ?1 and u.tipo_envio = ?2 ORDER BY d.fecha_presentacion DESC, r.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, usuario.getDepartamento().getId()).setParameter(2, Constantes.TIPO_ENVIO_ARCHIVO_ADM_REMITENTE).setParameter(3, Constantes.ESTADO_DOC_ADM_ACTIVO).setParameter(4, Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();

            } else {
                listaSalida = null;
            }
        }
        return listaSalida;
    }

    public List<DocumentosAdministrativos> buscarPorFechaAltaBorradores(EstadosTransferenciaDocumentoAdministrativo estado, Usuarios usuAlta, Usuarios usuResponsable) {
        List<DocumentosAdministrativos> listaBorr = new ArrayList<>();
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;

            if (usuAlta != null) {
                // listaBorr = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findByArchivoAdmEstado", DocumentosAdministrativos.class).setParameter("estado", estado).getResultList();
                // listaBorr = ejbFacade.getEntityManager().createNativeQuery("SELECT d.* FROM documentos_administrativos as d, transferencias_documento_administrativo as t WHERE t.documento_administrativo = d.id AND t.estado = ?1 AND t.usuario_elaboracion = ?2 and d.id not in (SELECT d.id FROM documentos_administrativos as d, transferencias_documento_administrativo as t WHERE t.documento_administrativo = d.id AND t.estado = ?3 AND t.usuario_revision = ?4) ORDER BY d.fecha_hora_alta", DocumentosAdministrativos.class).setParameter(1, estado.getCodigo()).setParameter(2, usuAlta.getId()).setParameter(3, estado.getCodigo()).setParameter(4, usuAlta.getId()).getResultList();
                listaBorr = ejbFacade.getEntityManager().createNativeQuery("SELECT d.* FROM documentos_administrativos as d, transferencias_documento_administrativo as t WHERE t.documento_administrativo = d.id AND t.estado = ?1 AND t.usuario_elaboracion = ?2 ORDER BY d.fecha_presentacion DESC, d.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, estado.getCodigo()).setParameter(2, usuAlta.getId()).getResultList();
            } else if (usuResponsable != null) {
                listaBorr = ejbFacade.getEntityManager().createNativeQuery("SELECT d.* FROM documentos_administrativos as d, transferencias_documento_administrativo as t WHERE t.documento_administrativo = d.id AND t.estado = ?1 AND t.usuario_revision = ?2 ORDER BY d.fecha_presentacion DESC, d.fecha_hora_alta DESC", DocumentosAdministrativos.class).setParameter(1, estado.getCodigo()).setParameter(2, usuResponsable.getId()).getResultList();
                /*} else {
                listaBorr = ejbFacade.getEntityManager().createNativeQuery("SELECT d.* FROM archivos_administrativo as a, documentos_administrativos as d WHERE a.documento_administrativo = d.id AND a.estado = ?1 ORDER BY d.fecha_hora_alta", DocumentosAdministrativos.class).setParameter(1, estado.getCodigo()).getResultList();
                 */
            }
        }

        return listaBorr;
    }

    /*
    public void buscarPorFechaAltaArchivados() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;
            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedFechaAltaAsignadoAll3", DocumentosAdministrativos.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            } else {
                listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findOrderedFechaAltaAsignado3", DocumentosAdministrativos.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoAdministrativo", canal).setParameter("tiposDocumentoAdministrativo", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            }

        }
    }
     */
    public void editarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosAdministrativos obs = getSelected().getObservacionDocumentoAdministrativo();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.save(null);

                getSelected().setObservacionDocumentoAdministrativo(obs);

                super.save(null);
            }

        }
    }

    public void agregarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosAdministrativos obs = new ObservacionesDocumentosAdministrativos();

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoAdministrativo(obs);

                super.save(null);

                obs.setDocumentoAdministrativo(getSelected());

                obsController.save(null);
            }

        }
    }

    public void editarEstadoProcesal() {

        if (getSelected() != null) {
            if (getSelected().verifEstadoProcesal()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraEstadoProcesal(fecha);

                getSelected().transferirEstadoProcesal();

                getSelected().setUsuarioEstadoProcesal(usuario);

                EstadosProcesalesDocumentosAdministrativos estadoProc = getSelected().getEstadoProcesalDocumentoAdministrativo();

                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
                estadosProcesalesDocumentosAdministrativosController.save(null);

                getSelected().setEstadoProcesalDocumentoAdministrativo(estadoProc);

                super.save(null);
            }

        }
    }

    public void agregarEstadoProcesal() {

        if (getSelected() != null) {
            if (getSelected().verifEstadoProcesal()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraEstadoProcesal(fecha);

                getSelected().transferirEstadoProcesal();

                getSelected().setUsuarioEstadoProcesal(usuario);

                EstadosProcesalesDocumentosAdministrativos estadoProc = new EstadosProcesalesDocumentosAdministrativos();

                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadoProc.setDocumentoAdministrativo(getSelected());

                estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
                estadosProcesalesDocumentosAdministrativosController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoAdministrativo(estadoProc);

                super.save(null);
            }

        }
    }

    /*
    @Override
    public void delete(ActionEvent event) {
        if (getSelected().getCanalEntradaDocumentoAdministrativo().equals(canal)) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
                super.delete(event);

                if (busquedaPorFechaAlta) {
                    buscarPorFechaAlta();
                } else {

                    if (fechaDesde == null) {
                        fechaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaHasta == null) {
                        fechaHasta = ejbFacade.getSystemDateOnly();
                    }

                    buscarPorFechaPresentacion();

                }
            } else {
                JsfUtil.addErrorMessage("Solo un funcionario de Secretaria puede borrarlo");
            }
        } else {
            JsfUtil.addErrorMessage("Solo puede borrar documentos que fueron ingresados directamente en Secretaria");
        }
    }
     */
    public void alzarArchivo(ArchivosAdministrativo arch) {

        if (getSelected() != null) {

            if (file == null) {
                return;
            } else if (file.getContent().length == 0) {
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + ".pdf";

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            if (arch == null) {
                ArchivosAdministrativo archivo = new ArchivosAdministrativo();

                archivo.setDocumentoAdministrativo(getSelected());
                archivo.setDescripcion(getSelected().getDescripcionMesaEntrada());
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
            } else {
                arch.setDocumentoAdministrativo(getSelected());
                arch.setDescripcion(getSelected().getDescripcionMesaEntrada());
                arch.setRuta(nombreArchivo);
                arch.setFechaHoraAlta(fecha);
                arch.setFechaHoraUltimoEstado(fecha);
                arch.setUsuarioAlta(usuario);
                arch.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(arch);
                archivosController.save(null);
            }
        }

    }

    public void desArchivar() {
        if (selectedArchivados != null) {

            List<EstadosDocumentoAdministrativo> lista = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();

            if (!lista.isEmpty()) {
                selectedArchivados.setEstado(lista.get(0));

                DocumentosAdministrativos docAnt = getSelected();

                setSelected(selectedArchivados);
                super.save(null);
                setSelected(docAnt);

                obtenerDatos(accion);
                resetParents();
                buscarBandejaArchivado();
            }
        }
    }

    public void archivar() {
        if (getSelected() != null) {

            List<EstadosDocumentoAdministrativo> lista = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ARCHIVADO).getResultList();

            if (!lista.isEmpty()) {
                getSelected().setEstado(lista.get(0));
                super.save(null);
                setSelected(null);
                obtenerDatos(accion);
                resetParents();
                buscarBandejaArchivado();
            }
        }
    }

    /*
    public void saveNew() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoNew") == null) ? false : params.get("guardandoNew"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", true);
                if (getSelected() != null) {
                    //if (detalles != null) {
                    //if (detalles.size() > 0) {
                    if (entradaDocumentoAdministrativo.getNroMesaEntrada() != null) {
                        if (!entradaDocumentoAdministrativo.getNroMesaEntrada().equals("")) {
                            if (tipoDocumentoAdministrativo != null) {
                                if (subcategoriaDocumentoAdministrativo != null) {
                                    if (entradaDocumentoAdministrativo != null) {
                                        if (entradaDocumentoAdministrativo.getNroMesaEntrada() == null) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        } else if ("".equals(entradaDocumentoAdministrativo.getNroMesaEntrada())) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        }
                                    }

                                    EstadosDocumentoAdministrativo estado = null;
                                    try {
                                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", "-1").getSingleResult();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JsfUtil.addErrorMessage("Error de configuracion. No se pudo guardar documento");
                                        return;
                                    }

                                    Date fecha = ejbFacade.getSystemDate();
                                    EntradasDocumentosAdministrativos entradaDoc = new EntradasDocumentosAdministrativos();

                                    entradaDoc.setFechaHoraUltimoEstado(fecha);
                                    entradaDoc.setUsuarioUltimoEstado(usuario);
                                    entradaDoc.setFechaHoraAlta(fecha);
                                    entradaDoc.setUsuarioAlta(usuario);
                                    entradaDoc.setEmpresa(usuario.getEmpresa());
                                    entradaDoc.setNroMesaEntrada(eerSgteNroMesaEntradaAdministrativa());

                                    //entradasDocumentosAdministrativosController.setSelected(entradaDoc);
                                    //entradasDocumentosAdministrativosController.saveNew(null);
                                    getSelected().setFechaHoraUltimoEstado(fecha);
                                    getSelected().setUsuarioUltimoEstado(usuario);
                                    getSelected().setFechaHoraAlta(fecha);
                                    getSelected().setUsuarioAlta(usuario);
                                    getSelected().setDepartamento(usuario.getDepartamento());
                                    getSelected().setEntradaDocumentoAdministrativo(entradaDoc);
                                    getSelected().setDescripcionMesaEntrada(descripcionMesaEntrada);
                                    getSelected().setCaratula(descripcionMesaEntrada);
                                    getSelected().setEstadoProcesal(estado.getDescripcion());
                                    getSelected().setFechaPresentacion(fecha);
                                    getSelected().setEstado(estado);

                                    getSelected().setResponsable(usuario);
                                    getSelected().setCanalEntradaDocumentoAdministrativo(canal);
                                    getSelected().setTipoDocumentoAdministrativo(tipoDocumentoAdministrativo);
                                    getSelected().setSubcategoriaDocumentoAdministrativo(subcategoriaDocumentoAdministrativo);
                                    getSelected().setMostrarWeb("NO");

                                    super.saveNew(null);

                                    alzarArchivo(null);

                                    CambiosEstadoDocumentoAdministrativo cambioEstadoDocumento = new CambiosEstadoDocumentoAdministrativo();
                                    cambioEstadoDocumento.setDocumentoAdministrativo(getSelected());

                                    cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoOriginal(estado);

                                    cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoFinal(estado);

                                    cambioEstadoDocumento.setFechaHoraAlta(fecha);
                                    cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                                    cambioEstadoDocumento.setUsuarioAlta(usuario);
                                    cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                                    transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
                                    transferenciasDocumentoAdministrativoController.save(null);

                                    EstadosProcesalesDocumentosAdministrativos estadoProc = estadosProcesalesDocumentosAdministrativosController.prepareCreate(null);

                                    estadoProc.setUsuarioAlta(usuario);
                                    estadoProc.setUsuarioUltimoEstado(usuario);
                                    estadoProc.setFechaHoraAlta(fecha);
                                    estadoProc.setFechaHoraUltimoEstado(fecha);
                                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                                    estadoProc.setDocumentoAdministrativo(getSelected());

                                    estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
                                    estadosProcesalesDocumentosAdministrativosController.saveNew(null);

                                    getSelected().setEstadoProcesalDocumentoAdministrativo(estadoProc);
                                    getSelected().setUsuarioEstadoProcesal(usuario);
                                    getSelected().setFechaHoraEstadoProcesal(fecha);

                                    super.save(null);

                                    if (fechaDesde == null) {
                                        fechaDesde = ejbFacade.getSystemDateOnly(-30);
                                    }
                                    if (fechaHasta == null) {
                                        fechaHasta = ejbFacade.getSystemDateOnly();
                                    }

                                    buscarPorFechaPresentacion();

                                } else {
                                    JsfUtil.addErrorMessage("Subcategoria: campo requerido");
                                }
                            } else {
                                JsfUtil.addErrorMessage("Tipo Documento: campo requerido");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                    }
                }
            }
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", false);
        }
    }
     */
 /*
    private EstadosDocumentoAdministrativo obtenerSgteEstado(String estadoIni){
        
        List<RolesPorUsuarios> roles = ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class).setParameter("usuario", usuario.getId()).getResultList();

        if (roles.isEmpty()) {
            JsfUtil.addErrorMessage("El usuario no tiene roles asociados");
            return null;
        }

        boolean encontro = false;
        RolesPorUsuarios rolFlujo = null;
        for (RolesPorUsuarios rol : roles) {
            if (rol.getRoles().getId() < 0) {
                if (encontro) {
                    JsfUtil.addErrorMessage("El usuario tiene mas de un rol de flujo configurado");
                    return null;
                }

                encontro = true;
                rolFlujo = rol;
            }
        }

        EstadosDocumentoAdministrativo estado = null;
        if (rolFlujo != null) {
            List<FlujosDocumentoAdministrativo> flujo = ejbFacade.getEntityManager().createNamedQuery("FlujosDocumentoAdministrativo.findSgteEstadoSegunRol", FlujosDocumentoAdministrativo.class).setParameter("estadoDocumentoActual", estadoIni).setParameter("tipoDocumento", tipoDocumentoAdministrativo.getCodigo()).setParameter("rolFinal", rolFlujo.getRoles()).getResultList();
            if (flujo.isEmpty()) {
                JsfUtil.addErrorMessage("El usuario no tiene configurado un flujo de documento");
                return null;
            }

            if (flujo.size() > 1) {
                JsfUtil.addErrorMessage("El usuario tiene un flujo configurado");
                return null;
            }

            List<EstadosDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", flujo.get(0).getEstadoDocumentoFinal()).getResultList();
            if (estados.isEmpty()) {
                JsfUtil.addErrorMessage("No se encontro estado");
                return null;
            }

            estado = estados.get(0);
        }
        
        return estado;
    }
     */
 /*
    private void agregarQR(String nombreOri, String nombre, String hash, boolean primeraPagina) {
        // nombre + "_temp"
        PdfWriter writer;
        try {

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombreOri), new PdfWriter(nombre));

            Document document = new Document(pdfDoc);

            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_ACTUACION;
            BarcodeQRCode qrCode = new BarcodeQRCode(path + "?hash=" + hash);
            // PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDoc);
            // Image barcodeImage = new Image(barcodeObject).setWidth(50f).setHeight(50f);

            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));

            if (primeraPagina) {
                // qrCode.createAwtImage(Color.darkGray, Color.darkGray);
                PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
                pdfDoc.addNewPage(1, ps);

                PdfPage page = pdfDoc.getPage(1);

                float fontSize = 50.0f;
                float allowedWidth = 500;

                Paragraph paragraph = new Paragraph("Es copia del instrumento que tuve a la vista").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(fontSize);

                Canvas canvas = new Canvas(new PdfCanvas(page), pdfDoc, page.getMediaBox());
                RootRenderer canvasRenderer = canvas.getRenderer();
                // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                while (paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                    paragraph.setFontSize(--fontSize);
                }

                float xCoord = page.getPageSize().getRight() / 2;
                float yCoord = 120;

                paragraph.setWidth(allowedWidth);
                canvas.showTextAligned(paragraph, xCoord, yCoord, TextAlignment.CENTER);
                canvas.close();
            }

            //PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {

                PdfPage pagina = pdfDoc.getPage(i + 1);

                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

                // PdfXObject xObject = PdfXObject();
                Rectangle rect = new Rectangle(pagina.getPageSize().getRight() - 100, 1, 80, 80);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
                //under.addImage(ImageDataFactory.create(imagen, Color.white), 50, 0f, 0f, 50, 0, 0, false);

                Paragraph paragraph2 = new Paragraph("Para conocer la validez del documento verifique aqui").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE)).setFontSize(8);
                Canvas canvas2 = new Canvas(new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc), pdfDoc, pagina.getMediaBox());

                RootRenderer canvasRenderer2 = canvas2.getRenderer();
                paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(0, new Rectangle(pagina.getPageSize().getRight() - 100, 6 * 2))));
                paragraph2.setWidth(60);
                canvas2.showTextAligned(paragraph2, pagina.getPageSize().getRight() - 100, 10, TextAlignment.RIGHT);
                canvas2.close();

                under.saveState();
            }

            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    private void agregarSelloDeCargoQR(String nombre, String nombreAbogado, String ci, String nombreCodigo, float tamano, Date fechaHoraAlta, DocumentosAdministrativos doc, Date fecha, String codigoArchivo) {
        PdfWriter writer;
        try {

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombre + "_temp"), new PdfWriter(nombre));

            // qrCode.createAwtImage(Color.darkGray, Color.darkGray);
            PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
            pdfDoc.addNewPage(pdfDoc.getNumberOfPages() + 1, ps);

            PdfPage page = pdfDoc.getPage(pdfDoc.getNumberOfPages());

            float fontSize = 12.0f;
            float allowedWidth = 500;

            String fechaString = ejbFacade.getSystemDateString(fecha, 0);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            // codigoArchivo = generarCodigoArchivo();
            String mensaje = "DOCUMENTO PRESENTADO ELECTRÓNICAMENTE CON FECHA DE SELLO DE CARGO: " + fechaString + " A LAS " + format.format(fecha) + ", CONFORME EL PROTOCOLO DE TRAMITACIÓN ELECTRÓNICA DEL JURADO DE ENJUICIAMIENTO DE MAGISTRADOS. QUEDA CERTIFICADA SU RECEPCIÓN" + (codigoArchivo == null ? "" : ("".equals(codigoArchivo) ? "" : "\nCÓDIGO DE VERIFICACIÓN: " + codigoArchivo));

            Paragraph paragraph = new Paragraph(mensaje).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);

            Canvas canvas = new Canvas(new PdfCanvas(page), pdfDoc, page.getMediaBox());
            RootRenderer canvasRenderer = canvas.getRenderer();
            // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
            paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 750))));

            paragraph.setWidth(500);
            canvas.showTextAligned(paragraph, 50, 350, TextAlignment.LEFT);

            /*
            if(codigoArchivo != null){
                Paragraph paragraph2 = new Paragraph("CÓDIGO DE VERIFICACIÓN: " + codigoArchivo).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
                paragraph2.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

                paragraph2.setWidth(500);
                canvas.showTextAligned(paragraph2, 50, 300, TextAlignment.LEFT);
            }
             */
            String path = validarExterno(doc);

            // String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;
            BarcodeQRCode qrCode = new BarcodeQRCode(path);
            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));
            PdfCanvas under = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
            Rectangle rect = new Rectangle(20, 100, 200, 200);
            under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);

            // Paragraph paragraph1 = new Paragraph("Registrado electrónicamente por: " + nombreAbogado + " CI. " + ci).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            Paragraph paragraph1 = new Paragraph("Registrado electrónicamente por: " + nombreAbogado).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph1.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph1.setWidth(500);
            canvas.showTextAligned(paragraph1, 50, 300, TextAlignment.LEFT);


            /*
            Paragraph paragraph2 = new Paragraph("NOMBRE: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph2.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph2.setWidth(130);
            canvas.showTextAligned(paragraph2, 250, 250, TextAlignment.LEFT);

            Paragraph paragraph2_ = new Paragraph(nombreCodigo).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph2_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph2_.setWidth(200);
            canvas.showTextAligned(paragraph2_, 400, 250, TextAlignment.LEFT);

            Paragraph paragraph3 = new Paragraph("TAMAÑO: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph3.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph3.setWidth(130);
            canvas.showTextAligned(paragraph3, 250, 230, TextAlignment.LEFT);

            Paragraph paragraph3_ = new Paragraph(DecimalFormat.getNumberInstance().format(tamano) + " MB").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph3_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph3_.setWidth(200);
            canvas.showTextAligned(paragraph3_, 400, 230, TextAlignment.LEFT);

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Paragraph paragraph4 = new Paragraph("FECHA REGISTRO ELECTRONICO: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph4.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(500, 500))));

            paragraph4.setWidth(130);
            canvas.showTextAligned(paragraph4, 250, 190, TextAlignment.LEFT);

            Paragraph paragraph4_ = new Paragraph(format2.format(fecha)).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph4_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph4_.setWidth(200);
            canvas.showTextAligned(paragraph4_, 400, 190, TextAlignment.LEFT);

            Paragraph paragraph5 = new Paragraph(hash + hash + hash + hash).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph5.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(500, 500))));

            paragraph5.setWidth(300);
            canvas.showTextAligned(paragraph5, 250, 130, TextAlignment.LEFT);
             */
            canvas.close();

            /*
            //PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
                PdfPage pagina = pdfDoc.getPage(i + 1);
                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

                // PdfXObject xObject = PdfXObject();
                Rectangle rect = new Rectangle(page.getPageSize().getRight() - 100, 1, 50, 50);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
                //under.addImage(ImageDataFactory.create(imagen, Color.white), 50, 0f, 0f, 50, 0, 0, false);

                Paragraph paragraph2 = new Paragraph("Para conocer la validez del documento verifique aqui").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE)).setFontSize(8);
                Canvas canvas2 = new Canvas(new PdfCanvas(pagina), pdfDoc, pagina.getMediaBox());
                RootRenderer canvasRenderer2 = canvas.getRenderer();
                paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(0, new Rectangle(page.getPageSize().getRight() - 100, 6 * 2))));
                paragraph2.setWidth(60);
                canvas2.showTextAligned(paragraph2, page.getPageSize().getRight() - 100, 10, TextAlignment.RIGHT);
                canvas2.close();

                under.saveState();
            }
             */
            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String generarCodigoArchivo() {
        Random aleatorio;
        int valor = 0;
        String pin = "";
        aleatorio = new Random(System.currentTimeMillis());
        valor = aleatorio.nextInt(999999);
        pin = String.valueOf(valor);
        while (pin.trim().length() < 6) {
            pin = '0' + pin;
        }
        return pin;
    }

    public void saveNewDocAdm() {
        if (getSelected() != null) {

            /*
            if (getSelected().getFechaPresentacion() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Fecha Presentacion");
                return;
            }

            if (getSelected().getCausa() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Nro Causa");
                return;
            } else if ("".equals(getSelected().getCausa())) {
                JsfUtil.addErrorMessage("Debe ingresar Nro Causa");
                return;
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Caratula");
                return;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage("Debe ingresar Caratula");
                return;
            }

            if (getSelected().getMotivoProceso() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Motivo Proceso");
                return;
            } else if ("".equals(getSelected().getMotivoProceso())) {
                JsfUtil.addErrorMessage("Debe ingresar Motivo Proceso");
                return;
            }

            if (getSelected().getEstadoProcesal() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Estado Procesal");
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage("Debe ingresar Estado Procesal");
                return;
            }
             */
            if (archivo == null) {
                JsfUtil.addErrorMessage("No se puede guardar documento");
                return;
            }

            if (archivo.getFormato() == null) {
                JsfUtil.addErrorMessage("Debe elegir un formato");
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage("Debe elegir un formato");
                return;
            }
            if (subcategoriaDocumentoAdministrativo == null) {
                JsfUtil.addErrorMessage("Debe ingresar tipo documento");
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage("Debe ingresar tipo documento");
                return;

            }
            /*
            if (tipoPrioridad == null) {
                JsfUtil.addErrorMessage("Debe elegir prioridad");
                return;
            }
             */
            if (listaUsuariosRemitente == null) {
                JsfUtil.addErrorMessage("Debe elegir un remitente");
                return;
            } else if (listaUsuariosRemitente.isEmpty()) {
                JsfUtil.addErrorMessage("Debe elegir un remitente.");
                return;
            }

            if (listaUsuariosEnviar == null) {
                JsfUtil.addSuccessMessage("Debe elegir al menos un destinatario");
                return;
            } else if (listaUsuariosEnviar.isEmpty()) {
                JsfUtil.addSuccessMessage("Debe elegir al menos un destinatario.");
                return;
            }

            // EstadosDocumentoAdministrativo estado = obtenerSgteEstado(Constantes.ESTADO_DOC_ADM_EN_PROYECTO);
            List<EstadosDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_EN_PROYECTO).getResultList();

            EstadosDocumentoAdministrativo estado = null;

            if (!estados.isEmpty()) {
                estado = estados.get(0);
            }

            if (estado == null) {
                return;
            }

            /*
            EstadosDocumentoAdministrativo estado = null;
            if (rolFlujo != null) {
                List<FlujosDocumentoAdministrativo> flujo = ejbFacade.getEntityManager().createNamedQuery("FlujosDocumentoAdministrativo.findSgteEstadoSegunRol", FlujosDocumentoAdministrativo.class).setParameter("estadoDocumentoActual", Constantes.ESTADO_DOCUMENTO_INGRESADO).setParameter("tipoDocumento", tipoDocumentoAdministrativo.getCodigo()).setParameter("rolFinal", rolFlujo.getRoles()).getResultList();
                if (flujo.isEmpty()) {
                    JsfUtil.addErrorMessage("El usuario no tiene configurado un flujo de documento");
                    return;
                }

                if (flujo.size() > 1) {
                    JsfUtil.addErrorMessage("El usuario tiene un flujo configurado");
                    return;
                }

                List<EstadosDocumentoAdministrativo> estados = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", flujo.get(0).getEstadoDocumentoFinal()).getResultList();
                if (estados.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encontro estado");
                    return;
                }

                estado = estados.get(0);
            }
             */
            try {
                Date fecha = ejbFacade.getSystemDate();

                EntradasDocumentosAdministrativos entradaDoc = new EntradasDocumentosAdministrativos();

                entradaDoc.setFechaHoraUltimoEstado(fecha);

                entradaDoc.setUsuarioUltimoEstado(usuario);

                entradaDoc.setFechaHoraAlta(fecha);

                entradaDoc.setUsuarioAlta(usuario);

                entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

                getSelected().setSubcategoriaDocumentoAdministrativo(subcategoriaDocumentoAdministrativo);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                getSelected().setFechaHoraAlta(fecha);
                getSelected().setUsuarioAlta(usuario);
                getSelected().setFechaHoraElaboracion(fecha);
                getSelected().setUsuarioElaboracion(usuario);
                getSelected().setDescripcionMesaEntrada(descripcionMesaEntrada);
                getSelected().setEntradaDocumentoAdministrativo(entradaDoc);
                //getSelected().setUltimaObservacion(ultimaObservacion);
                // getSelected().setFechaUltimaObservacion(fecha);
                getSelected().setCanalEntradaDocumentoAdministrativo(canal);
                // getSelected().setTipoDocumentoAdministrativo(tiposDoc.get(0));
                getSelected().setTipoDocumentoAdministrativo(subcategoriaDocumentoAdministrativo.getTipoDocumentoAdministrativo());
                getSelected().setResponsable(usuarioRemitente);
                getSelected().setDepartamento(usuarioRemitente.getDepartamento());
                getSelected().setMostrarWeb("NO");
                getSelected().setFechaPresentacion(fecha);
                getSelected().setResponsableAnterior(null);
                getSelected().setDepartamentoAnterior(null);
                getSelected().setUsuarioVisto(null);
                getSelected().setFechaHoraVisto(null);
                getSelected().setVisto(false);
                // getSelected().setTipoPrioridad(tipoPrioridad);

                /*
            EstadosDocumento estado = estadoController.prepareCreate(null);
            estado.setCodigo("0");
                 */
                getSelected().setEstado(estado);
                /*
            if (file != null) {
                if (file.getContents().length > 0) {

                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputstream());
                    } catch (IOException ex) {
                    }

                    // getSelected().setDocumento(bytes);
                }

            }
                 */

                EntradasDocumentosAdministrativos doc = new EntradasDocumentosAdministrativos();

                doc.setFechaHoraUltimoEstado(fecha);
                doc.setUsuarioUltimoEstado(usuario);
                doc.setFechaHoraAlta(fecha);
                doc.setUsuarioAlta(usuario);

                jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery("select ifnull(max(CONVERT(substring(nro_mesa_entrada,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_administrativos WHERE substring(nro_mesa_entrada,1,4) = 'AUTO';", NroMesaEntrada.class);

                NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

                doc.setNroMesaEntrada("AUTO-" + String.valueOf(cod.getCodigo() + 1));

                //entradasDocumentosAdministrativosController.setSelected(doc);
                //entradasDocumentosAdministrativosController.saveNew(null);
                getSelected().setEntradaDocumentoAdministrativo(doc);

                super.saveNew(null);

                archivo.setDocumentoAdministrativo(getSelected());
                archivo.setDescripcion(getSelected().getDescripcionMesaEntrada());
                archivo.setRuta(null);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);
                //archivo.setUsuarioRevision(usuarioRemitente);
                //archivo.setUsuarioRevisado(null);
                //archivo.setFechaHoraRevisado(null);

                /*
            if (usuario.equals(usuarioRemitente)) {
                estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getSingleResult();
            } else {
                estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getSingleResult();
            }
                 */
 /*if (usuario.equals(usuarioRemitente)) {
                estadoArchivo = estadoTransDocAdmEnRevision;
            } else {*/
                estadoArchivo = estadoTransDocAdmEnProyecto;
                //}

                // archivo.setEstado(estadoArchivo);
                archivo.setTipoArchivo(subcategoriaDocumentoAdministrativo.getTipoArchivo());

                archivosController.setSelected(archivo);

                archivosController.saveNew(null);

                TransferenciasDocumentoAdministrativo transf = new TransferenciasDocumentoAdministrativo();

                transf.setDocumentoAdministrativo(getSelected());
                transf.setResponsableOrigen(usuarioRemitente);
                transf.setDepartamentoOrigen(usuarioRemitente.getDepartamento());
                transf.setResponsableDestino(usuarioEnviar);
                transf.setDepartamentoDestino(usuarioEnviar.getDepartamento());
                transf.setUsuarioRevision(usuarioRemitente);
                transf.setRevisado(false);
                transf.setUsuarioFirma(usuarioRemitente);
                transf.setFirmado(false);
                transf.setFechaHoraAlta(fecha);
                transf.setFechaHoraUltimoEstado(fecha);
                transf.setUsuarioAlta(usuario);
                transf.setUsuarioUltimoEstado(usuario);
                transf.setEstado(estadoArchivo);
                transf.setUsuarioElaboracion(usuario);
                transf.setFechaHoraElaboracion(fecha);

                transferenciasDocumentoAdministrativoController.setSelected(transf);
                transferenciasDocumentoAdministrativoController.saveNew(null);

                saveUsuarios(tipoEnvioArchivoAdmConCopia, listaUsuariosCC, transf);
                saveUsuarios(tipoEnvioArchivoAdmRemitente, listaUsuariosRemitente, transf);
                saveUsuarios(tipoEnvioArchivoAdmDestinatario, listaUsuariosEnviar, transf);
                /*
            if (files != null) {
                for (UploadedFile f : files.getFiles()) {
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(f.getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo");
                        return;
                    }
                    alzarArchivo("Archivo adjunto", bytes, getSelected());
                }
            }
                 */

                if (listaFiles != null) {
                    for (UploadedFile f : listaFiles) {
                        byte[] bytes = null;
                        if (f != null) {
                            try {
                                bytes = IOUtils.toByteArray(f.getInputStream());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JsfUtil.addErrorMessage("Error al leer archivo");
                                return;
                            }
                            alzarArchivo("Archivo adjunto", bytes, getSelected());
                        }
                    }
                }

                if (getSelected().getUltimaObservacion() != null && !"".equals(getSelected().getUltimaObservacion())) {
                    ObservacionesDocumentosAdministrativos obs = new ObservacionesDocumentosAdministrativos();

                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fecha);
                    obs.setFechaHoraUltimoEstado(fecha);
                    obs.setObservacion(getSelected().getUltimaObservacion());
                    obs.setDocumentoAdministrativo(getSelected());

                    obsController.setSelected(obs);
                    obsController.saveNew(null);

                    getSelected().setObservacionDocumentoAdministrativo(obs);

                    super.save(null);
                }

                setSelected(null);

                // super.saveNew2(null);
                if (busquedaPorFechaAlta) {
                    /*
                if (fechaAltaDesde == null) {
                    fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaAltaHasta == null) {
                    fechaAltaHasta = ejbFacade.getSystemDateOnly();
                }
                     */
                    obtenerDatos("");
                } else {

                    if (fechaDesde == null) {
                        fechaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaHasta == null) {
                        fechaHasta = ejbFacade.getSystemDateOnly();
                    }

                    buscarPorFechaPresentacion();

                }

                if (!isErrorPersistencia()) {
                    borrarAutoGuardado(usuario);
                }

            } finally {
                editando = false;
            }

            obtenerArchivos();
        }

    }

    private ArchivosAdministrativo obtenerPrimerArchivo(DocumentosAdministrativos doc) {
        if (doc != null) {
            List<ArchivosAdministrativo> lista = ejbFacade.getEntityManager().createNativeQuery("select a.* from archivos_administrativo as a where a.documento_administrativo = ?1 and id in (select min(id) from archivos_administrativo as d where d.documento_administrativo = a.documento_administrativo)", ArchivosAdministrativo.class).setParameter(1, doc.getId()).getResultList();
            if (!lista.isEmpty()) {
                return lista.get(0);
            }
        }

        return null;
    }

    private TransferenciasDocumentoAdministrativo obtenerTransferenciaActual(DocumentosAdministrativos doc) {
        if (doc != null) {
            List<EstadosTransferenciaDocumentoAdministrativo> estados = new ArrayList<>();
            estados.add(estadoTransDocAdmEnProyecto);
            estados.add(estadoTransDocAdmEnRevision);
            List<TransferenciasDocumentoAdministrativo> list = ejbFacade.getEntityManager().createNamedQuery("TransferenciasDocumentoAdministrativo.findByDocumentoAdministrativoANDEstados", TransferenciasDocumentoAdministrativo.class
            ).setParameter("documentoAdministrativo", doc).setParameter("estados", estados).getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    public void saveUsuarios() {
        if (getSelected() != null) {

            ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());

            if (arch != null) {

                if (usuario.equals(usuarioRemitente)) {
                    estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class
                    ).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getSingleResult();
                } else {
                    estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class
                    ).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getSingleResult();
                }
                /*
                arch.setEstado(estadoArchivo);
                //arch.setUsuarioRevision(usuarioRemitente);

                archivosController.setSelected(arch);
                archivosController.save(null);
                 */

                TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                if (transf != null) {
                    transf.setUsuarioRevision(usuarioRemitente);
                    transf.setUsuarioFirma(usuarioRemitente);
                    transf.setResponsableOrigen(usuarioRemitente);
                    transf.setDepartamentoOrigen(usuarioRemitente.getDepartamento());
                    transf.setResponsableDestino(usuarioEnviar);
                    transf.setDepartamentoDestino(usuarioEnviar.getDepartamento());
                    transf.setEstado(estadoArchivo);
                    transferenciasDocumentoAdministrativoController.setSelected(transf);
                    transferenciasDocumentoAdministrativoController.save(null);

                    saveUsuarios(tipoEnvioArchivoAdmConCopia, listaUsuariosCC, transf);
                    saveUsuarios(tipoEnvioArchivoAdmRemitente, listaUsuariosRemitente, transf);
                    saveUsuarios(tipoEnvioArchivoAdmDestinatario, listaUsuariosEnviar, transf);
                }

                obtenerDatos(accion);
                resetParentsBorradores();
            }
        }
    }

    public void saveDocAdm() {
        if (getSelected() != null) {

            if (listaUsuariosRemitente == null) {
                JsfUtil.addErrorMessage("Debe elegir un remitente");
                return;
            } else if (listaUsuariosRemitente.isEmpty()) {
                JsfUtil.addErrorMessage("Debe elegir un remitente.");
                return;
            }

            if (listaUsuariosEnviar == null) {
                JsfUtil.addErrorMessage("Debe elegir al menos un destinatario");
                return;
            } else if (listaUsuariosEnviar.isEmpty()) {
                JsfUtil.addErrorMessage("Debe elegir al menos un destinatario.");
                return;
            }
            /*
            if (tipoPrioridad == null) {
                JsfUtil.addErrorMessage("Debe elegir prioridad");
                return;
            }
             */
            try {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                getSelected().setResponsable(usuarioRemitente);
                getSelected().setDepartamento(usuarioRemitente.getDepartamento());
                // getSelected().setTipoPrioridad(tipoPrioridad);

                // List<ArchivosAdministrativo> lista = ejbFacade.getEntityManager().createNamedQuery("ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", ArchivosAdministrativo.class).setParameter("documentoAdministrativo", getSelected()).getResultList();
                ArchivosAdministrativo arch = obtenerPrimerArchivo(getSelected());

                if (arch != null) {
                    if (arch.equals(archivo)) {
                        getSelected().setDescripcionMesaEntrada(descripcionMesaEntrada);
                    }
                }

                super.save(null);

                archivo.setDescripcion(getSelected().getDescripcionMesaEntrada());
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.save(null);

                /*if (usuario.equals(usuarioRemitente)) {
                estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getSingleResult();
            } else {
                estadoArchivo = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getSingleResult();
            }
                 */
 /*
            arch.setEstado(estadoArchivo);
            //arch.setUsuarioRevision(usuarioRemitente);

            archivosController.setSelected(arch);
            archivosController.save(null);
                 */
                TransferenciasDocumentoAdministrativo transf = obtenerTransferenciaActual(getSelected());
                if (transf != null) {
                    transf.setUsuarioRevision(usuarioRemitente);
                    transf.setUsuarioFirma(usuarioRemitente);
                    transf.setResponsableOrigen(usuarioRemitente);
                    transf.setDepartamentoOrigen(usuarioRemitente.getDepartamento());
                    transf.setResponsableDestino(usuarioEnviar);
                    transf.setDepartamentoDestino(usuarioEnviar.getDepartamento());
                    // transf.setEstado(estadoArchivo);
                    transferenciasDocumentoAdministrativoController.setSelected(transf);
                    transferenciasDocumentoAdministrativoController.save(null);

                    saveUsuarios(tipoEnvioArchivoAdmConCopia, listaUsuariosCC, transf);
                    saveUsuarios(tipoEnvioArchivoAdmRemitente, listaUsuariosRemitente, transf);
                    saveUsuarios(tipoEnvioArchivoAdmDestinatario, listaUsuariosEnviar, transf);
                }

                obtenerDatos(accion);
                resetParentsBorradores();

                if (!isErrorPersistencia()) {
                    borrarAutoGuardado(usuario);
                }

            } finally {
                editando = false;
            }

            /*
            if (getSelected().getUltimaObservacion() != null && !"".equals(getSelected().getUltimaObservacion())) {
                ObservacionesDocumentosAdministrativos obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacion());
                obs.setDocumentoAdministrativo(getSelected());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoAdministrativo(obs);

                super.save(null);
            }
             */
 /*
            if (busquedaPorFechaAlta) {
                buscarPorFechaAl();
            } else {

                if (fechaDesde == null) {
                    fechaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaHasta == null) {
                    fechaHasta = ejbFacade.getSystemDateOnly();
                }

                buscarPorFechaPresentacion();

            }
            obtenerArchivos();
             */
        }
    }

    public void saveObs() {

        boolean guardar = true;

        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();

            if (getSelected().verifObs() && guardar) {

                getSelected().transferirObs();

                ObservacionesDocumentosAdministrativos obs = new ObservacionesDocumentosAdministrativos();

                List<EstadosDocumentoAdministrativo> estAdm = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_DOC_ADM_ACTIVO).getResultList();

                if (!estAdm.isEmpty()) {
                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fecha);
                    obs.setFechaHoraUltimoEstado(fecha);
                    obs.setObservacion(getSelected().getUltimaObservacion());
                    obs.setDocumentoAdministrativo(getSelected());
                    obs.setEstadoDocumentoAdministrativo(estAdm.get(0));

                    obsController.setSelected(obs);
                    obsController.saveNew(null);

                    getSelected().setFechaUltimaObservacion(fecha);
                    getSelected().setObservacionDocumentoAdministrativo(obs);
                    getSelected().setUsuarioUltimaObservacion(usuario);
                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usuario);

                    super.save(null);
                }
            }
        }
    }

    /*
    public void save() {

        boolean guardar = true;

        if (getSelected() != null) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

                if (getSelected().getFechaPresentacion() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Fecha Presentacion");
                    guardar = false;
                }
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);

                if (getSelected().verifObs() && guardar) {

                    getSelected().transferirObs();

                    ObservacionesDocumentosAdministrativos obs = obsController.prepareCreate(null);

                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fecha);
                    obs.setFechaHoraUltimoEstado(fecha);
                    obs.setObservacion(getSelected().getUltimaObservacion());
                    obs.setDocumentoAdministrativo(getSelected());

                    obsController.setSelected(obs);
                    obsController.saveNew(null);

                    getSelected().setFechaUltimaObservacion(fecha);
                    getSelected().setObservacionDocumentoAdministrativo(obs);
                    getSelected().setUsuarioUltimaObservacion(usuario);
                }
            } else {
                JsfUtil.addErrorMessage("Solo un funcionario de Secretaria puede editarlo");
            }
        }

        if (guardar) {
            super.save(null);
        } else {

            if (busquedaPorFechaAlta) {
                buscarPorFechaAlta();
            } else {

                if (fechaDesde == null) {
                    fechaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaHasta == null) {
                    fechaHasta = ejbFacade.getSystemDateOnly();
                }

                buscarPorFechaPresentacion();

            }
        }
    }
     */
 /*
    public void desarchivar() {
        if (selectedArchivados != null) {
            Date fecha = ejbFacade.getSystemDate();
            TransferenciasDocumentoAdministrativo cambioEstadoDocumento = new TransferenciasDocumentoAdministrativo();
            cambioEstadoDocumento.setDocumentoAdministrativo(selectedArchivados);

            cambioEstadoDocumento.setResponsableOrigen(selectedArchivados.getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(selectedArchivados.getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(selectedArchivados.getEstado());

            cambioEstadoDocumento.setResponsableDestino(selectedArchivados.getResponsable());
            cambioEstadoDocumento.setDepartamentoDestino(selectedArchivados.getDepartamento());

            cambioEstadoDocumento.setEstadoFinal(selectedArchivados.getEstadoAnterior());

            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
            transferenciasDocumentoAdministrativoController.saveNew2(null);

            EstadosProcesalesDocumentosAdministrativos estadoProc = new EstadosProcesalesDocumentosAdministrativos();

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEstadoProcesal(selectedArchivados.getEstadoAnterior().getDescripcion());
            estadoProc.setDocumentoAdministrativo(selectedArchivados);

            selectedArchivados.setFechaHoraUltimoEstado(fecha);
            selectedArchivados.setUsuarioUltimoEstado(usuario);
            EstadosDocumentoAdministrativo est = selectedArchivados.getEstadoAnterior();
            selectedArchivados.setEstadoAnterior(selectedArchivados.getEstado());
            selectedArchivados.setEstadoProcesal(selectedArchivados.getEstadoAnterior().getDescripcion());

            selectedArchivados.setEstadoProcesalDocumentoAdministrativo(estadoProc);
            selectedArchivados.setFechaHoraEstadoProcesal(fecha);
            selectedArchivados.setUsuarioEstadoProcesal(usuario);
            selectedArchivados.setEstado(est);

            setSelected(selectedArchivados);

            super.save(null);

            setSelected(null);

            selectedArchivados = null;

            buscarPorFechaAlta();
            obtenerListaSalida();
            buscarPorFechaAltaArchivados();
        }
    }
     */

 /*
    public void saveDptoPendiente() {
        saveDptoPendiente(getSelected(), listaUsuariosTransf);
    }

    public void saveDptoPendiente(DocumentosAdministrativos doc, Collection<Usuarios> lista) {
        if (doc != null) {
            Date fecha = ejbFacade.getSystemDate();
            FlujosDocumentoAdministrativo flujoDoc = null;
            EstadosDocumentoAdministrativo estado = null;

            for (Usuarios usu : lista) {
                if (usu.getId().equals(responsableDestinoId)) {
                    responsableDestino = usu;
                }
            }

            if (responsableDestino.getId() > 0) {
                if (responsableDestino.equals(doc.getResponsableAnterior())) {
                    estado = doc.getEstadoAnterior();
                } else {
                    try {
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumentoAdministrativo.findSgteEstadoSegunRol", FlujosDocumentoAdministrativo.class).setParameter("tipoDocumento", doc.getTipoDocumentoAdministrativo().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }
            } else {
                estado = responsableDestino.getEstadoDocumentoFinalAdministrativo();
            }

            CambiosEstadoDocumentoAdministrativoPendientes cambioEstadoDocumento = new CambiosEstadoDocumentoAdministrativoPendientes();
            cambioEstadoDocumento.setDocumentoAdministrativo(doc);

            cambioEstadoDocumento.setResponsableOrigen(usuario);
            cambioEstadoDocumento.setDepartamentoOrigen(usuario.getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(doc.getEstado());
            cambioEstadoDocumento.setRespuesta("IN");

            if (responsableDestino.getId() > 0) {
                cambioEstadoDocumento.setResponsableDestino(responsableDestino);
                cambioEstadoDocumento.setDepartamentoDestino(responsableDestino.getDepartamento());
            } else {
                cambioEstadoDocumento.setResponsableDestino(doc.getResponsable());
                cambioEstadoDocumento.setDepartamentoDestino(doc.getDepartamento());
            }
            cambioEstadoDocumento.setEstadoFinal(estado);

            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            cambiosEstadoDocumentoPendientesController.setSelected(cambioEstadoDocumento);
            cambiosEstadoDocumentoPendientesController.saveNew2(null);

        }
    }
     */
 /*
    public void actualizarTransferPendiente(CambiosEstadoDocumentoAdministrativoPendientes item, String respuesta) {

        if (Constantes.SI.equals(respuesta)) {

            DocumentosAdministrativos doc = null;
            try {
                doc = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativos.findById", DocumentosAdministrativos.class).setParameter("id", item.getDocumentoAdministrativo().getId()).getSingleResult();
            } catch (Exception e) {

            }

            if (doc == null) {
                JsfUtil.addErrorMessage("El documento a transferir ya no existe");
                return;
            }
            if (!doc.getDepartamento().equals(item.getDepartamentoOrigen())) {
                JsfUtil.addErrorMessage("El departamento origen ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }

            if (!doc.getEstado().equals(item.getEstadoOriginal())) {
                JsfUtil.addErrorMessage("El estado original ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosAdministrativos> col = null;

            FlujosDocumentoAdministrativo flujoDoc = null;
            EstadosDocumentoAdministrativo estado = null;
            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", item.getEstadoFinal().getCodigo()).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. El pedido de transferencia está desactualizado");
                return;
            }

            TransferenciasDocumentoAdministrativo cambioEstadoDocumento = null;
            if (usuarioBkp == null) {
                cambioEstadoDocumento = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento.setResponsableOrigen(item.getResponsableOrigen());
                cambioEstadoDocumento.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento.setResponsableDestino(item.getResponsableDestino());
                cambioEstadoDocumento.setDepartamentoDestino(item.getDepartamentoDestino());

                cambioEstadoDocumento.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento.setUsuarioUltimoEstado(item.getUsuarioAlta());

                //transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
                //transferenciasDocumentoAdministrativoController.save(null);
                doc.setResponsableAnterior(doc.getResponsable());
                doc.setResponsable(item.getResponsableDestino());
                doc.setDepartamentoAnterior(doc.getDepartamento());
                doc.setDepartamento(item.getDepartamentoDestino());

            } else {
                TransferenciasDocumentoAdministrativo cambioEstadoDocumento2 = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento2.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento2.setResponsableOrigen(item.getResponsableOrigen());
                cambioEstadoDocumento2.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento2.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento2.setResponsableDestino(usuarioBkp);
                cambioEstadoDocumento2.setDepartamentoDestino(item.getDepartamentoOrigen());

                cambioEstadoDocumento2.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento2.setFechaHoraAlta(fecha);
                cambioEstadoDocumento2.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento2.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento2.setUsuarioUltimoEstado(item.getUsuarioAlta());

                transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento2);
                transferenciasDocumentoAdministrativoController.saveNew2(null);

                cambioEstadoDocumento = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento.setResponsableOrigen(usuarioBkp);
                cambioEstadoDocumento.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento.setResponsableDestino(item.getResponsableDestino());
                cambioEstadoDocumento.setDepartamentoDestino(item.getDepartamentoDestino());

                cambioEstadoDocumento.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento.setUsuarioUltimoEstado(item.getUsuarioAlta());

                //transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento2);
                //transferenciasDocumentoAdministrativoController.save(null);
                doc.setResponsableAnterior(doc.getResponsable());
                doc.setResponsable(item.getResponsableDestino());
                doc.setDepartamentoAnterior(doc.getDepartamento());
                doc.setDepartamento(item.getDepartamentoDestino());

            }

            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(item.getUsuarioAlta());

            doc.setEstadoAnterior(doc.getEstado());
            doc.setEstado(item.getEstadoFinal());
            doc.setEstadoProcesal(item.getEstadoFinal().getDescripcion());

            //setSelected(doc);
            //super.save(null);
            EstadosProcesalesDocumentosAdministrativos estadoProc = new EstadosProcesalesDocumentosAdministrativos();

            estadoProc.setUsuarioAlta(item.getUsuarioAlta());
            estadoProc.setUsuarioUltimoEstado(item.getUsuarioAlta());
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEstadoProcesal(item.getEstadoFinal().getDescripcion());
            estadoProc.setDocumentoAdministrativo(doc);

            //                    estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
            //                    estadosProcesalesDocumentosAdministrativosController.saveNew2(null);
            doc.setEstadoProcesalDocumentoAdministrativo(estadoProc);
            doc.setFechaHoraEstadoProcesal(fecha);
            doc.setUsuarioEstadoProcesal(item.getUsuarioAlta());

            setSelected(doc);

            super.save(null);

            item.setRespuesta(respuesta);
            item.setUsuarioRespuesta(usuario);
            item.setFechaHoraRespuesta(ejbFacade.getSystemDate());
            item.setCambioEstadoDocumento(cambioEstadoDocumento);

            cambiosEstadoDocumentoPendientesController.setSelected(item);
            cambiosEstadoDocumentoPendientesController.saveNew2(null);

        }

        prepareListTransferir();

        buscarPorFechaAlta();
        obtenerListaSalida();
        buscarPorFechaAltaArchivados();
    }
     */
 /*
    public void saveDpto() {
        saveDpto(getSelected(), listaUsuariosTransf);
    }
     */
 /*

    public void saveDpto(DocumentosAdministrativos doc, Collection<Usuarios> lista) {

        if (doc != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosAdministrativos> col = null;
            //Usuarios resp = getSelected().getResponsable();
            FlujosDocumentoAdministrativo flujoDoc = null;
            EstadosDocumentoAdministrativo estado = null;

            for (Usuarios usu : lista) {
                if (usu.getId().equals(responsableDestinoId)) {
                    responsableDestino = usu;
                }
            }

            if (responsableDestino.getId() > 0) {
                if (responsableDestino.equals(doc.getResponsableAnterior())) {
                    estado = doc.getEstadoAnterior();
                } else {
                    try {
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumentoAdministrativo.findSgteEstadoSegunRol", FlujosDocumentoAdministrativo.class).setParameter("tipoDocumento", doc.getTipoDocumentoAdministrativo().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumentoAdministrativo.findByCodigo", EstadosDocumentoAdministrativo.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }

            } else {
                estado = responsableDestino.getEstadoDocumentoFinalAdministrativo();
            }

            if (usuarioBkp == null) {
                TransferenciasDocumentoAdministrativo cambioEstadoDocumento = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento.setResponsableOrigen(doc.getResponsable());
                cambioEstadoDocumento.setDepartamentoOrigen(doc.getDepartamento());
                cambioEstadoDocumento.setEstadoOriginal(doc.getEstado());

                if (responsableDestino.getId() > 0) {
                    cambioEstadoDocumento.setResponsableDestino(responsableDestino);
                    cambioEstadoDocumento.setDepartamentoDestino(responsableDestino.getDepartamento());
                } else {
                    cambioEstadoDocumento.setResponsableDestino(doc.getResponsable());
                    cambioEstadoDocumento.setDepartamentoDestino(doc.getDepartamento());
                }
                cambioEstadoDocumento.setEstadoFinal(estado);

                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
                transferenciasDocumentoAdministrativoController.saveNew2(null);

                doc.setFechaHoraUltimoEstado(fecha);
                doc.setUsuarioUltimoEstado(usuario);
                if (responsableDestino.getId() > 0) {
                    doc.setResponsableAnterior(doc.getResponsable());
                    doc.setDepartamentoAnterior(doc.getDepartamento());
                    doc.setResponsable(responsableDestino);
                    doc.setDepartamento(responsableDestino.getDepartamento());
                }
                doc.setEstadoAnterior(doc.getEstado());
                doc.setEstado(estado);
                doc.setEstadoProcesal(estado.getDescripcion());
            } else {
                TransferenciasDocumentoAdministrativo cambioEstadoDocumento = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento.setResponsableOrigen(doc.getResponsable());
                cambioEstadoDocumento.setDepartamentoOrigen(doc.getDepartamento());
                cambioEstadoDocumento.setEstadoOriginal(doc.getEstado());

                cambioEstadoDocumento.setResponsableDestino(usuarioBkp);
                cambioEstadoDocumento.setDepartamentoDestino(usuario.getDepartamento());

                cambioEstadoDocumento.setEstadoFinal(doc.getEstado());

                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento);
                transferenciasDocumentoAdministrativoController.saveNew2(null);

                TransferenciasDocumentoAdministrativo cambioEstadoDocumento2 = new TransferenciasDocumentoAdministrativo();
                cambioEstadoDocumento2.setDocumentoAdministrativo(doc);

                cambioEstadoDocumento2.setResponsableOrigen(usuarioBkp);
                cambioEstadoDocumento2.setDepartamentoOrigen(usuario.getDepartamento());
                cambioEstadoDocumento2.setEstadoOriginal(doc.getEstado());

                if (responsableDestino.getId() > 0) {
                    cambioEstadoDocumento2.setResponsableDestino(responsableDestino);
                    cambioEstadoDocumento2.setDepartamentoDestino(responsableDestino.getDepartamento());
                } else {
                    cambioEstadoDocumento2.setResponsableDestino(usuario);
                    cambioEstadoDocumento2.setDepartamentoDestino(usuario.getDepartamento());
                }
                cambioEstadoDocumento2.setEstadoFinal(estado);

                cambioEstadoDocumento2.setFechaHoraAlta(fecha);
                cambioEstadoDocumento2.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento2.setUsuarioAlta(usuario);
                cambioEstadoDocumento2.setUsuarioUltimoEstado(usuario);

                transferenciasDocumentoAdministrativoController.setSelected(cambioEstadoDocumento2);
                transferenciasDocumentoAdministrativoController.saveNew2(null);

                doc.setFechaHoraUltimoEstado(fecha);
                doc.setUsuarioUltimoEstado(usuario);
                if (responsableDestino.getId() > 0) {
                    doc.setResponsableAnterior(doc.getResponsable());
                    doc.setDepartamentoAnterior(doc.getDepartamento());
                    doc.setResponsable(responsableDestino);
                    doc.setDepartamento(responsableDestino.getDepartamento());
                }
                doc.setEstadoAnterior(doc.getEstado());
                doc.setEstado(estado);
                doc.setEstadoProcesal(estado.getDescripcion());
            }

            //setSelected(doc);
            //super.save(null);
            EstadosProcesalesDocumentosAdministrativos estadoProc = new EstadosProcesalesDocumentosAdministrativos();

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEstadoProcesal(estado.getDescripcion());
            estadoProc.setDocumentoAdministrativo(doc);

            //                    estadosProcesalesDocumentosAdministrativosController.setSelected(estadoProc);
            //                    estadosProcesalesDocumentosAdministrativosController.saveNew2(null);
            doc.setEstadoProcesalDocumentoAdministrativo(estadoProc);
            doc.setFechaHoraEstadoProcesal(fecha);
            doc.setUsuarioEstadoProcesal(usuario);

            setSelected(doc);

            super.save(null);

            setSelected(null);

            buscarPorFechaAlta();
            obtenerListaSalida();
            buscarPorFechaAltaArchivados();

        }
    }
     */
}
