package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosJudiciales;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.CambiosEstadoDocumento;
import py.com.startic.gestion.models.CambiosEstadoDocumentoPendientes;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosEscaneados;
import py.com.startic.gestion.models.EntradasDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.SubcategoriasDocumentosJudiciales;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "verDocumentosAdministrativosMesaController")
@ViewScoped
public class VerDocumentosAdministrativosMesaController extends AbstractController<DocumentosJudiciales> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private ObservacionesDocumentosJudicialesController obsController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;
    @Inject
    private EntradasDocumentosJudicialesController entradasDocumentosJudicialesController;
    @Inject
    private DocumentosEscaneadosController documentosEscaneadosController;
    @Inject
    private CambiosEstadoDocumentoController cambiosEstadoDocumentoController;
    @Inject
    private CambiosEstadoDocumentoPendientesController cambiosEstadoDocumentoPendientesController;
    @Inject
    private ArchivosController archivosController;
    @Inject
    private EstadosDocumentoController estadosDocumentoController;
    private final FiltroURL filtroURL = new FiltroURL();
    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private String nuevaCausa;
    private String nombreJuez;
    private String nombreEstado;
    private String ultimaObservacion;
    private CanalesEntradaDocumentoJudicial canal;
    //private TiposDocumentosJudiciales tipoDoc;
    //private TiposDocumentosJudiciales tipoDoc2;
    private List<TiposDocumentosJudiciales> tiposDoc;
    private String tiposDocString;
    private Usuarios usuario;
    private Usuarios usuarioBkp;
    // private Departamentos departamento;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaInicio;
    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private FlujosDocumento flujoDoc;
    private String sgteEstado;
    private Collection<Usuarios> listaUsuariosTransf;
    private Collection<Usuarios> listaUsuariosTransfArchivados;
    private boolean hayDocumentosAtencion;
    private Usuarios responsableDestino;
    private Integer responsableDestinoId;
    private SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
    private List<CambiosEstadoDocumentoPendientes> listaTransfer;
    private CambiosEstadoDocumentoPendientes selectedTransfer;
    private Integer plazo;
    private List<CambiosEstadoDocumento> listaSalida;
    private List<DocumentosJudiciales> listaArchivados;
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Archivos> listaArchivos;
    List<Archivos> listaArchivosSalida;
    CambiosEstadoDocumento selectedSalida;
    List<Archivos> listaArchivosArchivados;
    DocumentosJudiciales selectedArchivados;
    private String descripcionArchivo;
    private ParametrosSistema par;
    private Archivos docImprimir;
    private String content;
    private String nombre;
    private String url;
    private String endpoint;
    private HttpSession session;
    private SimpleDateFormat formatAno = new SimpleDateFormat("YY");
    private String descripcionMesaEntrada;
    private SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial;
    private List<SubcategoriasDocumentosJudiciales> listaSubcategoriaDocumentoJudicial;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private String folios;
    private List<ObservacionesDocumentosJudiciales> listaObservaciones;
    private List<ObservacionesDocumentosJudiciales> listaObservacionesSalida;
    private List<ObservacionesDocumentosJudiciales> listaObservacionesArchivados;
    private String titulo;
    private Integer activeTab;
    private String eleccionFiltro;
    private String asuntoFiltro;
    private String textoFiltro;
    private String eleccionSalidaFiltro;
    private String asuntoSalidaFiltro;
    private String textoSalidaFiltro;
    private String eleccionArchivadosFiltro;
    private String asuntoArchivadosFiltro;
    private String textoArchivadosFiltro;
    private Date fechaAltaSalidaDesde;
    private Date fechaAltaSalidaHasta;
    private Date fechaAltaArchivadosDesde;
    private Date fechaAltaArchivadosHasta;

    private boolean busquedaPorFechaAlta;

    private UploadedFile file;

    public String getAsuntoSalidaFiltro() {
        return asuntoSalidaFiltro;
    }

    public void setAsuntoSalidaFiltro(String asuntoSalidaFiltro) {
        this.asuntoSalidaFiltro = asuntoSalidaFiltro;
    }

    public Date getFechaAltaArchivadosDesde() {
        return fechaAltaArchivadosDesde;
    }

    public void setFechaAltaArchivadosDesde(Date fechaAltaArchivadosDesde) {
        this.fechaAltaArchivadosDesde = fechaAltaArchivadosDesde;
    }

    public Date getFechaAltaArchivadosHasta() {
        return fechaAltaArchivadosHasta;
    }

    public void setFechaAltaArchivadosHasta(Date fechaAltaArchivadosHasta) {
        this.fechaAltaArchivadosHasta = fechaAltaArchivadosHasta;
    }

    public Date getFechaAltaSalidaDesde() {
        return fechaAltaSalidaDesde;
    }

    public void setFechaAltaSalidaDesde(Date fechaAltaSalidaDesde) {
        this.fechaAltaSalidaDesde = fechaAltaSalidaDesde;
    }

    public Date getFechaAltaSalidaHasta() {
        return fechaAltaSalidaHasta;
    }

    public void setFechaAltaSalidaHasta(Date fechaAltaSalidaHasta) {
        this.fechaAltaSalidaHasta = fechaAltaSalidaHasta;
    }

    public String getEleccionSalidaFiltro() {
        return eleccionSalidaFiltro;
    }

    public void setEleccionSalidaFiltro(String eleccionSalidaFiltro) {
        this.eleccionSalidaFiltro = eleccionSalidaFiltro;
    }

    public String getTextoSalidaFiltro() {
        return textoSalidaFiltro;
    }

    public void setTextoSalidaFiltro(String textoSalidaFiltro) {
        this.textoSalidaFiltro = textoSalidaFiltro;
    }

    public String getEleccionArchivadosFiltro() {
        return eleccionArchivadosFiltro;
    }

    public void setEleccionArchivadosFiltro(String eleccionArchivadosFiltro) {
        this.eleccionArchivadosFiltro = eleccionArchivadosFiltro;
    }

    public String getAsuntoArchivadosFiltro() {
        return asuntoArchivadosFiltro;
    }

    public void setAsuntoArchivadosFiltro(String asuntoArchivadosFiltro) {
        this.asuntoArchivadosFiltro = asuntoArchivadosFiltro;
    }

    public String getTextoArchivadosFiltro() {
        return textoArchivadosFiltro;
    }

    public void setTextoArchivadosFiltro(String textoArchivadosFiltro) {
        this.textoArchivadosFiltro = textoArchivadosFiltro;
    }

    public String getEleccionFiltro() {
        return eleccionFiltro;
    }

    public void setEleccionFiltro(String eleccionFiltro) {
        this.eleccionFiltro = eleccionFiltro;
    }

    public String getAsuntoFiltro() {
        return asuntoFiltro;
    }

    public void setAsuntoFiltro(String asuntoFiltro) {
        this.asuntoFiltro = asuntoFiltro;
    }

    public String getTextoFiltro() {
        return textoFiltro;
    }

    public void setTextoFiltro(String textoFiltro) {
        this.textoFiltro = textoFiltro;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public List<ObservacionesDocumentosJudiciales> getListaObservacionesSalida() {
        return listaObservacionesSalida;
    }

    public void setListaObservacionesSalida(List<ObservacionesDocumentosJudiciales> listaObservacionesSalida) {
        this.listaObservacionesSalida = listaObservacionesSalida;
    }

    public List<ObservacionesDocumentosJudiciales> getListaObservacionesArchivados() {
        return listaObservacionesArchivados;
    }

    public void setListaObservacionesArchivados(List<ObservacionesDocumentosJudiciales> listaObservacionesArchivados) {
        this.listaObservacionesArchivados = listaObservacionesArchivados;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<ObservacionesDocumentosJudiciales> getListaObservaciones() {
        return listaObservaciones;
    }

    public void setListaObservaciones(List<ObservacionesDocumentosJudiciales> listaObservaciones) {
        this.listaObservaciones = listaObservaciones;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public SubcategoriasDocumentosJudiciales getSubcategoriaDocumentoJudicial() {
        return subcategoriaDocumentoJudicial;
    }

    public void setSubcategoriaDocumentoJudicial(SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial) {
        this.subcategoriaDocumentoJudicial = subcategoriaDocumentoJudicial;
    }

    public List<SubcategoriasDocumentosJudiciales> getListaSubcategoriaDocumentoJudicial() {
        return listaSubcategoriaDocumentoJudicial;
    }

    public void setListaSubcategoriaDocumentoJudicial(List<SubcategoriasDocumentosJudiciales> listaSubcategoriaDocumentoJudicial) {
        this.listaSubcategoriaDocumentoJudicial = listaSubcategoriaDocumentoJudicial;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    public List<DocumentosJudiciales> getListaArchivados() {
        return listaArchivados;
    }

    public void setListaArchivados(List<DocumentosJudiciales> listaArchivados) {
        this.listaArchivados = listaArchivados;
    }

    public List<Archivos> getListaArchivosArchivados() {
        return listaArchivosArchivados;
    }

    public void setListaArchivosArchivados(List<Archivos> listaArchivosArchivados) {
        this.listaArchivosArchivados = listaArchivosArchivados;
    }

    public DocumentosJudiciales getSelectedArchivados() {
        return selectedArchivados;
    }

    public void setSelectedArchivados(DocumentosJudiciales selectedArchivados) {
        this.selectedArchivados = selectedArchivados;
    }

    public CambiosEstadoDocumento getSelectedSalida() {
        return selectedSalida;
    }

    public void setSelectedSalida(CambiosEstadoDocumento selectedSalida) {
        this.selectedSalida = selectedSalida;
    }

    public List<Archivos> getListaArchivosSalida() {
        return listaArchivosSalida;
    }

    public void setListaArchivosSalida(List<Archivos> listaArchivosSalida) {
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

    public EntradasDocumentosJudiciales getEntradaDocumentoJudicial() {
        return entradaDocumentoJudicial;
    }

    public void setEntradaDocumentoJudicial(EntradasDocumentosJudiciales entradaDocumentoJudicial) {
        this.entradaDocumentoJudicial = entradaDocumentoJudicial;
    }

    public List<CambiosEstadoDocumentoPendientes> getListaTransfer() {
        return listaTransfer;
    }

    public void setListaTransfer(List<CambiosEstadoDocumentoPendientes> listaTransfer) {
        this.listaTransfer = listaTransfer;
    }

    public CambiosEstadoDocumentoPendientes getSelectedTransfer() {
        return selectedTransfer;
    }

    public void setSelectedTransfer(CambiosEstadoDocumentoPendientes selectedTransfer) {
        this.selectedTransfer = selectedTransfer;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public List<CambiosEstadoDocumento> getListaSalida() {
        return listaSalida;
    }

    public void setListaSalida(List<CambiosEstadoDocumento> listaSalida) {
        this.listaSalida = listaSalida;
    }

    public List<Archivos> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(List<Archivos> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public VerDocumentosAdministrativosMesaController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        /*
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(5); // Secretaria
         */

 /*
        tipoDoc2 = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc2.setCodigo("JU");
         */
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        canal = new CanalesEntradaDocumentoJudicial();
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_ME);
        TiposDocumentosJudiciales tipoDoc = new TiposDocumentosJudiciales();
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_AD);
        TiposDocumentosJudiciales tipoDoc2 = new TiposDocumentosJudiciales();
        tipoDoc2.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);

        tiposDoc = new ArrayList<>();
        tiposDoc.add(tipoDoc);
        tiposDoc.add(tipoDoc2);

        tiposDocString = " ('" + Constantes.TIPO_DOCUMENTO_JUDICIAL_AD + "', '" + Constantes.TIPO_DOCUMENTO_JUDICIAL_JU + "') ";
        titulo = "Documentos Administrativos por Mesa de Entrada";

        eleccionFiltro = "1";
        eleccionSalidaFiltro = "1";
        eleccionArchivadosFiltro = "1";

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
        
        resetearFechas();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        try {
            tipoDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosJudiciales.findByCodigo", TiposDocumentosJudiciales.class).setParameter("codigo", Constantes.TIPO_DOCUMENTO_JUDICIAL_AD).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error de configuracion. No se puede iniciar pantalla");
            return;
        }

        activeTab = 0;

        obtenerDatos();

    }
    
    private void resetearFechas(){
        
        if (filtroURL.verifPermiso("verTodosDocsAdm")) {
            //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
            Calendar myCal = Calendar.getInstance();
            myCal.set(Calendar.YEAR, 2018);
            myCal.set(Calendar.MONTH, 0);
            myCal.set(Calendar.DAY_OF_MONTH, 1);
            fechaInicio = myCal.getTime();

            fechaAltaHasta = ejbFacade.getSystemDateOnly();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, -31);
            fechaAltaDesde = cal.getTime();

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(fechaAltaHasta);
            cal2.add(Calendar.DATE, 1);
            fechaAltaHasta = cal2.getTime();
        } else {
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
        }

    }

    public void obtenerDatos() {
        if (null != activeTab) {

            setItems(null);
            listaSalida = null;
            listaArchivados = null;

            int tabSalida = filtroURL.verifPermiso("verTodosDocsAdm") ? 3 : 1;
            int tabArchivado = filtroURL.verifPermiso("verTodosDocsAdm") ? 1 : 2;

            if (activeTab == 0) {
                buscarPorFechaAlta();
            } else if (activeTab == tabSalida) {
                obtenerListaSalida();
            } else if (activeTab == tabArchivado) {
                buscarPorFechaAltaArchivados();
            }
        }

    }


    /*
    @Override
    public DocumentosJudiciales prepareCreate(ActionEvent event) {
        DocumentosJudiciales doc = super.prepareCreate(event);

        entradaDocumentoJudicial = entradasDocumentosJudicialesController.prepareCreate(event);
        entradaDocumentoJudicial.setRecibidoPor(usuario);

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;

        return doc;
    }
     */
    public String obtenerSgteNroMesaEntradaAdministrativa() {

        Date fecha = ejbFacade.getSystemDate();

        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                // "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);
                //"select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);
                "select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial in ('" + tipoDocumentoJudicial.getCodigo() + "') AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return Utils.padLeft(String.valueOf(cod.getCodigo() + 1) + "-" + formatAno.format(fecha), "0", 6);
    }

    public void cambiarSubcategorias() {
        listaSubcategoriaDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicialEstado", SubcategoriasDocumentosJudiciales.class).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    @Override
    public DocumentosJudiciales prepareCreate(ActionEvent event) {
        DocumentosJudiciales doc = super.prepareCreate(event);

        entradaDocumentoJudicial = new EntradasDocumentosJudiciales();
        entradaDocumentoJudicial.setRecibidoPor(usuario);
        // entradaDocumentoJudicial.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

        folios = "";

        /*
        detalles = null;
        detalleSelected = null;
        folios = "";
        nuevoRecurrente = "";
        descripcionMesaEntrada = "";
        subcategoriaDocumentoJudicial = null;

        detalles = null;
         */
        descripcionMesaEntrada = "";
        subcategoriaDocumentoJudicial = null;

        cambiarSubcategorias();

        return doc;
    }

    public void prepareEdit() {

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;
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

    public void prepareListTransferir(DocumentosJudiciales doc) {
        listaTransfer = ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoPendientes.findByDocumentoJudicialDepartamentoOrigen", CambiosEstadoDocumentoPendientes.class).setParameter("documentoJudicial", doc).setParameter("departamentoOrigen", usuario.getDepartamento()).getResultList();
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
        obtenerArchivos();
        obtenerObservaciones();
    }

    public void resetParentsSalida() {
        obtenerArchivosSalida();
        obtenerObservacionesSalida();
    }

    public void resetParentsArchivados() {
        obtenerArchivosArchivados();
        obtenerObservacionesArchivados();
    }

    public void prepareVerDoc(Archivos doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

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

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    private void obtenerObservaciones() {
        if (getSelected() != null) {
            listaObservaciones = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
        }
    }

    private void obtenerObservacionesArchivados() {
        if (selectedArchivados != null) {
            listaObservacionesArchivados = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", selectedArchivados).getResultList();
        }
    }

    private void obtenerObservacionesSalida() {
        if (selectedSalida != null) {
            listaObservacionesSalida = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", selectedSalida.getDocumentoJudicial()).getResultList();
        }
    }

    private void obtenerArchivos() {
        if (getSelected() != null) {
            listaArchivos = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicial", Archivos.class).setParameter("documentoJudicial", getSelected()).getResultList();
        }
    }

    private void obtenerArchivosArchivados() {
        if (selectedArchivados != null) {
            listaArchivosArchivados = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicial", Archivos.class).setParameter("documentoJudicial", selectedArchivados).getResultList();
        }
    }

    private void obtenerArchivosSalida() {
        if (selectedSalida != null) {
            listaArchivosSalida = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicial", Archivos.class).setParameter("documentoJudicial", selectedSalida.getDocumentoJudicial()).getResultList();
        }
    }

    public void prepareTransferir() {
        listaUsuariosTransf = prepareTransferir(getSelected());
    }

    public void prepareTransferirArchivados() {
        listaUsuariosTransfArchivados = prepareTransferir(selectedArchivados);
    }

    public Collection<Usuarios> prepareTransferir(DocumentosJudiciales doc) {
        Collection<Usuarios> lista = null;
        if (doc != null) {

            lista = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_ENCARGADO).getResultList();

            // if (doc.getResponsableAnterior() != null && !Constantes.TIPO_ESTADO_DOCUMENTO_AR.equals(doc.getEstado().getTipo())) {
            if (doc.getResponsableAnterior() != null && !Constantes.ESTADO_DOCUMENTO_ARCHIVADO_SECRETARIA.equals(doc.getEstado().getCodigo())) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", doc.getResponsableAnterior().getId()).getResultList();
                if (!respAnt.isEmpty()) {
                    boolean encontro = false;
                    for (Usuarios trans : lista) {
                        if (trans.equals(respAnt.get(0))) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        lista.add(respAnt.get(0));
                    }
                }
            }

            List<FlujosDocumento> flujos = null;
            try {
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).getResultList();

                int contador = -1;
                for (FlujosDocumento flujo : flujos) {

                    EstadosDocumento est = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujo.getEstadoDocumentoFinal()).getSingleResult();
                    /*
                    Usuarios usu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", 2).getSingleResult();
                    usu.setId(contador);
                    usu.setNombresApellidos(est.getDescripcion());
                    usu.setEstadoDocumentoFinal(est);
                     */
                    Usuarios usu = new Usuarios(contador, est.getDescripcion(), est);
                    lista.add(usu);
                    contador--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lista = new ArrayList<>();
        }

        responsableDestino = null;
        responsableDestinoId = null;

        return lista;
    }

    /*
    public void prepareTransferirArchivados(DocumentosJudiciales doc) {
        if (doc != null) {
            
            listaUsuariosTransfArchivados = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_ENCARGADO).getResultList();

            // if (doc.getResponsableAnterior() != null && !Constantes.TIPO_ESTADO_DOCUMENTO_AR.equals(doc.getEstado().getTipo())) {
            if (doc.getResponsableAnterior() != null) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", doc.getResponsableAnterior().getId()).getResultList();
                if (!respAnt.isEmpty()) {
                    boolean encontro = false;
                    for (Usuarios trans : listaUsuariosTransfArchivados) {
                        if (trans.equals(respAnt.get(0))) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        listaUsuariosTransfArchivados.add(respAnt.get(0));
                    }
                }
            }
            

            List<FlujosDocumento> flujos = null;
            try {
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).getResultList();

                int contador = -1;
                for (FlujosDocumento flujo : flujos) {

                    EstadosDocumento est = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujo.getEstadoDocumentoFinal()).getSingleResult();
                
                    Usuarios usu = new Usuarios(contador, est.getDescripcion(), est);
                    listaUsuariosTransfArchivados.add(usu);
                    contador--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listaUsuariosTransfArchivados = new ArrayList<>();
        }

        responsableDestino = null;
    }
     */
    public void guardarPlazo() {
        super.save(null);
    }

    private CambiosEstadoDocumento obtenerUltimoCambioEstadoDocumentoTipoFlujo(Integer doc) {
        List<CambiosEstadoDocumento> cambios = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from cambios_estado_documento as a, estados_documento as e where a.estado_final = e.codigo and a.documento_judicial = ?1 and e.tipo = 'FL' and a.fecha_hora_alta in (select max(b.fecha_hora_alta) from cambios_estado_documento as b, estados_documento as f where b.estado_final = f.codigo and b.documento_judicial = ?2 and f.tipo = 'FL')", CambiosEstadoDocumento.class).setParameter(1, doc).setParameter(2, doc).getResultList();

        if (cambios.isEmpty()) {
            return null;
        }

        return cambios.get(0);
    }

    public Departamentos obtenerDepartamentoOrigen(DocumentosJudiciales doc) {

        CambiosEstadoDocumento cambio = obtenerUltimoCambioEstadoDocumentoTipoFlujo(doc.getId());

        return cambio == null ? null : cambio.getDepartamentoOrigen();
    }

    public Usuarios obtenerUsuarioOrigen(DocumentosJudiciales doc) {

        CambiosEstadoDocumento cambio = obtenerUltimoCambioEstadoDocumentoTipoFlujo(doc.getId());

        return cambio == null ? null : cambio.getResponsableOrigen();
    }

    public boolean verifDocumentosAtencion() {
        hayDocumentosAtencion = false;
        Collection<DocumentosJudiciales> art = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByAtencion", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("responsable", usuario).getResultList();

        if (art != null) {
            if (art.size() > 0) {
                hayDocumentosAtencion = true;
            }
        }
        return hayDocumentosAtencion;
    }

    public boolean renderedNuevoDocAdm() {
        return filtroURL.verifPermiso("/pages/verDocumentosAdministrativosMesa/index.xhtml", "MEMO");
    }

    public boolean renderedListTransferir() {
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_TRANSFERENCIAS_PENDIENTES);
    }

    public boolean deshabilitarFechaDesdeFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("1"));
    }

    public boolean deshabilitarFechaHastaFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("1"));
    }

    public boolean deshabilitarAsuntoFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("2"));
    }

    public boolean deshabilitarTextoFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("3"));
    }

    public boolean deshabilitarFechaSalidaDesdeFiltro() {
        return !(eleccionSalidaFiltro == null ? false : eleccionSalidaFiltro.equals("1"));
    }

    public boolean deshabilitarFechaSalidaHastaFiltro() {
        return !(eleccionSalidaFiltro == null ? false : eleccionSalidaFiltro.equals("1"));
    }

    public boolean deshabilitarAsuntoSalidaFiltro() {
        return !(eleccionSalidaFiltro == null ? false : eleccionSalidaFiltro.equals("2"));
    }

    public boolean deshabilitarTextoSalidaFiltro() {
        return !(eleccionSalidaFiltro == null ? false : eleccionSalidaFiltro.equals("3"));
    }
    
    public boolean deshabilitarFechaArchivadosDesdeFiltro() {
        return !(eleccionArchivadosFiltro == null ? false : eleccionArchivadosFiltro.equals("1"));
    }

    public boolean deshabilitarFechaArchivadosHastaFiltro() {
        return !(eleccionArchivadosFiltro == null ? false : eleccionArchivadosFiltro.equals("1"));
    }

    public boolean deshabilitarAsuntoArchivadosFiltro() {
        return !(eleccionArchivadosFiltro == null ? false : eleccionArchivadosFiltro.equals("2"));
    }

    public boolean deshabilitarTextoArchivadosFiltro() {
        return !(eleccionArchivadosFiltro == null ? false : eleccionArchivadosFiltro.equals("3"));
    }
    
    public boolean deshabilitarListTransferir() {
        return !filtroURL.verifPermiso(Constantes.PERMISO_VER_TRANSFERENCIAS_PENDIENTES);
    }

    public boolean deshabilitarBotonTransferirPend(DocumentosJudiciales doc) {
        if (doc != null) {
            // if (filtroURL.verifPermiso(Constantes.PERMISO_VER_TRANSFERENCIAS_PENDIENTES)) {
            List<CambiosEstadoDocumentoPendientes> lista = ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoPendientes.findByDocumentoJudicialDepartamento", CambiosEstadoDocumentoPendientes.class).setParameter("documentoJudicial", doc).setParameter("departamentoOrigen", usuario.getDepartamento()).getResultList();
            return lista.isEmpty();
            // }
        }
        return true;
    }

    public boolean desabilitarBotonAlzarArchivo() {
        return false;
    }

    public boolean deshabilitarEditPlazo() {
        return !filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_PLAZOS);
    }

    public boolean deshabilitarActualizarTransfer(CambiosEstadoDocumentoPendientes cambio) {
        if (cambio != null) {
            return (cambio.getDocumentoJudicial() == null) || deshabilitarListTransferir() || !"IN".equals(cambio.getRespuesta());
        }

        return true;
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            if (getSelected().getResponsable().getDepartamento().equals(usuario.getDepartamento())) {

                try {
                    flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                    return false;
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }

        return true;
    }

    public String rowClass(DocumentosJudiciales item) {
        return (item.getResponsable().equals(usuario)) ? ((item.getEstado().getTipo().equals("RE")) ? "white" : "green") : "";
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
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            Archivos archivo = new Archivos();

            archivo.setDocumentoJudicial(getSelected());
            archivo.setDescripcion(descripcionArchivo);
            archivo.setRuta(nombreArchivo);
            archivo.setFechaHoraAlta(fecha);
            archivo.setFechaHoraUltimoEstado(fecha);
            archivo.setUsuarioAlta(usuario);
            archivo.setUsuarioUltimoEstado(usuario);
            archivo.setDepartamento(usuario.getDepartamento());

            archivosController.setSelected(archivo);
            archivosController.saveNew(null);
            obtenerArchivos();
        }

    }

    public boolean deshabilitarBandejaSalida() {
        return filtroURL.verifPermiso("verTodosDocsAdm");
    }

    public void obtenerListaSalida() {
        listaSalida = new ArrayList<>();

        if (!filtroURL.verifPermiso("verTodosDocsAdm")) {
            // String comando = "select a.* from cambios_estado_documento as a, estados_documento as b where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and  a.estado_original = b.codigo and a.departamento_origen = " + usuario.getDepartamento().getId() + " and b.tipo = 'RE' and a.fecha_hora_alta in (select max(c.fecha_hora_alta) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_original = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and d.tipo = 'RE' and a.documento_judicial = c.documento_judicial)";
            String comando = "select a.* from cambios_estado_documento as a, estados_documento as b, documentos_judiciales as d where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and  a.estado_final = b.codigo and d.id = a.documento_judicial and d.tipo_documento_judicial in " + tiposDocString + " and a.departamento_origen = " + usuario.getDepartamento().getId() + " and a.id in (select max(c.id) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_final = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and a.documento_judicial = c.documento_judicial) order by a.fecha_hora_alta desc";
            List<CambiosEstadoDocumento> cambios = ejbFacade.getEntityManager().createNativeQuery(comando, CambiosEstadoDocumento.class).getResultList();
            for (CambiosEstadoDocumento cambio : cambios) {

                //comando = "select a.* from cambios_estado_documento as a, estados_documento as e where a.estado_final = e.codigo and a.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and e.tipo = 'FL' and a.fecha_hora_alta in (select max(b.fecha_hora_alta) from cambios_estado_documento as b, estados_documento as f where b.estado_final = f.codigo and a.fecha_hora_alta > '" + format3.format(cambio.getFechaHoraAlta()) + "'  and b.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and f.tipo = 'FL')";
                comando = "select a.* from cambios_estado_documento as a, estados_documento as e where a.estado_final = e.codigo and a.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and a.fecha_hora_alta >= '" + format3.format(cambio.getFechaHoraAlta()) + "' ORDER BY a.fecha_hora_alta DESC";
                List<CambiosEstadoDocumento> lista = this.ejbFacade.getEntityManager().createNativeQuery(comando, CambiosEstadoDocumento.class).getResultList();
                for (CambiosEstadoDocumento cam : lista) {
                    if (!cam.getDepartamentoOrigen().equals(cam.getDepartamentoDestino())) {
                        listaSalida.add(cam);
                        break;
                    }
                }

            }
        }
    }

    public void obtenerListaSalidaFiltro() {
        listaSalida = new ArrayList<>();

        if (!filtroURL.verifPermiso("verTodosDocsAdm")) {
            // String comando = "select a.* from cambios_estado_documento as a, estados_documento as b where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and  a.estado_original = b.codigo and a.departamento_origen = " + usuario.getDepartamento().getId() + " and b.tipo = 'RE' and a.fecha_hora_alta in (select max(c.fecha_hora_alta) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_original = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and d.tipo = 'RE' and a.documento_judicial = c.documento_judicial)";
            String comando = null;

            if (eleccionSalidaFiltro != null) {
                if (eleccionSalidaFiltro.equals("1")) {
                    comando = "select a.* from cambios_estado_documento as a, estados_documento as b, documentos_judiciales as d where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and a.fecha_hora_alta >= '" + format2.format(fechaAltaSalidaDesde) + "' and a.fecha_hora_alta <= '" + format2.format(fechaAltaSalidaHasta) + "' and  a.estado_final = b.codigo and d.id = a.documento_judicial and d.tipo_documento_judicial in " + tiposDocString + " and a.departamento_origen = " + usuario.getDepartamento().getId() + " and a.id in (select max(c.id) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_final = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and a.documento_judicial = c.documento_judicial) order by a.fecha_hora_alta desc";
                } else if (eleccionSalidaFiltro.equals("2")) {
                    comando = "select a.* from cambios_estado_documento as a, estados_documento as b, documentos_judiciales as d where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and d.descripcion_mesa_entrada like '%" + asuntoSalidaFiltro + "%' and  a.estado_final = b.codigo and d.id = a.documento_judicial and d.tipo_documento_judicial in " + tiposDocString + " and a.departamento_origen = " + usuario.getDepartamento().getId() + " and a.id in (select max(c.id) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_final = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and a.documento_judicial = c.documento_judicial) order by a.fecha_hora_alta desc";
                } else if (eleccionSalidaFiltro.equals("3")) {
                    comando = "select a.* from cambios_estado_documento as a, estados_documento as b, documentos_judiciales as d, entradas_documentos_judiciales as e where a.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and e.nro_mesa_entrada like '%" + textoSalidaFiltro + "%' and  a.estado_final = b.codigo and d.id = a.documento_judicial and d.entrada_documento = e.id and d.tipo_documento_judicial in " + tiposDocString + " and a.departamento_origen = " + usuario.getDepartamento().getId() + " and a.id in (select max(c.id) from cambios_estado_documento as c, estados_documento as d where c.fecha_hora_alta > '" + format2.format(fechaInicio) + "' and c.estado_final = d.codigo and c.departamento_origen = " + usuario.getDepartamento().getId() + " and a.documento_judicial = c.documento_judicial) order by a.fecha_hora_alta desc";
                }

                if (comando != null) {
                    List<CambiosEstadoDocumento> cambios = ejbFacade.getEntityManager().createNativeQuery(comando, CambiosEstadoDocumento.class).getResultList();
                    for (CambiosEstadoDocumento cambio : cambios) {

                        //comando = "select a.* from cambios_estado_documento as a, estados_documento as e where a.estado_final = e.codigo and a.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and e.tipo = 'FL' and a.fecha_hora_alta in (select max(b.fecha_hora_alta) from cambios_estado_documento as b, estados_documento as f where b.estado_final = f.codigo and a.fecha_hora_alta > '" + format3.format(cambio.getFechaHoraAlta()) + "'  and b.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and f.tipo = 'FL')";
                        comando = "select a.* from cambios_estado_documento as a, estados_documento as e where a.estado_final = e.codigo and a.documento_judicial = " + cambio.getDocumentoJudicial().getId() + " and a.fecha_hora_alta >= '" + format3.format(cambio.getFechaHoraAlta()) + "' ORDER BY a.fecha_hora_alta DESC";
                        List<CambiosEstadoDocumento> lista = this.ejbFacade.getEntityManager().createNativeQuery(comando, CambiosEstadoDocumento.class).getResultList();
                        for (CambiosEstadoDocumento cam : lista) {
                            if (!cam.getDepartamentoOrigen().equals(cam.getDepartamentoDestino())) {
                                listaSalida.add(cam);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void cambiarEstado() {
        if (getSelected() != null) {
            try {
                flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede cambiar estado");
                return;
            }
            /*
            getSelected().setEstado(flujoDoc.getEstadoDocumentoFinal());
            getSelected().setEstadoProcesal(flujoDoc.getEstadoDocumentoFinal().getDescripcion());

            save(null);
             */

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            EstadosDocumento estadoDocumentoFinal = null;
            try {
                estadoDocumentoFinal = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar estado final del documento. Documento no se puede cambiar estado");
                return;
            }

            CambiosEstadoDocumento cambioEstadoDocumento = cambiosEstadoDocumentoController.prepareCreate(null);
            cambioEstadoDocumento.setDocumentoJudicial(getSelected());

            cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());

            cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoFinal(estadoDocumentoFinal);

            cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
            cambiosEstadoDocumentoController.save(null);

            getSelected().setEstado(estadoDocumentoFinal);
            getSelected().setEstadoProcesal(estadoDocumentoFinal.getDescripcion());

            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estadoDocumentoFinal.getDescripcion());
            estadoProc.setDocumentoJudicial(getSelected());

            // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            // estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);

            verifDocumentosAtencion();

            /*
            Collection<DocumentosJudiciales> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (col != null) {
                for (DocumentosJudiciales doc : col) {

                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setEstado(flujoDoc.getEstadoDocumentoFinal());
                    doc.setEstadoProcesal(flujoDoc.getEstadoDocumentoFinal().getDescripcion());

                    //setSelected(doc);
                    //super.save(null);
                    
                    
                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(flujoDoc.getEstadoDocumentoFinal().getDescripcion());
                    estadoProc.setDocumentoJudicial(doc);
                    
                                        // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                        // estadosProcesalesDocumentosJudicialesController.saveNew2(null);
                     
                    doc.setEstadoProcesalDocumentoJudicial(estadoProc);
                    doc.setFechaHoraEstadoProcesal(fecha);
                    doc.setUsuarioEstadoProcesal(usuario);

                    setSelected(doc);

                    super.save(null);

                }

                if (busquedaPorFechaAlta) {

                    if (fechaAltaDesde == null) {
                        fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaAltaHasta == null) {
                        fechaAltaHasta = ejbFacade.getSystemDateOnly();
                    }

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
             */
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     *
     *
     * public void prepareEstadoProcesal() { ultimaObservacion = null; if
     * (getSelected() != null) { getSelected().setEstadoProcesalAux(null);
     * getSelected().setEstadoProcesal(null); } }
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the EstadosDocumento controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
    }

    public String navigateObservacionesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            //FacesContext
            //        .getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class
            //        ).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/verDocumentosAdministrativosMesa/index");
        }
        return "/pages/observacionesDocumentosJudiciales/index";
    }

    public String navigateEstadosProcesalesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext
                    .getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class
                    ).setParameter("documentoJudicial", getSelected()).getResultList());
        }
        return "/pages/estadosProcesalesDocumentosJudiciales/index";
    }

    public String navigateCambiosEstadoDocumento() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("documento_judicial_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/verDocumentosAdministrativosMesa/index");
        }
        return "/pages/cambiosEstadoDocumento/index";
    }

    /*
    @Override
    public Collection<DocumentosJudiciales> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", fechaAltaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).getResultList();
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
    public Collection<DocumentosJudiciales> getItems() {
        return super.getItems2();
    }

    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;
            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedAsignadoAll2", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedAsignado2", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).getResultList());
            }
        }
    }

    public void buscarPorFechaAltaFiltro() {

        if (eleccionFiltro != null) {
            switch (eleccionFiltro) {
                case "1":
                    long diffInMillies = Math.abs(fechaAltaHasta.getTime() - fechaAltaDesde.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff > 31) {
                        JsfUtil.addErrorMessage("El rango de fecha no puede ser de mas de 31 dias");
                        return;
                    }
                    buscarPorFechaAlta();
                    break;
                case "2":
                    if (asuntoFiltro != null) {
                        if (!asuntoFiltro.equals("")) {
                            buscarPorAsunto(asuntoFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar descripción");
                            return;
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar descripción.");
                        return;
                    }
                    break;
                case "3":
                    if (textoFiltro != null) {
                        if (!textoFiltro.equals("")) {
                            buscarPorTexto(textoFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar nro mesa entrada");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar nro mesa entrada.");
                    }
                default:
                    break;
            }

        }
    }

    public void buscarPorFechaAltaArchivadosFiltro() {

        if (eleccionArchivadosFiltro != null) {
            switch (eleccionArchivadosFiltro) {
                case "1":
                    long diffInMillies = Math.abs(fechaAltaArchivadosHasta.getTime() - fechaAltaArchivadosDesde.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff > 31) {
                        JsfUtil.addErrorMessage("El rango de fecha no puede ser de mas de 31 dias");
                        return;
                    }
                    buscarPorFechaAltaArchivados(fechaAltaArchivadosDesde, fechaAltaArchivadosHasta);
                    break;
                case "2":
                    if (asuntoArchivadosFiltro != null) {
                        if (!asuntoArchivadosFiltro.equals("")) {
                            buscarPorAsuntoArchivados(asuntoArchivadosFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar descripción");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar descripción.");
                    }
                    break;
                case "3":
                    if (textoArchivadosFiltro != null) {
                        if (!textoArchivadosFiltro.equals("")) {
                            buscarPorTextoArchivados(textoArchivadosFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar nro mesa entrada");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar nro mesa entrada.");
                    }
                default:
                    break;
            }

        }
    }

    public void buscarPorAsunto(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        if (filtroURL.verifPermiso("verTodosDocsAdm")) {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedDescripcionMesaEntradaAsignadoAll2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("descripcionMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            setItems(lista);
        } else {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedDescripcionMesaEntradaAsignado2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("descripcionMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            setItems(lista);
        }

    }


    public void buscarPorAsuntoArchivados(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        if (filtroURL.verifPermiso("verTodosDocsAdm")) {
            listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedDescripcionMesaEntradaAsignadoAll3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("descripcionMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
        } else {
            listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedDescripcionMesaEntradaAsignado3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("descripcionMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
        }

    }

    public void buscarPorTexto(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        if (filtroURL.verifPermiso("verTodosDocsAdm")) {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedNroMesaEntradaAsignadoAll2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            setItems(lista);
        } else {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedNroMesaEntradaAsignado2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            setItems(lista);
        }

    }

    public void buscarPorTextoArchivados(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        if (filtroURL.verifPermiso("verTodosDocsAdm")) {
            listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedNroMesaEntradaAsignadoAll3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
        } else {
            listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedNroMesaEntradaAsignado3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
        }

    }

    public List<DocumentosJudiciales> buscarPorFechaAlta() {
        List<DocumentosJudiciales> lista = new ArrayList<>();
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;

            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignadoAll2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
                setItems(lista);
            } else {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignado2", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
                setItems(lista);
            }
        }
        return lista;
    }

    public void buscarPorFechaAltaArchivados() {
        buscarPorFechaAltaArchivados(fechaAltaDesde, fechaAltaHasta);
    }

    public void buscarPorFechaAltaArchivados(Date fecDesde, Date fecHasta) {
        if (fecDesde == null || fecHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;
            if (filtroURL.verifPermiso("verTodosDocsAdm")) {
                listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignadoAll3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("fechaDesde", fecDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            } else {
                listaArchivados = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignado3", DocumentosJudiciales.class).setParameter("fechaInicio", fechaInicio).setParameter("fechaDesde", fecDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usuario.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getResultList();
            }

        }
    }

    public void editarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosJudiciales obs = getSelected().getObservacionDocumentoJudicial();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.save(null);

                getSelected().setObservacionDocumentoJudicial(obs);

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

                ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoJudicial(obs);

                super.save(null);

                obs.setDocumentoJudicial(getSelected());

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

                EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();

                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesController.save(null);

                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);

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

                EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEmpresa(usuario.getEmpresa());
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadoProc.setDocumentoJudicial(getSelected());

                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);

                super.save(null);
            }

        }
    }

    /*
    public void editarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosJudiciales obs = getSelected().getObservacionDocumentoJudicial();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.save(null);

                getSelected().setObservacionDocumentoJudicial(obs);

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

                ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoJudicial(obs);

                super.save(null);

                obs.setDocumentoJudicial(getSelected());

                obsController.save(null);
            }

        }
    }
     */
    public boolean renderedBorrarArchivo() {
        return filtroURL.verifPermiso("borrarArchivo");
    }

    private boolean deshabilitarCamposAdm() {
        return filtroURL.verifPermiso("verTodosDocsAdm");
    }

    public boolean deshabilitarVerObs() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarAgregarObs() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarTransferir() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarAlzar() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarPlazo() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarTransferenciasPendientes() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarBotonDesarchivar() {
        return deshabilitarCamposAdm();
    }

    public boolean deshabilitarCambiosEstado() {
        return !(!deshabilitarCamposAdm() && filtroURL.verifPermiso("/pages/cambiosEstadoDocumento/index.xhtml"));
    }

    public boolean deshabilitarBorrarArchivo(Archivos item) {
        if (filtroURL.verifPermiso("borrarArchivo")) {
            if (item != null) {
                if (getSelected() != null) {
                    if (getSelected().getDepartamento().equals(usuario.getDepartamento())) {
                        if (item.getDepartamento() != null) {
                            if (item.getDepartamento().equals(usuario.getDepartamento())) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public void borrarArchivo(Archivos item) {
        if (filtroURL.verifPermiso("borrarArchivo")) {
            if (item != null) {
                if (getSelected() != null) {
                    if (getSelected().getDepartamento().equals(usuario.getDepartamento())) {
                        if (item.getDepartamento() != null) {
                            if (item.getDepartamento().equals(usuario.getDepartamento())) {

                                item.setUsuarioBorrado(usuario);
                                item.setFechaHoraBorrado(ejbFacade.getSystemDate());

                                archivosController.setSelected(item);
                                archivosController.save(null);

                                archivosController.delete(null);
                                obtenerArchivos();
                            } else {
                                JsfUtil.addErrorMessage("No se puede borrar el archivo");
                            }
                        } else {
                            JsfUtil.addErrorMessage("No se puede borrar el archivo");
                        }
                    } else {
                        JsfUtil.addErrorMessage("No se puede borrar el archivo");
                    }
                } else {
                    JsfUtil.addErrorMessage("No se puede borrar el archivo");
                }
            } else {
                JsfUtil.addErrorMessage("No se puede borrar el archivo");
            }
        } else {
            JsfUtil.addErrorMessage("No se puede borrar el archivo");
        }
    }

    @Override
    public void delete(ActionEvent event) {
        if (getSelected().getCanalEntradaDocumentoJudicial().equals(canal)) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
                super.delete(event);

                if (busquedaPorFechaAlta) {
                    /*
                    if (fechaAltaDesde == null) {
                        fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                    }
                    if (fechaAltaHasta == null) {
                        fechaAltaHasta = ejbFacade.getSystemDateOnly();
                    }
                     */
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

    public void alzarArchivo(Archivos arch) {

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
                Archivos archivo = new Archivos();

                archivo.setDocumentoJudicial(getSelected());
                archivo.setDescripcion(getSelected().getDescripcionMesaEntrada());
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
            } else {
                arch.setDocumentoJudicial(getSelected());
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

    public void saveNew() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoNew") == null) ? false : params.get("guardandoNew"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", true);
                if (getSelected() != null) {
                    //if (detalles != null) {
                    //if (detalles.size() > 0) {
                    if (entradaDocumentoJudicial.getNroMesaEntrada() != null) {
                        if (!entradaDocumentoJudicial.getNroMesaEntrada().equals("")) {
                            if (tipoDocumentoJudicial != null) {
                                if (subcategoriaDocumentoJudicial != null) {
                                    if (entradaDocumentoJudicial != null) {
                                        if (entradaDocumentoJudicial.getNroMesaEntrada() == null) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        } else if ("".equals(entradaDocumentoJudicial.getNroMesaEntrada())) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        }
                                    }

                                    EstadosDocumento estado = null;
                                    try {
                                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", "-1").getSingleResult();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JsfUtil.addErrorMessage("Error de configuracion. No se pudo guardar documento");
                                        return;
                                    }

                                    Date fecha = ejbFacade.getSystemDate();
                                    EntradasDocumentosJudiciales entradaDoc = entradasDocumentosJudicialesController.prepareCreate(null);

                                    entradaDoc.setFechaHoraUltimoEstado(fecha);
                                    entradaDoc.setUsuarioUltimoEstado(usuario);
                                    entradaDoc.setFechaHoraAlta(fecha);
                                    entradaDoc.setUsuarioAlta(usuario);
                                    entradaDoc.setEmpresa(usuario.getEmpresa());
                                    if (entradaDocumentoJudicial.getRecibidoPor() != null) {
                                        entradaDoc.setRecibidoPor(entradaDocumentoJudicial.getRecibidoPor());
                                    } else {
                                        entradaDoc.setRecibidoPor(usuario);
                                    }
                                    entradaDoc.setEntregadoPor(entradaDocumentoJudicial.getEntregadoPor());
                                    entradaDoc.setTelefono(entradaDocumentoJudicial.getTelefono());
                                    entradaDoc.setNroCedulaRuc(entradaDocumentoJudicial.getNroCedulaRuc());
                                    entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

                                    //entradasDocumentosJudicialesController.setSelected(entradaDoc);
                                    //entradasDocumentosJudicialesController.saveNew(null);
                                    getSelected().setFechaHoraUltimoEstado(fecha);
                                    getSelected().setUsuarioUltimoEstado(usuario);
                                    getSelected().setFechaHoraAlta(fecha);
                                    getSelected().setUsuarioAlta(usuario);
                                    getSelected().setEmpresa(usuario.getEmpresa());
                                    getSelected().setDepartamento(usuario.getDepartamento());
                                    getSelected().setEntradaDocumento(entradaDoc);
                                    getSelected().setDescripcionMesaEntrada(descripcionMesaEntrada);
                                    getSelected().setCaratula(descripcionMesaEntrada);
                                    getSelected().setEstadoProcesal(estado.getDescripcion());
                                    getSelected().setFolios(null);
                                    getSelected().setFechaPresentacion(fecha);
                                    getSelected().setEstado(estado);

                                    getSelected().setResponsable(usuario);
                                    getSelected().setCanalEntradaDocumentoJudicial(canal);
                                    getSelected().setTipoDocumentoJudicial(tipoDocumentoJudicial);
                                    getSelected().setSubcategoriaDocumentoJudicial(subcategoriaDocumentoJudicial);
                                    getSelected().setMostrarWeb("SI");

                                    super.saveNew(null);

                                    alzarArchivo(null);
                                    //EntradasDocumentosJudiciales ent = this.ejbFacade.getEntityManager().createNamedQuery("EntradasDocumentosJudiciales.findById", EntradasDocumentosJudiciales.class).setParameter("id", entradasDocumentosJudicialesController.getSelected().getId()).getSingleResult();
                                    if (estado == null) {

                                        estado = estadosDocumentoController.prepareCreate(null);

                                        estado.setCodigo("-1");

                                        estado.setDescripcion("Entro por Mesa de Entrada");
                                        estado.setEmpresa(usuario.getEmpresa());

                                        estadoController.setSelected(estado);

                                        estadoController.saveNew(null);
                                    }

                                    CambiosEstadoDocumento cambioEstadoDocumento = cambiosEstadoDocumentoController.prepareCreate(null);
                                    cambioEstadoDocumento.setDocumentoJudicial(getSelected());

                                    cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoOriginal(estado);

                                    cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoFinal(estado);

                                    cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
                                    cambioEstadoDocumento.setFechaHoraAlta(fecha);
                                    cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                                    cambioEstadoDocumento.setUsuarioAlta(usuario);
                                    cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                                    cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                                    cambiosEstadoDocumentoController.save(null);

                                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                                    estadoProc.setUsuarioAlta(usuario);
                                    estadoProc.setUsuarioUltimoEstado(usuario);
                                    estadoProc.setFechaHoraAlta(fecha);
                                    estadoProc.setFechaHoraUltimoEstado(fecha);
                                    estadoProc.setEmpresa(usuario.getEmpresa());
                                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                                    estadoProc.setDocumentoJudicial(getSelected());

                                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                    estadosProcesalesDocumentosJudicialesController.saveNew(null);

                                    getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                                    getSelected().setUsuarioEstadoProcesal(usuario);
                                    getSelected().setFechaHoraEstadoProcesal(fecha);

                                    super.save(null);

                                    /*
                                    Iterator<DocumentosJudiciales> it = detalles.iterator();

                                    DocumentosJudiciales doc = null;

                                    while (it.hasNext()) {
                                        doc = it.next();

                                        DocumentosJudiciales docGuardar = super.prepareCreate(null);

                                        docGuardar.setFechaHoraUltimoEstado(fecha);
                                        docGuardar.setUsuarioUltimoEstado(usuario);
                                        docGuardar.setFechaHoraAlta(fecha);
                                        docGuardar.setUsuarioAlta(usuario);
                                        docGuardar.setEmpresa(usuario.getEmpresa());
                                        docGuardar.setDepartamento(usuario.getDepartamento());
                                        docGuardar.setEntradaDocumento(entradaDoc);
                                        docGuardar.setDescripcionMesaEntrada(doc.getDescripcionMesaEntrada());
                                        docGuardar.setCaratula(doc.getDescripcionMesaEntrada());
                                        docGuardar.setEstadoProcesal(estado.getDescripcion());
                                        docGuardar.setFolios(doc.getFolios());
                                        docGuardar.setFechaPresentacion(fecha);
                                        docGuardar.setEstado(estado);
                                        
                                        docGuardar.setResponsable(usuario);
                                        docGuardar.setCanalEntradaDocumentoJudicial(canal);
                                        docGuardar.setTipoDocumentoJudicial(tipoDocumentoJudicial);
                                        docGuardar.setSubcategoriaDocumentoJudicial(subcategoriaDocumentoJudicial);
                                        docGuardar.setMostrarWeb("SI");

                                        setSelected(docGuardar);

                                        super.saveNew2(null);

                                        DocumentosJudiciales docGuardar2 = null;
                                        try {
                                            docGuardar2 = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumentoMaxDoc", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getSingleResult();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        if (docGuardar2 != null) {
                                            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                                            estadoProc.setUsuarioAlta(usuario);
                                            estadoProc.setUsuarioUltimoEstado(usuario);
                                            estadoProc.setFechaHoraAlta(fecha);
                                            estadoProc.setFechaHoraUltimoEstado(fecha);
                                            estadoProc.setEmpresa(usuario.getEmpresa());
                                            estadoProc.setEstadoProcesal(estado.getDescripcion());
                                            estadoProc.setDocumentoJudicial(docGuardar2);
                                            
                                            docGuardar2.setEstadoProcesalDocumentoJudicial(estadoProc);
                                            docGuardar2.setFechaHoraEstadoProcesal(fecha);
                                            docGuardar2.setUsuarioEstadoProcesal(usuario);

                                            setSelected(docGuardar2);

                                            super.save(null);
                                        }

                                        setSelected(null);
                                    }
                            
                                     */
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
                    /*    } else {
                    JsfUtil.addErrorMessage("Debe guardar al menos un documento");
                }
            } else {
                JsfUtil.addErrorMessage("Debe guardar al menos un documento");
            }*/
                }
            }
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", false);
        }
    }

    public void save() {

        boolean guardar = true;

        if (getSelected() != null) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

                if (getSelected().getFechaPresentacion() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Fecha Presentacion");
                    guardar = false;
                }
                /*
                if (getSelected().getCausa() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Nro Causa");
                    guardar = false;
                } else if ("".equals(getSelected().getCausa())) {
                    JsfUtil.addErrorMessage("Debe ingresar Nro Causa");
                    guardar = false;
                }
                
                if (getSelected().getCaratula() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Caratula");
                    guardar = false;
                } else if ("".equals(getSelected().getCaratula())) {
                    JsfUtil.addErrorMessage("Debe ingresar Caratula");
                    guardar = false;
                }

                if (getSelected().getMotivoProceso() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Motivo Proceso");
                    guardar = false;
                } else if ("".equals(getSelected().getMotivoProceso())) {
                    JsfUtil.addErrorMessage("Debe ingresar Motivo Proceso");
                    guardar = false;
                }

                if (getSelected().getEstadoProcesal() == null) {
                    JsfUtil.addErrorMessage("Debe ingresar Estado Procesal");
                    guardar = false;
                } else if ("".equals(getSelected().getEstadoProcesal())) {
                    JsfUtil.addErrorMessage("Debe ingresar Estado Procesal");
                    guardar = false;
                }

                 */
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                /*
                if (file != null && guardar) {
                    if (file.getContents().length > 0) {

                        byte[] bytes = null;
                        try {
                            bytes = IOUtils.toByteArray(file.getInputstream());
                        } catch (IOException ex) {
                        }

                        //getSelected().setDocumento(bytes);
                        DocumentosEscaneados docEsc = null;

                        if (getSelected().getDocumentoEscaneado() == null) {
                            docEsc = documentosEscaneadosController.prepareCreate(null);

                            docEsc.setEmpresa(usuario.getEmpresa());
                            docEsc.setDocumento(bytes);

                            documentosEscaneadosController.setSelected(docEsc);
                            documentosEscaneadosController.saveNew(null);

                            getSelected().setDocumento(documentosEscaneadosController.getSelected().getId());
                        } else {
                            docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class
                            ).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();
                            documentosEscaneadosController.setSelected(docEsc);
                            documentosEscaneadosController.save(null);
                        }

                    }
                }
                 */
 /*
                if (getSelected().verifEstadoProcesal() && guardar) {

                    getSelected().transferirEstadoProcesal();
                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                    estadoProc.setDocumentoJudicial(getSelected());

                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                    estadosProcesalesDocumentosJudicialesController.saveNew(null);

                    getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                    getSelected().setFechaHoraEstadoProcesal(fecha);
                    getSelected().setUsuarioEstadoProcesal(usuario);

                }
                 */
                if (getSelected().verifObs() && guardar) {

                    getSelected().transferirObs();

                    ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fecha);
                    obs.setFechaHoraUltimoEstado(fecha);
                    obs.setEmpresa(usuario.getEmpresa());
                    obs.setObservacion(getSelected().getUltimaObservacion());
                    obs.setDocumentoJudicial(getSelected());

                    obsController.setSelected(obs);
                    obsController.saveNew(null);

                    getSelected().setFechaUltimaObservacion(fecha);
                    getSelected().setObservacionDocumentoJudicial(obs);
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
                /*
                if (fechaAltaDesde == null) {
                    fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaAltaHasta == null) {
                    fechaAltaHasta = ejbFacade.getSystemDateOnly();
                }
                 */
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

    public void saveDoc() {
        if (file != null) {
            if (file.getContent().length > 0) {

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputStream());
                } catch (IOException ex) {
                }

                // getSelected().setDocumento(bytes);
                DocumentosEscaneados docEsc = null;

                if (getSelected().getDocumentoEscaneado() == null) {
                    docEsc = documentosEscaneadosController.prepareCreate(null);

                    docEsc.setEmpresa(usuario.getEmpresa());
                    docEsc.setDocumento(bytes);

                    documentosEscaneadosController.setSelected(docEsc);
                    documentosEscaneadosController.saveNew(null);

                    getSelected().setDocumento(documentosEscaneadosController.getSelected().getId());

                    super.save(null);

                } else {
                    docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class
                    ).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();
                    docEsc.setDocumento(bytes);
                    documentosEscaneadosController.setSelected(docEsc);
                    documentosEscaneadosController.save(null);
                }

            }

        }
    }

    public void desarchivar() {
        if (selectedArchivados != null) {
            Date fecha = ejbFacade.getSystemDate();
            CambiosEstadoDocumento cambioEstadoDocumento = new CambiosEstadoDocumento();
            cambioEstadoDocumento.setDocumentoJudicial(selectedArchivados);

            cambioEstadoDocumento.setResponsableOrigen(selectedArchivados.getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(selectedArchivados.getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(selectedArchivados.getEstado());

            cambioEstadoDocumento.setResponsableDestino(selectedArchivados.getResponsable());
            cambioEstadoDocumento.setDepartamentoDestino(selectedArchivados.getDepartamento());

            cambioEstadoDocumento.setEstadoFinal(selectedArchivados.getEstadoAnterior());

            cambioEstadoDocumento.setEmpresa(selectedArchivados.getEmpresa());
            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
            cambiosEstadoDocumentoController.saveNew2(null);

            EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(selectedArchivados.getEstadoAnterior().getDescripcion());
            estadoProc.setDocumentoJudicial(selectedArchivados);

            selectedArchivados.setFechaHoraUltimoEstado(fecha);
            selectedArchivados.setUsuarioUltimoEstado(usuario);
            EstadosDocumento est = selectedArchivados.getEstadoAnterior();
            selectedArchivados.setEstadoAnterior(selectedArchivados.getEstado());
            selectedArchivados.setEstadoProcesal(selectedArchivados.getEstadoAnterior().getDescripcion());

            selectedArchivados.setEstadoProcesalDocumentoJudicial(estadoProc);
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

    public void saveDptoPendiente() {
        saveDptoPendiente(getSelected(), listaUsuariosTransf);
    }

    public void saveDptoPendiente(DocumentosJudiciales doc, Collection<Usuarios> lista) {
        if (doc != null) {
            Date fecha = ejbFacade.getSystemDate();
            FlujosDocumento flujoDoc = null;
            EstadosDocumento estado = null;

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
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                        //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();

                        //                flujoDoc
                        //                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        //                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstadoSegunRol", FlujosDocumento.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                        ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }
            } else {
                estado = responsableDestino.getEstadoDocumentoFinal();
            }

            CambiosEstadoDocumentoPendientes cambioEstadoDocumento = new CambiosEstadoDocumentoPendientes();
            cambioEstadoDocumento.setDocumentoJudicial(doc);

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

            cambioEstadoDocumento.setEmpresa(doc.getEmpresa());
            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            cambiosEstadoDocumentoPendientesController.setSelected(cambioEstadoDocumento);
            cambiosEstadoDocumentoPendientesController.saveNew2(null);

        }
    }

    /*
    
            if(usuarioBkp == null){
                CambiosEstadoDocumento cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(getSelected());

                cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
                cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
                cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());

                if (responsableDestino.getId() > 0) {
                    cambioEstadoDocumento.setResponsableDestino(responsableDestino);
                    cambioEstadoDocumento.setDepartamentoDestino(responsableDestino.getDepartamento());
                } else {
                    cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
                    cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
                }
                cambioEstadoDocumento.setEstadoFinal(estado);

                cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                cambiosEstadoDocumentoController.save(null);

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                if (responsableDestino.getId() > 0) {
                    getSelected().setResponsableAnterior(getSelected().getResponsable());
                    getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                    getSelected().setResponsable(responsableDestino);
                    getSelected().setDepartamento(responsableDestino.getDepartamento());
                }
                getSelected().setEstadoAnterior(getSelected().getEstado());
                getSelected().setEstado(estado);
                getSelected().setEstadoProcesal(estado.getDescripcion());
            }else{
                CambiosEstadoDocumento cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(getSelected());

                cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
                cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
                cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());

                cambioEstadoDocumento.setResponsableDestino(usuarioBkp);
                cambioEstadoDocumento.setDepartamentoDestino(usuario.getDepartamento());
                
                cambioEstadoDocumento.setEstadoFinal(getSelected().getEstado());

                cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                cambiosEstadoDocumentoController.save(null);
                
                
                CambiosEstadoDocumento cambioEstadoDocumento2 = new CambiosEstadoDocumento();
                cambioEstadoDocumento2.setDocumentoJudicial(getSelected());

                cambioEstadoDocumento2.setResponsableOrigen(usuarioBkp);
                cambioEstadoDocumento2.setDepartamentoOrigen(usuario.getDepartamento());
                cambioEstadoDocumento2.setEstadoOriginal(getSelected().getEstado());

                if (responsableDestino.getId() > 0) {
                    cambioEstadoDocumento2.setResponsableDestino(responsableDestino);
                    cambioEstadoDocumento2.setDepartamentoDestino(responsableDestino.getDepartamento());
                } else {
                    cambioEstadoDocumento2.setResponsableDestino(usuario);
                    cambioEstadoDocumento2.setDepartamentoDestino(usuario.getDepartamento());
                }
                cambioEstadoDocumento2.setEstadoFinal(estado);

                cambioEstadoDocumento2.setEmpresa(getSelected().getEmpresa());
                cambioEstadoDocumento2.setFechaHoraAlta(fecha);
                cambioEstadoDocumento2.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento2.setUsuarioAlta(usuario);
                cambioEstadoDocumento2.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento2);
                cambiosEstadoDocumentoController.save(null);

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                if (responsableDestino.getId() > 0) {
                    getSelected().setResponsableAnterior(usuarioBkp);
                    getSelected().setDepartamentoAnterior(usuario.getDepartamento());
                    getSelected().setResponsable(responsableDestino);
                    getSelected().setDepartamento(responsableDestino.getDepartamento());
                }
                getSelected().setEstadoAnterior(getSelected().getEstado());
                getSelected().setEstado(estado);
                getSelected().setEstadoProcesal(estado.getDescripcion());
            }
     */
    public void actualizarTransferPendiente(CambiosEstadoDocumentoPendientes item, String respuesta) {

        if (Constantes.SI.equals(respuesta)) {

            DocumentosJudiciales doc = null;
            try {
                doc = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findById", DocumentosJudiciales.class).setParameter("id", item.getDocumentoJudicial().getId()).getSingleResult();
            } catch (Exception e) {

            }

            if (doc == null) {
                JsfUtil.addErrorMessage("El documento a transferir ya no existe");
                return;
            }
            /*
            if(!doc.getResponsable().equals(item.getResponsableOrigen())){
                JsfUtil.addErrorMessage("El responsable origen ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }
             */
            if (!doc.getDepartamento().equals(item.getDepartamentoOrigen())) {
                JsfUtil.addErrorMessage("El departamento origen ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }

            if (!doc.getEstado().equals(item.getEstadoOriginal())) {
                JsfUtil.addErrorMessage("El estado original ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosJudiciales> col = null;

            FlujosDocumento flujoDoc = null;
            EstadosDocumento estado = null;
            /*
            try {
                RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class
                ).setParameter("usuario", item.getResponsableDestino().getId()).getSingleResult();

                flujoDoc
                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstadoSegunRol", FlujosDocumento.class
                        ).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. El pedido de transferencia está desactualizado");
                return;
            }

            if(item.getEstadoFinal().equals(flujoDoc.getEstadoDocumentoFinal())){
                JsfUtil.addErrorMessage("El estado final ya ha cambiado. El pedido de transferencia está desactualizado");
                return;
            }
             */
            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                ).setParameter("codigo", item.getEstadoFinal().getCodigo()).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. El pedido de transferencia está desactualizado");
                return;
            }

            CambiosEstadoDocumento cambioEstadoDocumento = null;
            if (usuarioBkp == null) {
                cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(doc);

                cambioEstadoDocumento.setResponsableOrigen(item.getResponsableOrigen());
                cambioEstadoDocumento.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento.setResponsableDestino(item.getResponsableDestino());
                cambioEstadoDocumento.setDepartamentoDestino(item.getDepartamentoDestino());

                cambioEstadoDocumento.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento.setEmpresa(item.getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento.setUsuarioUltimoEstado(item.getUsuarioAlta());

                //cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                //cambiosEstadoDocumentoController.save(null);
                doc.setResponsableAnterior(doc.getResponsable());
                doc.setResponsable(item.getResponsableDestino());
                doc.setDepartamentoAnterior(doc.getDepartamento());
                doc.setDepartamento(item.getDepartamentoDestino());

            } else {
                CambiosEstadoDocumento cambioEstadoDocumento2 = new CambiosEstadoDocumento();
                cambioEstadoDocumento2.setDocumentoJudicial(doc);

                cambioEstadoDocumento2.setResponsableOrigen(item.getResponsableOrigen());
                cambioEstadoDocumento2.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento2.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento2.setResponsableDestino(usuarioBkp);
                cambioEstadoDocumento2.setDepartamentoDestino(item.getDepartamentoOrigen());

                cambioEstadoDocumento2.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento2.setEmpresa(item.getEmpresa());
                cambioEstadoDocumento2.setFechaHoraAlta(fecha);
                cambioEstadoDocumento2.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento2.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento2.setUsuarioUltimoEstado(item.getUsuarioAlta());

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento2);
                cambiosEstadoDocumentoController.saveNew2(null);

                cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(doc);

                cambioEstadoDocumento.setResponsableOrigen(usuarioBkp);
                cambioEstadoDocumento.setDepartamentoOrigen(item.getDepartamentoOrigen());
                cambioEstadoDocumento.setEstadoOriginal(item.getEstadoOriginal());

                cambioEstadoDocumento.setResponsableDestino(item.getResponsableDestino());
                cambioEstadoDocumento.setDepartamentoDestino(item.getDepartamentoDestino());

                cambioEstadoDocumento.setEstadoFinal(item.getEstadoFinal());

                cambioEstadoDocumento.setEmpresa(item.getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(item.getUsuarioAlta());
                cambioEstadoDocumento.setUsuarioUltimoEstado(item.getUsuarioAlta());

                //cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento2);
                //cambiosEstadoDocumentoController.save(null);
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
            EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

            estadoProc.setUsuarioAlta(item.getUsuarioAlta());
            estadoProc.setUsuarioUltimoEstado(item.getUsuarioAlta());
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(item.getEmpresa());
            estadoProc.setEstadoProcesal(item.getEstadoFinal().getDescripcion());
            estadoProc.setDocumentoJudicial(doc);

            //                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            //                    estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            doc.setEstadoProcesalDocumentoJudicial(estadoProc);
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

    public void saveDpto() {
        saveDpto(getSelected(), listaUsuariosTransf);
    }

    public void saveDpto(DocumentosJudiciales doc, Collection<Usuarios> lista) {

        if (doc != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosJudiciales> col = null;
            /*
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
             */
            //Usuarios resp = getSelected().getResponsable();
            FlujosDocumento flujoDoc = null;
            EstadosDocumento estado = null;

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
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                        //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();

                        //                flujoDoc
                        //                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        //                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstadoSegunRol", FlujosDocumento.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                        ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }

            } else {
                estado = responsableDestino.getEstadoDocumentoFinal();
            }

            if (usuarioBkp == null) {
                CambiosEstadoDocumento cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(doc);

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

                cambioEstadoDocumento.setEmpresa(doc.getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                cambiosEstadoDocumentoController.saveNew2(null);

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
                CambiosEstadoDocumento cambioEstadoDocumento = new CambiosEstadoDocumento();
                cambioEstadoDocumento.setDocumentoJudicial(doc);

                cambioEstadoDocumento.setResponsableOrigen(doc.getResponsable());
                cambioEstadoDocumento.setDepartamentoOrigen(doc.getDepartamento());
                cambioEstadoDocumento.setEstadoOriginal(doc.getEstado());

                cambioEstadoDocumento.setResponsableDestino(usuarioBkp);
                cambioEstadoDocumento.setDepartamentoDestino(usuario.getDepartamento());

                cambioEstadoDocumento.setEstadoFinal(doc.getEstado());

                cambioEstadoDocumento.setEmpresa(doc.getEmpresa());
                cambioEstadoDocumento.setFechaHoraAlta(fecha);
                cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento.setUsuarioAlta(usuario);
                cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                cambiosEstadoDocumentoController.saveNew2(null);

                CambiosEstadoDocumento cambioEstadoDocumento2 = new CambiosEstadoDocumento();
                cambioEstadoDocumento2.setDocumentoJudicial(doc);

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

                cambioEstadoDocumento2.setEmpresa(doc.getEmpresa());
                cambioEstadoDocumento2.setFechaHoraAlta(fecha);
                cambioEstadoDocumento2.setFechaHoraUltimoEstado(fecha);
                cambioEstadoDocumento2.setUsuarioAlta(usuario);
                cambioEstadoDocumento2.setUsuarioUltimoEstado(usuario);

                cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento2);
                cambiosEstadoDocumentoController.saveNew2(null);

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
            EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estado.getDescripcion());
            estadoProc.setDocumentoJudicial(doc);

            //                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            //                    estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            doc.setEstadoProcesalDocumentoJudicial(estadoProc);
            doc.setFechaHoraEstadoProcesal(fecha);
            doc.setUsuarioEstadoProcesal(usuario);

            setSelected(doc);

            super.save(null);

            setSelected(null);

            buscarPorFechaAlta();
            obtenerListaSalida();
            buscarPorFechaAltaArchivados();

            /*
            if (col != null) {
                for (DocumentosJudiciales doc : col) {

                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setResponsable(resp);
                    doc.setDepartamento(resp.getDepartamento());
                    doc.setEstado(estado);
                    doc.setEstadoProcesal(estado.getDescripcion());

                    //setSelected(doc);
                    //super.save(null);

                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                    estadoProc.setDocumentoJudicial(doc);
                    
                    //                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                    //                    estadosProcesalesDocumentosJudicialesController.saveNew2(null);
                     
                    doc.setEstadoProcesalDocumentoJudicial(estadoProc);
                    doc.setFechaHoraEstadoProcesal(fecha);
                    doc.setUsuarioEstadoProcesal(usuario);

                    setSelected(doc);

                    super.save(null);

                }

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
             */
        }
    }

    public void verDoc() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            if (getSelected().getDocumentoEscaneado() != null) {
                try {
                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.setContentType("application/pdf");
                    // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                    httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    DocumentosEscaneados docEsc = ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class
                    ).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();

                    servletOutputStream.write(docEsc.getDocumento());
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
            } else {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }
        } else {
            JsfUtil.addErrorMessage("Debe seleccionar un documento");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
}
