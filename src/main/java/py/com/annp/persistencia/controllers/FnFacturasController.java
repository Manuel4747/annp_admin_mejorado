package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnFacturas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnFacturasController")
@ViewScoped
public class FnFacturasController extends AbstractController<FnFacturas> {

    @Inject
    private FnOrdenesDeCompraController ordenCompraController;
    @Inject
    private FnProveedoresController proveedorController;

    public FnFacturasController() {
        // Inform the Abstract parent controller of the concrete FnFacturas Entity
        super(FnFacturas.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        ordenCompraController.setSelected(null);
        proveedorController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnOrdenesDeCompra controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareOrdenCompra(ActionEvent event) {
        if (this.getSelected() != null && ordenCompraController.getSelected() == null) {
            ordenCompraController.setSelected(this.getSelected().getOrdenCompra());
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

    /**
     * Sets the "items" attribute with a collection of FnPagos entities that are
     * retrieved from FnFacturas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for FnPagos page
     */
    public String navigateFnPagosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnPagos_items", this.getSelected().getFnPagosCollection());
        }
        return "/pages/fnPagos/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleFactura entities
     * that are retrieved from FnFacturas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnDetalleFactura page
     */
    public String navigateFnDetalleFacturaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleFactura_items", this.getSelected().getFnDetalleFacturaCollection());
        }
        return "/pages/fnDetalleFactura/index";
    }

}
