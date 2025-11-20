package py.com.startic.gestion.controllers;

//import static ch.qos.logback.core.FileAppender.DEFAULT_BUFFER_SIZE;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import py.com.startic.gestion.models.Actividades;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
//import static org.primefaces.behavior.validate.ClientValidator.PropertyKeys.event;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepAvanceAcciones;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.CambiosValor;
import py.com.startic.gestion.models.DatosHistorico;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.EstadosActividad;
import py.com.startic.gestion.models.EstadosInforme;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ObservacionesDocumentosPlanificacion;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.PlanEstrategicas;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "detallesPlanEstrategicasController")
@ViewScoped
public class DetallesPlanEstrategicasController extends AbstractController<DetallesPlanEstrategicas> {

    @Inject
    private PlanEstrategicasController planEstrategicasController;
    @Inject
    private CambiosValorController cambiosValorController;
    @Inject
    private ArchivosController archivosController;
    @Inject
    private ObservacionesDocumentosPlanificacionController obsController;
    @Inject
    private ProgramacionController programacionController;
    @Inject
    private DetallesPlanEstrategicasController detallesPlanEstrategicasController;
    @Inject
    private DatosHistoricoController datosHistoricoController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<Acciones> listaAcciones;
    private TiposObjetivos tiposObjetivos;
    private Acciones accion;
    private UploadedFile file;
    private String descripcionArchivo;
    private Usuarios usuario;
    List<Archivos> listaArchivos;
    List<DetallesPlanEstrategicas> selectedDetalles;
    private Integer activeTab;
    private HttpSession session;
    private DetallesPlanEstrategicas detallesPlanEstrategica;
    private PlanEstrategicas detallesSelected;
    private Programacion programacionSelected;
    private Programacion programacion;
    private FlujosDocumento flujoDoc;
    private TiposDocumentosJudiciales tipoDoc;
    private EstadosInforme estInforme;
    private Collection<Usuarios> listaUsuarios;
    private Collection<Archivos> detalles;
    private String ultimaObservacion;
    private Archivos docImprimir;
    private String nombre;
    private String content;
    private String url;
    private ParametrosSistema par;
    private double resultado;
    private double variable2;
    private double variable;
    private double valor;
    private double valorVariable;
    private double programaPresupuestario;
    private double presupuesto;
    private double resultadoPresupuesto;
    private double nuevaVariable;
    private EstadosActividad estadoActividad;
    private List<CambiosValor> detallesCambios;
    private Date fechaDesde;
    private Date fechaHasta;
    private String menu;
    private String endpoint;
    private Collection<DetallesPlanEstrategicas> filtrado;
    private List<UploadedFile> listaFiles;
    private List<UploadedFile> listaFilesEdit;
    private int anexoIndex;
    private String operacion = "";
    private Archivos archivo;
    private Date fechaEjecucion;
    private List<Actividades> actividades;
    private Actividades actividadSeleccionada;
    private String titulo = "";

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        detallesPlanEstrategica = prepareCreate(null);
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo("MC");
        tipoDoc.setCodigo("MI");
        tipoDoc.setCodigo("TC");
        detallesSelected = null;
        selectedDetalles = null;
        usuario = null;

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        activeTab = 0;
        menu = Constantes.NO;
        setItems(ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findOrdered", DetallesPlanEstrategicas.class).setParameter("departamento", usuario.getDepartamento()).setParameter("responsable", usuario).getResultList());
        if (!getItems2().isEmpty()) {
            DetallesPlanEstrategicas art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }
    }

    @Override
    public DetallesPlanEstrategicas prepareCreate(ActionEvent event) {
        programacionSelected = null;
        valorVariable = 0;
        //activeTab = 0;
        return super.prepareCreate(event);

    }

    public void prepareEdit() {
        ultimaObservacion = null;
        file = null;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public List<UploadedFile> getListaFiles() {
        return listaFiles;
    }

    public void setListaFiles(List<UploadedFile> listaFiles) {
        this.listaFiles = listaFiles;
    }

    public List<UploadedFile> getListaFilesEdit() {
        return listaFilesEdit;
    }

    public void setListaFilesEdit(List<UploadedFile> listaFilesEdit) {
        this.listaFilesEdit = listaFilesEdit;
    }

    public int getAnexoIndex() {
        return anexoIndex;
    }

    public void setAnexoIndex(int anexoIndex) {
        this.anexoIndex = anexoIndex;
    }

    public Archivos getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivos archivo) {
        this.archivo = archivo;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public List<Actividades> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividades> actividades) {
        this.actividades = actividades;
    }

