package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnTiposReprogramaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnTiposReprogramacionesController")
@ViewScoped
public class FnTiposReprogramacionesController extends AbstractController<FnTiposReprogramaciones> {

    public FnTiposReprogramacionesController() {
        // Inform the Abstract parent controller of the concrete FnTiposReprogramaciones Entity
        super(FnTiposReprogramaciones.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnReprogramaciones
     * entities that are retrieved from FnTiposReprogramaciones?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnReprogramaciones page
     */
    public String navigateFnReprogramacionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnReprogramaciones_items", this.getSelected().getFnReprogramacionesCollection());
        }
        return "/pages/fnReprogramaciones/index";
    }

}
