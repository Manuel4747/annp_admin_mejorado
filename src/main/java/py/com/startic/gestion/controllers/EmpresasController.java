package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.Empresas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "empresasController")
@ViewScoped
public class EmpresasController extends AbstractController<Empresas> {


    public EmpresasController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(Empresas.class);
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection());
        }
        return "/pages/usuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of EstadosUsuario entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for EstadosUsuario page
     */
    public String navigateEstadosUsuarioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosUsuario_items", this.getSelected().getEstadosUsuarioCollection());
        }
        return "/pages/estadosUsuario/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Empresas?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of Sexos entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Sexos page
     */
    public String navigateSexosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Sexos_items", this.getSelected().getSexosCollection());
        }
        return "/pages/sexos/index";
    }

    /**
     * Sets the "items" attribute with a collection of PermisosPorRoles entities
     * that are retrieved from Empresas?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection());
        }
        return "/pages/proveedores/index";
    }

    /**
     * Sets the "items" attribute with a collection of TiposPersona entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for TiposPersona page
     */
    public String navigateTiposPersonaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("TiposPersona_items", this.getSelected().getTiposPersonaCollection());
        }
        return "/pages/tiposPersona/index";
    }

    /**
     * Sets the "items" attribute with a collection of Pantallas entities that
     * are retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Pantallas page
     */
    public String navigatePantallasCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Pantallas_items", this.getSelected().getPantallasCollection());
        }
        return "/pages/pantallas/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosReparacionBienes page
     */
    public String navigateMovimientosReparacionBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of TiposMovimiento entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for TiposMovimiento page
     */
    public String navigateTiposMovimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("TiposMovimiento_items", this.getSelected().getTiposMovimientoCollection());
        }
        return "/pages/tiposMovimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Permisos entities that
     * are retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Permisos page
     */
    public String navigatePermisosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Permisos_items", this.getSelected().getPermisosCollection());
        }
        return "/pages/permisos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Estados entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Estados page
     */
    public String navigateEstadosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Estados_items", this.getSelected().getEstadosCollection());
        }
        return "/pages/estados/index";
    }

    /**
     * Sets the "items" attribute with a collection of Indices entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Indices page
     */
    public String navigateIndicesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Indices_items", this.getSelected().getIndicesCollection());
        }
        return "/pages/indices/index";
    }
    
    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Empresas?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection());
        }
        return "/pages/movimientosBienes/index";
    }
     
}
