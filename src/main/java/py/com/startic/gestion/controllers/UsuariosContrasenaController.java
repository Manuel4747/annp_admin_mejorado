package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.faces.application.FacesMessage;

import py.com.startic.gestion.models.Usuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;

@Named(value = "usuariosContrasenaController")
@ViewScoped
public class UsuariosContrasenaController extends AbstractController<Usuarios> {

    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosUsuarioController estadoController;
    @Inject
    private PantallasController pantallaPrincipalController;
    @Inject
    private SexosController sexoController;
    @Inject
    private UsuariosContrasenaController usuarioAltaController;
    @Inject
    private UsuariosContrasenaController usuarioUltimoEstadoController;
    private final Query query = new Query();

    private String usuario;
    private String contrasena;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String home;
    private String politicas;

    public UsuariosContrasenaController() {
        // Inform the Abstract parent controller of the concrete Usuarios Entity
        super(Usuarios.class);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        if (usu != null) {

            if ("FE".equals(usu.getSexo().getCodigo())) {
                nombreUsu = "Bienvenida ";
            } else {
                nombreUsu = "Bienvenido ";
            }
            nombreUsu += usu.getNombresApellidos() + " - (" + usu.getUsuario() + ")";
        }
        
        politicas = Utils.politicasContrasena;
    }

    public String getPoliticas() {
        return politicas;
    }

    public void setPoliticas(String politicas) {
        this.politicas = politicas;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
    
    public String obtenerHome() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        return (String) session.getAttribute("Home");
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        departamentoController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        pantallaPrincipalController.setSelected(null);
        sexoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection1());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection1());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
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
     * Sets the "selected" attribute of the EstadosUsuario controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Pantallas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePantallaPrincipal(ActionEvent event) {
        if (this.getSelected() != null && pantallaPrincipalController.getSelected() == null) {
            pantallaPrincipalController.setSelected(this.getSelected().getPantallaPrincipal());
        }
    }

    /**
     * Sets the "selected" attribute of the Sexos controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSexo(ActionEvent event) {
        if (this.getSelected() != null && sexoController.getSelected() == null) {
            sexoController.setSelected(this.getSelected().getSexo());
        }
    }

    /**
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection1());
        }
        return "/pages/usuarios/index";
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
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection1());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection2());
        }
        return "/pages/rolesPorUsuarios/index";
    }
    
    /**
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection1());
        }
        return "/pages/proveedores/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Usuarios?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosReparacionBienes page
     */
    public String navigateMovimientosReparacionBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection1());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection1());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection2());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
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

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection1());
        }
        return "/pages/movimientosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection2());
        }
        return "/pages/movimientosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection3() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection3());
        }
        return "/pages/movimientosBienes/index";
    }

    @Override
    public Collection<Usuarios> getItems() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
        Collection<Usuarios> col = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", usu.getId()).getResultList();
        if(col.size() > 0){
            setSelected(col.iterator().next());
        }
        
        return col;
    }

    public String loginControl() {

        Usuarios usu = query.loginControl(usuario, contrasena, "AC");
        if (usu != null) {
            if (usu.getPantallaPrincipal() != null) {
                home = usu.getPantallaPrincipal().getUrl();
            } else {
                home = "index";
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

            return home + ".xhtml?faces-redirect=true";
        }

        PrimeFaces.current().ajax().update("growl");
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error de acceso"));
        return "";

    }

    public String cerrarSession() {
        this.usuario = null;
        this.contrasena = null;
        HttpSession httpSession;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();//para borrar la session
        return "/login?faces-redirect=true";

    }
    
    public void setSelected(Usuarios selected) {
        super.setSelected(selected);
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

    public void saveContrasena(ActionEvent event) {

        if (getSelected() != null) {

            if (cambioContrasena1.equals(cambioContrasena2)) {
                
                
                String resp = Utils.politicasContrasena(cambioContrasena1);

                if (!"".equals(resp)) {
                    JsfUtil.addErrorMessage(resp);
                    return;
                }


                String password = Utils.passwordToHash(cambioContrasena1);
                ((Usuarios) getSelected()).setContrasena(password);
                cambioContrasena1 = "";
                cambioContrasena2 = "";
                this.save(event);

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }
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

            if (cambioContrasena1 != null && cambioContrasena2 != null) {
                if ("".equals(cambioContrasena1) || "".equals(cambioContrasena2)) {
                    JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena.");

                } else if (cambioContrasena1.equals(cambioContrasena2)) {

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                    Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                    Date fecha = ejbFacade.getSystemDate();

                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usu);
                    getSelected().setFechaHoraAlta(fecha);
                    getSelected().setUsuarioAlta(usu);
                    getSelected().setEmpresa(usu.getEmpresa());

                    getSelected().setContrasena(cambioContrasena1);
                    cambioContrasena1 = "";
                    cambioContrasena2 = "";
                    super.saveNew(event);

                } else {
                    JsfUtil.addErrorMessage("Contrasenas no coinciden");
                }
            } else {
                JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena");
            }
        }

    }
}
