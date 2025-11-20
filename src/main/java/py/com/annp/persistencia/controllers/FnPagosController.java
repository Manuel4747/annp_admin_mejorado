package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnPagos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnPagosController")
@ViewScoped
public class FnPagosController extends AbstractController<FnPagos> {

    @Inject
    private FnFacturasController facturaController;
    @Inject
    private FnChequesController chequeController;

    public FnPagosController() {
        // Inform the Abstract parent controller of the concrete FnPagos Entity
        super(FnPagos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        facturaController.setSelected(null);
        chequeController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnComprobantesDePago
     * entities that are retrieved from FnPagos?cap_first and returns the
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

    /**
     * Sets the "selected" attribute of the FnCheques controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCheque(ActionEvent event) {
        if (this.getSelected() != null && chequeController.getSelected() == null) {
            chequeController.setSelected(this.getSelected().getCheque());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnRetenciones entities
     * that are retrieved from FnPagos?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnRetenciones page
     */
    public String navigateFnRetencionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnRetenciones_items", this.getSelected().getFnRetencionesCollection());
        }
        return "/pages/fnRetenciones/index";
    }

}
