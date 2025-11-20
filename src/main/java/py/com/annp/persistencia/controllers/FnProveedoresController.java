package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnProveedores;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnProveedoresController")
@ViewScoped
public class FnProveedoresController extends AbstractController<FnProveedores> {

    public FnProveedoresController() {
        // Inform the Abstract parent controller of the concrete FnProveedores Entity
        super(FnProveedores.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnFacturas entities that
     * are retrieved from FnProveedores?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of FnComprobantesDePago
     * entities that are retrieved from FnProveedores?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnComprobantesDePago page
     */
    public String navigateFnComprobantesDePagoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnComprobantesDePago_items", this.getSelected().getFnComprobantesDePagoCollection());
        }
        return "/pages/fnComprobantesDePago/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnContratos entities that
     * are retrieved from FnProveedores?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnContratos page
     */
    public String navigateFnContratosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnContratos_items", this.getSelected().getFnContratosCollection());
        }
        return "/pages/fnContratos/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnLicitaciones entities
     * that are retrieved from FnProveedores?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnLicitaciones page
     */
    public String navigateFnLicitacionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnLicitaciones_items", this.getSelected().getFnLicitacionesCollection());
        }
        return "/pages/fnLicitaciones/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleLicitacion
     * entities that are retrieved from FnProveedores?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnDetalleLicitacion page
     */
    public String navigateFnDetalleLicitacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleLicitacion_items", this.getSelected().getFnDetalleLicitacionCollection());
        }
        return "/pages/fnDetalleLicitacion/index";
    }

    /**
     * Sets the "items" attribute with a collection of FnOrdenesDeCompra
     * entities that are retrieved from FnProveedores?cap_first and returns the
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
