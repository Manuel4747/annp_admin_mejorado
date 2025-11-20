package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetalleLicitacion;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnDetalleLicitacionController")
@ViewScoped
public class FnDetalleLicitacionController extends AbstractController<FnDetalleLicitacion> {

    @Inject
    private FnLicitacionesController licitacionController;
    @Inject
    private FnProveedoresController oferenteController;

    public FnDetalleLicitacionController() {
        // Inform the Abstract parent controller of the concrete FnDetalleLicitacion Entity
        super(FnDetalleLicitacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        licitacionController.setSelected(null);
        oferenteController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnLicitaciones controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareLicitacion(ActionEvent event) {
        if (this.getSelected() != null && licitacionController.getSelected() == null) {
            licitacionController.setSelected(this.getSelected().getLicitacion());
        }
    }

    /**
     * Sets the "selected" attribute of the FnProveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareOferente(ActionEvent event) {
        if (this.getSelected() != null && oferenteController.getSelected() == null) {
            oferenteController.setSelected(this.getSelected().getOferente());
        }
    }
}
