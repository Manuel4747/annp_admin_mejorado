package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.Subcuentas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Cuentas;
import py.com.startic.gestion.models.EspecificacionesSubcuenta;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "subcuentasController")
@ViewScoped
public class SubcuentasController extends AbstractController<Subcuentas> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private CuentasController cuentaController;
    private Cuentas cuentaOrigen;

    public SubcuentasController() {
        // Inform the Abstract parent controller of the concrete Subcuentas Entity
        super(Subcuentas.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        cuentaOrigen = (Cuentas) session.getAttribute("cuenta_origen");

        session.removeAttribute("cuenta_origen");

    }

    
    @Override
    public Collection<Subcuentas> getItems() {
        return getItems2();
    }
    
    @Override
    public Subcuentas prepareCreate(ActionEvent event) {
        Subcuentas subcuenta = super.prepareCreate(event);

        if (cuentaOrigen != null) {
            subcuenta.setCuenta(cuentaOrigen);
        }

        return subcuenta;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
        cuentaController.setSelected(null);
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
     * Sets the "selected" attribute of the Cuentas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCuenta(ActionEvent event) {
        if (this.getSelected() != null && cuentaController.getSelected() == null) {
            cuentaController.setSelected(this.getSelected().getCuenta());
        }
    }
    
    /**
     * Sets the "items" attribute with a collection of EspecificacionesSubcuenta entities that
     * are retrieved from Cuentas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Subcuentas page
     */
    public String navigateEspecificacionesSubcuentaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("subcuenta_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EspecificacionesSubcuenta_items", ejbFacade.getEntityManager().createNamedQuery("EspecificacionesSubcuenta.findBySubcuenta", EspecificacionesSubcuenta.class).setParameter("subcuenta", getSelected()).getResultList());
        }
        return "/pages/especificacionesSubcuenta/index";
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

            super.saveNew(event);

            
            setItems(ejbFacade.getEntityManager().createNamedQuery("Subcuentas.findByCuenta", Subcuentas.class).setParameter("cuenta", cuentaOrigen).getResultList());

        }
    }
    
    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("Subcuentas.findByCuenta", Subcuentas.class).setParameter("cuenta", cuentaOrigen).getResultList());
    }
}
