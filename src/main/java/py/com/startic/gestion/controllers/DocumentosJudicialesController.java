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

import py.com.startic.gestion.models.DocumentosJudiciales;
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
import py.com.startic.gestion.models.DocumentosEscaneados;
import py.com.startic.gestion.models.EntradasDocumentosJudiciales;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosProceso;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.ObservacionesDocumentosJudicialesAntecedentes;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.PersonasPorDocumentosJudiciales;
import py.com.startic.gestion.models.ResuelvePorResolucionesPorPersonas;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposExpediente;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "documentosJudicialesController")
@ViewScoped
public class DocumentosJudicialesController extends AbstractController<DocumentosJudiciales> {

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
    private ObservacionesDocumentosJudicialesAntecedentesController obsAntecedenteController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;
    @Inject
    private EntradasDocumentosJudicialesController entradasDocumentosJudicialesController;
    @Inject
    private DocumentosEscaneadosController documentosEscaneadosController;
    @Inject
    private PersonasPorDocumentosJudicialesController personasPorDocumentosJudicialesController;
    @Inject
    private ResuelvePorResolucionesPorPersonasController resuelvePorResolucionesPorPersonasController;
    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private String nuevaCausa;
    private String nombreJuez;
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
    private List<TiposExpediente> listaTiposExpediente;
    private List<Personas> listaPersonas;
    private Personas personaSelected;
    private Personas persona;
    private String caratula;
    private String caratulaAnterior;
    private EstadosProceso estadoProceso;
    private EstadosProceso estadoProcesoAnterior;
    private String causa;
    private TiposExpediente tipoExpedienteActual;
    private TiposExpediente tipoExpediente;
    private TiposExpediente tipoExpedienteAnterior;
    private boolean cambioAEnjuiciamiento;

    private boolean busquedaPorFechaAlta;

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
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

    public TiposExpediente getTipoExpedienteActual() {
        return tipoExpedienteActual;
    }

    public void setTipoExpedienteActual(TiposExpediente tipoExpedienteActual) {
        this.tipoExpedienteActual = tipoExpedienteActual;
    }

