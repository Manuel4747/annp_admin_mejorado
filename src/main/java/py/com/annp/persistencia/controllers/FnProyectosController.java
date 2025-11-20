package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnProyectos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnProyectosController")
@ViewScoped
public class FnProyectosController extends AbstractController<FnProyectos> {

    public FnProyectosController() {
        // Inform the Abstract parent controller of the concrete FnProyectos Entity
        super(FnProyectos.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePlanFinanciero
     * entities that are retrieved from FnProyectos?cap_first and returns the
     * navigation outcome.
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
     * entities that are retrieved from FnProyectos?cap_first and returns the
     * navigation outcome.
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
