package py.com.startic.gestion.controllers;

import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.AvisosPorRoles;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "avisosPorRolesController")
@ViewScoped
public class AvisosPorRolesController extends AbstractController<AvisosPorRoles> {

    public AvisosPorRolesController() {
        // Inform the Abstract parent controller of the concrete AvisosPorRoles Entity
        super(AvisosPorRoles.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getAvisosPorRolesPK().setPermiso(this.getSelected().getAvisos().getId());
        this.getSelected().getAvisosPorRolesPK().setRol(this.getSelected().getRoles().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setAvisosPorRolesPK(new py.com.startic.gestion.models.AvisosPorRolesPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
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

            super.saveNew(event);

        }

    }
}
