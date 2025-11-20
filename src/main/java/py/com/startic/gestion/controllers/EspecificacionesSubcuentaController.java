package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.EspecificacionesSubcuenta;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Subcuentas;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "especificacionesSubcuentaController")
@ViewScoped
public class EspecificacionesSubcuentaController extends AbstractController<EspecificacionesSubcuenta> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private SubcuentasController subcuentaController;
    private Subcuentas subcuentaOrigen;

    public EspecificacionesSubcuentaController() {
        // Inform the Abstract parent controller of the concrete EspecificacionesSubcuenta Entity
        super(EspecificacionesSubcuenta.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
        subcuentaController.setSelected(null);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        subcuentaOrigen = (Subcuentas) session.getAttribute("subcuenta_origen");

        session.removeAttribute("subcuenta_origen");

    }

    @Override
    public Collection<EspecificacionesSubcuenta> getItems() {
        return getItems2();
    }

    @Override
    public EspecificacionesSubcuenta prepareCreate(ActionEvent event) {
        EspecificacionesSubcuenta esp = super.prepareCreate(event);

        if (subcuentaOrigen != null) {
            esp.setSubcuenta(subcuentaOrigen);
        }

        return esp;
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
     * Sets the "selected" attribute of the Subcuentas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSubcuenta(ActionEvent event) {
        if (this.getSelected() != null && subcuentaController.getSelected() == null) {
            subcuentaController.setSelected(this.getSelected().getSubcuenta());
        }
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

            setItems(ejbFacade.getEntityManager().createNamedQuery("EspecificacionesSubcuenta.findBySubcuenta", EspecificacionesSubcuenta.class).setParameter("subcuenta", subcuentaOrigen).getResultList());

        }
    }

    @Override
    public void delete(ActionEvent event) {
        super.delete(event);
        setItems(ejbFacade.getEntityManager().createNamedQuery("EspecificacionesSubcuenta.findBySubcuenta", EspecificacionesSubcuenta.class).setParameter("subcuenta", subcuentaOrigen).getResultList());
    }
}
