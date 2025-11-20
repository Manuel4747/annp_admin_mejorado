package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnTipoDeVariacionRp;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnTipoDeVariacionRpController")
@ViewScoped
public class FnTipoDeVariacionRpController extends AbstractController<FnTipoDeVariacionRp> {

    public FnTipoDeVariacionRpController() {
        // Inform the Abstract parent controller of the concrete FnTipoDeVariacionRp Entity
        super(FnTipoDeVariacionRp.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleReprogramacion
     * entities that are retrieved from FnTipoDeVariacionRp?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnDetalleReprogramacion page
     */
    public String navigateFnDetalleReprogramacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleReprogramacion_items", this.getSelected().getFnDetalleReprogramacionCollection());
        }
        return "/pages/fnDetalleReprogramacion/index";
    }

}
