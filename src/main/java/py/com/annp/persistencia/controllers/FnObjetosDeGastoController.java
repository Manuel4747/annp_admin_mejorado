package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnObjetosDeGasto;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnObjetosDeGastoController")
@ViewScoped
public class FnObjetosDeGastoController extends AbstractController<FnObjetosDeGasto> {

    public FnObjetosDeGastoController() {
        // Inform the Abstract parent controller of the concrete FnObjetosDeGasto Entity
        super(FnObjetosDeGasto.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePlanFinanciero
     * entities that are retrieved from FnObjetosDeGasto?cap_first and returns
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
     * Sets the "items" attribute with a collection of FnDetallePresupuesto
     * entities that are retrieved from FnObjetosDeGasto?cap_first and returns
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
