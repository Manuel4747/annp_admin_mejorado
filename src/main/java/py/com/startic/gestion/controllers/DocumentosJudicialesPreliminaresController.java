package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosJudicialesPreliminares;
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
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosEscaneadosPreliminares;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudicialesPreliminares;
import py.com.startic.gestion.models.EstadosProcesoPreliminar;
import py.com.startic.gestion.models.ObservacionesDocumentosJudicialesPreliminares;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "documentosJudicialesPreliminaresController")
@ViewScoped
public class DocumentosJudicialesPreliminaresController extends AbstractController<DocumentosJudicialesPreliminares> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private ObservacionesDocumentosJudicialesPreliminaresController obsPreliminaresController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesPreliminaresController estadosProcesalesDocumentosJudicialesPreliminaresController;
    @Inject
    private DocumentosEscaneadosPreliminaresController documentosEscaneadosPreliminaresController;
    private String nuevaCausa;
    private String nombreJuez;
    private String nombreEstado;
    private String ultimaObservacion;
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

    private boolean busquedaPorFechaAlta;

    private UploadedFile file;

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

    public DocumentosJudicialesPreliminaresController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudicialesPreliminares.class);
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

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(5); // Secretaria
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2018);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();
        
        pantalla = Constantes.NO;

        buscarPorFechaAlta();
    }

    @Override
    public DocumentosJudicialesPreliminares prepareCreate(ActionEvent event) {
        DocumentosJudicialesPreliminares doc = super.prepareCreate(event);
        EstadosProcesoPreliminar est = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesoPreliminar.findByCodigo", EstadosProcesoPreliminar.class).setParameter("codigo", Constantes.ESTADO_PROCESO_PRELIMINAR_EN_TRAMITE).getSingleResult();
        doc.setMostrarWeb(Constantes.NO);
        
        doc.setEstadoProcesoPreliminar(est);
        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;

        return doc;
    }

    public void prepareEdit() {

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;

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

    public String navigateObservacionesDocumentosJudicialesPreliminaresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudicialesPreliminares/index");

        }
        return "/pages/observacionesDocumentosJudicialesPreliminares/index";
    }

    public String navigateEstadosProcesalesDocumentosJudicialesPreliminaresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosJudicialesPreliminares_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudicialesPreliminares.findByDocumentoJudicialPreliminar", EstadosProcesalesDocumentosJudicialesPreliminares.class).setParameter("documentoJudicialPreliminar", getSelected()).getResultList());
        }
        return "/pages/estadosProcesalesDocumentosJudicialesPreliminares/index";
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
    
    public String abrirCreate(String desde, String updateCerrar, String nombreVariable) {
        pantalla = Constantes.NO;
        if(Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)){
            pantalla = Constantes.SI;
            this.updateCerrar = updateCerrar;
            this.nombreVariable = nombreVariable;
        }
        
        prepareCreate(null);
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('DocumentosCreateDialog').show();");
        return "";
    }
    
    public String updateCreate() {
        if (Constantes.SI.equals(pantalla)) {
            return updateCerrar;
        } else {
            return "display,:DocumentosJudicialesListForm:datalist,:growl";
        }
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
    public Collection<DocumentosJudicialesPreliminares> getItems() {
        return super.getItems2();
    }
    
/*
    @Override
    public Collection<DocumentosJudiciales> getItems() {
        setItems(ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findAll", DocumentosJudiciales.class).getResultList());
        return getItems2();
    }
    */
    
    public boolean deshabilitarBorrar(){
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE)){
                    return false;
            }
        }
        
        return true;
    }
    
    public boolean deshabilitarEdit(){
        if(getSelected() != null){
            return false;
        }
        
        return true;
    }
    
    public boolean deshabilitarAdjuntarDocumento(){
        if(Constantes.SI.equals(pantalla)){
            return true;
        }
        
        return false;
    }
    
    public boolean deshabilitarMostrarWeb(){
        return false;
    }
    
    public boolean deshabilitarGuardarSinAjax(){
        if(Constantes.SI.equals(pantalla)){
            return true;
        }
        
        return false;
    }
    
    public boolean deshabilitarGuardarConAjax(){
        if(Constantes.SI.equals(pantalla)){
            return false;
        }
        
        return true;
    }

    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesPreliminares.findOrdered", DocumentosJudicialesPreliminares.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());
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
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesPreliminares.findOrderedFechaAlta", DocumentosJudicialesPreliminares.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());

        }
    }

    public void editarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosJudicialesPreliminares obs = getSelected().getObservacionDocumentoJudicialPreliminar();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsPreliminaresController.setSelected(obs);
                obsPreliminaresController.save(null);

                getSelected().setObservacionDocumentoJudicialPreliminar(obs);

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

                ObservacionesDocumentosJudicialesPreliminares obs = obsPreliminaresController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsPreliminaresController.setSelected(obs);
                obsPreliminaresController.saveNew(null);

                getSelected().setObservacionDocumentoJudicialPreliminar(obs);

                super.save(null);

                obs.setDocumentoJudicialPreliminar(getSelected());

                obsPreliminaresController.save(null);
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

                EstadosProcesalesDocumentosJudicialesPreliminares estadoProc = getSelected().getEstadoProcesalDocumentoJudicialPreliminar();

                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadosProcesalesDocumentosJudicialesPreliminaresController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesPreliminaresController.save(null);

                getSelected().setEstadoProcesalDocumentoJudicialPreliminar(estadoProc);

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

                EstadosProcesalesDocumentosJudicialesPreliminares estadoProc = estadosProcesalesDocumentosJudicialesPreliminaresController.prepareCreate(null);

                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEmpresa(usuario.getEmpresa());
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());
                estadoProc.setDocumentoJudicialPreliminar(getSelected());

                estadosProcesalesDocumentosJudicialesPreliminaresController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesPreliminaresController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoJudicialPreliminar(estadoProc);

                super.save(null);
            }

        }
    }

    @Override
    public void delete(ActionEvent event) {
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
    }

    public void saveNew() {
        if (getSelected() != null) {

            if (getSelected().getFechaPresentacion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_fechaPresentacion"));
                return;
            }

            if (getSelected().getCausa() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_nroCausa"));
                return;
            } else if ("".equals(getSelected().getCausa())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_nroCausa"));
                return;
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_caratula"));
                return;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_caratula"));
                return;
            }

            if (getSelected().getMotivoProceso() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_motivoProceso"));
                return;
            } else if ("".equals(getSelected().getMotivoProceso())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_motivoProceso"));
                return;
            }

            if (getSelected().getEstadoProcesal() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_estadoProcesal"));
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_estadoProcesal"));
                return;
            }

            if (getSelected().getPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesPreliminaresHelpText_persona"));
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFolios("");
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDescripcionMesaEntrada(getSelected().getCaratula());
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(usuario.getDepartamento());
            getSelected().setMostrarWeb("NO");

            if (file != null) {
                if (file.getContent().length > 0) {

                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }

                    // getSelected().setDocumento(bytes);
                    DocumentosEscaneadosPreliminares docEsc = null;

                    if (getSelected().getDocumentoEscaneadoPreliminar() == null) {
                        docEsc = documentosEscaneadosPreliminaresController.prepareCreate(null);

                        docEsc.setEmpresa(usuario.getEmpresa());
                        docEsc.setDocumento(bytes);

                        documentosEscaneadosPreliminaresController.setSelected(docEsc);
                        documentosEscaneadosPreliminaresController.saveNew(null);

                        getSelected().setDocumentoPreliminar(documentosEscaneadosPreliminaresController.getSelected().getId());

                    } else {
                        docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneadosPreliminares.findById", DocumentosEscaneadosPreliminares.class).setParameter("id", getSelected().getDocumentoEscaneadoPreliminar()).getSingleResult();
                        docEsc.setDocumento(bytes);
                        documentosEscaneadosPreliminaresController.setSelected(docEsc);
                        documentosEscaneadosPreliminaresController.save(null);
                    }

                }

            }
            
            super.saveNew(null);
            
            if(!this.isErrorPersistencia()){
                if(Constantes.SI.equals(pantalla)){
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreVariable, getSelected());
                }
            }

            if (getSelected().getUltimaObservacion() != null && !"".equals(getSelected().getUltimaObservacion())) {
                ObservacionesDocumentosJudicialesPreliminares obs = obsPreliminaresController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacion());
                obs.setDocumentoJudicialPreliminar(getSelected());

                obsPreliminaresController.setSelected(obs);
                obsPreliminaresController.saveNew(null);

                getSelected().setObservacionDocumentoJudicialPreliminar(obs);
                getSelected().setFechaUltimaObservacion(fecha);
                getSelected().setUsuarioUltimaObservacion(usuario);

                super.save(null);
                /*
                obs.setDocumentoJudicial(getSelected());

                obsController.save(null);
                 */
            }

            if (getSelected().getEstadoProcesal() != null && !"".equals(getSelected().getEstadoProcesal())) {
                EstadosProcesalesDocumentosJudicialesPreliminares estadoProc = estadosProcesalesDocumentosJudicialesPreliminaresController.prepareCreate(null);

                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEmpresa(usuario.getEmpresa());
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                estadoProc.setDocumentoJudicialPreliminar(getSelected());

                estadosProcesalesDocumentosJudicialesPreliminaresController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesPreliminaresController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoJudicialPreliminar(estadoProc);
                getSelected().setFechaHoraEstadoProcesal(fecha);
                getSelected().setUsuarioEstadoProcesal(usuario);

                super.save(null);
                /*
                obs.setDocumentoJudicial(getSelected());

                obsController.save(null);
                 */
            }

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
        }
    }

    public void save() {

        boolean guardar = true;

        if (getSelected() != null) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

                if (getSelected().getFechaPresentacion() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_fechaPresentacion"));
                    guardar = false;
                }

                if (getSelected().getCausa() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
                    guardar = false;
                } else if ("".equals(getSelected().getCausa())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
                    guardar = false;
                }

                if (getSelected().getCaratula() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
                    guardar = false;
                } else if ("".equals(getSelected().getCaratula())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
                    guardar = false;
                }

                if (getSelected().getMotivoProceso() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
                    guardar = false;
                } else if ("".equals(getSelected().getMotivoProceso())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
                    guardar = false;
                }

                if (getSelected().getEstadoProcesal() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                } else if ("".equals(getSelected().getEstadoProcesal())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                }

                if (getSelected().getPersona() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_persona"));
                    return;
                }

                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);

                if (file != null && guardar) {
                    if (file.getContent().length > 0) {

                        byte[] bytes = null;
                        try {
                            bytes = IOUtils.toByteArray(file.getInputStream());
                        } catch (IOException ex) {
                        }

                        // getSelected().setDocumento(bytes);
                        DocumentosEscaneadosPreliminares docEsc = null;

                        if (getSelected().getDocumentoEscaneadoPreliminar() == null) {
                            docEsc = documentosEscaneadosPreliminaresController.prepareCreate(null);

                            docEsc.setEmpresa(usuario.getEmpresa());
                            docEsc.setDocumento(bytes);

                            documentosEscaneadosPreliminaresController.setSelected(docEsc);
                            documentosEscaneadosPreliminaresController.saveNew(null);

                            getSelected().setDocumentoPreliminar(documentosEscaneadosPreliminaresController.getSelected().getId());

                        } else {
                            docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneadosPreliminares.findById", DocumentosEscaneadosPreliminares.class).setParameter("id", getSelected().getDocumentoEscaneadoPreliminar()).getSingleResult();
                            docEsc.setDocumento(bytes);
                            documentosEscaneadosPreliminaresController.setSelected(docEsc);
                            documentosEscaneadosPreliminaresController.save(null);
                        }

                    }
                }

                if (getSelected().verifEstadoProcesal() && guardar) {

                    getSelected().transferirEstadoProcesal();
                    EstadosProcesalesDocumentosJudicialesPreliminares estadoProc = estadosProcesalesDocumentosJudicialesPreliminaresController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                    estadoProc.setDocumentoJudicialPreliminar(getSelected());

                    estadosProcesalesDocumentosJudicialesPreliminaresController.setSelected(estadoProc);
                    estadosProcesalesDocumentosJudicialesPreliminaresController.saveNew(null);

                    getSelected().setEstadoProcesalDocumentoJudicialPreliminar(estadoProc);
                    getSelected().setFechaHoraEstadoProcesal(fecha);
                    getSelected().setUsuarioEstadoProcesal(usuario);

                }

                if (getSelected().verifObs() && guardar) {

                    getSelected().transferirObs();

                    ObservacionesDocumentosJudicialesPreliminares obs = obsPreliminaresController.prepareCreate(null);

                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fecha);
                    obs.setFechaHoraUltimoEstado(fecha);
                    obs.setEmpresa(usuario.getEmpresa());
                    obs.setObservacion(getSelected().getUltimaObservacion());
                    obs.setDocumentoJudicialPreliminar(getSelected());

                    obsPreliminaresController.setSelected(obs);
                    obsPreliminaresController.saveNew(null);

                    getSelected().setFechaUltimaObservacion(fecha);
                    getSelected().setObservacionDocumentoJudicialPreliminar(obs);
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
/*
    public void saveDpto() {

        if (getSelected() != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosJudicialesPreliminares> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesPreliminares.findByEntradaDocumento", DocumentosJudicialesPreliminares.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Usuarios resp = getSelected().getResponsable();

            if (col != null) {
                for (DocumentosJudiciales doc : col) {

                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setResponsable(resp);
                    doc.setDepartamento(resp.getDepartamento());

                    setSelected(doc);
                    super.save(null);
                }
            }
        }
    }
        */

    public void verDoc() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            if (getSelected().getDocumentoEscaneadoPreliminar() != null) {
                try {
                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.setContentType("application/pdf");
                    // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                    httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    DocumentosEscaneadosPreliminares docEsc = ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneadosPreliminares.findById", DocumentosEscaneadosPreliminares.class).setParameter("id", getSelected().getDocumentoEscaneadoPreliminar()).getSingleResult();

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
