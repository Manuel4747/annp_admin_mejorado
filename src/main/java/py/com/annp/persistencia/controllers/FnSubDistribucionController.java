package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnSubDistribucion;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnSubDistribucionController")
@ViewScoped
public class FnSubDistribucionController extends AbstractController<FnSubDistribucion> {

    @Inject
    private FnDistribucionController distribucionController;

    public FnSubDistribucionController() {
        // Inform the Abstract parent controller of the concrete FnSubDistribucion Entity
        super(FnSubDistribucion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        distribucionController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePlanFinanciero
     * entities that are retrieved from FnSubDistribucion?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for FnDetallePlanFinanciero page
     */
    public String navigateFnDetallePlanFinancieroCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetallePlanFinanciero_items", this.getSelected().getFnDetallePlanFinancieroCollection());
        }
        return "/pages/fnDetallePlanFinanciero/index";
    }

    /**
     * Sets the "selected" attribute of the FnDistribucion controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDistribucion(ActionEvent event) {
        if (this.getSelected() != null && distribucionController.getSelected() == null) {
            distribucionController.setSelected(this.getSelected().getDistribucion());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePresupuesto
     * entities that are retrieved from FnSubDistribucion?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for FnDetallePresupuesto page
     */
    public String navigateFnDetallePresupuestoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetallePresupuesto_items", this.getSelected().getFnDetallePresupuestoCollection());
        }
        return "/pages/fnDetallePresupuesto/index";
    }

}
