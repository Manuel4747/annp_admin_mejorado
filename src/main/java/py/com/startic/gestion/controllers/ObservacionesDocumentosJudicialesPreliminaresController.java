package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.ObservacionesDocumentosJudicialesPreliminares;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.DocumentosJudicialesPreliminares;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "observacionesDocumentosJudicialesPreliminaresController")
@ViewScoped
public class ObservacionesDocumentosJudicialesPreliminaresController extends AbstractController<ObservacionesDocumentosJudicialesPreliminares> {

    @Inject
    private DocumentosJudicialesPreliminaresController documentoJudicialPreliminarController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    private DocumentosJudicialesPreliminares docOrigen;
    private String paginaVolver;

    public ObservacionesDocumentosJudicialesPreliminaresController() {
        // Inform the Abstract parent controller of the concrete ObservacionesDocumentosJudiciales Entity
        super(ObservacionesDocumentosJudicialesPreliminares.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        docOrigen = (DocumentosJudicialesPreliminares) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        documentoJudicialPreliminarController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }
/*
    @Override
    public ObservacionesDocumentosJudiciales prepareCreate(ActionEvent event) {
        ObservacionesDocumentosJudiciales obsDoc = super.prepareCreate(event);

        if (docOrigen != null) {
            obsDoc.setDocumentoJudicial(docOrigen);
        }

        return obsDoc;
    }
    */
    /**
     * Sets the "selected" attribute of the DocumentosJudiciales controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDocumentoJudicialPreliminar(ActionEvent event) {
        if (this.getSelected() != null && documentoJudicialPreliminarController.getSelected() == null) {
            documentoJudicialPreliminarController.setSelected(this.getSelected().getDocumentoJudicialPreliminar());
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
     * Sets the "items" attribute with a collection of DocumentosJudiciales
     * entities that are retrieved from
     * ObservacionesDocumentosJudiciales?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for DocumentosJudiciales page
     */
    public String navigateDocumentosJudicialesPreliminaresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentosJudicialesPreliminares_items", this.getSelected().getDocumentosJudicialesPreliminaresCollection());
        }
        return "/pages/documentosJudicialesPreliminares/index";
    }

    public String navigateVolver() {
        return paginaVolver;
    }
    
    @Override
    public Collection<ObservacionesDocumentosJudicialesPreliminares> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudicialesPreliminares.findByDocumentoJudicialPreliminar", ObservacionesDocumentosJudicialesPreliminares.class).setParameter("documentoJudicialPreliminar", docOrigen).getResultList();
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

            super.saveNew(event);
/*
            if (docOrigen != null) {

                docOrigen.setObservacionDocumentoJudicial(getSelected());
                docOrigen.setUltimaObservacion(getSelected().getObservacion());

                documentoJudicialController.save(event);
            }
*/
            // setItems(ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", docOrigen).getResultList());

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findById", ObservacionesBienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }
    }
}
