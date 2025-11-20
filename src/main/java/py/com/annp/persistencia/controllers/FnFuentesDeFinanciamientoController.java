package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnFuentesDeFinanciamiento;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnFuentesDeFinanciamientoController")
@ViewScoped
public class FnFuentesDeFinanciamientoController extends AbstractController<FnFuentesDeFinanciamiento> {

    public FnFuentesDeFinanciamientoController() {
        // Inform the Abstract parent controller of the concrete FnFuentesDeFinanciamiento Entity
        super(FnFuentesDeFinanciamiento.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetallePlanFinanciero
     * entities that are retrieved from FnFuentesDeFinanciamiento?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnDetallePlanFinanciero page
     */
    public String navigateFnDetallePlanFinancieroCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetallePlanFinanciero_items", this.getSelected().getFnDetallePlanFinancieroCollection());
        }
        return "/pages/fnDetallePlanFinanciero/index";
    }

}
