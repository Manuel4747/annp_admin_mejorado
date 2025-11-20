package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnOrganismosFinaciadores;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "fnOrganismosFinaciadoresController")
@ViewScoped
public class FnOrganismosFinaciadoresController extends AbstractController<FnOrganismosFinaciadores> {

    public FnOrganismosFinaciadoresController() {
        // Inform the Abstract parent controller of the concrete FnOrganismosFinaciadores Entity
        super(FnOrganismosFinaciadores.class);
    }

    /**
     * Sets the "items" attribute with a collection of FnPresupuesto entities
     * that are retrieved from FnOrganismosFinaciadores?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for FnPresupuesto page
     */
    public String navigateFnPresupuestoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnPresupuesto_items", this.getSelected().getFnPresupuestoCollection());
        }
        return "/pages/fnPresupuesto/index";
    }

}
