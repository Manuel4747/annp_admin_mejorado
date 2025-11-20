package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.EstadosProcesalesDocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.DocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "estadosProcesalesDocumentosAdministrativosController")
@ViewScoped
public class EstadosProcesalesDocumentosAdministrativosController extends AbstractController<EstadosProcesalesDocumentosAdministrativos> {

    private DocumentosJudiciales docOrigen;
    private String paginaVolver;

    public EstadosProcesalesDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete EstadosProcesalesDocumentosAdministrativos Entity
        super(EstadosProcesalesDocumentosAdministrativos.class);
    }


    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        docOrigen = (DocumentosJudiciales) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }
    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<EstadosProcesalesDocumentosAdministrativos> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosAdministrativos.findByDocumentoJudicial", EstadosProcesalesDocumentosAdministrativos.class).setParameter("documentoJudicial", docOrigen).getResultList();
    }

    public String navigateVolver() {
        return paginaVolver;
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

            super.saveNew(event);
        }
    }
}
