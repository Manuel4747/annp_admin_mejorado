package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnSubPrograma;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnSubProgramaController")
@ViewScoped
public class FnSubProgramaController extends AbstractController<FnSubPrograma> {

    @Inject
    private FnProgramasController programaController;

    public FnSubProgramaController() {
        // Inform the Abstract parent controller of the concrete FnSubPrograma Entity
        super(FnSubPrograma.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnPresupuesto entities
     * that are retrieved from FnSubPrograma?cap_first and returns the
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
}
