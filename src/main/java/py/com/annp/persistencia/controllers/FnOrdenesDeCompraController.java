package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnOrdenesDeCompra;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnOrdenesDeCompraController")
@ViewScoped
public class FnOrdenesDeCompraController extends AbstractController<FnOrdenesDeCompra> {

    @Inject
    private FnProveedoresController proveedorController;
    @Inject
    private FnContratosController contratoController;

    public FnOrdenesDeCompraController() {
        // Inform the Abstract parent controller of the concrete FnOrdenesDeCompra Entity
        super(FnOrdenesDeCompra.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        proveedorController.setSelected(null);
        contratoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnFacturas entities that
     * are retrieved from FnOrdenesDeCompra?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnFacturas page
     */
    public String navigateFnFacturasCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnFacturas_items", this.getSelected().getFnFacturasCollection());
        }
        return "/pages/fnFacturas/index";
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

    /**
     * Sets the "selected" attribute of the FnContratos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareContrato(ActionEvent event) {
        if (this.getSelected() != null && contratoController.getSelected() == null) {
            contratoController.setSelected(this.getSelected().getContrato());
        }
    }
}
