package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnRubroArticulo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnRubroArticuloController")
@ViewScoped
public class FnRubroArticuloController extends AbstractController<FnRubroArticulo> {

    public FnRubroArticuloController() {
        // Inform the Abstract parent controller of the concrete FnRubroArticulo Entity
        super(FnRubroArticulo.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnArticulos entities that
     * are retrieved from FnRubroArticulo?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnArticulos page
     */
    public String navigateFnArticulosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnArticulos_items", this.getSelected().getFnArticulosCollection());
        }
        return "/pages/fnArticulos/index";
    }

}
