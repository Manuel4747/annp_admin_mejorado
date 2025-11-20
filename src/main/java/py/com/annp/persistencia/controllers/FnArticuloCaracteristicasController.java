package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnArticuloCaracteristicas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnArticuloCaracteristicasController")
@ViewScoped
public class FnArticuloCaracteristicasController extends AbstractController<FnArticuloCaracteristicas> {

    @Inject
    private FnArticulosController articuloController;

    public FnArticuloCaracteristicasController() {
        // Inform the Abstract parent controller of the concrete FnArticuloCaracteristicas Entity
        super(FnArticuloCaracteristicas.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        articuloController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnArticulos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareArticulo(ActionEvent event) {
        if (this.getSelected() != null && articuloController.getSelected() == null) {
            articuloController.setSelected(this.getSelected().getArticulo());
        }
    }
}
