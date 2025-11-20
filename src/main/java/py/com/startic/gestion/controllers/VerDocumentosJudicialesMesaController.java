package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosJudiciales;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.CambiosEstadoDocumento;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosEscaneados;
import py.com.startic.gestion.models.EntradasDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "verDocumentosJudicialesMesaController")
@ViewScoped
public class VerDocumentosJudicialesMesaController extends AbstractController<DocumentosJudiciales> {

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
    private FiltroURL filtroURL = new FiltroURL();
    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private String nuevaCausa;
    private String nombreJuez;
    private String nombreEstado;
    private String ultimaObservacion;
    private CanalesEntradaDocumentoJudicial canal;
    private TiposDocumentosJudiciales tipoDoc;
    private Usuarios usuario;
    // private Departamentos departamento;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private FlujosDocumento flujoDoc;
    private String sgteEstado;
    private Collection<Usuarios> listaUsuariosTransf;
    private boolean hayDocumentosAtencion;
    private String descripcionCambioEstado;
    private Usuarios responsableDestino;

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public String getDescripcionCambioEstado() {
        return descripcionCambioEstado;
    }

    public void setDescripcionCambioEstado(String descripcionCambioEstado) {
        this.descripcionCambioEstado = descripcionCambioEstado;
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

    public String getSgteEstado() {
        return sgteEstado;
    }

    public void setSgteEstado(String sgteEstado) {
        this.sgteEstado = sgteEstado;
    }

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

    public EntradasDocumentosJudiciales getEntradaDocumentoJudicial() {
        return entradaDocumentoJudicial;
    }

    public void setEntradaDocumentoJudicial(EntradasDocumentosJudiciales entradaDocumentoJudicial) {
        this.entradaDocumentoJudicial = entradaDocumentoJudicial;
    }

    public VerDocumentosJudicialesMesaController() {
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
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo("ME");
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo("JU");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        //fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2018);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();
        buscarPorFechaAlta();

        descripcionCambioEstado = "Marcar como Recibido";
    }

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
            if (Constantes.ESTADO_DOCUMENTO_19.equals(getSelected().getEstado().getCodigo())) {
                descripcionCambioEstado = "Marcar como Adjuntado al Expediente";
            } else {
                descripcionCambioEstado = "Marcar como Recibido";
            }
        } else {
            descripcionCambioEstado = "Marcar como Recibido";
        }

    }

    public void prepareTransferir() {
        if (getSelected() != null) {
            listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
        } else {
            listaUsuariosTransf = new ArrayList<>();
        }
        responsableDestino = null;
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

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            if (getSelected().getResponsable().getDepartamento().equals(usuario.getDepartamento())) {

                try {
                    flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return true;
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
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/verDocumentosJudicialesMesa/index");
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
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/verDocumentosJudicialesMesa/index");
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
            if (filtroURL.verifPermiso("verTodosDocsJud")) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedAsignadoAll", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tipoDocumentoJudicial", tipoDoc).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedAsignado", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tipoDocumentoJudicial", tipoDoc).setParameter("departamento", usuario.getDepartamento()).getResultList());
            }
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
            if (filtroURL.verifPermiso("verTodosDocsJud")) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignadoAll", DocumentosJudiciales.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tipoDocumentoJudicial", tipoDoc).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignado", DocumentosJudiciales.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("tipoDocumentoJudicial", tipoDoc).setParameter("departamento", usuario.getDepartamento()).getResultList());
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

    public void saveNew() {
        if (getSelected() != null) {

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

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFolios("");
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDescripcionMesaEntrada(getSelected().getCaratula());
            //getSelected().setUltimaObservacion(ultimaObservacion);
            getSelected().setFechaUltimaObservacion(fecha);
            getSelected().setCanalEntradaDocumentoJudicial(canal);
            getSelected().setTipoDocumentoJudicial(tipoDoc);
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(usuario.getDepartamento());
            //getSelected().setMostrarWeb("SI");

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
                }

            }

            /*
            if (getSelected().getEstado() == null) {
                if (nombreEstado != null) {
                    if (!nombreEstado.equals("")) {
                        EstadosDocumento estado = estadoController.prepareCreate(null);
                        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                                "select ifnull(max(CONVERT(codigo,SIGNED INTEGER)),0) as VALOR from estados_documento", CodigoEstadoDocumento.class);

                        CodigoEstadoDocumento cod = (CodigoEstadoDocumento) query.getSingleResult();

                        estado.setCodigo(String.valueOf(cod.getCodigo() + 1));

                        estado.setDescripcion(nombreEstado);
                        estado.setEmpresa(usuario.getEmpresa());

                        estadoController.setSelected(estado);

                        estadoController.saveNew(null);

                        getSelected().setEstado(estado);
                    } else {
                        JsfUtil.addErrorMessage("Debe ingresar el estado procesal");
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe ingresar el estado procesal");
                    return;
                }
            }
             */
 /*
            if (getSelected().getCausa() == null) {
                if (nuevaCausa != null) {
                    if (!nuevaCausa.equals("")) {
                        Causas causa = causaController.prepareCreate(null);

                        causa.setFechaHoraUltimoEstado(fecha);
                        causa.setUsuarioUltimoEstado(usuario);
                        causa.setFechaHoraAlta(fecha);
                        causa.setUsuarioAlta(usuario);
                        causa.setEmpresa(usuario.getEmpresa());
                        causa.setNroCausa(nuevaCausa);
                        causa.setId(0);

                        causaController.setSelected(causa);

                        causaController.saveNew(null);
                        getSelected().setCausa(causa);
                        
                    } else {
                        JsfUtil.addErrorMessage("Debe ingresar nro de nueva causa");
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe ingresar nro de nueva causa");
                    return;
                }
            }
             */
 /*
            if (getSelected().getJuez() == null) {
                if (nombreJuez != null) {
                    if (!"".equals(nombreJuez)) {
                        Jueces juez = juezController.prepareCreate(null);

                        juez.setFechaHoraUltimoEstado(fecha);
                        juez.setUsuarioUltimoEstado(usuario);
                        juez.setFechaHoraAlta(fecha);
                        juez.setUsuarioAlta(usuario);
                        juez.setEmpresa(usuario.getEmpresa());
                        juez.setNombresApellidos(nombreJuez);

                        juezController.setSelected(juez);

                        juezController.saveNew(null);

                        getSelected().setJuez(juez);
                    } else {
                        JsfUtil.addErrorMessage("Debe ingresar nombre nuevo juez");
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe ingresar nombre nuevo juez");
                    return;
                }
            }
             */
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
                    "select ifnull(max(CONVERT(substring(nro_mesa_entrada,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_judiciales WHERE substring(nro_mesa_entrada,1,4) = 'AUTO';", NroMesaEntrada.class
            );

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            doc.setNroMesaEntrada("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosJudicialesController.setSelected(doc);
            //entradasDocumentosJudicialesController.saveNew(null);
            getSelected().setEntradaDocumento(doc);

            super.saveNew(null);

            if (getSelected().getUltimaObservacion() != null && !"".equals(getSelected().getUltimaObservacion())) {
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

                getSelected().setObservacionDocumentoJudicial(obs);

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

    /*
    public void saveDoc() {

        if (file != null) {
            if (file.getContents().length > 0) {

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputstream());
                } catch (IOException ex) {
                }

                // getSelected().setDocumento(bytes);
                
                
            } else {
                getSelected().setDocumento(null);
            }

        } else {
            getSelected().setDocumento(null);
        }

        super.save(null);
    }
     */
    public void saveDpto() {

        if (getSelected() != null) {
            // if (getSelected().getResponsable().equals(usuario)) {

            Date fecha = ejbFacade.getSystemDate();
/*
            Collection<DocumentosJudiciales> col = null;

            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
*/

            // Usuarios resp = getSelected().getResponsable();
            FlujosDocumento flujoDoc = null;

            try {
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();
                flujoDoc
                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosDocumento estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }
            
            CambiosEstadoDocumento cambioEstadoDocumento = cambiosEstadoDocumentoController.prepareCreate(null);
            cambioEstadoDocumento.setDocumentoJudicial(getSelected());
            
            cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());
            
            cambioEstadoDocumento.setResponsableDestino(responsableDestino);
            cambioEstadoDocumento.setDepartamentoDestino(responsableDestino.getDepartamento());
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
            getSelected().setResponsable(responsableDestino);
            getSelected().setDepartamento(responsableDestino.getDepartamento());
            getSelected().setEstado(estado);
            getSelected().setEstadoProcesal(estado.getDescripcion());

            //setSelected(doc);
            //super.save(null);
            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estado.getDescripcion());
            estadoProc.setDocumentoJudicial(getSelected());

            //                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            //                    estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            
            getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);
            
            buscarPorFechaAlta();
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
