package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnTiposPresupuesto;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnTiposPresupuestoController")
@ViewScoped
public class FnTiposPresupuestoController extends AbstractController<FnTiposPresupuesto> {

    public FnTiposPresupuestoController() {
        // Inform the Abstract parent controller of the concrete FnTiposPresupuesto Entity
        super(FnTiposPresupuesto.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnPresupuesto entities
     * that are retrieved from FnTiposPresupuesto?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnPresupuesto page
     */
    public String navigateFnPresupuestoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnPresupuesto_items", this.getSelected().getFnPresupuestoCollection());
        }
        return "/pages/fnPresupuesto/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnPlanFinanciero entities
     * that are retrieved from FnTiposPresupuesto?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnPlanFinanciero page
     */
    public String navigateFnPlanFinancieroCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnPlanFinanciero_items", this.getSelected().getFnPlanFinancieroCollection());
        }
        return "/pages/fnPlanFinanciero/index";
    }

}
