package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnCategoriaArticulo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnCategoriaArticuloController")
@ViewScoped
public class FnCategoriaArticuloController extends AbstractController<FnCategoriaArticulo> {

    public FnCategoriaArticuloController() {
        // Inform the Abstract parent controller of the concrete FnCategoriaArticulo Entity
        super(FnCategoriaArticulo.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnArticulos entities that
     * are retrieved from FnCategoriaArticulo?cap_first and returns the
     * navigation outcome.
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
