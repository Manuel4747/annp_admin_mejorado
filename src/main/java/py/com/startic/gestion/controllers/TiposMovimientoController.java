package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposMovimiento;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "tiposMovimientoController")
@ViewScoped
public class TiposMovimientoController extends AbstractController<TiposMovimiento> {

    @Inject
    private EmpresasController empresaController;

    public TiposMovimientoController() {
        // Inform the Abstract parent controller of the concrete TiposMovimiento Entity
        super(TiposMovimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * TiposMovimiento?cap_first and returns the navigation outcome.
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
}
