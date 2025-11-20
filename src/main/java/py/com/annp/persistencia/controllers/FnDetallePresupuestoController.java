package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetallePresupuesto;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnDetallePresupuestoController")
@ViewScoped
public class FnDetallePresupuestoController extends AbstractController<FnDetallePresupuesto> {

    @Inject
    private FnDistribucionController distribucionController;
    @Inject
    private FnObjetosDeGastoController objetoDeGastoController;
    @Inject
    private FnPresupuestoController presupuestoController;
    @Inject
    private FnProyectosController proyectoController;
    @Inject
    private FnSubDistribucionController subdistribucionController;

    public FnDetallePresupuestoController() {
        // Inform the Abstract parent controller of the concrete FnDetallePresupuesto Entity
        super(FnDetallePresupuesto.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        distribucionController.setSelected(null);
        objetoDeGastoController.setSelected(null);
        presupuestoController.setSelected(null);
        proyectoController.setSelected(null);
        subdistribucionController.setSelected(null);
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
     * Sets the "selected" attribute of the FnObjetosDeGasto controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareObjetoDeGasto(ActionEvent event) {
        if (this.getSelected() != null && objetoDeGastoController.getSelected() == null) {
            objetoDeGastoController.setSelected(this.getSelected().getObjetoDeGasto());
        }
    }

    /**
     * Sets the "selected" attribute of the FnPresupuesto controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePresupuesto(ActionEvent event) {
        if (this.getSelected() != null && presupuestoController.getSelected() == null) {
            presupuestoController.setSelected(this.getSelected().getPresupuesto());
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
    public void prepareSubdistribucion(ActionEvent event) {
        if (this.getSelected() != null && subdistribucionController.getSelected() == null) {
            subdistribucionController.setSelected(this.getSelected().getSubdistribucion());
        }
    }
}
