package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnComprobantesDePago;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnComprobantesDePagoController")
@ViewScoped
public class FnComprobantesDePagoController extends AbstractController<FnComprobantesDePago> {

    @Inject
    private FnPagosController pagoController;
    @Inject
    private FnProveedoresController proveedorController;

    public FnComprobantesDePagoController() {
        // Inform the Abstract parent controller of the concrete FnComprobantesDePago Entity
        super(FnComprobantesDePago.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        pagoController.setSelected(null);
        proveedorController.setSelected(null);
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

    /**
     * Sets the "selected" attribute of the FnProveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
        }
    }
}
