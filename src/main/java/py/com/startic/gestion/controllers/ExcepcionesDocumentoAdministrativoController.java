package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import py.com.startic.gestion.models.ExcepcionesDocumentoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "excepcionesDocumentoAdministrativoController")
@ViewScoped
public class ExcepcionesDocumentoAdministrativoController extends AbstractController<ExcepcionesDocumentoAdministrativo> {

    public ExcepcionesDocumentoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete ExcepcionesDocumentoAdministrativo Entity
        super(ExcepcionesDocumentoAdministrativo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExcepcionesDocumentoAdministrativo> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("ExcepcionesDocumentoAdministrativo.findOrdered", ExcepcionesDocumentoAdministrativo.class).getResultList();
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

            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);

            super.saveNew(event);
            
        }
    }
}
