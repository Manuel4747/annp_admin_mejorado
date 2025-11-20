package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.CambiosEstadoDocumentoPendientes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.DocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "cambiosEstadoDocumentoPendientesController")
@ViewScoped
public class CambiosEstadoDocumentoPendientesController extends AbstractController<CambiosEstadoDocumentoPendientes> {

    @Inject
    private DocumentosJudicialesController documentoJudicialController;
    @Inject
    private DepartamentosController departamentoOrigenController;
    @Inject
    private DepartamentosController departamentoDestinoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController responsableOrigenController;
    @Inject
    private UsuariosController responsableDestinoController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    private DocumentosJudiciales documentoJudicialOrigen;
    private String paginaVolver;

    public DocumentosJudiciales getDocumentoJudicialOrigen() {
        return documentoJudicialOrigen;
    }

    public void setDocumentoJudicialOrigen(DocumentosJudiciales documentoJudicialOrigen) {
        this.documentoJudicialOrigen = documentoJudicialOrigen;
    }

    public CambiosEstadoDocumentoPendientesController() {
        // Inform the Abstract parent controller of the concrete CambiosEstadoDocumentoPendientes Entity
        super(CambiosEstadoDocumentoPendientes.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        documentoJudicialOrigen = (DocumentosJudiciales) session.getAttribute("documento_judicial_origen");

        session.removeAttribute("documento_judicial_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        documentoJudicialController.setSelected(null);
        departamentoOrigenController.setSelected(null);
        departamentoDestinoController.setSelected(null);
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        responsableOrigenController.setSelected(null);
        responsableDestinoController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Bienes controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDocumentoJudicial(ActionEvent event) {
        if (this.getSelected() != null && documentoJudicialController.getSelected() == null) {
            documentoJudicialController.setSelected(this.getSelected().getDocumentoJudicial());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamentoOrigen(ActionEvent event) {
        if (this.getSelected() != null && departamentoOrigenController.getSelected() == null) {
            departamentoOrigenController.setSelected(this.getSelected().getDepartamentoOrigen());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamentoDestino(ActionEvent event) {
        if (this.getSelected() != null && departamentoDestinoController.getSelected() == null) {
            departamentoDestinoController.setSelected(this.getSelected().getDepartamentoDestino());
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
    public void prepareResponsableOrigen(ActionEvent event) {
        if (this.getSelected() != null && responsableOrigenController.getSelected() == null) {
            responsableOrigenController.setSelected(this.getSelected().getResponsableOrigen());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareResponsableDestino(ActionEvent event) {
        if (this.getSelected() != null && responsableDestinoController.getSelected() == null) {
            responsableDestinoController.setSelected(this.getSelected().getResponsableDestino());
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

    public String navigateVolver() {
        return paginaVolver;
    }
    
    @Override
    public CambiosEstadoDocumentoPendientes prepareCreate(ActionEvent event) {
        CambiosEstadoDocumentoPendientes movBien = super.prepareCreate(event);

        if (documentoJudicialOrigen != null) {
            movBien.setDocumentoJudicial(documentoJudicialOrigen);
            movBien.setResponsableOrigen(documentoJudicialOrigen.getResponsable());
            movBien.setDepartamentoOrigen(documentoJudicialOrigen.getDepartamento());

        }
        
        return movBien;
    }

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

    @Override
    public Collection<CambiosEstadoDocumentoPendientes> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoPendientes.findByDocumentoJudicial", CambiosEstadoDocumentoPendientes.class).setParameter("documentoJudicial", documentoJudicialOrigen).getResultList();
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
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);

            documentoJudicialOrigen.setResponsable(getSelected().getResponsableDestino());
            documentoJudicialOrigen.setDepartamento(getSelected().getDepartamentoDestino());

            documentoJudicialController.setSelected(documentoJudicialOrigen);

            documentoJudicialController.save(event);

            setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoPendientes.findByBien", CambiosEstadoDocumentoPendientes.class).setParameter("bien", documentoJudicialOrigen).getResultList());

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findById", ObservacionesBienes.class).setParameter("id", getSelected().getId()).getSingleResult());
        }
    }
    
    @Override
    public void saveNew2(ActionEvent event) {
        super.saveNew2(event);
    }

    public void saveNewOnly(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);
        }
    }

    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoPendientes.findByDocumentoJudicial", CambiosEstadoDocumentoPendientes.class).setParameter("documentoJudicial", documentoJudicialOrigen).getResultList());
    }
}
