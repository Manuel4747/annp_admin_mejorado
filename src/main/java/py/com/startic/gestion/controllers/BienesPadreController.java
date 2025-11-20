package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import py.com.startic.gestion.models.Bienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.CambiosRotuladosBienes;
import py.com.startic.gestion.models.ObservacionesBienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "bienesPadreController")
@ViewScoped
public class BienesPadreController extends AbstractController<Bienes> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosBienesController estadoController;
    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private UsuariosController responsableController;

    public BienesPadreController() {
        // Inform the Abstract parent controller of the concrete Bienes Entity
        super(Bienes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        proveedorController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        responsableController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FotosBienes entities that
     * are retrieved from Bienes?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for FotosBienes page
     */
    public String navigateFotosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FotosBienes_items", this.getSelected().getFotosBienesCollection());
        }
        return "/pages/fotosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of CambiosRotuladosBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for CambiosRotuladosBienes page
     */
    public String navigateCambiosRotuladosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("CambiosRotuladosBienes_items", ejbFacade.getEntityManager().createNamedQuery("CambiosRotuladosBienes.findByBien", CambiosRotuladosBienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/cambiosRotuladosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Bienes?cap_first and returns the navigation outcome.
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
     * Sets the "selected" attribute of the EstadosBienes controller in order to
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
     * Sets the "selected" attribute of the Proveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareResponsable(ActionEvent event) {
        if (this.getSelected() != null && responsableController.getSelected() == null) {
            responsableController.setSelected(this.getSelected().getResponsable());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ObservacionesBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ObservacionesBienes page
     */
    public String navigateObservacionesBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesBienes_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findByBien", Bienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/observacionesBienes/index";
    }



    /**
     * Sets the "items" attribute with a collection of Subcuentas entities that
     * are retrieved from Cuentas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Subcuentas page
     */
    public String navigateSubcuentasCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesBienes.findByBien", ObservacionesBienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/subcuentas/index";
    }
    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Bienes?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bien_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", ejbFacade.getEntityManager().createNamedQuery("MovimientosBienes.findByBien", Bienes.class).setParameter("bien", getSelected()).getResultList());
        }
        return "/pages/movimientosBienes/index";
    }

    @Override
    public Collection<Bienes> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Bienes.findPadres", Bienes.class).getResultList();
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
            if(getSelected().getResponsable() != null){
                getSelected().setDepartamento(getSelected().getResponsable().getDepartamento());
            }

            super.saveNew(event);

            // setSelected(ejbFacade.getEntityManager().createNamedQuery("Bienes.findById", Bienes.class).setParameter("id", getSelected().getId()).getSingleResult());

        }

    }
}
