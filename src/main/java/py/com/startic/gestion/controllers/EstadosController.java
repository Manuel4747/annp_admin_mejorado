package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Estados;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "estadosController")
@ViewScoped
public class EstadosController extends AbstractController<Estados> {

    @Inject
    private EmpresasController empresaController;

    public EstadosController() {
        // Inform the Abstract parent controller of the concrete Estados Entity
        super(Estados.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Estados?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of PermisosPorRoles entities
     * that are retrieved from Estados?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for PermisosPorRoles page
     */
    public String navigatePermisosPorRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("PermisosPorRoles_items", this.getSelected().getPermisosPorRolesCollection());
        }
        return "/pages/permisosPorRoles/index";
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
}
