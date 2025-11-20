package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetallePlanFinanciero;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnDetallePlanFinancieroController")
@ViewScoped
public class FnDetallePlanFinancieroController extends AbstractController<FnDetallePlanFinanciero> {

    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private FnDistribucionController distribucionController;
    @Inject
    private FnFuentesDeFinanciamientoController fuentesDeFinanciamientoController;
    @Inject
    private FnObjetosDeGastoController objetosDeGastoController;
    @Inject
    private FnPlanFinancieroController planFinancieroController;
    @Inject
    private FnProyectosController proyectoController;
    @Inject
    private FnSubDistribucionController subDistribucionController;

    public FnDetallePlanFinancieroController() {
        // Inform the Abstract parent controller of the concrete FnDetallePlanFinanciero Entity
        super(FnDetallePlanFinanciero.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        departamentoController.setSelected(null);
        distribucionController.setSelected(null);
        fuentesDeFinanciamientoController.setSelected(null);
        objetosDeGastoController.setSelected(null);
        planFinancieroController.setSelected(null);
        proyectoController.setSelected(null);
        subDistribucionController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnDepartamento controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
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
     * Sets the "selected" attribute of the FnFuentesDeFinanciamiento controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareFuentesDeFinanciamiento(ActionEvent event) {
        if (this.getSelected() != null && fuentesDeFinanciamientoController.getSelected() == null) {
            fuentesDeFinanciamientoController.setSelected(this.getSelected().getFuentesDeFinanciamiento());
        }
    }

    /**
     * Sets the "selected" attribute of the FnObjetosDeGasto controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareObjetosDeGasto(ActionEvent event) {
        if (this.getSelected() != null && objetosDeGastoController.getSelected() == null) {
            objetosDeGastoController.setSelected(this.getSelected().getObjetosDeGasto());
        }
    }

    /**
     * Sets the "selected" attribute of the FnPlanFinanciero controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePlanFinanciero(ActionEvent event) {
        if (this.getSelected() != null && planFinancieroController.getSelected() == null) {
            planFinancieroController.setSelected(this.getSelected().getPlanFinanciero());
        }
    }

    /**
     * Sets the "selected" attribute of the FnProyectos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProyecto(ActionEvent event) {
        if (this.getSelected() != null && proyectoController.getSelected() == null) {
            proyectoController.setSelected(this.getSelected().getProyecto());
        }
    }

    /**
     * Sets the "selected" attribute of the FnSubDistribucion controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSubDistribucion(ActionEvent event) {
        if (this.getSelected() != null && subDistribucionController.getSelected() == null) {
            subDistribucionController.setSelected(this.getSelected().getSubDistribucion());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleReprogramacion
     * entities that are retrieved from FnDetallePlanFinanciero?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnDetalleReprogramacion page
     */
    public String navigateFnDetalleReprogramacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleReprogramacion_items", this.getSelected().getFnDetalleReprogramacionCollection());
        }
        return "/pages/fnDetalleReprogramacion/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleReprogramacion
     * entities that are retrieved from FnDetallePlanFinanciero?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnDetalleReprogramacion page
     */
    public String navigateFnDetalleReprogramacionCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleReprogramacion_items", this.getSelected().getFnDetalleReprogramacionCollection1());
        }
        return "/pages/fnDetalleReprogramacion/index";
    }

}
