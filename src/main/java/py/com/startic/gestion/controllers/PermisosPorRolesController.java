package py.com.startic.gestion.controllers;

import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.PermisosPorRoles;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "permisosPorRolesController")
@ViewScoped
public class PermisosPorRolesController extends AbstractController<PermisosPorRoles> {

    @Inject
    private PermisosController permisosController;
    @Inject
    private RolesController rolesController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosController estadoController;

    public PermisosPorRolesController() {
        // Inform the Abstract parent controller of the concrete PermisosPorRoles Entity
        super(PermisosPorRoles.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getPermisosPorRolesPK().setPermiso(this.getSelected().getPermisos().getId());
        this.getSelected().getPermisosPorRolesPK().setRol(this.getSelected().getRoles().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setPermisosPorRolesPK(new py.com.startic.gestion.models.PermisosPorRolesPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        permisosController.setSelected(null);
        rolesController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Permisos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePermisos(ActionEvent event) {
        if (this.getSelected() != null && permisosController.getSelected() == null) {
            permisosController.setSelected(this.getSelected().getPermisos());
        }
    }

    /**
     * Sets the "selected" attribute of the Roles controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRoles(ActionEvent event) {
        if (this.getSelected() != null && rolesController.getSelected() == null) {
            rolesController.setSelected(this.getSelected().getRoles());
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
     * Sets the "selected" attribute of the Estados controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
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

            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);

        }

    }
}
