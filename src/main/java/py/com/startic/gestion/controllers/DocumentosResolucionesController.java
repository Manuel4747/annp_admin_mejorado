package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.el.ValueBinding;

import py.com.startic.gestion.models.DocumentosResoluciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosResoluciones;
import py.com.startic.gestion.models.EntradasDocumentosResolucion;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosProceso;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "documentosResolucionesController")
@ViewScoped
public class DocumentosResolucionesController extends AbstractController<DocumentosResoluciones> {
    
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
    private TiposDocumentosJudicialesController tiposDocumentosResolucionesController;
    @Inject
    private ObservacionesDocumentosJudicialesController obsController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosResolucionesController;
     @Inject
    private EntradasDocumentosResolucionController entradasDocumentosResolucionController;
    private EntradasDocumentosResolucion entradaDocumentoResolucion;
    private String nuevaCausa;
    private String descripcion;
    private String nombreEstado;
    private String ultimaObservacion;
    private CanalesEntradaDocumentoJudicial canal;
    private TiposDocumentosJudiciales tipoDoc;
    private Usuarios usuario;
    private Departamentos departamento;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private String pantalla;
    private String updateCerrar;
    private String nombreVariable;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<Personas> listaPersonas;
    private Personas personaSelected;
    private Personas persona;
    private String tituloResolucion;
    private String nroResolucion;
    private String caratulaAnterior;
    private EstadosProceso estadoProceso;
    private EstadosProceso estadoProcesoAnterior;
    private String causa;
    private boolean cambioAEnjuiciamiento;
    
    private boolean busquedaPorFechaAlta;
    
    private UploadedFile file;
    
    public UploadedFile getFile() {
        return file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String getTituloResolucion() {
        return tituloResolucion;
    }
    
    public void setTituloResolucion(String tituloResolucion) {
        this.tituloResolucion = tituloResolucion;
    }
    
    public String getNroResolucion() {
        return nroResolucion;
    }
    
    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }
    
    public String getCaratulaAnterior() {
        return caratulaAnterior;
    }
    
