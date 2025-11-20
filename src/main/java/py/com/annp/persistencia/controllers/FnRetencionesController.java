package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnRetenciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnRetencionesController")
@ViewScoped
public class FnRetencionesController extends AbstractController<FnRetenciones> {

    @Inject
    private FnPagosController pagoController;

    public FnRetencionesController() {
        // Inform the Abstract parent controller of the concrete FnRetenciones Entity
        super(FnRetenciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        pagoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnPagos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePago(ActionEvent event) {
        if (this.getSelected() != null && pagoController.getSelected() == null) {
            pagoController.setSelected(this.getSelected().getPago());
        }
    }
}
