package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnReprogramaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnReprogramacionesController")
@ViewScoped
public class FnReprogramacionesController extends AbstractController<FnReprogramaciones> {

    @Inject
    private FnTiposReprogramacionesController tiposReprogramacionesController;

    public FnReprogramacionesController() {
        // Inform the Abstract parent controller of the concrete FnReprogramaciones Entity
        super(FnReprogramaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tiposReprogramacionesController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnTiposReprogramaciones controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTiposReprogramaciones(ActionEvent event) {
        if (this.getSelected() != null && tiposReprogramacionesController.getSelected() == null) {
            tiposReprogramacionesController.setSelected(this.getSelected().getTiposReprogramaciones());
        }
    }
}