    public void setCaratulaAnterior(String caratulaAnterior) {
        this.caratulaAnterior = caratulaAnterior;
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getNuevaCausa() {
        return nuevaCausa;
    }
    
    public void setNuevaCausa(String nuevaCausa) {
        this.nuevaCausa = nuevaCausa;
    }
    
    public EntradasDocumentosResolucionController getEntradasDocumentosResolucionController() {
        return entradasDocumentosResolucionController;
    }
    
    public void setEntradasDocumentosResolucionController(EntradasDocumentosResolucionController entradasDocumentosResolucionController) {
        this.entradasDocumentosResolucionController = entradasDocumentosResolucionController;
    }
    
    public DocumentosResolucionesController() {
        // Inform the Abstract parent controller of the concrete DocumentosResoluciones Entity
        super(DocumentosResoluciones.class);
    }
    
    public String getUpdateCerrar() {
        return updateCerrar;
    }
    
    public void setUpdateCerrar(String updateCerrar) {
        this.updateCerrar = updateCerrar;
    }
    
    public String getNombreVariable() {
        return nombreVariable;
    }
    
    public void setNombreVariable(String nombreVariable) {
        this.nombreVariable = nombreVariable;
    }
    
    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }
    
    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }
    
    public Personas getPersonaSelected() {
        return personaSelected;
    }
    
    public void setPersonaSelected(Personas personaSelected) {
        this.personaSelected = personaSelected;
    }
    
    public Personas getPersona() {
        return persona;
    }
    
    public void setPersona(Personas persona) {
        this.persona = persona;
    }
    
    public void refrescar() {
        buscarPorFechaAlta();
    }
    
    public List<Personas> getListaPersonasActivas() {
        List<Personas> listaPersonasActivas = new ArrayList<>();
        for (Personas per : listaPersonas) {
            if ("AC".equals(per.getEstado())) {
                listaPersonasActivas.add(per);
            }
        }
        
        return listaPersonasActivas;
    }
    
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(5); // Secretaria
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_SE);
        tipoDoc = tiposDocumentosResolucionesController.prepareCreate(null);
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_DR);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
        usuario = (Usuarios) session.getAttribute("Usuarios");
        //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2025);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();
        
        pantalla = Constantes.NO;
        
        buscarPorFechaAlta();
    }
    
    @Override
    public DocumentosResoluciones prepareCreate(ActionEvent event) {
        return prepararCreate(null);
    }
    
    public DocumentosResoluciones prepararCreate(String tituloResolucion) {
        DocumentosResoluciones doc = super.prepareCreate(null);
        
        entradaDocumentoResolucion = entradasDocumentosResolucionController.prepareCreate(null);
        // entradaDocumentoResolucion.setRecibidoPor(usuario);
        EstadosProceso est = null;
        
        if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA)) {
            est = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getSingleResult();
        } else {
            est = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_EN_TRAMITE).getSingleResult();
        }
        
        doc.setMostrarWeb(Constantes.NO);
        
        doc.setEstadoProceso(est);
        doc.setEstadoProcesoAnterior(est);
        doc.setTituloResolucion(tituloResolucion);
        
        nuevaCausa = null;
        descripcion = null;
        nroResolucion = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;
        listaPersonas = new ArrayList<>();
        persona = null;
        
        return doc;
    }
    
    public void prepareEdit() {
        
        nuevaCausa = null;
        descripcion = null;
        nombreEstado = null;
        ultimaObservacion = null;
        persona = null;
        file = null;
        cambioAEnjuiciamiento = false;
        
        listaPersonas = new ArrayList<>();
        
        if (getSelected() != null) {
            estadoProceso = getSelected().getEstadoProceso();
            estadoProcesoAnterior = getSelected().getEstadoProcesoAnterior();
            tituloResolucion = getSelected().getTituloResolucion();
        }
        
    }
    
    public void borrarPersona(Personas personaActual) {
        
        if (listaPersonas != null) {
            
            listaPersonas.remove(personaActual);
            
            personaActual.setEstado("IN");
            
            listaPersonas.add(personaActual);
            
        }
    }
    
    public void agregarPersona() {
        
        if (persona != null) {
            
            if (listaPersonas == null) {
                listaPersonas = new ArrayList<>();
            }
            
            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonas) {
                if (per.equals(persona)) {
                    perActual = per;
                    encontro = true;
                }
            }
            
            if (!encontro) {
                persona.setTipoExpedienteAnterior(false);
                listaPersonas.add(persona);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }
    
    public void prepareObs() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setUltimaObservacionAux(null);
            getSelected().setUltimaObservacion(null);
        }
    }
    
    public void prepareEstadoProcesal() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setEstadoProcesalAux(null);
            getSelected().setEstadoProcesal(null);
        }
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
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
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
    
    public String navigateObservacionesDocumentosResolucionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosResoluciones_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosResoluciones.findByDocumentoJudicial", ObservacionesDocumentosResoluciones.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosResoluciones/index");
            
        }
        return "/pages/observacionesDocumentosResoluciones/index";
    }
    
    public String navigateObservacionesDocumentosResolucionesAntecedentesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosResoluciones_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosResoluciones.findByDocumentoJudicial", ObservacionesDocumentosResoluciones.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosResoluciones/index");
            
        }
        return "/pages/observacionesDocumentosResolucionesAntecedentes/index";
    }
    
    public String navigateEstadosProcesalesDocumentosResolucionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosResoluciones_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosResoluciones/index");
        }
        return "/pages/estadosProcesalesDocumentosResoluciones/index";
    }

    /*
    @Override
    public Collection<DocumentosResoluciones> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findOrderedFechaAlta", DocumentosResoluciones.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", fechaAltaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).getResultList();
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
    
    public String abrirCreate(String desde, String updateCerrar, String caratula, String nombreVariable) {
        pantalla = Constantes.NO;
        if (Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)) {
            pantalla = Constantes.SI;
            this.updateCerrar = updateCerrar;
            this.nombreVariable = nombreVariable;
        }
        
        prepararCreate(caratula);
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('DocumentosCreateDialog').show();");
        return "";
    }
    
    public String updateCreate() {
        if (getSelected() != null) {
            if (Constantes.SI.equals(pantalla)) {
                return updateCerrar;
            } else {
                return "display,:DocumentosResolucionesListForm:datalist,:growl";
            }
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
    public Collection<DocumentosResoluciones> getItems() {
        return super.getItems2();
    }

    /*
    @Override
    public Collection<DocumentosResoluciones> getItems() {
        setItems(ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findAll", DocumentosResoluciones.class).getResultList());
        return getItems2();
    }
     */
    public boolean deshabilitarFirmado() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_FIRMADO)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarBorrar() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarVerEstadoProcesal() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.ESTADOS_PROCESALES_DOCUMENTOS_JUDICIALES)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarVerObs() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_OBSERVACIONES_DOCUMENTOS_JUDICIALES)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarAdjuntar() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_ADJUNTAR_DOCUMENTO_JUDICIAL)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarVerObsAntecedente() {
        /*
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.PERMITIR_OBSERVACIONES_DOCUMENTOS_JUDICIALES_ANTECEDENTES)){
                    return false;
            }
        }
         */
        return true;
    }
    
    public boolean deshabilitarEstadoProceso() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA)) {
                return true;
            } else {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarEstadoProcesoNuevo() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA)) {
                return true;
            } else {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarMostrarWeb() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_MOSTRAR_WEB)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarEdit() {
        if (getSelected() != null) {
            
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA)) {
                if (getSelected().getEstadoProceso() != null) {
                    if (Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA.equals(getSelected().getEstadoProceso().getCodigo())) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarGuardarSinAjax() {
        return deshabilitarAdjuntar();
    }
    
    public boolean deshabilitarGuardarConAjax() {
        return !deshabilitarAdjuntar();
    }
    
    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findOrdered", DocumentosResoluciones.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
        }
    }
    
    public void buscarPorFechaAlta() {
        if (fechaAltaDesde == null || fechaAltaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAltaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            busquedaPorFechaAlta = true;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findOrderedFechaAlta", DocumentosResoluciones.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            
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
                
                estadosProcesalesDocumentosResolucionesController.setSelected(estadoProc);
                estadosProcesalesDocumentosResolucionesController.save(null);
                
                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                
                super.save(null);
            }
            
        }
    }
    
   
    public boolean validarSaveNew() {
        if (getSelected() != null) {
            
            if (getSelected().getTituloResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosResolucionesHelpText_tituloResolucion"));
                return false;
            } else if ("".equals(getSelected().getTituloResolucion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosResolucionesHelpText_tituloResolucion"));
                return false;
            }
            
        }
        
        return false;
    }
    
    public void saveNew() {
        if (getSelected() != null) {
            
            if (!validarSaveNew()) {
                return;
            }
            
            Date fecha = ejbFacade.getSystemDate();
            
           // getSelected().setFolios("");
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setTituloResolucion(getSelected().getTituloResolucion());
            getSelected().setDescripcion(descripcion);
            getSelected().setNroResolucion(nroResolucion);
            getSelected().setCanalEntradaDocumentoJudicial(canal);
            getSelected().setTipoDocumentoJudicial(tipoDoc);
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(usuario.getDepartamento());
            //  getSelected().setMostrarWeb("SI");

            EstadosDocumento estado = estadoController.prepareCreate(null);
            estado.setCodigo("0");
            
            getSelected().setEstado(estado);
            
            if (file != null) {
                if (file.getContent().length > 0) {
                    
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }
                    
                }
                
            }
            
            EntradasDocumentosResolucion doc = entradasDocumentosResolucionController.prepareCreate(null);
            
            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(usuario);
            doc.setFechaHoraAlta(fecha);
            doc.setUsuarioAlta(usuario);
            doc.setEmpresa(usuario.getEmpresa());
            jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(substring(nro_identificacion,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_resolucion WHERE substring(nro_identificacion,1,4) = 'AUTO';", NroResolucion.class);
            
            NroResolucion cod = (NroResolucion) query.getSingleResult();
            
            doc.setNroIdentificacion("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosResolucionesController.setSelected(doc);
            //entradasDocumentosResolucionesController.saveNew(null);
            getSelected().setEntradaDocumento(doc);
            
            super.saveNew(null);

            // if (!this.isErrorPersistencia())
            if (!this.isErrorPersistencia()) {
                if (Constantes.SI.equals(pantalla)) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreVariable, getSelected());
                }
            }
            
            if (getSelected().getUltimaObservacion() != null && !"".equals(getSelected().getUltimaObservacion())) {
                ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);
                
                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacion());
               // obs.setDocumentosResoluciones(getSelected());
                obs.setVisible(true);
                obs.setFechaHoraVisible(fecha);
                
                obsController.setSelected(obs);
                obsController.saveNew(null);
                
                getSelected().setObservacionDocumentoJudicial(obs);
                getSelected().setFechaUltimaObservacion(fecha);
                getSelected().setUsuarioUltimaObservacion(usuario);
                
                super.save(null);

                /*
                    obs.setDocumentoJudicial(getSelected());

                    obsController.save(null);
                 */
            }
            
            if (getSelected().getEstadoProcesal() != null && !"".equals(getSelected().getEstadoProcesal())) {
                EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosResolucionesController.prepareCreate(null);
                
                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEmpresa(usuario.getEmpresa());
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
              //  estadoProc.setDocumentoJudicial(getSelected()); 
                estadoProc.setVisible(true);
                estadoProc.setFechaHoraVisible(fecha);
                
                estadosProcesalesDocumentosResolucionesController.setSelected(estadoProc);
                estadosProcesalesDocumentosResolucionesController.saveNew(null);
                
                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                getSelected().setFechaHoraEstadoProcesal(fecha);
                getSelected().setUsuarioEstadoProcesal(usuario);
                
                super.save(null);
                /*
                    obs.setDocumentoJudicial(getSelected());

                    obsController.save(null);
                 */
            }
            // }

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
            // Parte solo aplicable cuando la pantalla Create Comprobante Egreso es llamada desde la pantalla Edit Viajes
            /* FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            ValueBinding binding = application.createValueBinding("#{resolucionesController}");
            ResolucionesController resolucionController = (ResolucionesController) binding.getValue(context);
            if (resolucionController != null) {
                resolucionController.actualizarDatosDocumentoJudicialEnCreate(getSelected());
            }*/
        }
    }
    
    public boolean validarSave() {
        if (getSelected().getFechaResolucion() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosResolucionesHelpText_fechaResolucion"));
            return false;
        }

        // if (getSelected().getCaratula() == null) {
        if (tituloResolucion == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosResolucionesHelpText_titulo"));
            return false;
        } else if ("".equals(tituloResolucion)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosResolucionesHelpText_titulo"));
            return false;
        }
        
        return true;
    }
    
    public void save() {
        
        if (getSelected() != null) {
            boolean guardar = true;
            
            Date fecha = ejbFacade.getSystemDate();
            // if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
            //if (getSelected().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

            if (!validarSave()) {
                return;
            }
            
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setTituloResolucion(tituloResolucion);
            getSelected().setEstadoProceso(estadoProceso);
            getSelected().setEstadoProcesoAnterior(estadoProcesoAnterior);
            
            if (file != null && guardar) {
                if (file.getContent().length > 0) {
                    
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }
                    
                }
            }
            
            if (getSelected().verifEstadoProcesal() && guardar) {
                
                getSelected().transferirEstadoProcesal();
                
                if (getSelected().getEstadoProcesal() != null) {
                    if (!"".equals(getSelected().getEstadoProcesal())) {
                        
                        EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosResolucionesController.prepareCreate(null);
                        
                        estadoProc.setUsuarioAlta(usuario);
                        estadoProc.setUsuarioUltimoEstado(usuario);
                        estadoProc.setFechaHoraAlta(fecha);
                        estadoProc.setFechaHoraUltimoEstado(fecha);
                        estadoProc.setEmpresa(usuario.getEmpresa());
                        estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                   //     estadoProc.setDocumentoJudicial(getSelected());
                        estadoProc.setVisible(true);
                        
                        estadosProcesalesDocumentosResolucionesController.setSelected(estadoProc);
                        estadosProcesalesDocumentosResolucionesController.saveNew(null);
                        
                        getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                        getSelected().setFechaHoraEstadoProcesal(fecha);
                        getSelected().setUsuarioEstadoProcesal(usuario);
                    }
                }
                
            }
            
            if (getSelected().verifObs() && guardar) {
                
                getSelected().transferirObs();
                
                if (getSelected().getUltimaObservacion() != null) {
                    if (!"".equals(getSelected().getUltimaObservacion())) {
                        
                        ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);
                        
                        obs.setUsuarioAlta(usuario);
                        obs.setUsuarioUltimoEstado(usuario);
                        obs.setFechaHoraAlta(fecha);
                        obs.setFechaHoraUltimoEstado(fecha);
                        obs.setEmpresa(usuario.getEmpresa());
                        obs.setObservacion(getSelected().getUltimaObservacion());
             //           obs.setDocumentosResoluciones(getSelected());
                        obs.setVisible(true);
                        
                        obsController.setSelected(obs);
                        obsController.saveNew(null);
                        
                        getSelected().setFechaUltimaObservacion(fecha);
                        getSelected().setObservacionDocumentoJudicial(obs);
                        getSelected().setUsuarioUltimaObservacion(usuario);
                    }
                }
            }
            /*} else {
                JsfUtil.addErrorMessage("Solo un funcionario de Secretaria puede editarlo");
            }
             */
            
            if (guardar) {
                super.save(null);
                //actualizarPersonas(fecha);
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
    }
    
    public void saveDpto() {
        
        if (getSelected() != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();
            
            Collection<DocumentosResoluciones> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findByEntradaDocumento", DocumentosResoluciones.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            Usuarios resp = getSelected().getResponsable();
            
            if (col != null) {
                for (DocumentosResoluciones doc : col) {
                    
                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setResponsable(resp);
                    doc.setDepartamento(resp.getDepartamento());
                    
                    setSelected(doc);
                    super.save(null);
                }
            }/*
            } else {
                JsfUtil.addErrorMessage("Solo el responsable del documento puede transferirlo");
            }*/
        }
    }
    
    public void verDoc() {
        
        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            // if (getSelected().getDocumentoEscaneado() != null) {
            try {
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                
                httpServletResponse.setContentType("application/pdf");
                // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");
                
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                // DocumentosEscaneados docEsc = ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();
                //  servletOutputStream.write(docEsc.getDocumento());
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
            /* } else {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }*/
        } else {
            JsfUtil.addErrorMessage("Debe seleccionar un documento");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
}
