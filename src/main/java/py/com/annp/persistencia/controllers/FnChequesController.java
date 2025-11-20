package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnCheques;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnChequesController")
@ViewScoped
public class FnChequesController extends AbstractController<FnCheques> {

    public FnChequesController() {
        // Inform the Abstract parent controller of the concrete FnCheques Entity
        super(FnCheques.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnPagos entities that are
     * retrieved from FnCheques?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for FnPagos page
     */
    public String navigateFnPagosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnPagos_items", this.getSelected().getFnPagosCollection());
        }
        return "/pages/fnPagos/index";
    }

}
