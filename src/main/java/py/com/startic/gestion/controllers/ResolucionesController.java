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
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.Resoluciones;
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
import py.com.startic.gestion.models.CambiosResolucion;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.DocumentosJudiciales;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosProceso;
import py.com.startic.gestion.models.ObservacionesDocumentosJudiciales;
import py.com.startic.gestion.models.ObservacionesResoluciones;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.PersonasPorDocumentosJudiciales;
import py.com.startic.gestion.models.ResolucionesEscaneadas;
import py.com.startic.gestion.models.Resuelve;
import py.com.startic.gestion.models.ResuelvePorResolucionesPorPersonas;
import py.com.startic.gestion.models.SubtiposResolucion;
import py.com.startic.gestion.models.TiposExpediente;
import py.com.startic.gestion.models.TiposResolucion;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "resolucionesController")
@ViewScoped
public class ResolucionesController extends AbstractController<Resoluciones> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private ResolucionesEscaneadasController resolucionesEscaneadasController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    ObservacionesResolucionesController obsController;
    @Inject
    DocumentosJudicialesController docController;
    @Inject
    ResuelvePorResolucionesPorPersonasController resuelvePorResolucionesPorPersonasController;

    private UploadedFile file;
    private Date fechaDesde;
    private Date fechaHasta;

    private Date fechaAltaDesde;
    private Date fechaAltaHasta;

    private String nroResolucion;
    private TiposResolucion tipoResolucion;
    private Date fecha;
    private Date fechaSesion;
    private String caratula;
    private DocumentosJudiciales documentoJudicial;
    private boolean firmado;

    private EstadosProceso estadoProceso;

    private boolean busquedaPorFechaAlta;
    private List<Resuelve> listaResuelve;
    private List<DocumentosJudiciales> listaDocumentosJudiciales;
    private HttpSession session;
    private Usuarios usu;
    private CanalesEntradaDocumentoJudicial canal;
    private String ultimaObservacion;
    private String ultimaObservacionActual;
    private TiposExpediente tipoExpediente;
    private List<TiposExpediente> listaTiposExpediente;
    private String mostrarWeb;
    private final FiltroURL filtroURL = new FiltroURL();
    private SubtiposResolucion subtipoResolucion;
    private List<SubtiposResolucion> listaSubtiposResolucion;
    private List<ResuelvePorResolucionesPorPersonas> listaPersonas;
    private ResuelvePorResolucionesPorPersonas personaSelected;
    private Collection<Resoluciones> resolucionesbusqueda;
    private String busqueda;

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

    public Date getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Date fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public EstadosProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadosProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public List<Resuelve> getListaResuelve() {
        return listaResuelve;
    }

    public void setListaResuelve(List<Resuelve> listaResuelve) {
        this.listaResuelve = listaResuelve;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public TiposResolucion getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(TiposResolucion tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public SubtiposResolucion getSubtipoResolucion() {
        return subtipoResolucion;
    }

    public void setSubtipoResolucion(SubtiposResolucion subtipoResolucion) {
        this.subtipoResolucion = subtipoResolucion;
    }

    public List<SubtiposResolucion> getListaSubtiposResolucion() {
        return listaSubtiposResolucion;
    }

    public void setListaSubtiposResolucion(List<SubtiposResolucion> listaSubtiposResolucion) {
        this.listaSubtiposResolucion = listaSubtiposResolucion;
    }

    public List<ResuelvePorResolucionesPorPersonas> getListaPersonas() {
        /*
        if(getSelected() != null){
            if(documentoJudicial != null){
                return obtenerListaPersonasResuelve(getSelected());
            }
        }
         */
        return listaPersonas;
    }

    public void setListaPersonas(List<ResuelvePorResolucionesPorPersonas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public ResuelvePorResolucionesPorPersonas getPersonaSelected() {
        return personaSelected;
    }

    public void setPersonaSelected(ResuelvePorResolucionesPorPersonas personaSelected) {
        this.personaSelected = personaSelected;
    }

    public List<DocumentosJudiciales> getListaDocumentosJudiciales() {

        listaDocumentosJudiciales = obtenerListaDocumentosJudiciales();
        return listaDocumentosJudiciales;
    }

    public void setListaDocumentosJudiciales(List<DocumentosJudiciales> listaDocumentosJudiciales) {
        this.listaDocumentosJudiciales = listaDocumentosJudiciales;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        if (session.getAttribute("documentoJudicialSelected") != null) {
            documentoJudicial = (DocumentosJudiciales) session.getAttribute("documentoJudicialSelected");
            session.removeAttribute("documentoJudicialSelected");
        }
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public TiposExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TiposExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    public List<TiposExpediente> getListaTiposExpediente() {
        return listaTiposExpediente;
    }

    public void setListaTiposExpediente(List<TiposExpediente> listaTiposExpediente) {
        this.listaTiposExpediente = listaTiposExpediente;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public Collection<Resoluciones> getResolucionesbusqueda() {
        return resolucionesbusqueda;
    }

    public void setResolucionesbusqueda(Collection<Resoluciones> resolucionesbusqueda) {
        this.resolucionesbusqueda = resolucionesbusqueda;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }
    
    public void refrescar(){
        buscarPorFechaAlta();
    }

    public void actualizarDatosDocumentoJudicialEnEdit() {
        if (getSelected() != null) {
            if (documentoJudicial != null) {
                try {
                    List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", documentoJudicial.getId()).setParameter("estado", new Estados("AC")).getResultList();
                    List<ResuelvePorResolucionesPorPersonas> listaPer = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByResolucionEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

                    Personas per = null;
                    listaPersonas = new ArrayList<>();
                    boolean encontro = false;
                    ResuelvePorResolucionesPorPersonas res = null;
                    for (int i = 0; i < listaPersonasActual.size(); i++) {
                        per = listaPersonasActual.get(i).getPersona();
                        res = resuelvePorResolucionesPorPersonasController.prepareCreate(null);

                        for (ResuelvePorResolucionesPorPersonas res2 : listaPer) {
                            encontro = true;
                            if (res2.getPersona().equals(per)) {
                                res.setResuelve(res2.getResuelve());
                            }
                        }

                        res.setPersona(per);
                        listaPersonas.add(res);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                estadoProceso = documentoJudicial.getEstadoProceso();
                tipoExpediente = documentoJudicial.getTipoExpediente();
            }
        }

    }

    public void actualizarDatosDocumentoJudicialEnCreate() {
        actualizarDatosDocumentoJudicialEnCreate(documentoJudicial);
    }

    public void actualizarDatosDocumentoJudicialEnCreate(DocumentosJudiciales doc) {
        if (getSelected() != null) {
            if (doc != null) {
                try {
                    List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();

                    Personas per = null;
                    listaPersonas = new ArrayList<>();
                    ResuelvePorResolucionesPorPersonas res = null;
                    for (int i = 0; i < listaPersonasActual.size(); i++) {
                        per = listaPersonasActual.get(i).getPersona();
                        res = resuelvePorResolucionesPorPersonasController.prepareCreate(null);
                        res.setPersona(per);
                        listaPersonas.add(res);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                estadoProceso = doc.getEstadoProceso();
                tipoExpediente = doc.getTipoExpediente();
            }
        }

        //actualizarEstadoProceso();
    }

    public List<ResuelvePorResolucionesPorPersonas> obtenerListaPersonasResuelve(Resoluciones resolucion, DocumentosJudiciales doc) {
        List<ResuelvePorResolucionesPorPersonas> listaPer = null;
        try {
            listaPer = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByResolucionEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", resolucion).setParameter("estado", new Estados("AC")).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            listaPer = new ArrayList<>();
        }

        if (resolucion.getDocumentoJudicial() != null) {
            List<PersonasPorDocumentosJudiciales> listaPersonasDoc = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();

            boolean encontro = false;
            for (PersonasPorDocumentosJudiciales per : listaPersonasDoc) {
                encontro = false;
                for (ResuelvePorResolucionesPorPersonas res : listaPer) {
                    if (res.getPersona().equals(per.getPersona())) {
                        encontro = true;
                    }
                }
                if (!encontro) {
                    ResuelvePorResolucionesPorPersonas res2 = resuelvePorResolucionesPorPersonasController.prepareCreate(null);
                    res2.setEstado(new Estados("AC"));
                    res2.setPersona(per.getPersona());
                    res2.setResolucion(getSelected());
                    res2.setResuelve(null);
                    listaPer.add(res2);
                }
            }
        }

        return listaPer;

    }

    public ResolucionesController() {
        // Inform the Abstract parent controller of the concrete Resoluciones Entity
        super(Resoluciones.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usu = (Usuarios) session.getAttribute("Usuarios");
        // fechaAltaDesde = ejbFacade.getSystemDateOnly(-30);
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2018);
        myCal.set(Calendar.MONTH, 1);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaAltaDesde = myCal.getTime();
        fechaAltaHasta = ejbFacade.getSystemDateOnly();
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_SE);

        listaTiposExpediente = ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findAll", TiposExpediente.class).getResultList();

        buscarPorFechaAlta();
        setBusqueda("");
        setResolucionesbusqueda(new ArrayList<>());
    }

    public String obtenerResuelve() {
        return "";
    }

    public List<DocumentosJudiciales> obtenerListaDocumentosJudiciales() {
        return this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAll", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
    }

    public boolean buscarDocumento(String item, String buscar) {
        System.err.println("" + item + buscar);
        return true;
    }

    public void prepareEdit() {
        if (getSelected() != null) {
            caratula = getSelected().getCaratula();
            nroResolucion = getSelected().getNroResolucion();
            tipoResolucion = getSelected().getTipoResolucion();
            subtipoResolucion = getSelected().getSubtipoResolucion();
            fecha = getSelected().getFecha();
            fechaSesion = getSelected().getFechaSesion();
            documentoJudicial = getSelected().getDocumentoJudicial();
            firmado = getSelected().isFirmado();
            mostrarWeb = getSelected().getMostrarWeb();
            ultimaObservacion = obtenerUltimaObservacion(getSelected());
            ultimaObservacionActual = ultimaObservacion;

            if (getSelected().getDocumentoJudicial() != null) {
                tipoExpediente = getSelected().getDocumentoJudicial().getTipoExpediente();
                estadoProceso = getSelected().getDocumentoJudicial().getEstadoProceso();

                listaPersonas = obtenerListaPersonasResuelve(getSelected(), getSelected().getDocumentoJudicial());

            } else {
                tipoExpediente = null;
                estadoProceso = null;
                listaPersonas = new ArrayList<>();
            }

            actualizarResuelve();
        }
    }

    public String obtenerUltimaObservacion(Resoluciones res) {

        List<ObservacionesResoluciones> obsRes = ejbFacade.getEntityManager().createNamedQuery("ObservacionesResoluciones.findByResolucion", ObservacionesResoluciones.class).setParameter("resolucion", res).getResultList();

        if (obsRes != null) {
            if (obsRes.size() > 0) {
                return obsRes.get(0).getObservacion();
            }
        }

        return "";
    }

    public String obtenerUltimaObservacionString(Resoluciones res) {
        return obtenerUltimaObservacion(res).replace("\n", "<br />");
    }

    @Override
    public Resoluciones prepareCreate(ActionEvent event) {
        Resoluciones res = super.prepareCreate(event);
        caratula = null;
        nroResolucion = null;
        tipoResolucion = null;
        subtipoResolucion = null;
        tipoExpediente = null;
        fecha = null;
        fechaSesion = null;
        listaPersonas = new ArrayList<>();
        documentoJudicial = null;
        firmado = false;
        ultimaObservacion = "";
        estadoProceso = null;
        mostrarWeb = Constantes.NO;

        return res;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
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

    @Override
    public void delete(ActionEvent event) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_RESOLUCION) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_RESOLUCION_HISTORICO)) {
            Date fecha = ejbFacade.getSystemDateOnly();
            if(getSelected() != null){
                if (getSelected().getFechaHoraAlta().after(fecha)  || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_RESOLUCION_HISTORICO)) {
                    /*
                    List<CambiosResolucion> lista1 = ejbFacade.getEntityManager().createNamedQuery("CambiosResolucion.findByResolucion", CambiosResolucion.class).setParameter("resolucion", getSelected()).getResultList();

                    for(CambiosResolucion per : lista1){
                        cambioResolucionController.setSelected(per);
                        cambioResolucionController.delete(null);
                    }
    */

                    List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByResolucion", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", getSelected()).getResultList();

                    for(ResuelvePorResolucionesPorPersonas per : lista){

                        resuelvePorResolucionesPorPersonasController.setSelected(per);
                        per.setFechaHoraUltimoEstado(fecha);
                        per.setUsuarioUltimoEstado(usu);
                        resuelvePorResolucionesPorPersonasController.save(event);
                        resuelvePorResolucionesPorPersonasController.delete(null);
                    }


                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usu);
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

                        buscarPorFechaResolucion();

                    }
                } else {
                    JsfUtil.addErrorMessage("Solo se pueden borrar resoluciones creados en el dia");
                }
            }
        } else {
            JsfUtil.addErrorMessage("No tiene permisos para borrar resoluciones");
        }
    }
    
    public boolean deshabilitarBorrar(){
        if(getSelected() != null){
            if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {
                return false;
            }
        }
        return true;
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

                getSelected().setDocumento(bytes);
            } else {
                getSelected().setDocumento(null);
            }

        } else {
            getSelected().setDocumento(null);
        }

        super.save(null);
    }
     */
 /*
    
    private void actualizarCambioResolucion(Resoluciones resolucion,
            String caratulaAnterior,
            String nroResolucionAnterior, 
            TiposResolucion tipoResolucionAnterior,
            Date fechaAnterior,
            Personas personaAnterior,
            DocumentosJudiciales documentoJudicialAnterior,
            boolean firmadoAnterior,
            Resuelve resuelveAnterior,
            String caratulaActual,
            String nroResolucionActual, 
            TiposResolucion tipoResolucionActual,
            Date fechaActual,
            Personas personaActual,
            DocumentosJudiciales documentoJudicialActual,
            boolean firmadoActual,
            Resuelve resuelveActual,
            Usuarios usuarioAlta,
            Date fechaHoraAlta,
            Empresas empresa){
    }
    
     */
    
    /*
    private void actualizarCambioResolucion(Resoluciones resAnterior, Resoluciones resActual, Usuarios usuarioAlta, Date fechaHoraAlta, Empresas empresa) {

        boolean cambio = false;

        if ((resAnterior.getCaratula() != null && resActual.getCaratula() == null)
                || (resAnterior.getCaratula() == null && resActual.getCaratula() != null)) {
            cambio = true;
        } else if (resAnterior.getCaratula() != null && resActual.getCaratula() != null) {
            if (!resAnterior.getCaratula().equals(resActual.getCaratula())) {
                cambio = true;
            }
        }

        if ((resAnterior.getNroResolucion() != null && resActual.getNroResolucion() == null)
                || (resAnterior.getNroResolucion() == null && resActual.getNroResolucion() != null)) {
            cambio = true;
        } else if (resAnterior.getNroResolucion() != null && resActual.getNroResolucion() != null) {
            if (!resAnterior.getNroResolucion().equals(resActual.getNroResolucion())) {
                cambio = true;
            }
        }

        if ((resAnterior.getTipoResolucion() != null && resActual.getTipoResolucion() == null)
                || (resAnterior.getTipoResolucion() == null && resActual.getTipoResolucion() != null)) {
            cambio = true;
        } else if (resAnterior.getTipoResolucion() != null && resActual.getTipoResolucion() != null) {
            if (!resAnterior.getTipoResolucion().equals(resActual.getTipoResolucion())) {
                cambio = true;
            }
        }

        if ((resAnterior.getFecha() != null && resActual.getFecha() == null)
                || (resAnterior.getFecha() == null && resActual.getFecha() != null)) {
            cambio = true;
        } else if (resAnterior.getFecha() != null && resActual.getFecha() != null) {
            if (!resAnterior.getFecha().equals(resActual.getFecha())) {
                cambio = true;
            }
        }

        if ((resAnterior.getResolucionEscaneada() != null && resActual.getResolucionEscaneada() == null)
                || (resAnterior.getResolucionEscaneada() == null && resActual.getResolucionEscaneada() != null)) {
            cambio = true;
        } else if (resAnterior.getResolucionEscaneada() != null && resActual.getResolucionEscaneada() != null) {
            if (!resAnterior.getResolucionEscaneada().equals(resActual.getResolucionEscaneada())) {
                cambio = true;
            }
        }

        if ((resAnterior.getDocumentoJudicial() != null && resActual.getDocumentoJudicial() == null)
                || (resAnterior.getDocumentoJudicial() == null && resActual.getDocumentoJudicial() != null)) {
            cambio = true;
        } else if (resAnterior.getDocumentoJudicial() != null && resActual.getDocumentoJudicial() != null) {
            if (!resAnterior.getDocumentoJudicial().equals(resActual.getDocumentoJudicial())) {
                cambio = true;
            }
        }

        if (resAnterior.isFirmado() != resActual.isFirmado()) {
            cambio = true;
        }

        if (cambio) {
            CambiosResolucion cambioResolucion = new CambiosResolucion(resAnterior, resActual, usuarioAlta, fechaHoraAlta, empresa);

            cambioResolucionController.setSelected(cambioResolucion);
            cambioResolucionController.saveNew(null);
        }
        
    }
    */

    /*
    private void actualizarPersonas(Date fecha) {
        Personas per2 = null;
        PersonasPorDocumentosJudiciales perDocActual = null;
        boolean encontro = false;
        List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();
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
                personasPorDocumentosJudicialesController.setSelected(perDoc);
                personasPorDocumentosJudicialesController.saveNew(null);
            } else {
                if ("IN".equals(perDocActual.getEstado().getCodigo())) {
                    perDocActual.setEstado(new Estados("AC"));
                    perDocActual.setFechaHoraAlta(fecha);
                    perDocActual.setUsuarioAlta(usuario);
                    personasPorDocumentosJudicialesController.setSelected(perDocActual);
                    personasPorDocumentosJudicialesController.save(null);
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
                personasPorDocumentosJudicialesController.setSelected(listaPersonasActual.get(i));
                personasPorDocumentosJudicialesController.save(null);
            }
        }
    }
     */
    public void saveNew() {
        if (getSelected() != null) {

            Date fechaActual = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fechaActual);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fechaActual);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            getSelected().setCaratula(caratula);
            getSelected().setNroResolucion(nroResolucion);
            getSelected().setTipoResolucion(tipoResolucion);
            getSelected().setFecha(fecha);
            getSelected().setFechaSesion(fechaSesion);
            getSelected().setDocumentoJudicial(documentoJudicial);
            getSelected().setFirmado(firmado);
            getSelected().setSubtipoResolucion(subtipoResolucion);

            getSelected().setMostrarWeb(mostrarWeb);

            /*
            if (getSelected().getNroResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                return;
            } else if ("".equals(getSelected().getNroResolucion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                return;
            }
             */
 /* #DOCUMENTO JUDICIAL OPCIONAL
            if (getSelected().getDocumentoJudicial() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_documentoJudicial"));
                return;
            }

            if (tipoExpediente == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_tipoExpediente"));
                return;
            }

            if (listaPersonas == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_persona"));
                return;
            }
             */
            if (getSelected().getTipoResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_tipoResolucion"));
                return;
            }

            if (getSelected().getFecha() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_fecha"));
                return;
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return;
            }

            if (listaPersonas != null) {
                for (ResuelvePorResolucionesPorPersonas res : listaPersonas) {
                    if (res.getResuelve() == null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve"));
                        return;
                    }
                }
            }

            if (file != null) {
                if (file.getContent().length > 0) {

                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }

                    // getSelected().setDocumento(bytes);
                    ResolucionesEscaneadas docEsc = null;

                    if (getSelected().getResolucionEscaneada() == null) {
                        docEsc = resolucionesEscaneadasController.prepareCreate(null);

                        docEsc.setEmpresa(usu.getEmpresa());
                        docEsc.setDocumento(bytes);

                        resolucionesEscaneadasController.setSelected(docEsc);
                        resolucionesEscaneadasController.saveNew(null);

                        getSelected().setResolucionEscaneada(resolucionesEscaneadasController.getSelected().getId());

                    } else {
                        docEsc = this.ejbFacade.getEntityManager().createNamedQuery("ResolucionesEscaneadas.findById", ResolucionesEscaneadas.class).setParameter("id", getSelected().getResolucionEscaneada()).getSingleResult();
                        docEsc.setDocumento(bytes);
                        resolucionesEscaneadasController.setSelected(docEsc);
                        resolucionesEscaneadasController.save(null);
                    }

                }
            }

            super.saveNew(null);

            boolean guardar = false;

            if (estadoProceso != null && getSelected().getDocumentoJudicial() != null) {
                if (!estadoProceso.equals(getSelected().getDocumentoJudicial().getEstadoProceso())) {
                    getSelected().getDocumentoJudicial().setEstadoProceso(estadoProceso);
                    guardar = true;
                }
            }

            if (tipoExpediente != null && getSelected().getDocumentoJudicial() != null) {
                if (!tipoExpediente.equals(getSelected().getDocumentoJudicial().getTipoExpediente())) {
                    getSelected().getDocumentoJudicial().setTipoExpediente(tipoExpediente);
                    guardar = true;
                }
            }
            /*
            if(!persona.equals(getSelected().getDocumentoJudicial().getPersona())){
                getSelected().getDocumentoJudicial().setPersona(persona);
                guardar = true;
            }
             */

            if (guardar) {
                docController.setSelected(getSelected().getDocumentoJudicial());
                docController.save(null);
                docController.setSelected(null);
            }

            if (listaPersonas != null) {
                ResuelvePorResolucionesPorPersonas res = null;
                for (int i = 0; i < listaPersonas.size(); i++) {
                    res = new ResuelvePorResolucionesPorPersonas();

                    res.setResolucion(getSelected());
                    res.setEmpresa(usu.getEmpresa());
                    res.setFechaHoraAlta(fecha);
                    res.setFechaHoraUltimoEstado(fecha);
                    res.setUsuarioAlta(usu);
                    res.setUsuarioUltimoEstado(usu);
                    res.setEstado(new Estados("AC"));
                    res.setPersona(listaPersonas.get(i).getPersona());
                    res.setResuelve(listaPersonas.get(i).getResuelve());
                    resuelvePorResolucionesPorPersonasController.setSelected(res);
                    resuelvePorResolucionesPorPersonasController.saveNew(null);
                }
            }

            if (ultimaObservacion != null) {
                if (!"".equals(ultimaObservacion)) {
                    // && getSelected().getDocumentoJudicial() != null &&
                    // (Constantes.RESUELVE_CANCELACION.equals(getSelected().getResuelve().getCodigo()) || Constantes.RESUELVE_ENJUICIAMIENTO.equals(getSelected().getResuelve().getCodigo()))) {
                    ObservacionesResoluciones obs = obsController.prepareCreate(null);

                    obs.setUsuarioAlta(usu);
                    obs.setUsuarioUltimoEstado(usu);
                    obs.setFechaHoraAlta(fechaActual);
                    obs.setFechaHoraUltimoEstado(fechaActual);
                    obs.setEmpresa(usu.getEmpresa());
                    obs.setObservacion(ultimaObservacion);
                    obs.setResolucion(getSelected());

                    obsController.setSelected(obs);
                    obsController.saveNew(null);
                    /*
                obs.setDocumentoJudicial(getSelected());

                obsController.save(null);
                     */
                }
            }

            // actualizarCambioResolucion(resAnt, getSelected(), usu, fechaActual, getSelected().getEmpresa());

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

                buscarPorFechaResolucion();

            }
        }
    }

    public void save() {

        boolean guardar = true;

        Date fechaActual = ejbFacade.getSystemDate();

        if (getSelected() != null) {
            getSelected().setFechaHoraUltimoEstado(fechaActual);
            getSelected().setUsuarioUltimoEstado(usu);

            getSelected().setCaratula(caratula);
            getSelected().setNroResolucion(nroResolucion);
            getSelected().setTipoResolucion(tipoResolucion);
            getSelected().setFecha(fecha);
            getSelected().setFechaSesion(fechaSesion);
            getSelected().setDocumentoJudicial(documentoJudicial);
            getSelected().setFirmado(firmado);
            getSelected().setMostrarWeb(mostrarWeb);
            getSelected().setSubtipoResolucion(subtipoResolucion);

            /*
            if (getSelected().getNroResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                guardar = false;
            } else if ("".equals(getSelected().getNroResolucion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                guardar = false;
            }
             */

 /* #DOCUMENTO JUDICIAL OPCIONAL
            if (getSelected().getDocumentoJudicial() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_documentoJudicial"));
                guardar = false;
            }

            if (tipoExpediente == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_tipoExpediente"));
                return;
            }
             */
            if (getSelected().getTipoResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_tipoResolucion"));
                guardar = false;
            }

            if (getSelected().getFecha() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_fecha"));
                guardar = false;
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_caratula"));
                guardar = false;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_caratula"));
                guardar = false;
            }

            for (ResuelvePorResolucionesPorPersonas res : listaPersonas) {
                if (res.getResuelve() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve") + " (1)");
                    return;
                }
            }

            if (listaPersonas == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditResolucionesHelpText_persona"));
                guardar = false;
            }

            List<ResuelvePorResolucionesPorPersonas> listaPersonasActual = null;
            try {
                listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByResolucionEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", getSelected()).setParameter("estado", new Estados("AC")).getResultList();
            } catch (Exception e) {
                e.printStackTrace();
                listaPersonasActual = new ArrayList<>();
            }
            /*
            if (listaPersonasActual.size() != listaPersonas.size()) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve") + " (2)");
                return;
            }
             */
            boolean encontro = false;
            if (listaPersonas != null) {
                /*
                for (ResuelvePorResolucionesPorPersonas res : listaPersonas) {
                    encontro = false;
                    for (ResuelvePorResolucionesPorPersonas res2 : listaPersonasActual) {
                        if (res.equals(res2)) {
                            encontro = true;
                            break;
                        }
                    }

                    if (!encontro) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve") + " (3)");
                        return;
                    }
                }
                 */

                boolean guardarRes = false;
                for (ResuelvePorResolucionesPorPersonas res : listaPersonas) {
                    encontro = false;
                    for (ResuelvePorResolucionesPorPersonas res2 : listaPersonasActual) {
                        if (res.equals(res2)) {
                            encontro = true;
                            guardarRes = false;
                            if (res.getResuelve() != null && res2.getResuelve() == null) {
                                guardarRes = true;
                            } else if (res.getResuelve() == null && res2.getResuelve() != null) {
                                guardarRes = true;
                            } else if (res.getResuelve() != null && res2.getResuelve() != null) {
                                if (!res.getResuelve().equals(res2.getResuelve())) {
                                    guardarRes = true;
                                }
                            }
                            if (guardarRes) {
                                res2.setEstado(new Estados("IN"));
                                resuelvePorResolucionesPorPersonasController.setSelected(res2);
                                resuelvePorResolucionesPorPersonasController.save(null);
                                resuelvePorResolucionesPorPersonasController.setSelected(res);
                                resuelvePorResolucionesPorPersonasController.saveNew(null);
                            }
                            break;
                        }
                    }
                    if (!encontro) {
                        res.setEstado(new Estados("AC"));
                        res.setResolucion(getSelected());
                        resuelvePorResolucionesPorPersonasController.setSelected(res);
                        resuelvePorResolucionesPorPersonasController.saveNew(null);
                    }
                }
            }

            /*
            for(ResuelvePorResolucionesPorPersonas res : listaPersonasActual){
                encontro = false;
                for(ResuelvePorResolucionesPorPersonas res2 : listaPersonas){
                    if(res.equals(res2)){
                        encontro = true;
                    }
                }
                    
                if(!encontro){
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve") + " (4)");
                    return;
                }
            }
             */
            if (file != null && guardar) {
                if (file.getContent().length > 0) {

                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputStream());
                    } catch (IOException ex) {
                    }

                    //getSelected().setDocumento(bytes);
                    ResolucionesEscaneadas docEsc = null;

                    if (getSelected().getResolucionEscaneada() == null) {
                        docEsc = resolucionesEscaneadasController.prepareCreate(null);

                        docEsc.setEmpresa(usu.getEmpresa());
                        docEsc.setDocumento(bytes);

                        resolucionesEscaneadasController.setSelected(docEsc);
                        resolucionesEscaneadasController.saveNew(null);

                        getSelected().setResolucionEscaneada(resolucionesEscaneadasController.getSelected().getId());

                    } else {
                        try {
                            docEsc = this.ejbFacade.getEntityManager().createNamedQuery("ResolucionesEscaneadas.findById", ResolucionesEscaneadas.class).setParameter("id", getSelected().getResolucionEscaneada()).getSingleResult();
                            docEsc.setDocumento(bytes);
                            resolucionesEscaneadasController.setSelected(docEsc);
                            resolucionesEscaneadasController.save(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            docEsc = resolucionesEscaneadasController.prepareCreate(null);
                            docEsc.setDocumento(bytes);
                            docEsc.setEmpresa(usu.getEmpresa());
                            resolucionesEscaneadasController.setSelected(docEsc);
                            resolucionesEscaneadasController.saveNew(null);
                        }
                    }
                }
            }
        }

        if (guardar) {
            super.save(null);

            boolean guardarDoc = false;

            if (estadoProceso != null && getSelected().getDocumentoJudicial() != null) {
                if (!estadoProceso.equals(getSelected().getDocumentoJudicial().getEstadoProceso())) {
                    getSelected().getDocumentoJudicial().setEstadoProceso(estadoProceso);
                    guardarDoc = true;
                }
            }

            if (tipoExpediente != null && getSelected().getDocumentoJudicial() != null) {
                if (!tipoExpediente.equals(getSelected().getDocumentoJudicial().getTipoExpediente())) {
                    getSelected().getDocumentoJudicial().setTipoExpediente(tipoExpediente);
                    guardarDoc = true;
                }
            }

            // actualizarPersonas(Date fecha);
            if (guardarDoc) {
                docController.setSelected(getSelected().getDocumentoJudicial());
                docController.save(null);
                docController.setSelected(null);
            }

            if (ultimaObservacion != null) {
                if (!"".equals(ultimaObservacion)) {
                    if (!ultimaObservacion.equals(ultimaObservacionActual)) {
                        ObservacionesResoluciones obs = obsController.prepareCreate(null);

                        obs.setUsuarioAlta(usu);
                        obs.setUsuarioUltimoEstado(usu);
                        obs.setFechaHoraAlta(fechaActual);
                        obs.setFechaHoraUltimoEstado(fechaActual);
                        obs.setEmpresa(usu.getEmpresa());
                        obs.setObservacion(ultimaObservacion);
                        obs.setResolucion(getSelected());

                        obsController.setSelected(obs);
                        obsController.saveNew(null);
                    }
                }
            }

            // actualizarCambioResolucion(resAnt, getSelected(), usu, fechaActual, getSelected().getEmpresa());
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

                buscarPorFechaResolucion();

            }
        }
    }

    public void actualizarResuelve() {
        if (getSelected() != null) {
            if (tipoResolucion != null) {
                if (Constantes.TIPO_RESOLUCION_SD == tipoResolucion.getId()) {
                    listaResuelve = ejbFacade.getEntityManager().createNamedQuery("Resuelve.findByTipoResolucionOCancelacion", Resuelve.class).setParameter("tipoResolucion", tipoResolucion).getResultList();
                } else {
                    listaResuelve = ejbFacade.getEntityManager().createNamedQuery("Resuelve.findByTipoResolucion", Resuelve.class).setParameter("tipoResolucion", tipoResolucion).getResultList();
                }
                listaSubtiposResolucion = ejbFacade.getEntityManager().createNamedQuery("SubtiposResolucion.findByTipoResolucion", SubtiposResolucion.class).setParameter("tipoResolucion", tipoResolucion).getResultList();
            } else {
                listaResuelve = new ArrayList<>();
                listaSubtiposResolucion = new ArrayList<>();
            }
        } else {
            listaResuelve = new ArrayList<>();
            listaSubtiposResolucion = new ArrayList<>();
        }
    }

    /*
    public void actualizarObservacionYEstadoProceso(){
        if(getSelected() != null){
            if(resuelve != null){
                if(!Constantes.RESUELVE_CANCELACION.equals(resuelve.getCodigo()) && !Constantes.RESUELVE_ENJUICIAMIENTO.equals(resuelve.getCodigo())){
                    ultimaObservacion = "";
                }
            }else{
                ultimaObservacion = "";
            }
        }
        
        actualizarEstadoProceso();
    }
     */
 /*
    public void actualizarEstadoProceso() {
        if (getSelected() != null) {
            if (resuelve != null) {
                if (Constantes.RESUELVE_ENJUICIAMIENTO.equals(resuelve.getCodigo())) {
                    estadoProceso = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_EN_TRAMITE).getSingleResult();
                } else {
                    estadoProceso = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_FINALIZADO).getSingleResult();
                }
            } else {
                estadoProceso = null;
            }

        }
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
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    @Override
    public Collection<Resoluciones> getItems() {
        return super.getItems2();
    }

    public void buscarPorFechaResolucion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findOrdered", Resoluciones.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());
        }
    }
    public void realizarBusqueda() {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaDesde);
        cal2.add(Calendar.DATE, -1);
        fechaDesde = cal2.getTime();
        
        resolucionesbusqueda = ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findByFechaCaratula", Resoluciones.class)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .setParameter("caratula", "%"+busqueda+"%")
                .getResultList();
                
    }
    /*
    public boolean deshabilitarObservacion(){
        if(getSelected() != null){
            if(resuelve != null){
                if(resuelve.getCodigo() != null){
                    if(Constantes.RESUELVE_ENJUICIAMIENTO.equals(resuelve.getCodigo()) || Constantes.RESUELVE_CANCELACION.equals(resuelve.getCodigo())){
                        return false;
                    }
                }
            }
        }
        return true;
    }
     */
    public String navigateObservacionesResolucionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("res_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/resoluciones/index");

        }
        return "/pages/observacionesResoluciones/index";
    }
    
    public boolean deshabilitarSubtipoResolucion() {
        if (getSelected() != null) {
            if (tipoResolucion != null) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarVerObs() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_OBSERVACIONES_RESOLUCIONES)) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarRepAntecedentes() {
        /*
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.IMPRIMIR_REP_ANTECEDENTES)){
                return false;
            }
        }
        
        return true;
         */
        return getSelected()==null;
    }

    public boolean deshabilitarFirmado() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_FIRMADO)) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarFechaSesion() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_FECHA_SESION)) {
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

    public boolean deshabilitarAdjuntar() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_ADJUNTAR_DOCUMENTO_JUDICIAL)) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarPersona() {
        if (getSelected() != null) {
            if (getSelected().getDocumentoJudicial() == null) {
                return true;
            }
        }

        return false;
    }

    public boolean deshabilitarEstadoProceso() {
        if (getSelected() != null) {
            if (tipoResolucion != null) {
                if (Constantes.TIPO_RESOLUCION_AI == tipoResolucion.getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean deshabilitarResuelve() {
        if (getSelected() != null) {
            if (tipoResolucion != null) {
                if (Constantes.TIPO_RESOLUCION_SD == tipoResolucion.getId()) {
                    return false;
                }
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

    public boolean deshabilitarDocumentoJudicial() {
        if (getSelected() != null) {
            if (documentoJudicial == null) {
                return false;
            }
        }
        return true;
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
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findOrderedFechaAlta", Resoluciones.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
        }
    }

    public void verDoc() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            if (getSelected().getResolucionEscaneada() != null) {
                try {
                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.setContentType("application/pdf");
                    // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                    httpServletResponse.addHeader("Content-disposition", "inline; filename=documento.pdf");

                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    ResolucionesEscaneadas docEsc = ejbFacade.getEntityManager().createNamedQuery("ResolucionesEscaneadas.findById", ResolucionesEscaneadas.class).setParameter("id", getSelected().getResolucionEscaneada()).getSingleResult();

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
