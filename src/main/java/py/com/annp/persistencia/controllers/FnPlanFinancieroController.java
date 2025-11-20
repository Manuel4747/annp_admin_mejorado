package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnPlanFinanciero;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnPlanFinancieroController")
@ViewScoped
public class FnPlanFinancieroController extends AbstractController<FnPlanFinanciero> {

    @Inject
    private FnProgramasController programaController;
    @Inject
    private FnTiposPresupuestoController tiposPresupuestoController;

    public FnPlanFinancieroController() {
        // Inform the Abstract parent controller of the concrete FnPlanFinanciero Entity
        super(FnPlanFinanciero.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaController.setSelected(null);
        tiposPresupuestoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePlanFinanciero
     * entities that are retrieved from FnPlanFinanciero?cap_first and returns
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
     * Sets the "selected" attribute of the FnProgramas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePrograma(ActionEvent event) {
        if (this.getSelected() != null && programaController.getSelected() == null) {
            programaController.setSelected(this.getSelected().getPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the FnTiposPresupuesto controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTiposPresupuesto(ActionEvent event) {
        if (this.getSelected() != null && tiposPresupuestoController.getSelected() == null) {
            tiposPresupuestoController.setSelected(this.getSelected().getTiposPresupuesto());
        }
    }
}
