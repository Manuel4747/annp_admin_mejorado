package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnContratos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnContratosController")
@ViewScoped
public class FnContratosController extends AbstractController<FnContratos> {

    @Inject
    private FnLicitacionesController licitacionController;
    @Inject
    private FnProveedoresController proveedorController;

    public FnContratosController() {
        // Inform the Abstract parent controller of the concrete FnContratos Entity
        super(FnContratos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        licitacionController.setSelected(null);
        proveedorController.setSelected(null);
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
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnOrdenesDeCompra
     * entities that are retrieved from FnContratos?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnOrdenesDeCompra page
     */
    public String navigateFnOrdenesDeCompraCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnOrdenesDeCompra_items", this.getSelected().getFnOrdenesDeCompraCollection());
        }
        return "/pages/fnOrdenesDeCompra/index";
    }

}
