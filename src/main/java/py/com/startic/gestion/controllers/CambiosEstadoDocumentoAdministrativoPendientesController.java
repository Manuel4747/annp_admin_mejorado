package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.CambiosEstadoDocumentoAdministrativoPendientes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.DocumentosAdministrativos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "cambiosEstadoDocumentoAdministrativoPendientesController")
@ViewScoped
public class CambiosEstadoDocumentoAdministrativoPendientesController extends AbstractController<CambiosEstadoDocumentoAdministrativoPendientes> {

    @Inject
    private DocumentosAdministrativosController documentoAdministrativoController;
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

    private DocumentosAdministrativos documentoAdministrativoOrigen;
    private String paginaVolver;

    public DocumentosAdministrativos getDocumentoAdministrativoOrigen() {
        return documentoAdministrativoOrigen;
    }

    public void setDocumentoAdministrativoOrigen(DocumentosAdministrativos documentoAdministrativoOrigen) {
        this.documentoAdministrativoOrigen = documentoAdministrativoOrigen;
    }

    public CambiosEstadoDocumentoAdministrativoPendientesController() {
        // Inform the Abstract parent controller of the concrete CambiosEstadoDocumentoPendientes Entity
        super(CambiosEstadoDocumentoAdministrativoPendientes.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        documentoAdministrativoOrigen = (DocumentosAdministrativos) session.getAttribute("documento_administrativo_origen");

        session.removeAttribute("documento_Administrativo_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        documentoAdministrativoController.setSelected(null);
        departamentoOrigenController.setSelected(null);
        departamentoDestinoController.setSelected(null);
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        responsableOrigenController.setSelected(null);
        responsableDestinoController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    public String navigateVolver() {
        return paginaVolver;
    }
    
    @Override
    public CambiosEstadoDocumentoAdministrativoPendientes prepareCreate(ActionEvent event) {
        CambiosEstadoDocumentoAdministrativoPendientes movBien = super.prepareCreate(event);

        if (documentoAdministrativoOrigen != null) {
            movBien.setDocumentoAdministrativo(documentoAdministrativoOrigen);
            movBien.setResponsableOrigen(documentoAdministrativoOrigen.getResponsable());
            movBien.setDepartamentoOrigen(documentoAdministrativoOrigen.getDepartamento());

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
    public Collection<CambiosEstadoDocumentoAdministrativoPendientes> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativo", CambiosEstadoDocumentoAdministrativoPendientes.class).setParameter("documentoAdministrativo", documentoAdministrativoOrigen).getResultList();
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
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);

            documentoAdministrativoOrigen.setResponsable(getSelected().getResponsableDestino());
            documentoAdministrativoOrigen.setDepartamento(getSelected().getDepartamentoDestino());

            documentoAdministrativoController.setSelected(documentoAdministrativoOrigen);

            documentoAdministrativoController.save(event);

            setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoAdministrativoPendientes.findByBien", CambiosEstadoDocumentoAdministrativoPendientes.class).setParameter("bien", documentoAdministrativoOrigen).getResultList());

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
            if (getSelected().getResponsableDestino() != null) {
                getSelected().setDepartamentoDestino(getSelected().getResponsableDestino().getDepartamento());
            }

            super.saveNew(event);
        }
    }

    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("CambiosEstadoDocumentoAdministrativoPendientes.findByDocumentoAdministrativo", CambiosEstadoDocumentoAdministrativoPendientes.class).setParameter("documentoAdministrativo", documentoAdministrativoOrigen).getResultList());
    }
}
