package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetalleFactura;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnDetalleFacturaController")
@ViewScoped
public class FnDetalleFacturaController extends AbstractController<FnDetalleFactura> {

    @Inject
    private FnArticulosController articuloController;
    @Inject
    private FnFacturasController facturaController;

    public FnDetalleFacturaController() {
        // Inform the Abstract parent controller of the concrete FnDetalleFactura Entity
        super(FnDetalleFactura.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        articuloController.setSelected(null);
        facturaController.setSelected(null);
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

    /**
     * Sets the "selected" attribute of the FnFacturas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareFactura(ActionEvent event) {
        if (this.getSelected() != null && facturaController.getSelected() == null) {
            facturaController.setSelected(this.getSelected().getFactura());
        }
    }
}
