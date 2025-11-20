package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.RhRelojes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "rhRelojesController")
@ViewScoped
public class RhRelojesController extends AbstractController<RhRelojes> {


    public RhRelojesController() {
        // Inform the Abstract parent controller of the concrete RhRelojes Entity
        super(RhRelojes.class);
    }

    /**
     * Sets the "items" attribute with a collection of RhMarcaciones entities
     * that are retrieved from RhRelojes?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RhMarcaciones page
     */
    public String navigateRhMarcacionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RhMarcaciones_items", this.getSelected().getRhMarcacionesCollection());
        }
        return "/pages18/rhMarcaciones/index";
    }

}
