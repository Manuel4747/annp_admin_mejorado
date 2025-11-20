package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnPresupuesto;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnPresupuestoController")
@ViewScoped
public class FnPresupuestoController extends AbstractController<FnPresupuesto> {

    @Inject
    private FnOrganismosFinaciadoresController organismoFinanciadorController;
    @Inject
    private FnProgramasController programaController;
    @Inject
    private FnSubProgramaController subProgramaController;
    @Inject
    private FnTiposPresupuestoController tipoPresupuestoController;

    public FnPresupuestoController() {
        // Inform the Abstract parent controller of the concrete FnPresupuesto Entity
        super(FnPresupuesto.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        organismoFinanciadorController.setSelected(null);
        programaController.setSelected(null);
        subProgramaController.setSelected(null);
        tipoPresupuestoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnOrganismosFinaciadores controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareOrganismoFinanciador(ActionEvent event) {
        if (this.getSelected() != null && organismoFinanciadorController.getSelected() == null) {
            organismoFinanciadorController.setSelected(this.getSelected().getOrganismoFinanciador());
        }
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
     * Sets the "selected" attribute of the FnSubPrograma controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSubPrograma(ActionEvent event) {
        if (this.getSelected() != null && subProgramaController.getSelected() == null) {
            subProgramaController.setSelected(this.getSelected().getSubPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the FnTiposPresupuesto controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoPresupuesto(ActionEvent event) {
        if (this.getSelected() != null && tipoPresupuestoController.getSelected() == null) {
            tipoPresupuestoController.setSelected(this.getSelected().getTipoPresupuesto());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePresupuesto
     * entities that are retrieved from FnPresupuesto?cap_first and returns the
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