    public Actividades getActividadSeleccionada() {
        return actividadSeleccionada;
    }

    public void setActividadSeleccionada(Actividades actividadSeleccionada) {
        this.actividadSeleccionada = actividadSeleccionada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TiposObjetivos getTiposObjetivos() {
        if (session.getAttribute("tipoSelected") != null) {
            tiposObjetivos = (TiposObjetivos) session.getAttribute("tipoSelected");
            session.removeAttribute("tipoSelected");
        }
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public DetallesPlanEstrategicasController() {
        // Inform the Abstract parent controller of the concrete Actividades Entity
        super(DetallesPlanEstrategicas.class);
        seleccionarCambiosValor();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void prepareTransferir() {
        // listaUsuarios = prepareTransferir(getSelected());
    }

    public boolean deshabilitarVerObs() {
        return deshabilitarCamposAdm();
    }

    private boolean deshabilitarCamposAdm() {
        return filtroURL.verifPermiso("verTodosDocsAdm");
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public List<Archivos> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(List<Archivos> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public DetallesPlanEstrategicas getDetallesPlanEstrategica() {
        return detallesPlanEstrategica;
    }

    public void setDetallesPlanEstrategica(DetallesPlanEstrategicas detallesPlanEstrategica) {
        this.detallesPlanEstrategica = detallesPlanEstrategica;
    }

    public List<CambiosValor> getDetallesCambios() {
        return detallesCambios;
    }

    public void setDetallesCambios(List<CambiosValor> detallesCambios) {
        this.detallesCambios = detallesCambios;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public TiposDocumentosJudiciales getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TiposDocumentosJudiciales tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public EstadosInforme getEstInforme() {
        return estInforme;
    }

    public void setEstInforme(EstadosInforme estInforme) {
        this.estInforme = estInforme;
    }

    public Collection<Archivos> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<Archivos> detalles) {
        this.detalles = detalles;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Archivos getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(Archivos docImprimir) {
        this.docImprimir = docImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public double getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(double programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getVariable() {
        return variable;
    }

    public void setVariable(double variable) {
        this.variable = variable;
    }

    public double getResultadoPresupuesto() {
        return resultadoPresupuesto;
    }

    public void setResultadoPresupuesto(double resultadoPresupuesto) {
        this.resultadoPresupuesto = resultadoPresupuesto;
    }

    public void prepareVerDoc(Archivos doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public EstadosActividad getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(EstadosActividad estadoActividad) {
        this.estadoActividad = estadoActividad;
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

    public double getNuevaVariable() {
        return nuevaVariable;
    }

    public void setNuevaVariable(double nuevaVariable) {
        this.nuevaVariable = nuevaVariable;
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public PlanEstrategicas getDetallesSelected() {
        return detallesSelected;
    }

    public void setDetallesSelected(PlanEstrategicas detallesSelected) {
        this.detallesSelected = detallesSelected;
    }

    public List<DetallesPlanEstrategicas> getSelectedDetalles() {
        return selectedDetalles;
    }

    public void setSelectedDetalles(List<DetallesPlanEstrategicas> selectedDetalles) {
        this.selectedDetalles = selectedDetalles;
    }

    public Programacion getProgramacionSelected() {
        return programacionSelected;
    }

    public void setProgramacionSelected(Programacion programacionSelected) {
        this.programacionSelected = programacionSelected;
    }

    public boolean deshabilitarAgregarObs() {
        return deshabilitarCamposAdm();
    }

    public boolean renderedBorrarArchivo() {
        return filtroURL.verifPermiso("borrarArchivo");
    }

    public Collection<DetallesPlanEstrategicas> getFiltrado() {
        return filtrado;
    }

    public void setFiltrado(Collection<DetallesPlanEstrategicas> filtrado) {
        this.filtrado = filtrado;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    @Override
    public Collection<DetallesPlanEstrategicas> getItems() {
        return super.getItems2();
    }

    public Collection<Usuarios> getListaUsuarios() {
        if (getSelected() != null && getSelected().getEstado() != null && Constantes.ASIGNADO.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected().getTipoDocumentos() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDpto2", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }

        } else {
            if (getSelected().getTipoDocumentos() != null && getSelected().getEstado() != null) {
                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }
        }
        return listaUsuarios;

    }

    public boolean desabilitarBotonAdjuntarArchivos() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("7")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", -18).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public void handleFileUpload(FileUploadEvent event) {

        if (listaFiles == null) {
            listaFiles = new ArrayList<>();
        }
        listaFiles.add(event.getFile());

    }

    public void handleFileUploadEdit(FileUploadEvent event) {

        if (listaFilesEdit == null) {
            listaFilesEdit = new ArrayList<>();
        }
        listaFilesEdit.add(event.getFile());

    }

    public void borrarFile(UploadedFile item) {
        if (listaFiles != null) {
            listaFiles.remove(item);
        }
    }

    public void borrarFileEdit(UploadedFile item) {
        if (listaFilesEdit != null) {
            listaFilesEdit.remove(item);
        }
    }

    public boolean desabilitarBotonSinVariables() {

        if (getSelected() != null) {
            try {
                DetallesPlanEstrategicas sel = getSelected();
                boolean habilitar = sel.getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_2) && sel.getResponsable().equals(usuario);

                return !habilitar;

            } catch (Exception e) {

            }
        }
        return true;
    }

    public String rowClass(DetallesPlanEstrategicas item) {
        return (item.getResponsable().equals(usuario)) ? ((item.getTipoDocumentos().getCodigo().equals("MI")) ? "white" : "green") : "";
    }

    public boolean deshabilitarBorrarArchivo(Archivos item) {
        if (filtroURL.verifPermiso("borrarArchivo")) {
            if (item != null) {
                if (getSelected() != null) {
                    if (getSelected().getResponsable().equals(usuario)) {
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
                    if (getSelected().getResponsable().equals(usuario)) {
                        if (item.getUsuarioAlta() != null) {
                            if (item.getUsuarioAlta().equals(usuario)) {

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

    public boolean desabilitarBotonCalcular() {
        if (getSelected() != null) {
            if (!getSelected().getEstado().getCodigo().equals("5")) {
                return true;
            }

            if (!getSelected().getResponsable().equals(usuario)) {
                return true;
            }

            try {
                this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", 32).getSingleResult();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public void setListaUsuarios(Collection<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void cambiarEstado() {

        if (getSelected().getEstado() != null) {

            Date fecha = ejbFacade.getSystemDate();

            EstadosInforme estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class
                ).setParameter("codigo", getSelected().getEstado().getCodigo()).getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado");
                return;
            }
            if (estado.getCodigo().equals("1")) {
                getSelected().setEstado(estado);
                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setResponsable(usuario);
                getSelected().setUsuarioUltimoEstado(usuario);

            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

        }
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public void prepareObs() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setUltimaObservacionAux(null);
            getSelected().setUltimaObservacion(null);
        }
    }

    public String navigateObservacionesDocumentosPlanificacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            //FacesContext
            //        .getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class
            //        ).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/accionOperativa/index");
        }
        return "/pages/observacionesDocumentosPlanificacion/index";
    }

    public void agregarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosPlanificacion obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoPlanificacion(obs);

                super.save(null);
                obs.setDetallePlanEstrategica(getSelected());

                obsController.save(null);
            }
            saveFaltaInformacion();

        }
    }

    public void saveFaltaInformacion() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();

            EstadosInforme estado = null;
            String stadoStr = getSelected().getEstado().getCodigo();
            //VERIFICADO POR EL DIRECTOR Y EN PLANIFICACION
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_4) || (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_9))) {
                stadoStr = Constantes.ESTADOS_INFORME_11;
            }
            //VERIFICADO EN PLANIFICACION
            /*if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_9)) {
                stadoStr = Constantes.ESTADOS_INFORME_11;
            }*/

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class)
                        .setParameter("codigo", stadoStr)
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);

            super.save(null);

        }
    }

    public void saveCambioEstados() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();

            EstadosInforme estado = null;
            String stadoStr = getSelected().getEstado().getCodigo();
            //VERIFICADO POR EL DIRECTOR
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_4)) {
                stadoStr = Constantes.ESTADOS_INFORME_8;
            }
            //VERIFICADO EN PLANIFICACION
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_9)) {
                stadoStr = Constantes.ESTADOS_INFORME_10;
            }
            //APROBADO POR EL DIRECTOR DE PLANIFICACIÓN
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_5)) {
                stadoStr = Constantes.ESTADOS_INFORME_6;
            }
            //CARGAR INFORME
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_6)) {
                stadoStr = Constantes.ESTADOS_INFORME_1;
                getSelected().setResponsable(usuario);
            }
            //CARGAR INFORME
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_2)) {
                stadoStr = Constantes.ESTADOS_INFORME_3;
            }

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class)
                        .setParameter("codigo", stadoStr)
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);

            super.save(null);

        }
    }

    public void guardarCambio() {

        if (getSelected() != null) {
            DatosHistorico datos = datosHistoricoController.prepareCreate(null);
            //if (getSelected().getVariable2() != 0) {
            valorVariable = getSelected().getPlanEstrategica().getProgramacion().getValorVariable();
            variable2 = getSelected().getVariable2();
            variable = (valor + variable2);
            // resultado = (variable / valorVariable) * 100;
            resultado = ((valor + variable2) / valorVariable) * 100;
            if(valorVariable == 0){
             getSelected().setResultado(0); 
            }
            getSelected().setValor(valor);
            // getSelected().setValorVariable(valorVariable);
            getSelected().setVariable2(variable);
            getSelected().setValorVariable(valorVariable);
            getSelected().setResultado(Math.round(resultado));
            //getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);
            

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }

            plan.setValor(valor);
            // plan.setValorVariable(Long.MAX_VALUE);
            plan.setVariable2(variable);
            plan.setResultado(Math.round(resultado));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);

            datos.setAccion(plan.getAccion());
            datos.setFechaEjecucion(fechaEjecucion);
            datos.setCantidadAvance(plan.getValor());
            datos.setCantidadProgramada(plan.getProgramacion().getValorVariable());
            datos.setPeriodo(plan.getPeriodo());
            datos.setPlanEstrategicas(plan);
            datos.setDetallesPlan(getSelected());
            datos.setProgramacion(plan.getProgramacion());
            datos.setResultado(plan.getResultado());
            datos.setTotalAvance(plan.getVariable2());
            // datos.setDescripcionActividad(archivo.getDescripcion());
            datosHistoricoController.setSelected(datos);
            datosHistoricoController.saveNew(null);
        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }
        // seleccionarAccion();
    }

    public void guardarPrepuestoActual() {

        if (getSelected() != null) {
            DatosHistorico datos = datosHistoricoController.prepareCreate(null);
            // if (getSelected().getVariable2() != valor) {
            programaPresupuestario = getSelected().getPlanEstrategica().getProgramacion().getProgramaPresupuestario();
            variable2 = getSelected().getVariable2();
            variable = (valor + variable2);
            resultadoPresupuesto = (variable / programaPresupuestario) * 100;
            getSelected().setValor(valor);
            getSelected().setVariable2(variable);
            getSelected().setResultado(Math.round(resultadoPresupuesto));
            // getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }
            plan.setValor(valor);
            plan.setVariable2(variable);
            plan.setResultado(Math.round(resultadoPresupuesto));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);
            datos.setFechaEjecucion(fechaEjecucion);
            datos.setAccion(plan.getAccion());
            datos.setCantidadAvance(plan.getValor());
            datos.setCantidadProgramada(plan.getProgramacion().getProgramaPresupuestario());
            datos.setPeriodo(plan.getPeriodo());
            datos.setPlanEstrategicas(plan);
            datos.setDetallesPlan(getSelected());
            datos.setProgramacion(plan.getProgramacion());
            datos.setResultado(plan.getResultado());
            datos.setTotalAvance(plan.getVariable2());
            // datos.setDescripcionActividad(archivo.getDescripcion());
            datosHistoricoController.setSelected(datos);
            datosHistoricoController.saveNew(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    public void guardarSumatoria() {

        if (getSelected() != null) {
            DatosHistorico datos = datosHistoricoController.prepareCreate(null);
            // if (getSelected().getVariable2() != 0) {
            // valorVariable = getSelected().getPlanEstrategica().getProgramacion().getValorVariable();
            valorVariable = getSelected().getVariable2();
            resultado = valor + valorVariable;
            getSelected().setValor(valor);
            getSelected().setVariable2(resultado);
            getSelected().setResultado(Math.round(resultado));
            // getSelected().setEstado(new EstadosInforme("3"));
            getSelected().setResponsable(usuario);
            if (getSelected().getTipoDocumentos().getCodigo().equals("CO")) {
                //resultado = valor + valorVariable;
                if ("sumar".equals(operacion)) {
                    resultado = valor + valorVariable;
                } else if ("restar".equals(operacion)) {
                    resultado = valorVariable + (valor);
                }

                getSelected().setValor(valor);
                getSelected().setVariable2(resultado);
                getSelected().setResultado(Math.round(resultado));
                //getSelected().setEstado(new EstadosInforme("3"));
                getSelected().setResponsable(usuario);

            }

            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);
            PlanEstrategicas plan = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findById", PlanEstrategicas.class
            ).setParameter("id", getSelected().getPlanEstrategica().getId()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                plan.setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                plan.setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                plan.setEstado(new EstadosActividad("VE"));

            }

            plan.setValor(valor);
            plan.setVariable2(resultado);
            plan.setResultado(Math.round(resultado));
            planEstrategicasController.setSelected(plan);
            planEstrategicasController.save(null);
            datos.setFechaEjecucion(fechaEjecucion);
            datos.setAccion(plan.getAccion());
            datos.setCantidadAvance(plan.getValor());
            datos.setCantidadProgramada(valorVariable);
            datos.setPeriodo(plan.getPeriodo());
            datos.setPlanEstrategicas(plan);
            datos.setDetallesPlan(getSelected());
            datos.setProgramacion(plan.getProgramacion());
            datos.setResultado(plan.getResultado());
            datos.setTotalAvance(plan.getVariable2());
            // datos.setDescripcionActividad(archivo.getDescripcion());
            if (getSelected().getTipoDocumentos().getCodigo().equals("PI")) {
                datos.setCantidadProgramada(plan.getProgramacion().getValorVariable());
            }
            datosHistoricoController.setSelected(datos);
            datosHistoricoController.saveNew(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    private void seleccionarCambiosValor() {
        if (getSelected() != null) {
            detallesCambios = this.ejbFacade.getEntityManager().createNamedQuery("CambiosValor.findByProgramacion", CambiosValor.class).setParameter("programacion", getSelected()).getResultList();
        } else {
            detallesCambios = null;
        }

    }

    public void prepararCambio(Programacion item) {
        programacionSelected = item;
        nuevaVariable = getSelected().getProgramacion().getValorVariable();

    }

    public void prepararCambioPresupuestario(Programacion item) {
        programacionSelected = item;
        nuevaVariable = getSelected().getProgramacion().getProgramaPresupuestario();

    }

    public void guardarEdit() {

        if (getSelected().getProgramacion() != null) {

            if (getSelected().getProgramacion().getValorVariable() != nuevaVariable) {
                CambiosValor cambio = cambiosValorController.prepareCreate(null);

                cambio.setCantidadOriginal(getSelected().getProgramacion().getValorVariable());
                cambio.setCantidadFinal(nuevaVariable);
                cambio.setProgramacion(getSelected().getProgramacion());

                getSelected().getProgramacion().setValorVariable(nuevaVariable);
                // getSelected().getProgramacion().setProgramaPresupuestario(nuevaVariable);
                programacionController.setSelected(getSelected().getProgramacion());
                programacionController.save(null);
                if (getSelected().getTipoDocumentos().getCodigo().equals("CO")) {
                    getSelected().setVariable2(nuevaVariable);
                    detallesPlanEstrategicasController.setSelected(getSelected());
                    detallesPlanEstrategicasController.save(null);
                }

                cambiosValorController.setSelected(cambio);
                cambiosValorController.saveNew(null);
                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);

            f.delete();

            docImprimir = null;
        }
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

    public void resetParents() {
        planEstrategicasController.setSelected(null);
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
        obtenerArchivos();
        seleccionar();
        // seleccionarAccion();
        // obtenerListasAccionesOperativas();
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            getSelected().setEstado(new EstadosInforme(Constantes.ASIGNADO));

            super.saveNew(event);

        }

    }

    public void saveArchivos() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoNew") == null) ? false : params.get("guardandoNew"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", true);
                if (getSelected() != null) {

                    if (file != null && file.getContent().length != 0) {
                        alzarArchivo(null, file, false);
                    }

                    anexoIndex = 0;
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
                                //alzarArchivo("Archivo adjunto", bytes, getSelected());
                                alzarArchivo(null, f, true);
                            }
                        }
                    }

                }
            }
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoEdit", false);
        }

    }

    public void verificado() {
        saveCambioEstados();
    }

    public void aprobado() {
        saveCambioEstados();
    }

    public void cargarInforme() {
        saveCambioEstados();
    }

    public void sinVariables() {
        saveCambioEstados();
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            // if (getSelected().getUsuarioAlta() != null) {
            if (getSelected().getResponsable() != null) {

                if (getSelected().getResponsable().equals(usuario)) {

                    try {
                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            //}

        }

        return true;
    }

    public boolean desabilitarBotonTransferir() {

        if (getSelected() != null) {
            try {
                DetallesPlanEstrategicas sel = getSelected();
                boolean habilitar = sel.getEstado().getCodigo().equals(Constantes.ESTADOS_INFORME_8) && sel.getResponsable().equals(usuario);
                return !habilitar;

            } catch (Exception e) {

            }
        }
        return true;
    }

    public boolean desabilitarBotonObservar() {

        if (getSelected() != null) {
            if (getSelected().getResponsable() != null && getSelected().getEstado() != null) {
                return !(getSelected().getResponsable().equals("444") && getSelected().getEstado().getCodigo().equals("3"));
            }

        }

        return true;
    }

    private void obtenerArchivos() {
        if (getSelected() != null) {
            listaArchivos = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoPlanificacion", Archivos.class).setParameter("accionOperativa", getSelected()).getResultList();
        }
    }

    /* private void obtenerListasAccionesOperativas() {
        if (getSelected() != null) {
            selectedDetalles = ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByDocumentoPlanificacion", DetallesPlanEstrategicas.class).setParameter("tipoDocumento", getSelected().getTipoDocumentos()).getResultList();
        }
    }*/
    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDetallesPlanEstrategica", Archivos.class).setParameter("accionOperativa", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    public void alzarArchivo2() {

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
                // fos = new FileOutputStream("C:/Users/DELL/Documents/sistema/archivos" + nombreArchivo);
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

            archivo.setAccionOperativa(getSelected());
            archivo.setDescripcion(descripcionArchivo);
            archivo.setRuta(nombreArchivo);
            archivo.setFechaHoraAlta(fecha);
            archivo.setFechaHoraUltimoEstado(fecha);
            archivo.setUsuarioAlta(usuario);
            archivo.setUsuarioUltimoEstado(usuario);
            archivo.setDepartamento(getSelected().getDepartamento());

            getSelected().setResponsable(usuario);
            if (getSelected().getEstado().getCodigo().equals("7")) {
                getSelected().setEstado(new EstadosInforme("3"));
            } else {
                getSelected().setEstado(new EstadosInforme("2"));
            }
            detallesPlanEstrategicasController.setSelected(getSelected());
            detallesPlanEstrategicasController.save(null);

            archivosController.setSelected(archivo);
            archivosController.saveNew(null);
            obtenerArchivos();

        }

    }

    public void alzarArchivo(Archivos arch, UploadedFile elFile, boolean esAnexo) {
        anexoIndex++;
        if (getSelected() != null) {

            if (elFile == null) {
                return;
            } else if (elFile.getContent().length == 0) {
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + (esAnexo ? "_anexo" + anexoIndex : "") + "_.pdf";

            boolean rr = crearArchivoEnUbicacion(elFile, par.getRutaArchivos() + File.separator + nombreArchivo);
            if (!rr) {
                return;
            }
            if (getSelected().getArchivos() == null) {
                getSelected().setArchivos(new ArrayList<>());
            }

            if (arch == null) {
                Archivos archivo = new Archivos();

                archivo.setAccionOperativa(getSelected());
                archivo.setDescripcion(descripcionArchivo);
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                // archivo.setTipoDocumento(tipoDocumentoJudicial);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);
                archivo.setDepartamento(usuario.getDepartamento());

                getSelected().setResponsable(usuario);
                getSelected().setDepartamento(usuario.getDepartamento());
                getSelected().setEstado(new EstadosInforme("3"));
                /*if (getSelected().getEstado().getCodigo().equals("7")) {
                    getSelected().setEstado(new EstadosInforme("3"));
                } else {
                    getSelected().setEstado(new EstadosInforme("3"));
                }*/
                detallesPlanEstrategicasController.setSelected(getSelected());
                detallesPlanEstrategicasController.save(null);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
                getSelected().getArchivos().add(archivo);

            } else {
                arch.setAccionOperativa(getSelected());
                arch.setDescripcion(descripcionArchivo);
                arch.setRuta(nombreArchivo);
                arch.setFechaHoraAlta(fecha);
                arch.setFechaHoraUltimoEstado(fecha);
                // arch.setTipoDocumento(tipoDocumentoJudicial);
                arch.setUsuarioAlta(usuario);
                arch.setUsuarioUltimoEstado(usuario);
                arch.setDepartamento(usuario.getDepartamento());

                getSelected().setResponsable(usuario);
                getSelected().setDepartamento(usuario.getDepartamento());
                getSelected().setEstado(new EstadosInforme("3"));
                /* if (getSelected().getEstado().getCodigo().equals("7")) {
                    getSelected().setEstado(new EstadosInforme("3"));
                } else {
                    getSelected().setEstado(new EstadosInforme("2"));
                }*/
                detallesPlanEstrategicasController.setSelected(getSelected());
                detallesPlanEstrategicasController.save(null);

                archivosController.setSelected(arch);
                archivosController.save(null);
                getSelected().getArchivos().add(arch);
            }
        }

    }

    public boolean crearArchivoEnUbicacion(UploadedFile fileToUpload, String nombreArchivoCompleto) {

        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(fileToUpload.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error al leer archivo");
            return false;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(nombreArchivoCompleto);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;
            return false;
        }
        return true;
    }

    public void saveDpto() {

        if (getSelected() != null && getSelected().getEstado() != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();

            // Usuarios resp = getSelected().getResponsable();
            FlujosDocumento flujoDoc = null;

            try {
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", getSelected().getResponsable().getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();
                flujoDoc
                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentos().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosInforme estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosInforme.findByCodigo", EstadosInforme.class
                ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                /* if (estado.getCodigo().equals("6")) {

                    getSelected().setResponsableAutoriza(usuario);

                    super.save(null);
                }*/

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

        }
    }

    public void AccionEstrategicaBean() {
        actividades = Arrays.asList(
                new Actividades(26, "Causas en trámite (sin enjuiciamiento)"),
                new Actividades(27, "Causas en trámite (sin enjuiciamiento)"),
                new Actividades(28, "Cantidad Programada"),
                new Actividades(29, "Sesiones Realizada"),
                new Actividades(30, "Sesiones Realizada"),
                new Actividades(31, "Cantidad de convenios celebrados"),
                new Actividades(32, "Cantidad de Promociones"),
                new Actividades(33, "Total de Funcionarios"),
                new Actividades(34, "Total de Funcionarios"),
                new Actividades(35, "Total de Funcionarios"),
                new Actividades(36, "Procedimientos diseñados e implementados"),
                new Actividades(37, "Procedimientos diseñados e implementados"),
                new Actividades(38, "Inversión programada"),
                new Actividades(39, "Inversión programada"),
                new Actividades(40, "Inversión programada"),
                new Actividades(41, "Total de  presupuesto"),
                new Actividades(42, "Inversión programada"),
                new Actividades(43, "Inversión programada")
        );

    }

    public void seleccionarAccion() {
        AccionEstrategicaBean();
        if (getSelected().getActividad() != null) {
            //if (actividadSeleccionada != null) {
            Actividades a = null;
            for (Actividades act : actividades) {
                if (act.getId().equals(getSelected().getActividad().getId())) {
                    actividadSeleccionada = act;
                    break;
                }
            }

            titulo = actividadSeleccionada.getDescripcion();
            //}
        }
    }

}