    public TiposExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TiposExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    public TiposExpediente getTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(TiposExpediente tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
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

    public DocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
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

    public List<TiposExpediente> getListaTiposExpediente() {
        return listaTiposExpediente;
    }

    public void setListaTiposExpediente(List<TiposExpediente> listaTiposExpediente) {
        this.listaTiposExpediente = listaTiposExpediente;
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
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);
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
    public DocumentosJudiciales prepareCreate(ActionEvent event) {
        return prepararCreate(null);
    }
        
    public DocumentosJudiciales prepararCreate(String caratula) {
        DocumentosJudiciales doc = super.prepareCreate(null);

        entradaDocumentoJudicial = entradasDocumentosJudicialesController.prepareCreate(null);
        entradaDocumentoJudicial.setRecibidoPor(usuario);
        EstadosProceso est = null;

        if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA)) {
            est = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getSingleResult();
        } else {
            est = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_EN_TRAMITE).getSingleResult();
        }

        doc.setMostrarWeb(Constantes.NO);

        doc.setEstadoProceso(est);
        doc.setEstadoProcesoAnterior(est);
        doc.setCaratula(caratula);

        listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findAll", TiposExpediente.class).getResultList();

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;
        listaPersonas = new ArrayList<>();
        persona = null;

        return doc;
    }

    public void prepareEdit() {

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        persona = null;
        file = null;
        cambioAEnjuiciamiento = false;

        listaPersonas = new ArrayList<>();

        if (getSelected() != null) {
            tipoExpediente = getSelected().getTipoExpediente();
            tipoExpedienteAnterior = getSelected().getTipoExpedienteAnterior();
            tipoExpedienteActual = getSelected().getTipoExpediente();
            estadoProceso = getSelected().getEstadoProceso();
            estadoProcesoAnterior = getSelected().getEstadoProcesoAnterior();
            caratula = getSelected().getCaratula();
            caratulaAnterior = getSelected().getCaratulaAnterior();
            
            causa = getSelected().getCausa();
            listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findAll", TiposExpediente.class).getResultList();
            /*
            if (getSelected().getTipoExpediente() != null) {
                if (Constantes.TIPO_EXPEDIENTE_ENJUICIAMIENTO == getSelected().getTipoExpediente().getId()) {
                    listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findAll", TiposExpediente.class).getResultList();
                } else {
                    listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findByMostrarMenu", TiposExpediente.class).setParameter("mostrarMenu", true).getResultList();
                }
            } else {
                listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findByMostrarMenu", TiposExpediente.class).setParameter("mostrarMenu", true).getResultList();
            }
            */

            Personas per = null;

            List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();
            for (int i = 0; i < listaPersonasActual.size(); i++) {
                per = listaPersonasActual.get(i).getPersona();
                per.setEstado(listaPersonasActual.get(i).getEstado().getCodigo());
                per.setTipoExpedienteAnterior(listaPersonasActual.get(i).isTipoExpedienteAnterior());
                listaPersonas.add(per);
            }
        } else {
            listaTiposExpediente = null;
        }

    }

    public String obtenerPersonas(DocumentosJudiciales doc) {
        Personas per = null;

        String respuesta = "";

        if (doc != null) {
            try {
                List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                for (int i = 0; i < listaPersonasActual.size(); i++) {
                    per = listaPersonasActual.get(i).getPersona();
                    if ("".equals(respuesta)) {
                        respuesta = per.getNombresApellidos();
                    } else {
                        respuesta += ", " + per.getNombresApellidos();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return respuesta;
    }

    public void verificarResolucionesPersona(Personas personaActual) {
        if (getSelected() != null) {

            List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByDocumentoJudicialPersonaEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("documentoJudicial", getSelected()).setParameter("persona", personaActual).setParameter("estado", new Estados("AC")).getResultList();
            boolean encontro = false;
            for (ResuelvePorResolucionesPorPersonas res : lista) {
                encontro = true;
                break;
            }

            if (encontro) {
                personaSelected = personaActual;
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosJudicialesAvisoDialog').show();");
            } else {

                borrarPersona(personaActual);
            }

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

    public String navigateObservacionesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");

        }
        return "/pages/observacionesDocumentosJudiciales/index";
    }

    public String navigateObservacionesDocumentosJudicialesAntecedentesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");

        }
        return "/pages/observacionesDocumentosJudicialesAntecedentes/index";
    }

    public String navigateEstadosProcesalesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");
        }
        return "/pages/estadosProcesalesDocumentosJudiciales/index";
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
                return "display,:DocumentosJudicialesListForm:datalist,:growl";
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
    public Collection<DocumentosJudiciales> getItems() {
        return super.getItems2();
    }

    /*
    @Override
    public Collection<DocumentosJudiciales> getItems() {
        setItems(ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findAll", DocumentosJudiciales.class).getResultList());
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
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrdered", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
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
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());

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

    public void editarObsAntecedente() {

        if (getSelected() != null) {
            if (getSelected().verifObsAntecedente()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObsAntecedente();

                getSelected().setUsuarioUltimaObservacionAntecedente(usuario);

                ObservacionesDocumentosJudicialesAntecedentes obs = getSelected().getObservacionDocumentoJudicialAntecedente();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAntecedenteAux());

                obsAntecedenteController.setSelected(obs);
                obsAntecedenteController.save(null);

                getSelected().setObservacionDocumentoJudicialAntecedente(obs);

                super.save(null);
            }

        }
    }

    public void agregarObsAntecedente() {

        if (getSelected() != null) {
            if (getSelected().verifObsAntecedente()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacionAntecedente(fecha);

                getSelected().transferirObsAntecedente();

                getSelected().setUsuarioUltimaObservacionAntecedente(usuario);

                ObservacionesDocumentosJudicialesAntecedentes obs = obsAntecedenteController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAntecedenteAux());

                obsAntecedenteController.setSelected(obs);
                obsAntecedenteController.saveNew(null);

                getSelected().setObservacionDocumentoJudicialAntecedente(obs);

                super.save(null);

                obs.setDocumentoJudicial(getSelected());

                obsAntecedenteController.save(null);
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
    
    public void actualizarDatosAnteriores(){
        if(tipoExpedienteActual != null ){
            if(tipoExpediente != null){
                if(tipoExpediente.getId().equals(Constantes.TIPO_EXPEDIENTE_ENJUICIAMIENTO)){
                    if(!tipoExpedienteActual.getId().equals(tipoExpediente)){
                        tipoExpedienteAnterior = tipoExpedienteActual;
                        caratulaAnterior = caratula;
                        //caratula = "";
                        estadoProcesoAnterior = estadoProceso;
                        cambioAEnjuiciamiento = true;
                    }
                }else if(cambioAEnjuiciamiento){
                    cambioAEnjuiciamiento = false;
                    tipoExpedienteAnterior = null;
                    caratula = caratulaAnterior;
                    caratulaAnterior = "";
                    estadoProcesoAnterior = null;
                }
            }
            
        }
    }

    @Override
    public void delete(ActionEvent event) {
        if (getSelected().getCanalEntradaDocumentoJudicial().equals(canal)) {
            // if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId()) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {
            if (getSelected().getDepartamento().getId().equals(usuario.getDepartamento().getId()) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {
                Date fecha = ejbFacade.getSystemDateOnly();
                if (getSelected().getFechaHoraAlta().after(fecha) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {

                    List<PersonasPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

                    for (PersonasPorDocumentosJudiciales per : lista) {
                        personasPorDocumentosJudicialesController.setSelected(per);
                        per.setFechaHoraUltimoEstado(fecha);
                        per.setUsuarioUltimoEstado(usuario);
                        personasPorDocumentosJudicialesController.save(event);
                        personasPorDocumentosJudicialesController.delete(null);
                    }

                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usuario);
                    super.save(event);
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
                    JsfUtil.addErrorMessage("Solo se pueden borrar expedientes creados en el dia");
                }
            } else {
                JsfUtil.addErrorMessage("Solo un funcionario de Secretaria puede borrarlo");
            }
        } else {
            JsfUtil.addErrorMessage("Solo puede borrar documentos que fueron ingresados directamente en Secretaria");
        }
    }

    private void actualizarPersonas(Date fecha) {
        Personas per2 = null;
        PersonasPorDocumentosJudiciales perDocActual = null;
        boolean encontro = false;
        List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();
        for (Personas per : listaPersonas) {
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaPersonasActual.size(); i++) {
                per2 = listaPersonasActual.get(i).getPersona();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaPersonasActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                PersonasPorDocumentosJudiciales perDoc = personasPorDocumentosJudicialesController.prepareCreate(null);
                perDoc.setPersona(per);
                perDoc.setDocumentosJudiciales(getSelected());
                perDoc.setEmpresa(usuario.getEmpresa());
                perDoc.setFechaHoraAlta(fecha);
                perDoc.setFechaHoraUltimoEstado(fecha);
                perDoc.setUsuarioAlta(usuario);
                perDoc.setUsuarioUltimoEstado(usuario);
                perDoc.setEstado(new Estados("AC"));
                perDoc.setTipoExpedienteAnterior(per.isTipoExpedienteAnterior());
                personasPorDocumentosJudicialesController.setSelected(perDoc);
                personasPorDocumentosJudicialesController.saveNew(null);
            } else {
                // if ("IN".equals(perDocActual.getEstado().getCodigo())) {
                perDocActual.setEstado(new Estados(per.getEstado()));
                perDocActual.setFechaHoraAlta(fecha);
                perDocActual.setUsuarioAlta(usuario);
                perDocActual.setTipoExpedienteAnterior(per.isTipoExpedienteAnterior());
                personasPorDocumentosJudicialesController.setSelected(perDocActual);
                personasPorDocumentosJudicialesController.save(null);
                // }

                if ("IN".equals(per.getEstado())) {
                    List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByDocumentoJudicialPersonaEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("documentoJudicial", getSelected()).setParameter("persona", per).setParameter("estado", new Estados("AC")).getResultList();

                    for (ResuelvePorResolucionesPorPersonas res : lista) {
                        res.setEstado(new Estados("IN"));
                        res.setFechaHoraUltimoEstado(fecha);
                        res.setUsuarioUltimoEstado(usuario);
                        resuelvePorResolucionesPorPersonasController.setSelected(res);
                        resuelvePorResolucionesPorPersonasController.save(null);
                    }
                }
            }
        }

        for (int i = 0; i < listaPersonasActual.size(); i++) {
            per2 = listaPersonasActual.get(i).getPersona();
            encontro = false;
            for (Personas per : listaPersonas) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaPersonasActual.get(i).setEstado(new Estados("IN"));
                listaPersonasActual.get(i).setFechaHoraAlta(fecha);
                listaPersonasActual.get(i).setUsuarioAlta(usuario);
                listaPersonasActual.get(i).setTipoExpedienteAnterior(per2.isTipoExpedienteAnterior());
                personasPorDocumentosJudicialesController.setSelected(listaPersonasActual.get(i));
                personasPorDocumentosJudicialesController.save(null);

                List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByDocumentoJudicialPersonaEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("documentoJudicial", getSelected()).setParameter("persona", listaPersonasActual.get(i)).setParameter("estado", new Estados("AC")).getResultList();

                for (ResuelvePorResolucionesPorPersonas res : lista) {
                    res.setEstado(new Estados("IN"));
                    res.setFechaHoraUltimoEstado(fecha);
                    res.setUsuarioUltimoEstado(usuario);
                    resuelvePorResolucionesPorPersonasController.setSelected(res);
                    resuelvePorResolucionesPorPersonasController.save(null);
                }
            }
        }
    }

    public boolean validarSaveNew() {
        if (getSelected() != null) {
            /*
            if (getSelected().getFechaPresentacion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_fechaPresentacion"));
                return;
            }
             */

            if (getSelected().getTipoExpediente() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_tipoExpediente"));
                return false;
            }

            if (getSelected().getCausa() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                return false;
            } else if ("".equals(getSelected().getCausa())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                return false;
            } else {
                List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", getSelected().getCausa()).getResultList();
                if (!listaDocs.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                    return false;
                }
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                return false;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                return false;
            }
            /*
            if (getSelected().getMotivoProceso() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_motivoProceso"));
                return;
            } else if ("".equals(getSelected().getMotivoProceso())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_motivoProceso"));
                return;
            }
             */
 /*
            if (getSelected().getEstadoProcesal() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_estadoProcesal"));
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_estadoProcesal"));
                return;
            }
             */

            if(Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR != getSelected().getTipoExpediente().getId()){
                if (listaPersonas == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_persona"));
                    return false;
                } else if (listaPersonas.size() < 1) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_persona"));
                    return false;
                }
            }

            //PrimeFaces current = PrimeFaces.current();
            //current.executeScript("alert('hola'); $('#DocumentosCreateForm:botonGuardar').click();");
            return true;
        }

        return false;
    }

    public void saveNew() {
        if (getSelected() != null) {

            if (!validarSaveNew()) {
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
            //getSelected().setUltimaObservacion(ultimaObservacion);
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

                    // getSelected().setDocumento(bytes);
                    DocumentosEscaneados docEsc = null;

                    if (getSelected().getDocumentoEscaneado() == null) {
                        docEsc = documentosEscaneadosController.prepareCreate(null);

                        docEsc.setEmpresa(usuario.getEmpresa());
                        docEsc.setDocumento(bytes);

                        documentosEscaneadosController.setSelected(docEsc);
                        documentosEscaneadosController.saveNew(null);

                        getSelected().setDocumento(documentosEscaneadosController.getSelected().getId());

                    } else {
                        docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();
                        docEsc.setDocumento(bytes);
                        documentosEscaneadosController.setSelected(docEsc);
                        documentosEscaneadosController.save(null);
                    }

                }

            }

            EntradasDocumentosJudiciales doc = entradasDocumentosJudicialesController.prepareCreate(null);

            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(usuario);
            doc.setFechaHoraAlta(fecha);
            doc.setUsuarioAlta(usuario);
            doc.setEmpresa(usuario.getEmpresa());
            doc.setRecibidoPor(usuario);
            doc.setEntregadoPor(entradaDocumentoJudicial.getEntregadoPor());
            doc.setTelefono(entradaDocumentoJudicial.getTelefono());
            doc.setNroCedulaRuc(entradaDocumentoJudicial.getNroCedulaRuc());
            jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(substring(nro_mesa_entrada,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_judiciales WHERE substring(nro_mesa_entrada,1,4) = 'AUTO';", NroMesaEntrada.class);

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            doc.setNroMesaEntrada("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosJudicialesController.setSelected(doc);
            //entradasDocumentosJudicialesController.saveNew(null);
            getSelected().setEntradaDocumento(doc);

            super.saveNew(null);

            if (!this.isErrorPersistencia()) {

                for (Personas per : listaPersonas) {
                    if(Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())){
                        PersonasPorDocumentosJudiciales perDoc = personasPorDocumentosJudicialesController.prepareCreate(null);
                        perDoc.setPersona(per);
                        perDoc.setDocumentosJudiciales(getSelected());
                        perDoc.setEmpresa(usuario.getEmpresa());
                        perDoc.setFechaHoraAlta(fecha);
                        perDoc.setFechaHoraUltimoEstado(fecha);
                        perDoc.setUsuarioAlta(usuario);
                        perDoc.setUsuarioUltimoEstado(usuario);
                        perDoc.setTipoExpedienteAnterior(per.isTipoExpedienteAnterior());
                        perDoc.setEstado(new Estados("AC"));
                        personasPorDocumentosJudicialesController.setSelected(perDoc);
                        personasPorDocumentosJudicialesController.saveNew(null);
                    }
                }

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
                    obs.setDocumentoJudicial(getSelected());
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
                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                    estadoProc.setDocumentoJudicial(getSelected());
                    estadoProc.setVisible(true);
                    estadoProc.setFechaHoraVisible(fecha);

                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                    estadosProcesalesDocumentosJudicialesController.saveNew(null);

                    getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                    getSelected().setFechaHoraEstadoProcesal(fecha);
                    getSelected().setUsuarioEstadoProcesal(usuario);

                    super.save(null);
                    /*
                    obs.setDocumentoJudicial(getSelected());

                    obsController.save(null);
                     */
                }
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
            // Parte solo aplicable cuando la pantalla Create Comprobante Egreso es llamada desde la pantalla Edit Viajes
            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            ResolucionesController resolucionController = context.getApplication().evaluateExpressionGet(context, "#{ResolucionesController}", ResolucionesController.class);
            /*ValueBinding binding = application.createValueBinding("#{ResolucionesController}");
            ResolucionesController resolucionController = (ResolucionesController) binding.getValue(context);*/
            if (resolucionController != null) {
                resolucionController.actualizarDatosDocumentoJudicialEnCreate(getSelected());
            }
        }
    }

    public boolean validarSave() {
        if (getSelected().getFechaPresentacion() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_fechaPresentacion"));
            return false;
        }

        // if (getSelected().getTipoExpediente() == null) {
        if (tipoExpediente == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_tipoExpediente"));
            return false;
        }

        if (getSelected().getCausa() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
            return false;
        } else if ("".equals(getSelected().getCausa())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
            return false;
        } else {
            if (causa != null) {
                if (!causa.equals(getSelected().getCausa())) {
                    List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", getSelected().getCausa()).getResultList();
                    if (!listaDocs.isEmpty()) {
                        JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                        return false;
                    }
                }
            }
        }

        // if (getSelected().getCaratula() == null) {
        if (caratula == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
            return false;
        } else if ("".equals(caratula)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
            return false;
        }

        if (getSelected().getMotivoProceso() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
            return false;
        } else if ("".equals(getSelected().getMotivoProceso())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
            return false;
        }
        /*
                if (getSelected().getEstadoProcesal() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                } else if ("".equals(getSelected().getEstadoProcesal())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                }
         */
 /*
                if (getSelected().getPersona() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_persona"));
                    return;
                }
         */

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
                getSelected().setCaratula(caratula);
                getSelected().setCaratulaAnterior(caratulaAnterior);
                getSelected().setEstadoProceso(estadoProceso);
                getSelected().setEstadoProcesoAnterior(estadoProcesoAnterior);
                getSelected().setTipoExpediente(tipoExpediente);
                getSelected().setTipoExpedienteAnterior(tipoExpedienteAnterior);

                if (file != null && guardar) {
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

                        } else {
                            docEsc = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();
                            docEsc.setDocumento(bytes);
                            documentosEscaneadosController.setSelected(docEsc);
                            documentosEscaneadosController.save(null);
                        }

                    }
                }

                if (getSelected().verifEstadoProcesal() && guardar) {

                    getSelected().transferirEstadoProcesal();

                    if (getSelected().getEstadoProcesal() != null) {
                        if (!"".equals(getSelected().getEstadoProcesal())) {

                            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                            estadoProc.setUsuarioAlta(usuario);
                            estadoProc.setUsuarioUltimoEstado(usuario);
                            estadoProc.setFechaHoraAlta(fecha);
                            estadoProc.setFechaHoraUltimoEstado(fecha);
                            estadoProc.setEmpresa(usuario.getEmpresa());
                            estadoProc.setEstadoProcesal(getSelected().getEstadoProcesal());
                            estadoProc.setDocumentoJudicial(getSelected());
                            estadoProc.setVisible(true);

                            estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                            estadosProcesalesDocumentosJudicialesController.saveNew(null);

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
                            obs.setDocumentoJudicial(getSelected());
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
                actualizarPersonas(fecha);
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

            Collection<DocumentosJudiciales> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
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
            }/*
            } else {
                JsfUtil.addErrorMessage("Solo el responsable del documento puede transferirlo");
            }*/
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

                    DocumentosEscaneados docEsc = ejbFacade.getEntityManager().createNamedQuery("DocumentosEscaneados.findById", DocumentosEscaneados.class).setParameter("id", getSelected().getDocumentoEscaneado()).getSingleResult();

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
