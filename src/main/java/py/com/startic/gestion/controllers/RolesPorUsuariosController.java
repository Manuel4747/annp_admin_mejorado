package py.com.startic.gestion.controllers;

import java.util.Date;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.RolesPorUsuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "rolesPorUsuariosController")
@ViewScoped
public class RolesPorUsuariosController extends AbstractController<RolesPorUsuarios> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosController estadoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private RolesController rolesController;
    @Inject
    private UsuariosController usuariosController;

    public RolesPorUsuariosController() {
        // Inform the Abstract parent controller of the concrete RolesPorUsuarios Entity
        super(RolesPorUsuarios.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getRolesPorUsuariosPK().setUsuario(this.getSelected().getUsuarios().getId());
        this.getSelected().getRolesPorUsuariosPK().setRol(this.getSelected().getRoles().getId());
        this.getSelected().getRolesPorUsuariosPK().setPerfil(this.getSelected().getPerfiles().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setRolesPorUsuariosPK(new py.com.startic.gestion.models.RolesPorUsuariosPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        rolesController.setSelected(null);
        usuariosController.setSelected(null);
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarios(ActionEvent event) {
        if (this.getSelected() != null && usuariosController.getSelected() == null) {
            usuariosController.setSelected(this.getSelected().getUsuarios());
        }
    }

    public void prepareEdit(RolesPorUsuarios item) {
        setSelected(item);
    }

    public void borrar(RolesPorUsuarios item) {
        setSelected(item);
        super.delete(null);
        setSelected(null);
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

        setSelected(null);
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

        }

    }
}
